package pl.edu.agh.yamlconverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;

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

}
