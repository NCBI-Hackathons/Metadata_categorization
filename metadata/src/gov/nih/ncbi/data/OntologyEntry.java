package gov.nih.ncbi.data;

/**
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-05
 * 
 * data class to transfer data back and forth between Cell Line Ontology and Solr
 */

public class OntologyEntry {
	
	private String cellLine;
	private String cellType;
	private String organism;
	private String tissue;
	private String disease;
	public String getCellLine() {
		return cellLine;
	}
	public void setCellLine(String cellLine) {
		this.cellLine = cellLine;
	}
	public String getCellType() {
		return cellType;
	}
	public void setCellType(String cellType) {
		this.cellType = cellType;
	}
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
	}
	public String getTissue() {
		return tissue;
	}
	public void setTissue(String tissue) {
		this.tissue = tissue;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	
	
}
