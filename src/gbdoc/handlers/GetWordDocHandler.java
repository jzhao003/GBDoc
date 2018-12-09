package gbdoc.handlers;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import gbdoc.doc.EditDoc;
import gubo.http.grizzly.ModelAndTemplate;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringBinder;

public class GetWordDocHandler extends NannyHttpHandler {

	public Object doPost(Request req, Response res) throws Exception {
		EditDoc.editTemplate(req.getParameterValues("keywords[]"));
		return null;
	}
	
	public Object doGet(Request req, Response res) throws Exception {
		Map<String, String> conditions = QueryStringBinder.extractParameters(req);
		// get the section number
		String sectionNumber = conditions.get("section").substring(0, 5);
		StringBuffer wordFileContent = new StringBuffer();
		List<XWPFParagraph> paragraphList = EditDoc.getWordFileParagraph();
		
		int inputIdNumber = 1;
		for (XWPFParagraph paragraph : paragraphList) {
			if (paragraph.getText().startsWith(sectionNumber)) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (int i=0;i<runs.size();i++) {
					wordFileContent.append(runs.get(i).text());
					if (runs.get(i).getColor()!=null && runs.get(i).getColor().equals("FF0000")) {
						if (i<runs.size()&& runs.get(i+1).getColor()!=null && runs.get(i+1).getColor().equals("FF0000")) {
							continue;
						} else {
							wordFileContent.append("<input id='id"+inputIdNumber+"'></input>");
//							wordFileContent.append("<a href=\"url\">link text</a>");
							inputIdNumber++;
						}
					}
				}
			}
		}
		wordFileContent.append("<input hidden value='"+sectionNumber+"' id='id"+inputIdNumber+"'></input>");
		
		JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/editDoc.html"));
        JtwigModel m = JtwigModel.newModel();
        m.with("var", wordFileContent.toString());
        ModelAndTemplate ret = new ModelAndTemplate(m, t);
		return ret;
	}

}
