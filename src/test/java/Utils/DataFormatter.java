package Utils;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

public class DataFormatter {

    public String formatXMLStringRemoveEmptyTags(String inputXMLContents) throws Exception {
        String formattedXML=null;

        //Read string as document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(inputXMLContents)));

        //Remove empty tags form document
        removeEmptyXMLTags(doc.getDocumentElement());

        //Convert document to string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source,result);
        formattedXML = writer.toString();
        return formattedXML;
    }

    private static void removeEmptyXMLTags(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyXMLTags(child);
                if (child.getTextContent().trim().isEmpty()) {
                    node.removeChild(child);
                }
            }
        }//end for
    }//end removeEmptyTags

    public String formatJsonStringRemoveEmptyElements(String inputJsonContents) {
        String updatedJson=null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;

            jsonNode = objectMapper.readTree(inputJsonContents);

            //System.out.println("Removing empty json");
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                JsonNode child = it.next();
                //System.out.println("working on element:"+child.toString()+":"+child.textValue());
                //System.out.println("child.isEmpty():"+child.isEmpty()+"  --child.isNull():"+child.isNull());
                if (child.isNull()) {
                    it.remove(); // Remove null values
                } else if (child.isObject() && child.isEmpty(null)) {
                    it.remove();// Remove null objects
                }else if (child.isArray() && child.isEmpty()) {
                    it.remove(); //Remove null arrays
                }
            }
            updatedJson = objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return updatedJson;
    }//end formatJsonStringRemoveEmptyElements

}//end class

