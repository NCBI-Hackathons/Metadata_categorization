package gov.nih.ncbi.solr;

import gov.nih.ncbi.data.TsvParser;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

public class PopulateRecordsJob {
	
	public static void main(String[] args) {
		
		String sourceFile = args[0];
		SolrClient solr = SolrProvider.getConnection(SolrDatabases.Annotations);
		try {
			TsvParser parser = new TsvParser (sourceFile);
			//read header row
			parser.readHeaders();
			int count = 0;
			int queueId = 1;
			while (parser.readRecord()) {
				SolrInputDocument doc = new SolrInputDocument ();
				doc.setField("queueID", queueId);
				if (!parser.get("BioSampleID").equals(".")){
					doc.addField("id", Integer.parseInt(parser.get("BioSampleID")));
						
				}
				//this populates the cell line field with the best possible clue
				if (!parser.get("Cell_Line").equals(".")) {
					doc.addField("sourceCellLine", parser.get("Cell_Line"));
				}
				else {
					if (!parser.get("Sample_Name").equals(".")){
						doc.addField("sourceCellLine", parser.get("Sample_Name"));
					}
					else if (! parser.get("Sample_Title").equals(".")){
						doc.addField("sourceCellLine", parser.get("Sample_Title"));
					}
					else {
						doc.addField("sourceCellLine", "");
					}
				}
				
				if (!parser.get("Sample_Name").equals(".")){
					doc.addField("sampleName", parser.get("Sample_Name"));
						
				}
				if (!parser.get("Sample_Title").equals(".")){
					doc.addField("sampleTitle", parser.get("Sample_Title"));
						
				}
				if (!parser.get("Cell_Type").equals(".")){
					doc.addField("sourceCellType", parser.get("Cell_Type"));
						
				}
				if (!parser.get("Organism").equals(".")){
					doc.addField("sourceSpecies", parser.get("Organism"));
						
				}
				if (!parser.get("Tissue").equals(".")){
					doc.addField("sourceAnatomy", parser.get("Tissue"));
						
				}
				//construct disease blob
				String disease = "";
				String healthState = "";
				String phenotype = "";
				if (!parser.get("Disease").equals(".")){
					disease = parser.get("Disease");
				}
				if (!parser.get("Health_state").equals(".")){
					healthState = parser.get("Health_state");
				}
				if (!parser.get("Phenotype").equals(".")){
					phenotype = parser.get("Phenotype");
				}
				StringBuilder build = new StringBuilder();
				build.append(disease + " ");
				build.append(healthState + " ");
				build.append(phenotype);
				doc.addField("sourceDisease", build.toString());
				if (!parser.get("Treatment").equals(".")){
					doc.addField("sourceCellTreatment", parser.get("Treatment"));
						
				}
								
				if (count < 1000) {
					count ++;
				} 
				else {
					queueId++;
					count = 0;
					System.out.println ("queue " + queueId + " completed");
					solr.commit();
				}
				try {
					solr.add(doc);
//					System.out.println (dataArray[0] + " indexed");
				} catch (Exception e) {
//					e.printStackTrace();
					System.err.println(parser.get("BioSampleId") + "Skipped");
				}
				
			}
			parser.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
