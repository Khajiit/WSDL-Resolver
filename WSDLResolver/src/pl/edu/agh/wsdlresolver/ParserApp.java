package pl.edu.agh.wsdlresolver;

import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParserApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			WSDLDomParser wsdlParser = new WSDLDomParser("UserService.xml");
			System.out.println("The root element is " + wsdlParser.getRootElement());
			Element methodElement;
			for (Entry<Node, Map<String, Map<String, NodeList>>> entry : wsdlParser.getOperationsMap().entrySet()) {
				methodElement = (Element)entry.getKey();
				System.out.println("========");
				System.out.println("Webmethod: " + methodElement.getAttribute("name"));
				System.out.println("Parameters:");
				showParameters(entry, "input");
				System.out.println("Returns:");
				showParameters(entry, "output");
			}
			for(Entry<String, NodeList> typeEntry : wsdlParser.getComplexTypeMap().entrySet()){
				String typeName = typeEntry.getKey();
				System.out.println("======= TYPES =======");
				System.out.println("Typename: " + typeName);
				System.out.println("Fields: ");
				showFields(typeEntry);
				
			}
		}
		catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " 
					+ err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());

		}catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();

		}catch (Throwable t) {
			t.printStackTrace ();
		}

	}

	private static void showFields(Entry<String, NodeList> typeEntry) {
		Element typeElement;
		for(int i=0; i < typeEntry.getValue().getLength(); i++){
			typeElement = (Element)typeEntry.getValue().item(i);
			System.out.println(createIndent(1) + "-name: " + typeElement.getAttribute("name")
					+ ", type: " + typeElement.getAttribute("type"));
		}
		System.out.println();
	}

	private static void showParameters(Entry<Node, Map<String, Map<String, NodeList>>> entry, String type) {
		Element parameterElement;
		for (Entry<String, NodeList> messageEntry : entry.getValue().get(type).entrySet()) {
			String messageName = messageEntry.getKey();
			System.out.println(createIndent(1) + "Response name: " + messageName);
			for(int i=0; i < messageEntry.getValue().getLength(); i++){
				parameterElement = (Element)messageEntry.getValue().item(i);
				System.out.println(createIndent(1) + "-name: " + parameterElement.getAttribute("name")
						+ ", type: " + parameterElement.getAttribute("type"));
			}
		}
		System.out.println();
	}

	private static String createIndent(int n) {
		StringBuilder gap = new StringBuilder("");
		for(int i=0;i<n;i++){
			gap.append("   ");
		}
		return gap.toString();
	}

}
