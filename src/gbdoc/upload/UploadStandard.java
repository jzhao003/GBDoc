package gbdoc.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gbdoc.db.DBUtils;
import gbdoc.db.Standard;
import gbdoc.db.StandardSection;
import gubo.db.DaoManager;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import gubo.http.grizzly.handlers.InMemoryMultipartHttpHandler;

public class UploadStandard extends InMemoryMultipartHttpHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	DaoManager daoManager = new DaoManager();

	public Connection getConnection() throws SQLException {
		return DBUtils.getHikariConnection();
	}

	final HashMap<String, Integer> sizeLimit;
	{
		sizeLimit = new HashMap<String, Integer>();
		sizeLimit.put("ignore-this", InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE);
		sizeLimit.put("reject-this", InMemoryMultipartEntryHandler.SIZE_LIMIT_REJECT);
		sizeLimit.put("big-one", 1024 * 1024);
		sizeLimit.put("fff", 1024 * 1024);
	}

	public HashMap<String, Integer> getSizeLimit() {
		return sizeLimit;
	}

	public int getDefaultSizeLimit() {
		// 忽略 big-one 和 fff 以外的所有参数。
		return 1024 * 1024;
	}

	@Override
	public void onMultipartScanCompleted(Request request, Response response,
			InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler) throws IOException {
		ContentDisposition contentDisposition = inMemoryMultipartEntryHandler.getContentDisposition("fileName");
		String filename = contentDisposition.getDispositionParam("filename").replace("\"", "");

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fullFilePath = "upload/" + format.format(new Date()) + "/";
		// create a folder
		(new File(fullFilePath)).mkdirs();
		// create a file, put it in the folder
		try (FileOutputStream stream = new FileOutputStream(fullFilePath + filename)) {
			stream.write(inMemoryMultipartEntryHandler.getBytes("fileName"));
		}

		String standardId = new String(inMemoryMultipartEntryHandler.getBytes("standardId"));

		Standard newPojo = new Standard();
		SimplePoJoDAO dao = daoManager.getDao(Standard.class);
		newPojo.template_location = fullFilePath + filename;
		newPojo.id = Long.valueOf(standardId);
		try {
			// update in standard table
			dao.update(DBUtils.getDataSource(), newPojo);

			// parse stand section
			parseStandardSection(newPojo.template_location,newPojo.id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void parseStandardSection(String file, Long standardId) throws SQLException {
		Map<String, String> section = getSectionFromStanardDoc(file);
		SimplePoJoDAO dao = daoManager.getDao(StandardSection.class);
		StandardSection pojo = null;

		Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			
			for (Map.Entry<String, String> entry : section.entrySet()) {
				pojo = new StandardSection();
				pojo.section_number = entry.getKey();
				pojo.section_content = entry.getValue();
				pojo.standard_id = standardId;
				dao.insert(conn, pojo);
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public Map<String, String> getSectionFromStanardDoc(String filePath) {

		FileInputStream input;
		XWPFDocument xdoc;
		Map<String, String> section = new HashMap<String, String>();

		String strs[] = new String[2];
		String parentSectionNumber = null;
		try {
			input = new FileInputStream(filePath);
			xdoc = new XWPFDocument(OPCPackage.open(input));
			List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

			for (XWPFParagraph paragraph : paragraphList) {
				strs = getStandardSectionAndContent(paragraph.getText(), parentSectionNumber);
				if (strs[0] != null) {
					section.put(strs[0], strs[1]);
					parentSectionNumber = updateParentSectionNumber(strs[0]).trim();
				}

			}
			xdoc.close();
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return section;
	}

	/**
	 * 读取“标准”文档（如“CL05认可准则.docx”）的内容 传入参数：
	 * 
	 * @param text，待处理的文字，一般为一个word的段落
	 * @param parentSectionNumber， 如 6.1.2 返回一个数组，分别存储： - section number, 如 6.1.2，
	 *        6.1.3 a)，7.1 等等 - section content, 6.1.2后面的内容
	 */
	private String[] getStandardSectionAndContent(String text, String parentSectionNumber) {
		String sections[] = new String[2];
		if (text != null && !text.equals("") && !text.equals("\t")) {
			if (text.startsWith("\t")) {
				text = text.replace("\t", "");
			}
			if (Character.isDigit(text.charAt(0))) {
				// 数字开头段落，如 6.1.2
				Pattern pattern = Pattern.compile("([0-9\\.]+).*"); // got str like:1.1, 7.1.2
				Matcher matcher = pattern.matcher(text);

				if (matcher.find()) {
					// 找到段落的section
					sections[0] = matcher.group(1);
					sections[1] = text.replaceFirst(sections[0], "");
				}
			} else {
				// 字母开头段落，如 a) b)
				if (parentSectionNumber != null) {
					// a) 之前加数字，变成如，6.1.2 a)
					sections[0] = parentSectionNumber + " " + text.substring(0, 2);
					sections[1] = text.substring(2);
				}
			}
		}
		return sections;
	}

	/**
	 * 该方法主要使用正则表达式,去掉sectionNumber中的字母和括号 如，6.1.2 a) , 输出 6.1.2
	 * 
	 * @param sectionNumber 待检验的原始section
	 * @return 处理之后，得到的 parent section number
	 */
	public String updateParentSectionNumber(String sectionNumber) {
		if (sectionNumber == null) {
			return null;
		}
		Matcher m = Pattern.compile("[a-zA-Z]").matcher(sectionNumber);
		if (m.find()) {
			sectionNumber = sectionNumber.substring(0, sectionNumber.indexOf(m.group()));
		}
		return sectionNumber;
	}
}
