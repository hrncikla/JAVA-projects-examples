package lab01;

import org.xml.sax.SAXException;

import javax.xml.stream.*;
import java.io.InputStream;
import java.io.OutputStream;

public class StAXReceiptReaderWriter implements ReceiptReaderWriter {

    private Receipt receipt;
    private StringBuilder text;

    int amountCurrentItem;
    int unitPriceCurrentItem;
    String nameCurrentItem;

    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(input);

        boolean done = false;
        while (!done) {
            switch (reader.getEventType()) {
                case XMLStreamReader.START_ELEMENT:
                    String name = reader.getLocalName();

                    if ("receipt".equals(name)) {
                        try {
                            int total = Integer.parseInt(reader.getAttributeValue(null, "total"));
                            receipt = new Receipt(total);
                        } catch (NumberFormatException e) {
                            throw new SAXException("Missing argument 'total'");
                        }

                    } else if ("item".equals(name)) {
                        try {
                            amountCurrentItem = Integer.parseInt(reader.getAttributeValue(null, "amount"));
                            unitPriceCurrentItem = Integer.parseInt(reader.getAttributeValue(null, "unitPrice"));

                        } catch (NumberFormatException e) {
                            throw new SAXException("Missing argument 'amount' or 'unitPrice'");
                        }
                    }
                    text = new StringBuilder();
                    break;

                case XMLStreamReader.END_ELEMENT:
                    String nameEndElement = reader.getLocalName();

                    if ("name".equals(nameEndElement)) {
                        receipt.setName(text.toString().trim());
                    } else if ("itin".equals(nameEndElement)) {
                        receipt.setItin(text.toString().trim());
                    } else if ("item".equals(nameEndElement)) {
                        nameCurrentItem = text.toString().trim();
                        receipt.addNewItem(nameCurrentItem, amountCurrentItem, unitPriceCurrentItem);
                    }
                    break;

                case XMLStreamReader.CHARACTERS:
                    text.append(reader.getText());
                    break;

                default:
                    break;
            }
            if (reader.hasNext()) {
                reader.next();
            } else {
                done = true;
            }
        }
        return receipt;
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {

        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(output);

        xmlWriter.writeStartDocument();

        xmlWriter.writeStartElement("receipt");
        xmlWriter.writeAttribute("total", String.valueOf(receipt.getTotal()));

        xmlWriter.writeStartElement("name");
        xmlWriter.writeCharacters(receipt.getName());
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("itin");
        xmlWriter.writeCharacters(receipt.getItin());
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("items");

        if (receipt.getItems() != null) {
            for (Receipt.Item item : receipt.getItems()) {
                xmlWriter.writeStartElement("item");
                xmlWriter.writeAttribute("amount", String.valueOf(item.getAmount()));
                xmlWriter.writeAttribute("unitPrice", String.valueOf(item.getUnitPrice()));
                xmlWriter.writeCharacters(item.getName());
                xmlWriter.writeEndElement();
            }
        }

        xmlWriter.writeEndElement();
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
    }
}
