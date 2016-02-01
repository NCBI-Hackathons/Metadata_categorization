package gov.nih.ncbi.solr;

import java.util.Arrays;
import java.util.List;
/**
 * 
 * @author Lena Pons
 * NCBI Hackathon 2016 - Metadata Sorting group
 * 2016-01-04
 * 
 * configuration for the ontology solr core
 *
 */
public class OntologyConfig {
	
	public static List<String> configList = Arrays.asList(
			"{delete-field: {name:cellLine}}",
			"{delete-field: {name:cellType}}",
			"{delete-field: {name:species}}",
			"{delete-field: {name:organism}}",
			"{delete-field: {name:tissue}}",
			"{delete-field: {name:anatomy}}",
			"{delete-field: {name:disease}}",
			"{add-field: {name: cloId, type: string, indexed: true, stored: true, multiValued:false, uniqueKey:true}}",
			"{add-field: {name: cellLine, type: text_general, indexed: true, stored:true, multiValued:false}}",
			"{add-field: {name: cellType, type: text_general, indexed: true, stored:true, multiValued:false}}",
			"{add-field: {name: organism, type: text_general, indexed: true, stored:true, multiValued:false}}",
			"{add-field: {name: tissue, type: text_general, indexed: true, stored:true, multiValued:false}}",
			"{add-field: {name: disease, type: text_general, indexed: true, stored:true, multiValued:false}}");
}
