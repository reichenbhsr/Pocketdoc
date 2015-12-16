package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by Roman on 16.12.2015.
 */
public class XMLReader {

    private Document doc;

    public XMLReader(String file){

        try {

            File xmlFile = new File(getClass().getClassLoader().getResource(file).getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getValue(String config){

        NodeList nList = doc.getElementsByTagName(config);

        if (nList.getLength() > 0){
            Element ele = (Element) nList.item(0);
            return ele.getTextContent();
        }

        return null;

    }

}
