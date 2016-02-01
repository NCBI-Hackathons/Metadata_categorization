package gov.nih.ncbi.solr;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class IterateAndUpdate {
	
	private static List<String> fields = Arrays.asList("queueId", "id", "taxId", "sourceCellLine", "sampleName", "sampleTitle", "sourceCellType",
			"sourceSpecies", "sourceAnatomy", "sourceDisease", "annotCellLine", "annotCellType", "annotSpecies", "annotAnatomy",
			"annotDisease", "annotCellTreatment");
	
	private static SolrInputDocument initializeFields (SolrDocument d) {
		SolrInputDocument doc = new SolrInputDocument();
		for (String s: fields) {
			if (d.get(s) != null) {
				doc.setField(s, d.get(s));
			}
			else if (s.equals(("annotDisease"))) {
				doc.setField(s, "0");
			}
			else if (s.equals("taxId")) {
				doc.setField(s, 9606);
			}
			else {
				doc.setField(s,  "0");
			}
		}
		return doc;
	}
	
	public static void main(String args []) {
		
		long maxDocs;
		long count = 0;
		SolrClient solr = SolrProvider.getConnection(SolrDatabases.AnnotationsDev);
		SolrQuery query = new SolrQuery ("*:*").setRows(460000);
		QueryResponse response;
		try {
			response = solr.query(query);
			SolrDocumentList docs = response.getResults();
			maxDocs = docs.getNumFound();
			for (int i = 0; i < maxDocs; i ++) {
				SolrDocument d = docs.get(i);
				SolrInputDocument iDoc = initializeFields(d);
				solr.add(iDoc);				
				count++;
				if (count % 1000 == 0) {
					System.out.println (count + " documents processed");
					solr.commit();
				}
			}
			solr.commit();
			System.out.println ("iterating got " + count + " documents and the query returned " + maxDocs + " documents");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
 
}
