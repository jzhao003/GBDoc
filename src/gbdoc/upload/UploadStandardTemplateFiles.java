package gbdoc.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gbdoc.db.DBUtils;
import gbdoc.db.DocTemplate;
import gbdoc.util.ZipFileUtil;
import gubo.db.DaoManager;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import gubo.http.grizzly.handlers.InMemoryMultipartHttpHandler;

public class UploadStandardTemplateFiles extends InMemoryMultipartHttpHandler {
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
		ContentDisposition contentDisposition = inMemoryMultipartEntryHandler.getContentDisposition("zips");
		String filename = contentDisposition.getDispositionParam("filename").replace("\"", "");

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fullFilePath = "upload/" + format.format(new Date()) + "/";
		// create a folder
		(new File(fullFilePath)).mkdirs();
		// create a file, put it in the folder
		try (FileOutputStream stream = new FileOutputStream(fullFilePath + filename)) {
			stream.write(inMemoryMultipartEntryHandler.getBytes("zips"));
		}

		//unzip
		ZipFileUtil.unZipFiles(fullFilePath + filename, fullFilePath + "templates");
		
		String standardId = new String(inMemoryMultipartEntryHandler.getBytes("standardId"));
		
		DocTemplate newPojo = new DocTemplate();
		SimplePoJoDAO dao = daoManager.getDao(DocTemplate.class);
		newPojo.file_path = fullFilePath + "templates";
		newPojo.standard_id = Long.valueOf(standardId);
		try {
			// update in standard table
			dao.insert(DBUtils.getDataSource(), newPojo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

}
