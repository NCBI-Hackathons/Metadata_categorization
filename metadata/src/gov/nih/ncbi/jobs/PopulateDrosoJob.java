package gov.nih.ncbi.jobs;

import gov.nih.ncbi.data.TsvParser;
import gov.nih.ncbi.solr.SolrDatabases;
import gov.nih.ncbi.solr.SolrProvider;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class PopulateDrosoJob {
	public static void main (String args []) {
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
			int taxId = 7227; //drosophila specific taxId 
			//detect the highest queue Id and initialize one higher 
			int queueId = 1000;
			SolrQuery query = new SolrQuery("queueId:*").setSort("queueId", SolrQuery.ORDER.desc);
			QueryResponse response = solr.query(query);	
			SolrDocumentList docs = response.getResults();
			queueId = ((Integer)docs.get(0).get("queueId"));
			queueId++;
				while (parser.readRecord()) {
					SolrInputDocument doc = new SolrInputDocument ();
					doc.setField("queueId", queueId);
					doc.setField("taxId", taxId);
					try {
						if (!parser.get("BioSampleID").equals(".")){
							try {
								doc.addField("id", Integer.parseInt(parser.get("BioSampleID")));
							} catch (NumberFormatException nfe) {
//								System.err.println("Number FormatException");
								continue;
							}
								
						}
						//this populates the cell line field with the best possible clue
						if (!parser.get("Cell_Line").equals(".")) {
							try{
							doc.addField("sourceCellLine", parser.get("Cell_Line"));
							} catch (Exception e) {
								System.err.println("error");
							}
						}
						else if (!parser.get("Sample_Name").equals(".")){
								doc.addField("sourceCellLine", parser.get("Sample_Name"));
						}
						else if (! parser.get("Sample_Title").equals(".")){
							doc.addField("sourceCellLine", parser.get("Sample_Title"));
						}
						else if (! parser.get("ExperimentTitle").equals(".")){
							doc.addField("sourceCellLine", parser.get("ExperimentTitle"));
						}
						else {
//							System.err.println("No available data for this field");
							doc.addField("sourceCellLine", "0");
						}				
						
						if (!parser.get("Sample_Name").equals(".")){
							doc.addField("sampleName", parser.get("Sample_Name"));
								
						}
						else {
							doc.addField("sampleName", "0");
						}
						if (!parser.get("Sample_Title").equals(".")){
							doc.addField("sampleTitle", parser.get("Sample_Title"));
								
						}
						else {
							doc.addField("sampleTitle", "0");
						}
						if (!parser.get("Cell_Type").equals(".")){
							doc.addField("sourceCellType", parser.get("Cell_Type"));
						}
						else {
							doc.addField("sourceCellType", "0");
						}
						if (!parser.get("Organism").equals(".")){
							doc.addField("sourceSpecies", parser.get("Organism"));
								
						}
						else {
							doc.addField("sourceSpecies", "0");
						}
						if (!parser.get("Tissue").equals(".")){
							doc.addField("sourceAnatomy", parser.get("Tissue"));
								
						}
						else {
							doc.addField("sourceAnatomy", "0");
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
						String trimmed = build.toString().trim();
						if (!trimmed.equals("")){
							doc.addField("sourceDisease", build.toString().trim());
						}
						else{
							doc.addField("sourceDisease", "0");
						}
						if (!parser.get("Treatment").equals(".")){
							doc.addField("sourceCellTreatment", parser.get("Treatment"));							
						}
						else{
							doc.addField("sourceCellTreatment", "0");
						}
						//add the drosophila specific fields 
						try {
							if (!parser.get("Sex").equals(".")){
								doc.addField("sourceSex", parser.get("Sex"));
							}
							else {
								doc.addField("sourceSex", "0");
							}
							if (!parser.get("Development_Stage").equals(".")){
								doc.addField("sourceDevStage", parser.get("Development_Stage"));
							}
							else {
								doc.addField("sourceDevStage", "0");
							}
						} catch (NullPointerException e) {
							System.err.println ("data for sourceSex or sourceDevStage isn't correctly populating...");
							
						}
						
						//set all the annotation fields to default 0
						doc.addField("annotCellLine", "0");
						doc.addField("annotCellType", "0");
						doc.addField("annotSpecies", "0");
						doc.addField("annotAnatomy", "0");
						doc.addField("annotCellTreatment", "0");
						doc.addField("annotDisease", "0");
						doc.addField("annotSex", "0");
						doc.addField("annotDevStage", "0");
											
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
//						e.printStackTrace();
						System.err.println(parser.get("BioSampleId") + "Skipped");
						continue;
					}
					
				}
				solr.commit();
				parser.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}	

}
