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
public class AnnotationsConfig {
	
	public static List<String> configList = Arrays.asList(
			"{add-field: {name: sraId, type: int, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: cellLine, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: cellType, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: sampleName, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: sampleTitle, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: shortDescription, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: organism, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: tissue, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: disease, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: healthState, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: phenotype, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: treatment, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: description, type: text_general, indexed: true, stored:true, multiValued:false }");
	}
