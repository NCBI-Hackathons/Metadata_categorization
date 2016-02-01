package gov.nih.ncbi.ontology;

import gov.nih.ncbi.data.TsvParser;

import java.util.HashMap;
import java.util.Map;

public class OntologyLibrary {
	
	private static Map<String, String> ontoIdToLabel = new HashMap<>();
	
	public static Map<String, String> populateMap (String tsvFile) {
		try {
			TsvParser parser = new TsvParser(tsvFile);
			parser.readHeaders();
			while (parser.readRecord()) {
				ontoIdToLabel.put(parser.get("ontoid"), parser.get("label"));
			}
			parser.close();
			return ontoIdToLabel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ontoIdToLabel;
	}
 
}
