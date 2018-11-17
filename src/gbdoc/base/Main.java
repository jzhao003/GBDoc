package gbdoc.base;

import org.glassfish.grizzly.http.server.HttpServer;

import gbdoc.db.Standard;
import gbdoc.db.StandardSection;
import gbdoc.handlers.CreateRecordsHandler;
import gbdoc.handlers.GetSectionTreeHandler;
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
		
		// get stanard record by id???
		// curl -XGET -d  'http://localhost:8777/id'
		server.getServerConfiguration().addHttpHandler(new GetSectionTreeHandler(appCtx), "/id");

		
		server.getServerConfiguration().addHttpHandler(new CreateRecordsHandler(StandardSection.class), "/section");

		try {

			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
