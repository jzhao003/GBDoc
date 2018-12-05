package gbdoc.upload;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.EmptyCompletionHandler;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.ReadHandler;
import org.glassfish.grizzly.http.io.NIOInputStream;
import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.multipart.MultipartEntry;
import org.glassfish.grizzly.http.multipart.MultipartEntryHandler;
import org.glassfish.grizzly.http.multipart.MultipartScanner;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import gbdoc.db.DBUtils;
import gbdoc.db.DocTemplate;
import gubo.db.DaoManager;
import gubo.db.ISimplePoJo;
import gubo.db.SimplePoJoDAO;

public class UploaderHttpHandler extends HttpHandler {
	private static final Logger LOGGER = Grizzly.logger(UploaderHttpHandler.class);

    private static final String DESCRIPTION_NAME = "description";
    private static final String FILENAME_ENTRY = "fileName";

    // uploads counter (just for debugging/tracking reasons)
    private final AtomicInteger uploadsCounter = new AtomicInteger(1);

    /**
     * Service HTTP request.
     */
    @Override
    public void service(final Request request, final Response response)
            throws Exception {
        
        // Suspend the HTTP request processing
        // (in other words switch to asynchronous HTTP processing mode).
        response.suspend();

        // assign uploadNumber for this specific upload
        final int uploadNumber = uploadsCounter.getAndIncrement();

        LOGGER.log(Level.INFO, "Starting upload #{0}", uploadNumber);

        // Initialize MultipartEntryHandler, responsible for handling
        // multipart entries of this request
        final UploaderMultipartHandler uploader =
                new UploaderMultipartHandler(uploadNumber);

        // Start the asynchronous multipart request scanning...
        MultipartScanner.scan(request,
                uploader,
                new EmptyCompletionHandler<Request>() {
            // CompletionHandler is called once HTTP request processing is completed
            // or failed.
            @Override
            public void completed(final Request request) {
                // Upload is complete
                final int bytesUploaded = uploader.getBytesUploaded();
                
                LOGGER.log(Level.INFO, "Upload #{0}: is complete. "
                        + "{1} bytes uploaded",
                        new Object[] {uploadNumber, bytesUploaded});

                // Compose a server response.
                try {
                    response.setContentType("text/plain");
                    final Writer writer = response.getWriter();
                    writer.write("Completed. " + bytesUploaded + " bytes uploaded.");
                } catch (IOException ignored) {
                }

                // Resume the asychronous HTTP request processing
                // (in other words finish the asynchronous HTTP request processing).
                response.resume();
            }

            @Override
            public void failed(Throwable throwable) {
                // if failed - log the error
                LOGGER.log(Level.INFO, "Upload #" + uploadNumber + " failed", throwable);
                // Complete the asynchronous HTTP request processing.
                response.resume();
            }


        });
    }

    /**
     * {@link MultipartEntryHandler}, responsible for processing the upload.
     */
    private static final class UploaderMultipartHandler
            implements MultipartEntryHandler {
        
        // upload number
        private final int uploadNumber;
        // number of bytes uploaded
        private final AtomicInteger uploadedBytesCounter = new AtomicInteger();
        
        public UploaderMultipartHandler(final int uploadNumber) {
            this.uploadNumber = uploadNumber;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void handle(final MultipartEntry multipartEntry) throws Exception {
            // get the entry's Content-Disposition
            final ContentDisposition contentDisposition =
                    multipartEntry.getContentDisposition();
            // get the multipart entry name
            final String name = contentDisposition.getDispositionParamUnquoted("name");
            
//            System.out.println(contentDisposition.getDispositionParamUnquoted("name"));
//            System.out.println(contentDisposition.getDispositionParamUnquoted("sid"));
//            System.out.println("=======================");
            // if the multipart entry contains a file content
            if (FILENAME_ENTRY.equals(name)) {

                // get the filename for Content-Disposition
                final String filename =
                        contentDisposition.getDispositionParamUnquoted("filename");

                // Get the NIOInputStream to read the multipart entry content
                final NIOInputStream inputStream = multipartEntry.getNIOInputStream();

                LOGGER.log(Level.INFO, "Upload #{0}: uploading file {1}",
                        new Object[]{uploadNumber, filename});

                // start asynchronous non-blocking content read.
                inputStream.notifyAvailable(
                        new UploadReadHandler(uploadNumber, filename,
                        inputStream, uploadedBytesCounter));

            } else if (DESCRIPTION_NAME.equals(name)) { // if multipart entry contains a description field
                LOGGER.log(Level.INFO, "Upload #{0}: description came. "
                        + "Skipping...", uploadNumber);
                // skip the multipart entry
                multipartEntry.skip();
            }  else if ("id".equals(name)) { // if multipart entry contains a description field
                LOGGER.log(Level.INFO, "Upload #{0}: description came. "
                        + "Skipping...", uploadNumber);
                final NIOInputStream inputStream = multipartEntry.getNIOInputStream();
                inputStream.notifyAvailable(new ValueHandler(inputStream));
                
                // skip the multipart entry
                //multipartEntry.skip();
//                multipartEntry.getMultipartContext().getBoundary();
//                multipartEntry.getContentDisposition().getDisposition();
//                byte[] buf = new byte[2048];
//                int readBytes = multipartEntry.getNIOInputStream().read(buf);
//                String value = new String(buf,0,readBytes,"UTF-8");
//                System.out.println(value);
            } else { // Unexpected entry?
                LOGGER.log(Level.INFO, "Upload #{0}: unknown multipart entry. "
                        + "Skipping...", uploadNumber);
                // skip it
                multipartEntry.skip();
            }
        }

        /**
         * Returns the number of bytes uploaded for this multipart entry.
         * 
         * @return the number of bytes uploaded for this multipart entry.
         */
        int getBytesUploaded() {
            return uploadedBytesCounter.get();
        }
    }
    
    private static class ValueHandler implements ReadHandler {
    	private final byte[] buf;
    	private final NIOInputStream inputStream;
    	private ValueHandler(final NIOInputStream inputStream)
                throws FileNotFoundException {

            this.inputStream = inputStream;
//            this.uploadedBytesCounter = uploadedBytesCounter;
            buf = new byte[2048];
        }
		@Override
		public void onAllDataRead() throws Exception {
			// TODO Auto-generated method stub
			while (inputStream.isReady()) {
                // read the available bytes from input stream
                final int readBytes = inputStream.read(buf);
                // update the counter
                String value = new String(buf,0,readBytes,"UTF-8");
                System.out.println(value);
            }
		}
		@Override
		public void onDataAvailable() throws Exception {
			// TODO Auto-generated method stub
			while (inputStream.isReady()) {
                // read the available bytes from input stream
                final int readBytes = inputStream.read(buf);
                // update the counter
                String value = new String(buf,0,readBytes,"UTF-8");
                System.out.println(value);
            }
		}
		@Override
		public void onError(Throwable arg0) {
			// TODO Auto-generated method stub
			
		}
    }

    /**
     * Simple {@link ReadHandler} implementation, which is reading HTTP request
     * content (uploading file) in non-blocking mode and saves the content into
     * the specific file.
     */
    private static class UploadReadHandler implements ReadHandler {

        // the upload number
        private final int uploadNumber;
        // Non-blocking multipart entry input stream
        private final NIOInputStream inputStream;

        // the destination file output stream, where we save the data.
        private final FileOutputStream fileOutputStream;
        
        // temporary buffer
        private final byte[] buf;

        // uploaded bytes counter
        private final AtomicInteger uploadedBytesCounter;
        
        private UploadReadHandler(final int uploadNumber,
                final String filename,
                final NIOInputStream inputStream,
                final AtomicInteger uploadedBytesCounter)
                throws FileNotFoundException {

            this.uploadNumber = uploadNumber;
            fileOutputStream = new FileOutputStream("upload/"+filename);
            this.inputStream = inputStream;
            this.uploadedBytesCounter = uploadedBytesCounter;
            buf = new byte[2048];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDataAvailable() throws Exception {
            // save available file content
            readAndSaveAvail();

            // register this handler to be notified next time some data
            // becomes available
            inputStream.notifyAvailable(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onAllDataRead() throws Exception {
            // save available file content
            readAndSaveAvail();
            // finish the upload
            finish();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onError(Throwable t) {
            LOGGER.log(Level.WARNING, "Upload #" + uploadNumber + ": failed", t);
            // finish the upload
            finish();
        }

        /**
         * Read available file content data out of HTTP request and save the
         * chunk into local file output stream
         * 
         * @throws IOException
         */
        private void readAndSaveAvail() throws IOException {
            while (inputStream.isReady()) {
                // read the available bytes from input stream
                final int readBytes = inputStream.read(buf);
                // update the counter
                uploadedBytesCounter.addAndGet(readBytes);
                // save the file content to the file
                fileOutputStream.write(buf, 0, readBytes);
            }
        }

        /**
         * Finish the file upload
         */
        private void finish() {
            try {
                // close file output stream
                fileOutputStream.close();
                
                
                // call api to insert into db
                DocTemplate newPojo = new DocTemplate();
//                Long newid = InsertStatementGenerator.insertNew(dbconn, newPojo);
//    			newPojo.setId(newid);
//    			dbconn.commit();
    			
                // 取得standard_section_id
//                for () {
//                	
//                }
                
    			DaoManager daoManager = new DaoManager();
    			SimplePoJoDAO dao = daoManager.getDao(DocTemplate.class);
    			newPojo.standard_id=1L;
    			newPojo.standard_section_id=1L;
//    			dao.insert(DBUtils.getDataSource(), newPojo);
    			
            } catch (Exception e) {
            	System.err.println(e);
            }
        }
    }
}
