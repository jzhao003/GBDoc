package gbdoc.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Parameters;

import gubo.db.ISimplePoJo;
import gubo.http.grizzly.handlers.CreateSimplePojoHandler;

public class CreateSectionHandler extends CreateSimplePojoHandler {

	public CreateSectionHandler(Class<? extends ISimplePoJo> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}
//	public CreateSectionHandler(ApplicationContext clazz){
//		
//	}
//	public static void main(String args[]) {
////		CreateSectionHandler h = new CreateSectionHandler();
//		formatSection("1.1  前言\n" + 
//				"1.1.1 开始\n" + 
//				"1.1.2 结束\n" + 
//				"2.1 下一个\n" + 
//				"      这是段落\n" + 
//				"7.1.3 结束\n" + 
//				"");
//	}
	
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
	
	@Override
	public Object doPost(Request request, Response response) throws Exception {
		
		Map<String,String> section = null;
		String strandId = null;
		for (String pn : request.getParameterNames()) {

			if (pn.equalsIgnoreCase("sections")) {
				// 格式化为， key=1.1, value=前言 。。。。
				section = getFormatSectionNumberAndContent(request.getParameter(pn));

			} else {
				strandId = request.getParameter(pn);
			}
		}
		
		
		////////
		
		
		Parameters para = new Parameters();
		
		for (Map.Entry<String,String> entry : section.entrySet()) {
			request.clearParameters();
			para = new Parameters();
			para.addParameter("standard_id", strandId);
			para.addParameter("section_number", entry.getKey());
			para.addParameter("section_content", entry.getValue());
			request.setRequestParameters(para);
			super.doPost(request, response);
			
//			DBUtils.
		}
		
		return null;
	}
}
