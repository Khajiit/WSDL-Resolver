package pl.edu.agh.yamlconverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.yaml.snakeyaml.Yaml;

public class YAMLConverter {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		InputStream io = new FileInputStream("UserService.yml");
		Yaml yaml = new Yaml();
		Object obj = yaml.load(io);
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
		System.out.println(map);
	}
	
	/**
	 * Metoda wczytuje podany plik YAML i parsuje go do pliku WSDL
	 * @param path
	 * @throws ParserConfigurationException 
	 */
	public Document parse(String path) throws ParserConfigurationException {
		//tworzenie nowego dokumentu
		
		
						
		
		return null;
	}

}
