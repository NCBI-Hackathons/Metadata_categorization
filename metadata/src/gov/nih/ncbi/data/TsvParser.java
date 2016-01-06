package gov.nih.ncbi.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author Lena Pons
 * 2016-01-05
 * a parser for a tsv file
 *
 */
public class TsvParser {
	
	private static Map<String, Integer> headers = new HashMap<>();
	private String filename;
	private static BufferedReader tsvReader;
	private String dataRow;
	private String [] dataArray;
	
	public TsvParser (String file) throws FileNotFoundException {
		this.filename = file;
		tsvReader = new BufferedReader (new FileReader (filename));
	}
	
	public void readHeaders () throws IOException{
		
		String readLine = tsvReader.readLine();
		String [] fields = readLine.split("\t");
		for (int i = 0; i < fields.length; i ++) {
			headers.put(fields[i], i);
		}
		
	}
	
	public boolean readRecord () throws IOException{
		String dataRow = tsvReader.readLine();
		while (dataRow != null) {
			dataArray = dataRow.split("\t");
			return true;
		}
		return false;
	}
	
	public String get(String header) {
		return dataArray[headers.get(header)];
	}
	
	public void close() throws IOException {
		tsvReader.close();
	}

}
