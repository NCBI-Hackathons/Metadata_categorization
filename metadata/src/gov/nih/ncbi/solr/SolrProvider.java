package gov.nih.ncbi.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 
 * service to provide access to solr databases
 *
 */
public class SolrProvider {	
	
	public static final SolrClient getConnection (SolrDatabases db) throws Exception {
		return new HttpSolrClient(SolrConstants.getUrl(db));
	}
	
}
