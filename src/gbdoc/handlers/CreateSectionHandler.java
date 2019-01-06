package gbdoc.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Parameters;

import gbdoc.db.StandardSection;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.handlers.CreateSimplePojoHandler;

public class CreateSectionHandler extends CreateSimplePojoHandler {

	public CreateSectionHandler(Class<? extends ISimplePoJo> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		CreateSectionHandler h = new CreateSectionHandler(StandardSection.class);
		h.getFormatSectionNumberAndContent("7.1	组织和管理\n" + "7.1.1	实验室或其母体组织应有明确的法律地位和从事相关活动的资格。\n"
				+ "7.1.2	实验室所在的机构应设立生物安全委员会，负责咨询、指导、评估、监督实验室的生物安全相关事宜。实验室负责人应至少是所在机构生物安全委员会有职权的成员。\n"
				+ "7.1.3	实验室管理层应负责安全管理体系的设计、实施、维持和改进，应负责：\n" + "a）	为实验室所有人员提供履行其职责所需的适当权力和资源；\n"
				+ "b）	建立机制以避免管理层和实验室人员受任何不利于其工作质量的压力或影响（如：财务、人事或其他方面的），或卷入任何可能降低其公正性、判断力和能力的活动；\n"
				+ "c）	制定保护机密信息的政策和程序；\n" + "d）	明确实验室的组织和管理结构，包括与其他相关机构的关系；\n" + "e）	规定所有人员的职责、权力和相互关系；\n"
				+ "f）	安排有能力的人员，依据实验室人员的经验和职责对其进行必要的培训和监督；\n"
				+ "g）	指定一名安全负责人，赋予其监督所有活动的职责和权力，包括制定、维持、监督实验室安全计划的责任，阻止不安全行为或活动的权力，直接向决定实验室政策和资源的管理层报告的权力；\n"
				+ "h）	指定负责技术运作的技术管理层，并提供可以确保满足实验室规定的安全要求和技术要求的资源；\n"
				+ "i）	指定每项活动的项目负责人，其负责制定并向实验室管理层提交活动计划、风险评估报告、安全及应急措施、项目组人员培训及健康监督计划、安全保障及资源要求；\n"
				+ "j）	指定所有关键职位的代理人。\n" + "7.1.4	实验室安全管理体系应与实验室规模、实验室活动的复杂程度和风险相适应。\n" + "");
	}

	private void getContentSectionStartWithLetter(String currentSectionNumber, String inputSectionContent, Map<String,String> map) {
		Pattern pattern = Pattern.compile("[a-z]+\\）");
		Matcher matcher = pattern.matcher(inputSectionContent);
		
		if (matcher.find()) {
			// save 7.1.3
			map.put(currentSectionNumber, inputSectionContent.substring(0, matcher.start()));
			
			inputSectionContent=inputSectionContent.substring(matcher.start());
			matcher = pattern.matcher(inputSectionContent);
			
			String[] strs = pattern.split(inputSectionContent);
			
			int i=1;
			// save a), b) ....
			while (matcher.find()) {
				map.put(currentSectionNumber+matcher.group(), strs[i]);
				i++;
			}
		}
	}
	
	private String getContent(String sectionContent) {
		Pattern pattern = Pattern.compile("[0-9].+[0-9]");
		Matcher matcher = pattern.matcher(sectionContent);
		if (matcher.find()) {
			return sectionContent.substring(0, matcher.start());
		}
		return sectionContent;
	}

	public Map<String, String> getFormatSectionNumberAndContent(String inputSection) {
		Pattern pattern = Pattern.compile("[0-9].+[0-9]"); // got str like:1.1, 7.1.2
		Matcher matcher = pattern.matcher(inputSection);
		Map<String, String> map = new HashMap<String, String>();

		String sectionNumber = null;
		String sectionContent = null;

		while (matcher.find()) {
			sectionNumber = matcher.group();
			sectionContent = getContent(inputSection.replaceFirst(sectionNumber, ""));

			map.put(sectionNumber, sectionContent);
			getContentSectionStartWithLetter(sectionNumber,sectionContent,map);
			inputSection = inputSection.replaceFirst(sectionNumber + sectionContent, "");
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			
		}
		return map;
	}

	@Override
	public Object doPost(Request request, Response response) throws Exception {

		Map<String, String> section = null;
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

		for (Map.Entry<String, String> entry : section.entrySet()) {
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
