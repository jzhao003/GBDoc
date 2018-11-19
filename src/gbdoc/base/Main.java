package gbdoc.base;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import gbdoc.db.Standard;
import gbdoc.db.StandardSection;
import gbdoc.handlers.CreateRecordsHandler;
import gbdoc.handlers.CreateStandardSectionHanlder;
import gbdoc.handlers.GetFromStanardTableHandler;
import gbdoc.handlers.HtmlHandler;
import gbdoc.handlers.ListAllHandler;

public class Main {
	public static void main(String[] args) {
		ApplicationContext appCtx = new ApplicationContext();

		HttpServer server = HttpServer.createSimpleServer("0.0.0.0", 8777);

		// curl -XGET 'http://localhost:8777/list-all-standards'
		server.getServerConfiguration().addHttpHandler(new ListAllHandler(Standard.class, appCtx),
				"/list-all-standards");
		server.getServerConfiguration().addHttpHandler(new HtmlHandler(), "/app/hl");

		// curl -XPOST -d 'title=1&template_location=11' 'http://localhost:8777/add'
		server.getServerConfiguration().addHttpHandler(new CreateRecordsHandler(Standard.class), "/add");
		
		// get StanardTable record by id
		// curl -XGET 'http://localhost:8777/id?id=42'
		server.getServerConfiguration().addHttpHandler(new GetFromStanardTableHandler(appCtx), "/id");

		server.getServerConfiguration().addHttpHandler(
                new StaticHttpHandler("./templates"), "/static");
		
//		server.getServerConfiguration().addHttpHandler(new CreateSectionHandler(StandardSection.class), "/section");

		server.getServerConfiguration().addHttpHandler(new CreateStandardSectionHanlder(appCtx), "/section");

		//curl http://cc.test.org/api/test.do?param1=p1\&param2=p2\&param3=p3
		
		try {

			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
