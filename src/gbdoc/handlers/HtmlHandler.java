package gbdoc.handlers;

import java.io.File;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import gubo.http.grizzly.ModelAndTemplate;
import gubo.http.grizzly.NannyHttpHandler;

public class HtmlHandler extends NannyHttpHandler {

	@Override
    public Object doGet(Request request, Response response) throws Exception {
//        JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/simple.twig"));
        JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/hello.html"));
        JtwigModel m = JtwigModel.newModel();
        m.with("var", "zhao");
        m.with("text", "sshsss");
        ModelAndTemplate ret = new ModelAndTemplate(m, t);
        
        


        return ret;
    }
	
}
