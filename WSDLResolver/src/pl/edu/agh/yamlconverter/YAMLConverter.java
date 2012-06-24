package pl.edu.agh.yamlconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.yaml.snakeyaml.Yaml;

public class YAMLConverter {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, TransformerException {
		// TODO Auto-generated method stub
		InputStream io = new FileInputStream("UserService.yml");
		Yaml yaml = new Yaml();
		Object obj = yaml.load(io);
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
		System.out.println(map);
		
		YAMLConverter yamlConverter = new YAMLConverter();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(yamlConverter.parse("UserService.yml"));
		StreamResult result = new StreamResult(new File("result.xml"));
		StreamResult resultConsole = new StreamResult(System.out);
		transformer.transform(source, result);
		transformer.transform(source, resultConsole);
	}
	
	/**
	 * Metoda wczytuje podany plik YAML i parsuje go do pliku WSDL
	 * @param path
	 * @throws FileNotFoundException 
	 */
	public Document parse(String path) throws ParserConfigurationException, FileNotFoundException {
		WSDLBuilder builder = new WSDLBuilder();
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) loadYaml(path);
		
		//stworzenie g³ównego elementu
		builder.createDocument().createRootElement((LinkedHashMap<String, Object>) map.get("attributes"));
		
		//stworzenie sekcji types
		LinkedHashMap<String, Object> typesMap = (LinkedHashMap<String, Object>) map.get("types");
		LinkedHashMap<String, Object> schemaMap = (LinkedHashMap<String, Object>) typesMap.get("schema");
		ArrayList<Object> complexTypeList = (ArrayList<Object>) schemaMap.get("complexTypeList");
		builder.createTypesElement((LinkedHashMap<String, Object>) typesMap.get("attributes"));
		builder.createSchemaElement((LinkedHashMap<String, Object>) schemaMap.get("attributes"));
		for(Object complexType : complexTypeList) {
			LinkedHashMap<String, Object> ct = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) complexType).get("complexType");
			LinkedHashMap<String, Object> ct_sequence = (LinkedHashMap<String, Object>) ct.get("sequence");
			ArrayList<Object> elementList = (ArrayList<Object>) ct_sequence.get("elementList");
			builder.createComplexTypeElement((LinkedHashMap<String, Object>) ct.get("attributes"));
			builder.createSequenceElement((LinkedHashMap<String, Object>) ct_sequence.get("attributes"));
			for(Object element : elementList) {
				LinkedHashMap<String, Object> el = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) element).get("element");
				builder.createElementElement((LinkedHashMap<String, Object>) el.get("attributes"));
			}
		}
		
		//stworzenie sekcji message
		ArrayList<Object> messageList = (ArrayList<Object>) map.get("messageList");
		for(Object message : messageList) {
			LinkedHashMap<String, Object> msg = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) message).get("message");
			ArrayList<Object> partList = (ArrayList<Object>) msg.get("partList");
			builder.createMessageElement((LinkedHashMap<String, Object>) msg.get("attributes"));
			if(partList != null) {
				for(Object p : partList) {
					LinkedHashMap<String, Object> part = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) p).get("part");
					builder.createPartElement((LinkedHashMap<String, Object>) part.get("attributes"));
				}
			}
		}
		
		//stworzenie sekcji portType
		LinkedHashMap<String, Object> portTypeMap = (LinkedHashMap<String, Object>) map.get("portType");
		builder.createPortTypeElement((LinkedHashMap<String, Object>) portTypeMap.get("attributes"));
		ArrayList<Object> operationList = (ArrayList<Object>) portTypeMap.get("operationList");
		for(Object operation : operationList) {
			LinkedHashMap<String, Object> op = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) operation).get("operation");
			builder.createOperationElement((LinkedHashMap<String, Object>) op.get("attributes"));
			builder.createInputElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) op.get("input")).get("attributes"));
			builder.createOutputElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) op.get("output")).get("attributes"));
		}
		
		//stworzenie sekcji binding
		LinkedHashMap<String, Object> bindingMap = (LinkedHashMap<String, Object>) map.get("binding");
		builder.createBindingElement((LinkedHashMap<String, Object>) bindingMap.get("attributes"));
		builder.createInsideBindingElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) bindingMap.get("binding")).get("attributes"));
		ArrayList<Object> operationList2 = (ArrayList<Object>) bindingMap.get("operationList");
		for(Object operation : operationList2) {
			LinkedHashMap<String, Object> op = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) operation).get("operation");
			builder.createBindingOperationElement((LinkedHashMap<String, Object>) op.get("attributes"));
			builder.createInsideBindingOperationElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) op.get("operation")).get("attributes"));
			builder.createInsideInputElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) op.get("input")).get("attributes"));
			builder.createInsideInputBodyElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) op.get("input")).get("body")).get("attributes"));
			builder.createInsideOutputElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) op.get("output")).get("attributes"));
			builder.createInsideOutputBodyElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) op.get("output")).get("body")).get("attributes"));
		}
		
		//stworzenie sekcji service
		LinkedHashMap<String, Object> serviceMap = (LinkedHashMap<String, Object>) map.get("service");
		LinkedHashMap<String, Object> portMap = (LinkedHashMap<String, Object>) serviceMap.get("port");
		builder.createServiceElement((LinkedHashMap<String, Object>) serviceMap.get("attributes"));	
		builder.createPortElement((LinkedHashMap<String, Object>) portMap.get("attributes"));
		builder.createAddressElement((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) portMap.get("address")).get("attributes"));
		return builder.getDocument();
	}
	
	public Map loadYaml(String path) throws FileNotFoundException {
		InputStream io = new FileInputStream(path);
		Yaml yaml = new Yaml();
		Object obj = yaml.load(io);
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
		System.out.println("Loaded hashmap:");
		System.out.println(map);
		return map;
	}

}
