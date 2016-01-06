package gov.nih.ncbi.solr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

public class PopulateRecordsJob {
	/**
	 * method to blob together three possible candidate fields for disease
	 * @param dataRow - row from the tsv file generated from biosample parsing
	 * @return String concatenated from the three candidate fields
	 */
	public static String constructDisease (String [] dataRow, Map<String, Integer> headers) {
		StringBuilder build = new StringBuilder();
		try {
			if (!dataRow[headers.get("Disease")].equals(".")){
				build.append(dataRow[headers.get("Disease")] + " ");
			}
			if (!dataRow[headers.get("Health_state")].equals(".")){
				build.append(dataRow[headers.get("Health_state")] + " ");
			}
			if (!dataRow[headers.get("Phenotype")].equals(".")){
				build.append(dataRow[headers.get("Phenotype")]);
			}
		} catch (Exception e) {
			System.err.println("Array Index Out Of Bounds");
			return build.toString();
		}
		return build.toString();
	}
	/**
	 * method to construct the best possible textual clue for cell line
	 * essentially serves as a fail mechanism for cases where cell line field from the
	 * source data is blank 
	 * @param dataRow - row from the tsv file generated from Biosample parsing
	 * @return
	 */
	public static String populateCellLine (String [] dataRow, Map<String, Integer> headers) {
		String cellLine = "";
		try {
		if (!dataRow[headers.get("Cell_Line")].equals(".")){
			cellLine = dataRow[headers.get("Cell_Line")];
		}
		else {
			if (!dataRow[headers.get("Sample_Name")].equals(".")){
				cellLine = dataRow[headers.get("Sample_Name")];
			}
			else if (!dataRow[headers.get("Sample_Title")].equals(".")){
				cellLine = dataRow[headers.get("Sample_Title")];
			}
		}
		} catch (ArrayIndexOutOfBoundsException e) {
			cellLine = "";
		}
		return cellLine;
	}

	public static void main(String[] args) {
		
		String sourceFile = args[0];
		SolrClient solr = SolrProvider.getConnection(SolrDatabases.Annotations);
		Map<String, Integer> headers = new HashMap<>();
		try {
			BufferedReader tsvReader = new BufferedReader(new FileReader (sourceFile));
			//read header row
			String dataRow = tsvReader.readLine();
			String [] fields = dataRow.split("\t");
			for (int i = 0; i < fields.length; i ++) {
				headers.put(fields[i], i);
			}
			dataRow = tsvReader.readLine();
			int count = 0;
			int queueId = 1;
			while (dataRow != null) {
				SolrInputDocument doc = new SolrInputDocument ();
				doc.setField("queueID", queueId);
				String[] dataArray = dataRow.split ("\t");
				String cellLine = populateCellLine (dataArray, headers);
				doc.addField("sourceCellLine", cellLine);
				try {
					if (!dataArray[headers.get("Sample_Name")].equals(".")){
						doc.addField("sampleName", dataArray[headers.get("Sample_Name")]);
						
					}
					if (!dataArray[headers.get("Sample_Title")].equals(".")){
						doc.addField("sampleTitle", dataArray[headers.get("Sample_Title")]);
					}
					if (!dataArray[headers.get("Cell_Type")].equals(".")){
						doc.addField("sourceCellType", dataArray[headers.get("Cell_Type")]);
					}
					if (!dataArray[headers.get("BioSampleID")].equals(".")){
						doc.addField("id", Integer.parseInt(dataArray[headers.get("BioSampleID")]));
					}
					if (!dataArray[headers.get("Cell_Type")].equals(".")){
						doc.addField("sourceCellType", dataArray[headers.get("Cell_Type")]);
					}
					if (!dataArray[headers.get("Organism")].equals(".")){
						doc.addField("sourceSpecies", dataArray[headers.get("Organism")]);
					}
					if (!dataArray[headers.get("Tissue")].equals(".")){
						doc.addField("sourceAnatomy", dataArray[headers.get("Tissue")]);
					}
					String disease = constructDisease(dataArray, headers);
					if (disease.equals("")){
						doc.addField("sourceDisease", disease);
					}
					if (!dataArray[headers.get("Treatment")].equals(".")){
						doc.addField("sourceCellTreatment",dataArray[headers.get("Treatment")]);
					}				
				} catch (ArrayIndexOutOfBoundsException e) {
//					System.err.println("Array Index Out Of Bounds");
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
//					System.out.println (dataArray[0] + " indexed");
				} catch (Exception e) {
//					e.printStackTrace();
					System.err.println(dataArray[0] + "Skipped");
				}
				dataRow = tsvReader.readLine();
			}
			tsvReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
