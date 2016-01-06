package gov.nih.ncbi.data;
/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-05
 * data class to transfer data back and forth between BioSample and Solr
 *
 */
public class Record {
	
	private int queueId; 				//batch id for annotators
	private int id; 					//BioSample Id 
	private String sourceCellLine; 		//submitter clue for cell line 
	private String sampleName;			//submitter provided sample name
	private String sampleTitle;			//submitter provided sample title
	private String sourceCellType;		//submitter provided cell type
	private String sourceSpecies;		//submitter provided species or organism
	private String sourceAnatomy;		//submitter provided - should be tissue or similar source
	// blob together disease, healthState, and phenotype when read in from SRA
	private String sourceDisease;		//e.g. cancer
	private String sourceCellTreatment;	//typically something about how the sample was processed
	private String annotCellLine; 		//annotated cell line from CLO
	private String annotCellType;		//all other annotation fields should be annotator validated from CLO controlled vocabularies
	private String annotSpecies;
	private String annotAnatomy;
	private String annotDisease;
	private String annotCellTreatment;
	private String note;				//annotator note - used for notes about cell lines that can't be determined, etc.
	
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
