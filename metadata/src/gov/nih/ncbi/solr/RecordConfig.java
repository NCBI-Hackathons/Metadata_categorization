package gov.nih.ncbi.solr;

import java.util.Arrays;
import java.util.List;
/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-04
 * 
 * configuration for the annotations solr core
 *
 */
public class RecordConfig {
	
	public static List<String> configList = Arrays.asList(		
			"{delete-field: {name: queueID}}",
			"{add-field: {name: queueId, type: int, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: id, type: int, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceCellLine, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceCellType, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceSpecies, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceAnatomy, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceDisease, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceCellTreatment, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotCellLine, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotCellType, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotSpecies, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotAnatomy, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotDisease, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotCellTreatment, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: note, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sampleName, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sampleTitle, type: text_general, indexed: true, stored:true, multiValued:false }}");
}
