import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;

import lab01.DOMReceiptReaderWriter;
import lab01.SAXReceiptReaderWriter;
import lab01.Receipt;
import lab01.StAXReceiptReaderWriter;

public class Main {
    public static void main(String[] args) {

        try {
            Receipt receipt;
            FileInputStream inputSAX = new FileInputStream(Path.of("src", "Receipt.xml").toFile());
            FileInputStream inputStAX = new FileInputStream(Path.of("src", "Receipt.xml").toFile());
            FileInputStream inputDOM = new FileInputStream(Path.of("src", "Receipt.xml").toFile());

            FileOutputStream output = new FileOutputStream(Path.of("src", "Store.xml").toFile());

            System.out.println("----- SAX -------");
            SAXReceiptReaderWriter readerWriter1 = new SAXReceiptReaderWriter();
            receipt = readerWriter1.loadReceipt(inputSAX);
            receipt.printReceipt();

            System.out.println("----- StAX -------");
            StAXReceiptReaderWriter readerWriter2 = new StAXReceiptReaderWriter();
            receipt = readerWriter2.loadReceipt(inputStAX);
            receipt.printReceipt();
            //readerWriter2.storeReceipt(output, receipt);

            System.out.println("----- DOM -------");
            DOMReceiptReaderWriter readerWriter3 = new DOMReceiptReaderWriter();
            receipt = readerWriter3.loadReceipt(inputDOM);
            receipt.printReceipt();
            readerWriter3.storeReceipt(output, receipt);

            inputSAX.close();
            inputDOM.close();
            inputStAX.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}