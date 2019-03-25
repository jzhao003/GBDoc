package gbdoc.handlers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import gbdoc.base.ApplicationContext;
import gbdoc.db.CreatedDocument;
import gbdoc.db.DBUtils;
import gbdoc.db.DocTemplate;
import gbdoc.util.GbDocFileUtil;
import gubo.db.DaoManager;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringBinder;

/* 
 * actions on created_document db table
 * */
public class CreatedDocumentHandler extends NannyHttpHandler {

	private ApplicationContext appCtx;

	public CreatedDocumentHandler(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	public ApplicationContext getAppCtx() {
		return appCtx;
	}

	public void setAppCtx(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	@Override
	public Object doPost(Request request, Response response) {
		Map<String, String> params = QueryStringBinder.extractParameters(request);
		Long id = null;

		String standardId = params.get("standard_id");
		String templatesFile = getTemplatesFolderPath(standardId);
		String downloadFileFolder = GbDocFileUtil.copyTemplatesFromUploadToDownLoadFolder(templatesFile);

		try {
			DaoManager daoManager = appCtx.getDaoManager();
			SimplePoJoDAO dao = daoManager.getDao(CreatedDocument.class);
			CreatedDocument pojo = null;
			
			pojo = new CreatedDocument();
			pojo.standard_id = Long.valueOf(standardId);
			pojo.folder_name = downloadFileFolder;
			id = dao.insert(getConnection(), pojo).id;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
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
	
	public Connection getConnection() throws SQLException {
		return DBUtils.getHikariConnection();
	}

}
