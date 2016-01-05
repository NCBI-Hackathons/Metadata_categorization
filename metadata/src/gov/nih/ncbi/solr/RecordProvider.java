package gov.nih.ncbi.solr;

import gov.nih.ncbi.data.Record;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-05
 * 
 * framework for transfering data into Solr 
 * provides structure for extracting an OntologyEntry 
 */

public class RecordProvider {
	
	private SolrClient solr = SolrProvider.getConnection(SolrDatabases.Annotations);
	
	public 
}
