package faa_ocr.kml_parser;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;

/**
 * @author scott
 */
public class XMLtoKML
{
        //Initialized string to be written to the kml file
	private String kml_string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        private String kml_path = "";


        public XMLtoKML()
        {
        	//constructor
        }
        
        
        /**
	 * This method takes the XML representation of an airport and converts it to
	 * KML format.
	 * 
	 * @param xml_file Airport diagram in XML format
	 * @return String path of KML file
	 */
	public String writeKML(File xml_file)
	{
		try
		{
			Scanner scanner = new Scanner(xml_file);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document xml_doc = dBuilder.parse(xml_file);

			// Normalize the document
			xml_doc.getDocumentElement().normalize();

			// System.out.println("Root element : " +
			// xml_doc.getDocumentElement().getNodeName());
			
			kml_string += "\n" + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">";
			
			kml_string += "\n" + "  <Document>";
			kml_string += "\n" + "    <name>" //Airport name
				+ xml_doc.getElementsByTagName("name").item(0).getTextContent()
				+ "</name>";
			kml_string += "\n" + "    <description>" //Other info
				+ "\n     " + "Location: " + xml_doc.getElementsByTagName("location").item(0).getTextContent() + ","
				+ "\n     " + "Variation: " + xml_doc.getElementsByTagName("variation").item(0).getTextContent() + "." 
				+ "\n" + "    </description>";
			
			kml_string += "\n" + "    <Style id=\"runway\">";       //Style of Runway path
			kml_string += "\n" + "      <LineStyle>";
			kml_string += "\n" + "        <color>501400FF</color>"; //Red Line
			kml_string += "\n" + "        <width>10</width>";
			kml_string += "\n" + "      </LineStyle>";
			kml_string += "\n" + "    </Style>";
			kml_string += "\n" + "    <Style id=\"taxiway\">";      //Style of Taxiway Path
			kml_string += "\n" + "      <LineStyle>";
			kml_string += "\n" + "        <color>50000000</color>"; //Black line
			kml_string += "\n" + "        <width>8</width>";
			kml_string += "\n" + "      </LineStyle>";
			kml_string += "\n" + "    </Style>";
			
			
			// List of pathways
			NodeList path_list = xml_doc.getElementsByTagName("path");
					
			for (int temp = 0; temp < path_list.getLength(); temp++)
			{
				Node node = path_list.item(temp);

				// System.out.println("\nCurrent Element : " +
				// node.getNodeName());

				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					String type = element.getElementsByTagName("path_type").item(0).getTextContent();
										
					kml_string += "\n" + "    <Placemark>";
					kml_string += "\n" + "      <name>" + element.getElementsByTagName("path_name").item(0).getTextContent() + "</name>";  //Runway name
					kml_string += "\n" + "      <description>" //Other info
						+ "\n" + "       Path type: " + type + ","
						+ "\n" + "       Heading: " + element.getElementsByTagName("heading").item(0).getTextContent() + ","
						+ "\n" + "       Elevation: " + element.getElementsByTagName("elevation").item(0).getTextContent() + "."
						+ "\n" + "      </description>";
					if(type.equals("runway"))
					{
						kml_string += "\n" + "      <styleUrl>#runway</styleUrl>";
					}
					else
					{
						kml_string += "\n" + "      <styleUrl>#taxiway</styleUrl>";
					}
					kml_string += "\n" + "      <LineString>";
					kml_string += "\n" + "        <extrude>1</extrude>";
					kml_string += "\n" + "        <tessellate>1</tessellate>";
					kml_string += "\n" + "        <altitudeMode>absolute</altitudeMode>";
					kml_string += "\n" + "        <coordinates>";
							   // + "\n         " + element.getElementsByTagName("coordinates").item(0).getTextContent();
					kml_string += "\n" + "        </coordinates>";
					kml_string += "\n" + "      </LineString>";
					kml_string += "\n" + "    </Placemark>";
				}
			}
			kml_string += "\n" + "  </Document>";
			kml_string += "\n" + "</kml>";
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			
			// Define KML file path String
			kml_path = xml_file.getName().replace(".xml", ".kml");
			File kml_file = new File(kml_path);

			if (kml_file.createNewFile())
			{
				System.out.println("The file "+kml_file+" has been created.");
			} 
			else
			{
				System.out.println("There is already a file in this location named "+kml_file+".");
				System.out.println("A copy of "+kml_file+" has been created.");
				File kml_file_copy = new File("(Copy)"+kml_path);
				kml_file_copy.createNewFile();
				kml_file = kml_file_copy;
			}
			
			BufferedWriter output = new BufferedWriter(new FileWriter(kml_file));
			output.write(kml_string);
			output.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	return kml_path;
	}

}