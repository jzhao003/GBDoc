package gbdoc.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import gbdoc.db.DBUtils;
import gbdoc.db.DocTemplate;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringBinder;
import gubo.jdbc.mapping.ResultSetMapper;

public class WordDocHandler extends NannyHttpHandler {
	
	Class<? extends ISimplePoJo> clazz;

	public WordDocHandler(Class<? extends ISimplePoJo> clazz) {
		this.clazz = clazz;
	}
	
	protected final List<Object> doFilter(Map<String, String> params) throws Exception {
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);
			return ResultSetMapper.loadPoJoList(dbconn, clazz, params);
		} finally {
			dbconn.close();
		}
	}
	
	public Object doPost(Request request, Response response) throws Exception {
		// according to the standardId in request, get the template
		// use standardId , to get filePath in DB
		Map<String, String> conditions = QueryStringBinder.extractParameters(request);
		List<Object> list = this.doFilter(conditions);
		DocTemplate docTemplate=(DocTemplate) list.get(0);
		String filePath = docTemplate.file_path;
		String newFilePath = getNewFilePath(filePath);
		
		// copy template to a new file
		copyFileUsingStream(new File(filePath),new File(newFilePath));
		
		// return new file name and path
		return newFilePath;

	}

	private String getNewFilePath(String filePath) {
		// word.doc, change to word_copy.doc
		String[] fullFilePahtName = filePath.split("\\.");
		StringBuffer sb = new StringBuffer();
		sb.append(fullFilePahtName[0]).append("_copy");
		sb.append(".").append(fullFilePahtName[1]);
		return sb.toString();
	}
	
	public Object doGet(Request request, Response response) throws Exception {

		// according to the standardId in request, get the template

		// copy template to a new file

		// according to the section number in request, get the detail section from DOC
		// file
		return null;
	}
	
	public Connection getConnection() throws SQLException {
		return DBUtils.getHikariConnection();
	}
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}
