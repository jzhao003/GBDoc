package gbdoc.base;

import gbdoc.db.Standard;
import gbdoc.handlers.ListAllHandler;

import org.glassfish.grizzly.http.server.HttpServer;

public class Main {
	public static void main(String[] args) {

		ApplicationContext appCtx = new ApplicationContext();

		HttpServer server = HttpServer.createSimpleServer("0.0.0.0", 8777);

		// curl -XGET 'http://localhost:8777/list-all-standards'
		server.getServerConfiguration().addHttpHandler(
				new ListAllHandler(Standard.class, appCtx),
				"/list-all-standards");

		try {

			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
