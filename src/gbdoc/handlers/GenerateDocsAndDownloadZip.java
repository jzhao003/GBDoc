package gbdoc.handlers;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import gbdoc.base.ApplicationContext;
import gbdoc.db.DocTemplate;
import gbdoc.util.ZipFileUtil;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.ModelAndTemplate;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringBinder;

public class GenerateDocsAndDownloadZip extends NannyHttpHandler {
	ApplicationContext appCtx = null;
	public GenerateDocsAndDownloadZip(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}
	
	public Object doGet(Request req, Response res) {
		Map<String, String> conditions = QueryStringBinder.extractParameters(req);
		// 从request里，取得standardId
		String standardId = conditions.get("eq__standard_id");
		// generate zip
		
		String downloadPath = getDownLoadFilePath(standardId);
		ZipFileUtil.zipFile(downloadPath);
		return downloadPath + "/dirCompressed.zip";
	}
	
	public Object doPost(Request req, Response res) {
		Map<String, String> conditions = QueryStringBinder.extractParameters(req);
		// 从request里，取得standardId
		String standardId = conditions.get("standardId");
		// generate zip
		
		String downloadPath = getDownLoadFilePath(standardId);
		ZipFileUtil.zipFile(downloadPath);
		
//		JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/createDoc.html"));
//		JtwigModel m = JtwigModel.newModel();
//		// pass to html
//		m.with("zipPath", "/download/ztools.sql.zip");
//		ModelAndTemplate ret = new ModelAndTemplate(m, t);
		return null;
		
	}
	
	private String getDownLoadFilePath(String standardId) {
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
		if (obj.file_path!=null) {
			return obj.file_path.replace("upload", "download");
		}
		return null;
	}
}
