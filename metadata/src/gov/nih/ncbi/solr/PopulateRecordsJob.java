package gov.nih.ncbi.solr;

import java.util.Scanner;

import gov.nih.ncbi.data.TsvParser;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

public class PopulateRecordsJob {
	
	public static void main(String[] args) {
		//filename of the tsv file used to populate the records
		String sourceFile = args[0];
		//database name to access the proper Solr core
		String dbName = args [1];
		SolrDatabases db = null;
		if (dbName.equalsIgnoreCase("annotations")) {
			db = SolrDatabases.Annotations;
		}
		else if (dbName.equalsIgnoreCase("AnnotationsDev")){
			db= SolrDatabases.AnnotationsDev;
		}

		SolrClient solr = SolrProvider.getConnection(db);

		try {
			TsvParser parser = new TsvParser (sourceFile);
			//read header row
			parser.readHeaders();
			int count = 0;
			int queueId = 1;
			while (parser.readRecord()) {
				SolrInputDocument doc = new SolrInputDocument ();
				doc.setField("queueId", queueId);
				try {
					if (!parser.get("BioSampleID").equals(".")){
						try {
							doc.addField("id", Integer.parseInt(parser.get("BioSampleID")));
						} catch (NumberFormatException nfe) {
							System.err.println("Number FormatException");
							continue;
						}
							
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
					if (!parser.get("Health_State").equals(".")){
						healthState = parser.get("Health_State");
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
				} catch (ArrayIndexOutOfBoundsException e) {			
					continue;
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
				} catch (Exception e) {
//					e.printStackTrace();
					System.err.println(parser.get("BioSampleId") + "Skipped");
				}
				
			}
			solr.commit();
			parser.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
