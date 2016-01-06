package gov.nih.ncbi.solr;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-04
 * 
 * create and configure the solr schema
 *
 */
public class SchemaPost {
	
	public static void main(String args[]) throws Exception {
		String dbName;
		try {
			dbName = args[0];
		} catch (Exception e){
			System.out.println ("Please enter a valid database name: Annotations, AnnotationsDev, or Ontology");
			Scanner scan = new Scanner (System.in);
			dbName = scan.nextLine();
			scan.close();			
		}
		SolrDatabases db;
		if (dbName.equalsIgnoreCase("Annotations")) {
			db = SolrDatabases.Annotations;
		}
		else if (dbName.equalsIgnoreCase ("AnnotationsDev")){
			db = SolrDatabases.AnnotationsDev;
		}
		else if (dbName.equalsIgnoreCase("Ontology")){
			db = SolrDatabases.Ontology;
		}
		else {
			throw new Exception ("Please enter a valid database name. Correct names are Annotations, AnnotationsDev, or Ontology");
		}
		
		HttpClient client = HttpClientBuilder.create().build();

		try {
			HttpPost post = new HttpPost(SolrConstants.getUrl(db) + "/schema");
		
			
			List<String> config;
			
			if (db.equals(SolrDatabases.Annotations) || db.equals(SolrDatabases.AnnotationsDev)) {
				config = RecordConfig.configList;
			}
			else {
				config = OntologyConfig.configList;
			}
			
			for(String f: config) {
					try {
					post.setEntity(new StringEntity(f));
					System.out.println(IOUtils.toString(client.execute(post).getEntity().getContent()));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
