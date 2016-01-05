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
			"{add-field: {name: sraId, type: int, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: cellLine, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: cellType, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: species, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: anatomy, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: disease, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: cellTreatment, type: text_general, indexed: true, stored:true, multiValued:false }",
			"{add-field: {name: note, type: text_general, indexed: true, stored:true, multiValued:false }");
	}
