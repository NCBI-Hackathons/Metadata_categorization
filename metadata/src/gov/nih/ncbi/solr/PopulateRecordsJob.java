package gov.nih.ncbi.solr;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.solr.client.solrj.SolrClient;

public class PopulateRecordsJob {

	public static void main(String[] args) {
		
		String sourceFile = args[0];
		SolrClient solr = SolrProvider.getConnection(SolrDatabases.Annotations);
		try {
			BufferedReader tsvReader = new BufferedReader(new FileReader (sourceFile));
			//read header row
			String headers = tsvReader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
