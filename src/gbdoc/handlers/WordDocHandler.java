package gbdoc.handlers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import gbdoc.base.ApplicationContext;
import gbdoc.db.DocTemplate;
import gbdoc.db.StandardSection;
import gbdoc.doc.GbFileParagraph;
import gbdoc.util.GbDocFileUtil;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.ModelAndTemplate;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringBinder;

public class WordDocHandler extends NannyHttpHandler {
	ApplicationContext appCtx = null;

	@Override
	public Connection getConnection() throws SQLException {
		return appCtx.getDataSource().getConnection();
	}

	public WordDocHandler(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	// 保存文字到文件
	public Object doPost(Request req, Response res) throws Exception {
		String[] inputValues = req.getParameterValues("keywords[]");
		String fileName = req.getParameterValues("fileName")[0];
		String section = req.getParameterValues("sectionNumber")[0];
		GbDocFileUtil.replaceTemplatesWord(new File(fileName), section, inputValues);
		return null;
	}

	private String getSectionNumber(String sectionId) {
		SimplePoJoDAO dao;
		StandardSection obj = null;
		try {
			dao = this.appCtx.getDaoManager().getDao(StandardSection.class);
			obj = dao.loadPoJo(this.appCtx.getDataSource(), "select section_number from `standard_section` where id=?",
					sectionId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.section_number;
	}

	// 利用standardId，从doc_template,找到 模板 文件的路径
	private String getTemplatesFolderPath(String standardId) {
		SimplePoJoDAO dao;
		DocTemplate obj = null;
		try {
			dao = this.appCtx.getDaoManager().getDao(DocTemplate.class);
			obj = dao.loadPoJo(this.appCtx.getDataSource(), "select file_path from `doc_template` where standard_id=?",
					standardId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.file_path;
	}

	// 如果 download 文件夹内没有对应的文件，则从upload文件夹copy所有文件到download文件夹，并返回download folder path
	// 否则 ，直接返回download folder path
	private String getOutPutFileFolder(String templatesPath) {
		File file = new File("download/" + templatesPath.replace("upload/", ""));
		if (!file.exists()) {
			file.mkdirs();
			// copy templates from upload/ folder to download/ folder
			try {
				FileUtils.copyDirectory(new File(templatesPath), file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file.getPath();
	}

	/**
	 * 在UI上，开始编辑文档 当单击任意一个section时， 
	 * - 从UI取得standardId, 再利用standardId，从doc_template取得template 路径 
	 * - 判断文件是否存在，如不存在，copy 所有template folder到一个新folder
	 * - 从新folder内，遍历所有文件， 
	 * - 找到section对应的段落，开始编辑
	 * 
	 * 
	 * - 开始编辑的时候，每个可编辑的单词，显示成一个link；
	 * - 每点那个link，弹出一个新的page，page上显示 <模板>：<input框，填要写的文字>
	 * - 点save，关闭此page，并且更新word文件！
	 * - 刷新页面，这样，已经编辑的词，不会在显示link，然后可以开始编辑下一个单词？？
	 */
	public Object doGet(Request req, Response res) throws Exception {
		// 找到并编辑 template 文件的副本，如副本不存在，则创建副本；否则直接开始编辑	
		Map<String, String> conditions = QueryStringBinder.extractParameters(req);
		// 从request里，取得standardId
		String standardId = conditions.get("standardId");
		// get templates via standardId
		String templateFilePath = getTemplatesFolderPath(standardId);
		// get the section number 从request里，取得section number, 如 7.4.2
		String sectionNumber = getSectionNumber(conditions.get("section"));// conditions.get("section").substring(0, 5);
		String generateOutputFilePath = getOutPutFileFolder(templateFilePath);

		// 遍历所有文件，找到对应的section, GbDocFileUtil. findParagraphs
		File dir = new File(generateOutputFilePath);
		
		GbFileParagraph gbFileParagraph = GbDocFileUtil.findParagraphsInDirectory(dir,sectionNumber);
		List<XWPFParagraph> paragraphList = gbFileParagraph.getParagraphList();
		StringBuffer wordFileContent = new StringBuffer();
		if (paragraphList != null && paragraphList.size() != 0) {
			int inputIdNumber = 1;
			for (XWPFParagraph para : paragraphList) {
				boolean redFlag = false;
				wordFileContent.append("<p  class=\"ex2\">    ");
				List<XWPFRun> runs = para.getRuns();
				for (XWPFRun run : runs) {
					String text = run.getText(0);
					// 去掉 section number
					if (text.equals("#"+sectionNumber+"#")) {
						continue;
					}
					if (redFlag) {
						// 如果前面一个run是红色字符
						if (GbDocFileUtil.isRedFont(run.getColor())) {
							// 如果当前run也是红色字符，则把字符append在后面
							wordFileContent.append(text);
							redFlag = true;
						} else {
							// 如果前面一个run是红色，而当前run不是，则 先加<input> html element，再把字符append在后面
							wordFileContent.append("<input id='id" + inputIdNumber + "\'></input>" + text);
							inputIdNumber++;
							redFlag = false;
						}
					} else {
						wordFileContent.append(text);
						if (GbDocFileUtil.isRedFont(run.getColor())) {
							// 如果当前字符是红色，flag设置成 true
							redFlag = true;
						} else {
							redFlag = false;
						}
					}
				}
				wordFileContent.append("</p>");
			}
		}
		wordFileContent.append("<input hidden id='fileName' value='"+gbFileParagraph.getFile()+"'></input>");
		wordFileContent.append("<input hidden id='sectionNumber' value='"+sectionNumber+"'></input>");
		
		JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/editDoc.html"));
		JtwigModel m = JtwigModel.newModel();
		m.with("var", wordFileContent.toString());
		ModelAndTemplate ret = new ModelAndTemplate(m, t);
		return ret;
	}

}
