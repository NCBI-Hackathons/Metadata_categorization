package gov.nih.ncbi.data;
/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-05
 * data class to transfer data back and forth between BioSample and Solr
 *
 */
//change this to Record
public class Record {
	
	private int id;
	private String cellLine; //annotated cell line
	private String cellType;
	private String species;
	private String anatomy;
	// blob together disease, healthState, and phenotype when read in from SRA
	private String disease;
	private String cellTreatment;
	private String note; //change this to note
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getAnatomy() {
		return anatomy;
	}
	public void setAnatomy(String anatomy) {
		this.anatomy = anatomy;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getCellTreatment() {
		return cellTreatment;
	}
	public void setCellTreatment(String cellTreatment) {
		this.cellTreatment = cellTreatment;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
