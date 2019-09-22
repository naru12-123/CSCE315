import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class XMLTSVParser {

   public static void main(String[] args) {

      try {
         File inputFile = new File("input.txt");

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize(); // Up to this point is setup

         PrintWriter writer = new PrintWriter("output.tsv", "UTF-8");

         writer.println("Root element : " + doc.getDocumentElement().getNodeName()); // Print the start element
         
         NodeList tableRows = doc.getElementsByTagName("tr"); // Initialize a variable with all the rows in it

         writer.println("----------------------------");

         /************** Header For .tsv ****************/
         Node rowItems = tableRows.item(0);
         Element tableElement = (Element) rowItems;
         NodeList rowItemsHeader = tableElement.getElementsByTagName("th"); // The items of the row
         for(int i = 0; i < rowItemsHeader.getLength(); i++) {
             if(i == 0) writer.print(rowItemsHeader.item(0).getTextContent());
             else  writer.print("\t" + rowItemsHeader.item(i).getTextContent());
         }
         /***********************************************/

         for (int i = 1; i < tableRows.getLength(); i++) { // Iterate over the total number of rows
            rowItems = tableRows.item(i);
            tableElement = (Element) rowItems;
            NodeList rowItemsInfo = tableElement.getElementsByTagName("td"); // The items of the row
            if (rowItems.getNodeType() == Node.ELEMENT_NODE) {
               writer.println("");
               for(int j = 0; j < rowItemsInfo.getLength(); j++) {
                  if(j == 0) writer.print(rowItemsInfo.item(0).getTextContent());
                  else writer.print("\t" + rowItemsInfo.item(j).getTextContent());
               }
            }
         }
         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}