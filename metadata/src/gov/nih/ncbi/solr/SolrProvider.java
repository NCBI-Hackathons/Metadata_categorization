package gov.nih.ncbi.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;

/**
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-14
 * 
 * service to provide access to solr databases
 *
 */
public class SolrProvider {	
	
	public static final SolrClient getConnection (SolrDatabases db) {
		try {
			return new HttpSolrClient(SolrConstants.getUrl(db));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
