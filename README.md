# Metadata categorization
A crowdsourcing/expert curation platform for metadata categorization


The Metadata Categorization web tool will allow expert biological indexers to curate a sample set drawn from NCBI BioSample data. 
(More information on BioSample is here: http://www.ncbi.nlm.nih.gov/biosample). The purpose of the annotation tool is to identify terms in the user submission that can be correlated to controlled terms from the Cell Line Ontology (see http://www.clo-ontology.org/).
In the best case, the annotator can identify the correct cell line from submitted data and then additional information about the submitted data such as cell type, tissue, and disease can be populated from the Cell Line Ontology.

Solr-based Data Sources

This project depends on two Solr cores generated for this purpose which contain the relevant data from BioSample and the Cell Line Ontology. The Solr core generated from BioSample will store the user-submitted data extracted from BioSample as well as the annotations supplied by the curators.

Future work

- Incorporate a search feature into the web interface so annotators can look up controlled terms from the Cell Line Ontology.
- Incorporate a recommendations feature generated from minimal text processing to provide annotators with a "best guess" at the cell line to facilitate the annotation task.
- Add "Recently Used" shortcut buttons to the web interface so that annotators can quickly access frequently used annotation terms
