import java.io.PrintWriter;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

// import org.w3c.dom.Attr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileProcessor {

    public static void main(String[] args) {
        FileParser(args);
    }

    public static void FileParser(String[] args) {

        try {
            File inputFile = new File(args[0]);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize(); // Up to this point is setup for document

            PrintWriter writer = new PrintWriter("output.tsv", "UTF-8");

            // writer.println("Root element : " + doc.getDocumentElement().getNodeName()); // Print the start element
            
            NodeList tableRows = doc.getElementsByTagName("tr"); // Initialize a variable with all the rows in it

            // writer.println("----------------------------");

            /************** Header For .tsv ****************/
            Node rowItems = tableRows.item(0);
            Element tableElement = (Element) rowItems;
            NodeList rowItemsHeader = tableElement.getElementsByTagName("th"); // The items of the row

            for(int i = 0; i < rowItemsHeader.getLength(); i++) {
                if(i == 0) writer.print(rowItemsHeader.item(0).getTextContent()); // Print first item w/o tab
                else  writer.print("\t" + rowItemsHeader.item(i).getTextContent()); // Print the rest of the elements w/ tab
            }
            /***********************************************/

            /**************** Body For .tsv ****************/
            for (int i = 1; i < tableRows.getLength(); i++) { // Iterate over the total number of rows

                rowItems = tableRows.item(i);
                tableElement = (Element) rowItems; // Convert rowItems (Node type) to tableElement (Element type)

                NodeList rowItemsInfo = tableElement.getElementsByTagName("td"); // The items of the row (NodeList type)

                if (rowItems.getNodeType() == Node.ELEMENT_NODE) {
                writer.println(""); // Begin a new line each time
                for(int j = 0; j < rowItemsInfo.getLength(); j++) { // Loop over all the items in a row
                    if(j == 0) writer.print(rowItemsInfo.item(0).getTextContent()); // Print first item w/o tab
                    else writer.print("\t" + rowItemsInfo.item(j).getTextContent()); // Print the rest of the elements w/ tab
                }
                }
            }
            /***********************************************/

            writer.close(); // Close file for writing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void FileCreator(String[] args) {

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