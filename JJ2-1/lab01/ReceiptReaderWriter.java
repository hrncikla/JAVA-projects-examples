package lab01;

import java.io.InputStream;
import java.io.OutputStream;

public interface ReceiptReaderWriter {
    /**
     * Read the XML file from the stream and create
     * the corresponding object representing the receipt according to it.
     */
    public Receipt loadReceipt(InputStream input) throws Exception;

    /**
     * Saves an XML file to the corresponding stream
     * representing the given receipt.
     */
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception;
}

