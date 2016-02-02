package gov.nih.ncbi.ontology;

import gov.nih.ncbi.solr.SolrDataProvider;
import gov.nih.ncbi.solr.SolrDatabases;
import gov.nih.ncbi.solr.SolrProvider;

import org.apache.solr.client.solrj.SolrClient;

import com.lexicalintelligence.UnicodeCsvWriter;
/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-14
 * 
 * class to search ontology and produce recommended cell lines
 * 
 */
public class SuggestCellLine {
	/*
	 * in this initial test, will write a file where we have a cell line from the ontology followed by a CSV list of 
	 * 
	 */
	public static void main (String args []) {
		String filename = args[0]; 
		SolrClient annot = SolrProvider.getConnection(SolrDatabases.Annotations);
		SolrClient onto = SolrProvider.getConnection(SolrDatabases.Ontology);
		UnicodeCsvWriter writer = new UnicodeCsvWriter (filename);
		SolrDataProvider ontoProv = new SolrDataProvider(SolrDatabases.Ontology);
		
	}

}
