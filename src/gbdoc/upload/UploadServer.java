package gbdoc.upload;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;

public class UploadServer {
	private static final Logger LOGGER = Grizzly.logger(UploadServer.class);

    private static final int PORT = 18080;

    public static void main(String[] args) {
        // create a HttpServer
        final HttpServer server = new HttpServer();        
        final ServerConfiguration config = server.getServerConfiguration();

        // Map the path / to the FormHttpHandler
        config.addHttpHandler(new FormHttpHandler(), "/");
        // Map the path /upload to the UploaderHttpHandler
        config.addHttpHandler(new UploaderHttpHandler(), "/upload");

        // Create HTTP network listener on host "0.0.0.0" and port 18080.
        final NetworkListener listener = new NetworkListener("Grizzly",
                NetworkListener.DEFAULT_NETWORK_HOST, PORT);

        server.addListener(listener);

        try {
            // Start the server
            server.start();

            LOGGER.log(Level.INFO, "Server listens on port {0}", PORT);
            LOGGER.log(Level.INFO, "Press enter to exit");
            System.in.read();
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, ioe.toString(), ioe);
        } finally {
            // Stop the server
            server.shutdownNow();
        }
    }
}
