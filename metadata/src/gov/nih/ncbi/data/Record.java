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
	
	private int queueId;
	private int id;
	private String sourceCellLine; 
	private String sampleName;
	private String sampleTitle;
	private String sourceCellType;
	private String sourceSpecies;
	private String sourceAnatomy;
	// blob together disease, healthState, and phenotype when read in from SRA
	private String sourceDisease;
	private String sourceCellTreatment;
	private String annotCellLine; //annotated cell line
	private String annotCellType;
	private String annotSpecies;
	private String annotAnatomy;
	private String annotDisease;
	private String annotCellTreatment;
	private String note;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSourceCellLine() {
		return sourceCellLine;
	}
	public void setSourceCellLine(String sourceCellLine) {
		this.sourceCellLine = sourceCellLine;
	}
	public String getSourceCellType() {
		return sourceCellType;
	}
	public void setSourceCellType(String sourceCellType) {
		this.sourceCellType = sourceCellType;
	}
	public String getSourceSpecies() {
		return sourceSpecies;
	}
	public void setSourceSpecies(String sourceSpecies) {
		this.sourceSpecies = sourceSpecies;
	}
	public String getSourceAnatomy() {
		return sourceAnatomy;
	}
	public void setSourceAnatomy(String sourceAnatomy) {
		this.sourceAnatomy = sourceAnatomy;
	}
	public String getSourceDisease() {
		return sourceDisease;
	}
	public void setSourceDisease(String sourceDisease) {
		this.sourceDisease = sourceDisease;
	}
	public String getSourceCellTreatment() {
		return sourceCellTreatment;
	}
	public void setSourceCellTreatment(String sourceCellTreatment) {
		this.sourceCellTreatment = sourceCellTreatment;
	}
	public String getAnnotCellLine() {
		return annotCellLine;
	}
	public void setAnnotCellLine(String annotCellLine) {
		this.annotCellLine = annotCellLine;
	}
	public String getAnnotCellType() {
		return annotCellType;
	}
	public void setAnnotCellType(String annotCellType) {
		this.annotCellType = annotCellType;
	}
	public String getAnnotSpecies() {
		return annotSpecies;
	}
	public void setAnnotSpecies(String annotSpecies) {
		this.annotSpecies = annotSpecies;
	}
	public String getAnnotAnatomy() {
		return annotAnatomy;
	}
	public void setAnnotAnatomy(String annotAnatomy) {
		this.annotAnatomy = annotAnatomy;
	}
	public String getAnnotDisease() {
		return annotDisease;
	}
	public void setAnnotDisease(String annotDisease) {
		this.annotDisease = annotDisease;
	}
	public String getAnnotCellTreatment() {
		return annotCellTreatment;
	}
	public void setAnnotCellTreatment(String annotCellTreatment) {
		this.annotCellTreatment = annotCellTreatment;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getQueueId() {
		return queueId;
	}
	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getSampleTitle() {
		return sampleTitle;
	}
	public void setSampleTitle(String sampleTitle) {
		this.sampleTitle = sampleTitle;
	} 
	
}
