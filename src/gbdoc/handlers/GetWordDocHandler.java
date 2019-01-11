package gbdoc.handlers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import gbdoc.base.ApplicationContext;
import gbdoc.db.StandardSection;
import gbdoc.doc.EditDoc;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.ModelAndTemplate;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringBinder;

public class GetWordDocHandler extends NannyHttpHandler {
	ApplicationContext appCtx = null;

	@Override
	public Connection getConnection() throws SQLException {
		return appCtx.getDataSource().getConnection();
	}

	public GetWordDocHandler(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	public Object doPost(Request req, Response res) throws Exception {
		EditDoc.editTemplate(req.getParameterValues("keywords[]"));
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

	public Object doGet(Request req, Response res) throws Exception {
		Pattern p = Pattern.compile("\\#[0-9].+[0-9]+.*\\#"); // the pattern to search for
		Matcher matcher = null;

		Map<String, String> conditions = QueryStringBinder.extractParameters(req);
		// get the section number
		String sectionNumber = getSectionNumber(conditions.get("section"));// conditions.get("section").substring(0, 5);
		StringBuffer wordFileContent = new StringBuffer();
		List<XWPFParagraph> paragraphList = EditDoc.getWordFileParagraph();

		int inputIdNumber = 1;
		boolean isSectionParagraph = false;
		for (XWPFParagraph paragraph : paragraphList) {

			matcher = p.matcher(paragraph.getText());
			if (matcher.find()) {
				if (paragraph.getText().startsWith("#" + sectionNumber + "#")) {
					isSectionParagraph = true;

				} else {
					isSectionParagraph = false;
				}
			}

			if (isSectionParagraph) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (int i = 0; i < runs.size(); i++) {
					wordFileContent.append(runs.get(i).text());
					if (runs.get(i).getColor() != null && runs.get(i).getColor().equals("FF0000")) {
						if (i < runs.size() - 1 && runs.get(i + 1).getColor() != null
								&& runs.get(i + 1).getColor().equals("FF0000")) {
							continue;
						} else {
							wordFileContent.append("<input id='id" + inputIdNumber + "'></input>");
							inputIdNumber++;
						}
					}
				}
			}

//			if (paragraph.getText().startsWith("#"+sectionNumber+"#")) {
//				List<XWPFRun> runs = paragraph.getRuns();
//				for (int i=0;i<runs.size();i++) {
//					wordFileContent.append(runs.get(i).text());
//					if (runs.get(i).getColor()!=null && runs.get(i).getColor().equals("FF0000")) {
//						if (i<runs.size()&& runs.get(i+1).getColor()!=null && runs.get(i+1).getColor().equals("FF0000")) {
//							continue;
//						} else {
//							wordFileContent.append("<input id='id"+inputIdNumber+"'></input>");
////							wordFileContent.append("<a href=\"url\">link text</a>");
//							inputIdNumber++;
//						}
//					}
//				}
//			}
		}
		wordFileContent.append("<input hidden value='" + sectionNumber + "' id='id" + inputIdNumber + "'></input>");

		JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/editDoc.html"));
		JtwigModel m = JtwigModel.newModel();
		m.with("var", wordFileContent.toString());
		ModelAndTemplate ret = new ModelAndTemplate(m, t);
		return ret;
	}

}
