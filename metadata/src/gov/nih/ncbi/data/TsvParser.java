package gov.nih.ncbi.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
	private String [] dataArray;
	
	public TsvParser (String file) throws FileNotFoundException {
		this.filename = file;
		tsvReader = new BufferedReader (new FileReader (filename));
	}
	/**
	 * initialize header mapping, allows user to use String column names instead of indices
	 * @throws IOException
	 */
	public void readHeaders () throws IOException{
		
		String readLine = tsvReader.readLine();
		String [] fields = readLine.split("\t");
		for (int i = 0; i < fields.length; i ++) {
			headers.put(fields[i], i);
			System.out.println(fields[i]);
		}
		
	}
	
	public Set<String> getHeaders() {
		return headers.keySet();
	}
	/**
	 * flag for iterating over the tsv file
	 * @return true when there are more records in the file
	 * @throws IOException
	 */
	public boolean readRecord () throws IOException{
		String dataRow = tsvReader.readLine();
		while (dataRow != null) {
			dataArray = dataRow.split("\t");
			return true;
		}
		return false;
	}
	/**
	 * access a field by header name (column)
	 * @param header
	 * @return value contained in the file in that column
	 */
	public String get(String header) {
		try {
			return dataArray[headers.get(header)];
		} catch (Exception e) {
			return null;
		}
	}
	
	public void close() throws IOException {
		tsvReader.close();
	}

}
