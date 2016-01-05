package gov.nih.ncbi.solr;

import java.util.List;

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
		
		String dbName = args[0];
		SolrDatabases db;
		if (dbName.equalsIgnoreCase("Annotations")) {
			db = SolrDatabases.Annotations;
		}
		else if (dbName.equalsIgnoreCase("Ontology")){
			db = SolrDatabases.Ontology;
		}
		else {
			throw new Exception ("Please enter a valid database name. Correct names are Annotations or Ontology");
		}
		
		HttpClient client = HttpClientBuilder.create().build();

		try {
			HttpPost post = new HttpPost(SolrConstants.getUrl(db) + "/schema");
		
			
			List<String> config;
			
			if (db.equals(SolrDatabases.Annotations)) {
				config = AnnotationsConfig.configList;
			}
			else {
				config = OntologyConfig.configList;
			}
			
			config.stream().forEach(field -> {
				try {
					post.setEntity(new StringEntity(field));
					System.out.println(IOUtils.toString(client.execute(post).getEntity().getContent()));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			});
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
