package gbdoc.handlers;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Parameters;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import gbdoc.doc.EditDoc;
import gubo.http.grizzly.ModelAndTemplate;
import gubo.http.grizzly.NannyHttpHandler;

public class GetWordDocHandler extends NannyHttpHandler {

	public Object doPost(Request req, Response res) throws Exception {
		Map<String, String[]> paraMap = req.getParameterMap();
//		String[] inputValues = req.getParameterValues("keywords");
//		Parameters para = req.getParameters();
//		System.out.println(para);
		EditDoc.editTemplate(req.getParameterValues("keywords[]"));
		return null;
	}
	
	public Object doGet(Request req, Response res) throws Exception {
		StringBuffer wordFileContent = new StringBuffer();
		List<XWPFParagraph> paragraphList = EditDoc.getWordFileParagraph();
		
		int inputIdNumber = 1;
		for (XWPFParagraph paragraph : paragraphList) {
			List<XWPFRun> runs = paragraph.getRuns();
			for (int i=0;i<runs.size();i++) {
				wordFileContent.append(runs.get(i).text());
				if (runs.get(i).getColor()!=null && runs.get(i).getColor().equals("FF0000")) {
					if (i<runs.size()&& runs.get(i+1).getColor()!=null && runs.get(i+1).getColor().equals("FF0000")) {
						continue;
					} else {
						wordFileContent.append("<input id='id"+inputIdNumber+"'></input>");
//						wordFileContent.append("<a href=\"url\">link text</a>");
						inputIdNumber++;
					}
				}
			}
		}
		
		JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/editDoc.html"));
        JtwigModel m = JtwigModel.newModel();
        m.with("var", wordFileContent.toString());
        ModelAndTemplate ret = new ModelAndTemplate(m, t);
		return ret;
	}

}
