package gov.nih.ncbi.solr;

import gov.nih.ncbi.data.TsvParser;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

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
				if (!parser.get("cellLine").equals(".")){
					doc.addField("cellLine", parser.get("cellLine"));
				}
				else{
					doc.addField("cellLine", "0");
				}
				if (!parser.get("cellType").equals(".")){
					doc.addField("cellType", parser.get("cellType"));
				}
				else{
					doc.addField("cellType", "0");
				}
				if (!parser.get("organism").equals(".")){
					doc.addField("organism", parser.get("organism"));
				}
				else{
					doc.addField("organism", "0");
				}
				if (!parser.get("tissue").equals(".")){
					doc.addField("tissue", parser.get("tissue"));
				}
				else{
					doc.addField("tissue", "0");
				}
				if (!parser.get("disease").equals(".")){
					doc.addField("disease", parser.get("disease"));
				}
				else{
					doc.addField("disease", "0");
				}
				try {
					solr.add(doc);
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
