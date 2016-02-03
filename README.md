# Metadata categorization
A crowdsourcing/expert curation platform for metadata categorization


The Metadata Categorization web application allows expert biological indexers to curate a sample set drawn from [NCBI BioSample](http://www.ncbi.nlm.nih.gov/biosample) data.  The purpose of the annotation tool is to identify terms in the user submission that can be correlated to controlled terms from the [Cell Line Ontology](http://www.clo-ontology.org).  In the best case, the annotator can identify the correct cell line from submitted data and then additional information about the submitted data such as cell type, tissue, and disease can be populated from the Cell Line Ontology.

#Solr-based Data Sources

This project depends on two Solr cores generated for this purpose which contain the relevant data from BioSample and the Cell Line Ontology. The Solr core generated from BioSample will store the user-submitted data extracted from BioSample as well as the annotations supplied by the curators.

#Future work

- [Issue 3](https://github.com/NCBI-Hackathons/Metadata_categorization/issues/3): Incorporate a recommendations feature generated from minimal text processing to provide annotators with a "best guess" at the cell line to facilitate the annotation task.
- [Issue 6](https://github.com/NCBI-Hackathons/Metadata_categorization/issues/6): Incorporate a search feature into the web interface so annotators can look up controlled terms from the Cell Line Ontology.  
- [Issue 7](https://github.com/NCBI-Hackathons/Metadata_categorization/issues/7): Add "Recently Used" shortcut buttons to the web interface so that annotators can quickly access frequently used annotation terms

#Screenshots
Summary records, showing on-the-fly aggregations of individual BioSample records that have the same source cell line value:
![Summary records](screenshots/summary_records.png)

Individuals records for "HEK293T" source cell line, showing annotated value for "Tissue" field in green.  Unannotated (i.e. source) values are shown in red.
![Individual records](screenshots/individual_records.png)

Editing fields at the the summary record level propagates value to all individual records in that group
![Editing summary record](screenshots/summary_record_annotated_tissue.png)
![Propgated edit in individual records](screenshots/individual_records_all_annotated_tissue.png)
