package gov.nih.ncbi.solr;

import org.apache.solr.client.solrj.SolrClient;

/**
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-05
 * 
 * framework for transfering data into Solr 
 * provides structure for extracting an OntologyEntry 
 */

public class OntologyProvider {
	
	private SolrClient solr = SolrProvider.getConnection(SolrDatabases.Ontology);
}
