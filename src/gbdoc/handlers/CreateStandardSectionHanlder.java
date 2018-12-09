package gbdoc.handlers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import gbdoc.base.ApplicationContext;
import gbdoc.db.DBUtils;
import gbdoc.db.StandardSection;
import gubo.db.DaoManager;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.NannyHttpHandler;

public class CreateStandardSectionHanlder extends NannyHttpHandler {
	private ApplicationContext appCtx;
	
	public CreateStandardSectionHanlder (ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	public ApplicationContext getAppCtx() {
		return appCtx;
	}

	public void setAppCtx(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}
	
	@Override
	public Object doPost(Request request, Response response)  {
		List<Long> createdRecordIds = new ArrayList<Long>();
		Map<String,String> section = null;
		String standardId = null;
		for (String pn : request.getParameterNames()) {
			if (pn.equalsIgnoreCase("sections")) {
				// 格式化为， key=1.1, value=前言 。。。。
				section = getFormatSectionNumberAndContent(request.getParameter(pn));
			} else {
				standardId = request.getParameter(pn);
			}
		}
		try {
			DaoManager daoManager = appCtx.getDaoManager();
			SimplePoJoDAO dao = daoManager.getDao(StandardSection.class);
			StandardSection pojo = null;
			for (Map.Entry<String,String> entry : section.entrySet()) {
				pojo = new StandardSection();
				pojo.section_number = entry.getKey();
				pojo.section_content = entry.getValue();
				pojo.standard_id = Long.valueOf(standardId);
				createdRecordIds.add(dao.insert(getConnection(), pojo).id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return createdRecordIds;
	}
	
	private String getContent(String sectionContent) {
		Pattern pattern = Pattern.compile("[0-9].+[0-9]");
		Matcher matcher = pattern.matcher(sectionContent);
		if (matcher.find()) {
			return sectionContent.substring(0, matcher.start());
		}
		return sectionContent;
	}
	
	public Map<String,String> getFormatSectionNumberAndContent(String inputSection) {
		Pattern pattern = Pattern.compile("[0-9].+[0-9]"); // got str like:1.1, 7.1.2
		Matcher matcher = pattern.matcher(inputSection);
		Map<String,String> map = new HashMap<String,String>();
		
		String sectionNumber = null;
		String sectionContent = null;
		
		while (matcher.find()) {
			sectionNumber = matcher.group();
			sectionContent = getContent(inputSection.replaceFirst(sectionNumber, ""));
			
			map.put(sectionNumber, sectionContent);
			inputSection=inputSection.replaceFirst(sectionNumber+sectionContent, "");
		}
		return map;
	}
	
	public Connection getConnection() throws SQLException {
		return DBUtils.getHikariConnection();
	}
}
