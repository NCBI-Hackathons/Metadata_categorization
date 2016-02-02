package gov.nih.ncbi.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-14
 * 
 * service to iterate over a solr database
 *
 */
public class SolrDataProvider {
	 
	private int index;
	private SolrDocumentList docs;
	private int start = 0;
	private final int rows = 1000;
	private SolrClient solr;
	private SolrQuery query;
	
	public SolrDataProvider () {
		query = new SolrQuery("*:*");
	}
	
	public SolrDataProvider (SolrDatabases db) {
		solr = SolrProvider.getConnection(db);
		query = new SolrQuery("*:*");
	}
	
	public boolean hasNext() {
		if (index >= docs.size()) {
			try {
				docs = solr.query(query.setStart(start)).getResults();
				index = 0;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			start += rows;
		}
		return docs.size() > 0;
	}
	
	public SolrDocument retrieve (Object id) {
		SolrDocument doc = null;
		query = query.setQuery("id:" + (String) id);
		try {
			SolrDocumentList results  = solr.query(query).getResults();
			if (results.size() > 0) {
				doc = results.get(0);
			}
			else {
				System.err.println ("Document not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc; 
	}
	
	//include an update method here
	public void update (Object id, String fieldName, Object fieldValue) {
		SolrDocument doc = retrieve(id);
		doc.setField("fieldName", fieldValue);
//		solr.
	}

}
