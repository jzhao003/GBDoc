package gbdoc.base;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;

import gbdoc.db.DocTemplate;
import gbdoc.db.Standard;
import gbdoc.db.StandardSection;
import gbdoc.handlers.CreateRecordsHandler;
import gbdoc.handlers.CreateStandardSectionHanlder;
import gbdoc.handlers.GetFromStanardTableHandler;
import gbdoc.handlers.GetWordDocHandler;
import gbdoc.handlers.HtmlHandler;
import gbdoc.handlers.ListAllHandler;
import gbdoc.handlers.SectionTreeListHandler;
import gbdoc.upload.UploaderHttpHandler;

public class Main {
	private static final int PORT = 8777;
	
	public static void main(String[] args) {
		ApplicationContext appCtx = new ApplicationContext();

		HttpServer server = HttpServer.createSimpleServer("0.0.0.0", PORT);

		// curl -XGET 'http://localhost:8777/list-all-standards'
		server.getServerConfiguration().addHttpHandler(new ListAllHandler(Standard.class, appCtx),
				"/list-all-standards");
		
		// http://localhost:8777/app/hl , home page
		server.getServerConfiguration().addHttpHandler(new HtmlHandler(), "/app/hl");

		// curl -XPOST -d 'title=1&template_location=11' 'http://localhost:8777/add'   insert into Standard table 
		server.getServerConfiguration().addHttpHandler(new CreateRecordsHandler(Standard.class), "/addStandard");
		
		// get StanardTable record by id
		// curl -XGET 'http://localhost:8777/id?id=42'
		server.getServerConfiguration().addHttpHandler(new GetFromStanardTableHandler(appCtx), "/id");

		
		StaticHttpHandler staticHandler = new StaticHttpHandler("./templates");
		staticHandler.setFileCacheEnabled(false);
		server.getServerConfiguration().addHttpHandler(
		        staticHandler, "/static");
		
		// insert into StandardSection table
		server.getServerConfiguration().addHttpHandler(new CreateStandardSectionHanlder(appCtx), "/section");

		// curl -XGET 'http://localhost:8777/sections?eq__standard_id=60&order_by=section_number'  query from StandardSection, and build result as tree
		server.getServerConfiguration().addHttpHandler(new SectionTreeListHandler(StandardSection.class),"/sections");
		
		// insert into DocTemplate teble
		server.getServerConfiguration().addHttpHandler(new CreateRecordsHandler(DocTemplate.class), "/DocTemplate");
		
		
		// URL, http://localhost:8777/editTemplate
		server.getServerConfiguration().addHttpHandler(new GetWordDocHandler(), "/editTemplate");
		
		
		      
        // Map the path /upload to the UploaderHttpHandler
        server.getServerConfiguration().addHttpHandler(new UploaderHttpHandler(), "/upload");

        
		try {

			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
