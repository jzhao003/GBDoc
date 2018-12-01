package gbdoc.upload;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class FormHttpHandler extends HttpHandler {
	@Override
    public void service(final Request request, final Response response)
            throws Exception {
        // Set the response content type
        response.setContentType("text/html");

        // Return the HTML upload form
        response.getWriter().write(
                "<form action=\"upload\" method=\"post\" enctype=\"multipart/form-data\">"
                + "Description: <input name=\"description\"/><br/>"
                + "Select File: <input type=\"file\" name=\"fileName\"/><br/>"
                + "<input type=\"submit\" value=\"Submit\"/></form>");
    }
}
