package lab01;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.OutputStream;

public class SAXReceiptReaderWriter implements ReceiptReaderWriter {

    private Receipt receipt;
    private StringBuilder text;

    int amountCurrentItem;
    int unitPriceCurrentItem;
    String nameCurrentItem;

    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();

        parser.parse(input, new DefaultHandler() {

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if ("receipt".equals(qName)) {
                    try {
                        int total = Integer.parseInt(attributes.getValue("total"));
                        receipt = new Receipt(total);
                    } catch (NumberFormatException e) {
                        throw new SAXException("Missing argument 'total'.");
                    }

                } else if ("item".equals(qName)) {
                    try {
                        amountCurrentItem = Integer.parseInt(attributes.getValue("amount"));
                        unitPriceCurrentItem = Integer.parseInt(attributes.getValue("unitPrice"));

                    } catch (NumberFormatException e) {
                        throw new SAXException("Missing argument 'amount' or 'unitPrice'.");
                    }
                }
                text = new StringBuilder();
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if ("name".equals(qName)) {
                    receipt.setName(text.toString().trim());
                } else if ("itin".equals(qName)) {
                    receipt.setItin(text.toString().trim());
                } else if ("item".equals(qName)) {
                    nameCurrentItem = text.toString().trim();
                    receipt.addNewItem(nameCurrentItem, amountCurrentItem, unitPriceCurrentItem);
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                text.append(ch, start, length);
            }
        });
        return receipt;
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        throw new UnsupportedOperationException("Writing is not supported in SAXReceiptReaderWriter!");
    }
}
