import java.io.IOException;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileCreator {

    public static void main(String[] args) {

        String file = args[0];
        BufferedReader br = null;
        String line = "";
        String splitBy = "\\t+";

        String elementType = "";

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // root elements
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("table");
            doc.appendChild(rootElement);

            br = new BufferedReader(new FileReader(file));
            int j = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(splitBy);
                System.out.println(
                    "Values [nconst= " + values[0] +
                    ", name: " + values[1] +
                    ", birthYear: " + values[2] + 
                    ", deathYear: " + values[3] +
                    ", primaryProfession: " + values[4] +
                    ", knownFor: " + values[5] +
                    "]"
                    );

                
                if(j == 0) elementType = "th";
                else elementType = "td";

                Element row = doc.createElement("tr");
                rootElement.appendChild(row);

                for(int i = 0; i < values.length; i++) {
                    Element rowItem = doc.createElement(elementType);
                    rowItem.appendChild(doc.createTextNode(values[i]));
                    row.appendChild(rowItem);
                }
                j++;
            }

            // Write the content to XML file
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("output.xml"));

            transformer.transform(source, result);

        }  catch(IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException pce) {
		    pce.printStackTrace();
	    } catch (TransformerException tfe) {
		    tfe.printStackTrace();
	    }
    }
}