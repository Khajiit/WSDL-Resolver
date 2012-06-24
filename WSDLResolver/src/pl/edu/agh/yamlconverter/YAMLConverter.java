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
//		StreamResult result = new StreamResult(new File("result.xml"));
		StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);
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
		System.out.println(complexTypeList);
		
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
