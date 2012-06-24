package pl.edu.agh.yamlconverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class WSDLBuilder {
	private Document doc;
	
	public WSDLBuilder createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		return this;
	}
	
	public WSDLBuilder createRootElement(LinkedHashMap<String, Object> attr) {
		Element rootElement = doc.createElement("definitions");
		setAttributes(rootElement, attr);
		rootElement.setAttribute("xmlns:ns1", "http://schemas.xmlsoap.org/soap/http");
		rootElement.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/wsdl/soap/");
		rootElement.setAttribute("xmlns:wsdl", "http://schemas.xmlsoap.org/wsdl/");
		rootElement.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		doc.appendChild(rootElement);
		return this;
	}
	
	public WSDLBuilder createTypesElement(LinkedHashMap<String, Object> attr) {
		Element types = doc.createElement("types");
		if(attr != null)
			setAttributes(types, attr);
		doc.getElementsByTagName("definitions").item(0).appendChild(types);
		return this;
	}
	
	public WSDLBuilder createSchemaElement(LinkedHashMap<String, Object> attr) {
		Element element = doc.createElement("schema");
		if(attr != null)
			setAttributes(element, attr);
		doc.getElementsByTagName("types").item(0).appendChild(element);
		return this;
	}
	
	public WSDLBuilder createComplexTypeElement(LinkedHashMap<String, Object> attr) {
		Element element = doc.createElement("complexType");
		if(attr != null)
			setAttributes(element, attr);
		doc.getElementsByTagName("schema").item(0).appendChild(element);
		return this;
	}
	
	public WSDLBuilder createSequenceElement(LinkedHashMap<String, Object> attr) {
		Element element = doc.createElement("sequence");
		if(attr != null)
			setAttributes(element, attr);
		NodeList nodeList = doc.getElementsByTagName("complexType");
		nodeList.item(nodeList.getLength()-1).appendChild(element);
		return this;
	}
	
	public WSDLBuilder createElementElement(LinkedHashMap<String, Object> attr) {
		Element element = doc.createElement("element");
		if(attr != null)
			setAttributes(element, attr);
		NodeList nodeList = doc.getElementsByTagName("sequence");
		nodeList.item(0).appendChild(element);
		return this;
	}
	
	public void setAttributes(Element el, LinkedHashMap<String, Object> attr) {
		Set<String> s = attr.keySet();
		for(String at : s) {
			el.setAttribute(at, attr.get(at).toString());
		}
	}
	
	public Document getDocument() {
		return doc;
	}
	
}
