package pl.edu.agh.wsdlresolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
		if(args.length != 1){
			System.out.println("Invalid number of parameters. Application will exit.");
			System.exit(0);
		}
		try{
			WSDLDomParser wsdlParser = new WSDLDomParser(args[0]);
			System.out.println("Service name: " + wsdlParser.getServiceName());
			System.out.println("The root element is " + wsdlParser.getRootElement());
			showPorts(wsdlParser.getPorts());
			Element methodElement;
			System.out.println();
			System.out.println("====== WEBMETHODS ======");
			for (Entry<Node, Map<String, Map<String, NodeList>>> entry : wsdlParser.getOperationsMap().entrySet()) {
				methodElement = (Element)entry.getKey();
				System.out.println("Webmethod: " + methodElement.getAttribute("name"));
				System.out.println("Parameters:");
				showParameters(entry, "input");
				System.out.println("Returns:");
				showParameters(entry, "output");
			}
			System.out.println("======= TYPES =======");
			for(Entry<String, NodeList> typeEntry : wsdlParser.getComplexTypeMap().entrySet()){
				String typeName = typeEntry.getKey();
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

	private static String readFromUrl(String url) {
		URL wsdlUrl;
		StringBuilder wsdl = new StringBuilder();
		try {
			wsdlUrl = new URL(url);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(wsdlUrl.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null){
//				System.out.println(inputLine);
				wsdl.append(inputLine);
			}
			in.close();	
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return wsdl.toString();
		}
	}

	private static void showPorts(NodeList portList) {
		Element portElement;
		Element addressElement;
		for(int i=0; i < portList.getLength(); i++){
			portElement = (Element)portList.item(i);
			System.out.println("Port name: " + portElement.getAttribute("name") + ", binding: " + portElement.getAttribute("binding"));
			Node addressNode = portElement.getElementsByTagName("soap:address").item(0);
			addressElement = (Element)addressNode;
			System.out.println(createIndent(1) + "Address: " + addressElement.getAttribute("location"));
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
