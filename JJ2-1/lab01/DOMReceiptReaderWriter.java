package lab01;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DOMReceiptReaderWriter implements ReceiptReaderWriter {

    private Receipt receipt;

    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(input);

        Element root = doc.getDocumentElement();
        receipt = new Receipt(Integer.parseInt(root.getAttribute("total")));

        setName(receipt, root.getChildNodes().item(0).getTextContent());
        setItin(receipt, root.getChildNodes().item(1).getTextContent());
        setItems(receipt, root.getElementsByTagName("item"));

        return receipt;

    }

    private void setName(Receipt receipt, String name) {
        receipt.setName(name);
    }

    private void setItin(Receipt receipt, String itin) {
        receipt.setItin(itin);
    }

    private void setItems(Receipt receipt, NodeList items) {

        for (int i = 0; i < items.getLength(); i++) {
            Element itemElement = (Element) items.item(i);
            String itemName = itemElement.getTextContent();
            String itemPrice = itemElement.getAttribute("unitPrice");
            String itemAmount = itemElement.getAttribute("amount");
            receipt.addNewItem(itemName, Integer.parseInt(itemAmount), Integer.parseInt(itemPrice));
        }
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();

        Element rootElement = doc.createElement("receipt");
        rootElement.setAttribute("total", String.valueOf(receipt.getTotal()));
        doc.appendChild(rootElement);

        Element nameElement = doc.createElement("name");
        nameElement.setTextContent(receipt.getName());
        rootElement.appendChild(nameElement);

        Element itinElement = doc.createElement("itin");
        itinElement.setTextContent(receipt.getItin());
        rootElement.appendChild(itinElement);

        Element itemsElement = doc.createElement("items");
        rootElement.appendChild(itemsElement);

        if (receipt.getItems() != null) {
            for (Receipt.Item item : receipt.getItems()) {
                Element itemElement = doc.createElement("item");
                itemElement.setTextContent(item.getName());
                itemElement.setAttribute("amount", String.valueOf(item.getAmount()));
                itemElement.setAttribute("unitPrice", String.valueOf(item.getUnitPrice()));
                itemsElement.appendChild(itemElement);
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }
}
