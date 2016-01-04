package gov.nih.ncbi.solr;

import java.util.prefs.Preferences;
/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 
 * solr core url data
 *
 */
public class SolrConstants {
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(SolrConstants.class);
	
	public static void setUrl(String url) {
		PREFERENCES.put("url", url);
	}
	
	public static String getAnnotationUrl() {
		return PREFERENCES.get("url", "localhost:8983/solr/annotation");
	}
	
	public static void setOntologyUrl(String url) {
		PREFERENCES.put("onotology_url", url);
	}
	
	public static String getOntologyUrl() {
		return PREFERENCES.get("ontology_url", "localhost:8983/solr/ontology");
	}
	
	public static String getUrl(SolrDatabases db) throws Exception {
		if (db.equals(SolrDatabases.Annotations)) {
			return getAnnotationUrl();
		}
		else if (db.equals (SolrDatabases.Ontology)) {
			return getOntologyUrl();
		}
		else {
			throw new Exception ("Please specify correct database name");
		}
	}
	
}
