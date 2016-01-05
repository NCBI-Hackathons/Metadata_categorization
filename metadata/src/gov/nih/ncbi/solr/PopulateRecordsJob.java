package gov.nih.ncbi.solr;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

public class PopulateRecordsJob {
	
	public static String constructDisease (String [] dataRow) {
		StringBuilder build = new StringBuilder();
		if (!dataRow[13].equals(".")){
			build.append(dataRow[13] + " ");
		}
		if (!dataRow[14].equals(".")){
			build.append(dataRow[14] + " ");
		}
		if (!dataRow[15].equals(".")){
			build.append(dataRow[15] + " ");
		}
		return build.toString();
	}

	public static void main(String[] args) {
		
		String sourceFile = args[0];
		SolrClient solr = SolrProvider.getConnection(SolrDatabases.Annotations);
		try {
			BufferedReader tsvReader = new BufferedReader(new FileReader (sourceFile));
			//read header row
			String dataRow = tsvReader.readLine();
			int count = 0;
			int queueId = 1;
			while (dataRow != null) {
				SolrInputDocument doc = new SolrInputDocument ();
				doc.setField("queueID", queueId);
				String[] dataArray = dataRow.split ("\t");
				if (!dataArray[0].equals(".")){
					doc.addField("id", Integer.parseInt(dataArray[0]));
				}
				if (!dataArray[11].equals(".")){
					doc.addField("sourceCellType", dataArray[11]);
				}
				if (!dataArray[6].equals(".")){
					doc.addField("sourceSpecies", dataArray[6]);
				}
				if (!dataArray[7].equals(".")){
					doc.addField("sourceAnatomy", dataArray[7]);
				}
				String disease = constructDisease(dataArray);
				if (disease.equals("")){
					doc.addField("sourceDisease", disease);
				}
				if (!dataArray[16].equals(".")){
					doc.addField("sourceCellTreatment",dataArray[16]);
				}
				if (count < 1000) {
					count ++;
				} 
				else {
					queueId++;
					count = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
