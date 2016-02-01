package gov.nih.ncbi.solr;

import gov.nih.ncbi.data.TsvParser;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 
 * reads in Ontology data from parsed from CLO into Ontology solr core
 * 
 * tsv file must include these fields, which correspond to fields in the solr core
 * 
 * cloId - CLO_ID
 * cellLine - Label
 * cellType - Cell_Type
 * organism - NCBI_Taxon
 * tissue - Uberon OR Snome
 * disease - Disease_comment OR DOID
 *
 */
public class PopulateOntologyJob {

	public static void main (String args[]) {
		String file = args[0];
		SolrClient solr = SolrProvider.getConnection(SolrDatabases.Ontology);
		try {
			int count = 0;
			TsvParser parser = new TsvParser(file);
			parser.readHeaders();
			while (parser.readRecord()) {
				SolrInputDocument doc = new SolrInputDocument();
				if(!parser.get("CLO_ID").equals(".")) {
					doc.addField("cloId", parser.get("CLO_ID"));
				}
				if (!parser.get("Label").equals(".")) {
					doc.addField("cellLine", parser.get("Label"));
				}
				else {
					doc.addField("cellLine", "0");
				}
				if (!parser.get("Cell_Type").equals(".")) {
					doc.addField("cellType", parser.get("Cell_Type"));
				}
				else { 
					doc.addField("cellType", "0");
				}
				if (!parser.get("NCBI_Taxon").equals(".")) {
					doc.addField("organism", parser.get("NCBI_Taxon"));
				}
				else {
					doc.addField("organism", "0");
				}
				//tissue is constructed from two candidate fields "Uberon" or "Snome"
				if (!parser.get("Uberon").equals(".")) {
					doc.addField("tissue", parser.get("Uberon"));
				}
				else if (!parser.get("Snome").equals(".")) {
					doc.addField("tissue", parser.get("Snome"));
				}
				else {
					doc.addField("tissue", "0");
				}
				//disease is constructed from two fields "Disease_comment" and "DOID"
				if (!parser.get("Disease_comment").equals(".")) {
					doc.addField("disease", parser.get("Disease_comment"));
				}
				else if (! parser.get("DOID").equals(".")) {
					doc.addField("disease", parser.get("DOID"));
				}
				else {
					doc.addField("disease", "0");
				}
				try {
					solr.add(doc);
					System.out.println (parser.get("CLO_ID"));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if (count < 1000) {
					count ++;
				}
				else {
					solr.commit();
					count = 0;
				}
			}
			solr.close();
			parser.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
