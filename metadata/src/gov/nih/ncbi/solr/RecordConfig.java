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
			"{delete-field: {name: queueId}}",
			"{delete-field: {name: sourceCellLine}}",
			"{delete-field: {name: sourceCellType}}",
			"{delete-field: {name: sourceSpecies}}",
			"{delete-field: {name: sourceAnatomy}}",
			"{delete-field: {name: sourceDisease}}",
			"{delete-field: {name: sourceCellTreatment}}",
			"{delete-field: {name: sampleName}}",
			"{delete-field: {name: sampleTitle}}",
			"{delete-field: {name: annotCellLine}}",
			"{delete-field: {name: annotCellType}}",
			"{delete-field: {name: annotSpecies}}",
			"{delete-field: {name: annotAnatomy}}",
			"{delete-field: {name: annotDisease}}",
			"{delete-field: {name: annotCellTreatment}}",
			"{delete-field: {name: note}}",
			
			"{add-field: {name: queueId, type: int, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceCellLine, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceCellType, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceSpecies, type: text_general,  indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceAnatomy, type: text_general,  indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceDisease, type: text_general,  indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sourceCellTreatment, type: text_general,  indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotCellLine, type: text_general,  indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotCellType, type: text_general,  indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotSpecies, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotAnatomy, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotDisease, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: annotCellTreatment, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: note, type: text_general,  indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sampleName, type: text_general, indexed: true, stored:true, multiValued:false }}",
			"{add-field: {name: sampleTitle, type: text_general, indexed: true, stored:true, multiValued:false }}");
}
