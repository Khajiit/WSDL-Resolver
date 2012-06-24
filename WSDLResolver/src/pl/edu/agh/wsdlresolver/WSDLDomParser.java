package pl.edu.agh.wsdlresolver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WSDLDomParser{

	private Document doc;
	private Map<Node,Map<String,Map<String,NodeList>>> operationsMap;
	private NodeList messagesList;
	private Map<String, NodeList> complexTypeMap;
	
	public WSDLDomParser(String filename) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		doc = docBuilder.parse (new File(filename));
		doc.getDocumentElement ().normalize ();
		NodeList operationsList = doc.getElementsByTagName("wsdl:operation");
		messagesList = doc.getElementsByTagName("wsdl:message");
		createOperationsMap(operationsList);
		NodeList complexTypeList = doc.getElementsByTagName("xs:complexType");
		createComplexTypeMap(complexTypeList);
	}

	private void createComplexTypeMap(NodeList complexTypeList) {
		complexTypeMap = new HashMap<String, NodeList>();
		for(int s=0; s<complexTypeList.getLength(); s++){
			Node complexTypeNode = complexTypeList.item(s);
			Element complexTypeElement = (Element)complexTypeNode;
			NodeList elementList = complexTypeElement.getElementsByTagName("xs:element");
			complexTypeMap.put(complexTypeElement.getAttribute("name"), elementList);
		}
		
		
	}

	private Map<Node, Map<String, Map<String, NodeList>>> createOperationsMap(NodeList operationsList) {
		operationsMap = new HashMap<Node, Map<String, Map<String, NodeList>>>();
		Map<String, Map<String, NodeList>> allParamsMap;
		for(int s=0; s<operationsList.getLength(); s++){
			Node operationNode = operationsList.item(s);
			if(operationNode.getNodeType() == Node.ELEMENT_NODE){
				Element operationElement = (Element)operationNode;
				Map<String, NodeList> inParametersMap = resolveParameters(operationElement, "wsdl:input");
				Map<String, NodeList> outParametersMap = resolveParameters(operationElement, "wsdl:output");
				allParamsMap = new HashMap<String, Map<String, NodeList>>();
				allParamsMap.put("input", inParametersMap);
				allParamsMap.put("output", outParametersMap);
				operationsMap.put(operationNode, allParamsMap);
			}

		}
		return operationsMap;
	}

	private Map<String, NodeList> resolveParameters(Element operationElement, String tag) {
		NodeList typesList=null;
		Map<String, NodeList> messageMap = new HashMap<String, NodeList>();
		NodeList inParametersList = operationElement.getElementsByTagName(tag);
		for(int j=0; j<inParametersList.getLength();j++){
			Node parameterNode = inParametersList.item(j);
			if(parameterNode.getNodeType() ==  Node.ELEMENT_NODE){
				Element parameterElement = (Element)parameterNode;
				String nodeName = parameterElement.getAttribute("name");
				Node messageNode = getMessageByName(nodeName);
				Element messageElement = (Element)messageNode;
				typesList = messageElement.getElementsByTagName("wsdl:part");
				messageMap.put(messageElement.getAttribute("name"), typesList);
			}
		}
		return messageMap;
	}

	private Node getMessageByName(String nodeName) {
		Node messageNode;
		for(int j=0; j < messagesList.getLength();j++){
			messageNode = messagesList.item(j);
			if(messageNode.getNodeType() ==  Node.ELEMENT_NODE){
				Element parameterElement = (Element)messageNode;
				if(parameterElement.getAttribute("name").equals(nodeName)){
					return messageNode;
				}
			}
		}
		return null;
	}

//	public int getWebMethodsCount(){
//		return operationsMap.get();
//	}

	public String getRootElement() {
		return doc.getDocumentElement().getNodeName();
	}

	public Map<Node, Map<String, Map<String, NodeList>>> getOperationsMap() {
		return operationsMap;
	}

	public Map<String, NodeList> getComplexTypeMap() {
		return complexTypeMap;
	}

}