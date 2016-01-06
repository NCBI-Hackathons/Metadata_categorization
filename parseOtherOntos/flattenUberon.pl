#!/usr/bin/perl

# this script takes in an uberon.owl file (from
#   http://berkeleybop.org/ontologies/uberon.owl) and extracts the
#   UBERON_ID and label pairs, spitting them out to STDOUT separated
#   by tabs

$uber = '';

while (<>) {
    if (m{^\s*<rdfs:label\s+rdf:datatype="http://www.w3.org/2001/XMLSchema#string">([^<]+)</rdfs:label>\s*$}) {
	if ($uber ne '') {
	    print "$uber\t$1\n";
	    $uber = '';
	}
    }
    if (m{^\s*<owl:Class\s+rdf:about="http://purl.obolibrary.org/obo/(UBERON_\d+)">\s*$}) {
	$uber = $1;
    }
}
