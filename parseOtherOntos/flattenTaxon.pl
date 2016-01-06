#!/usr/bin/perl

# this script takes in a taxonomy owl file and extracts the
#   NCBITaxon_ID and label pairs, spitting them out to STDOUT
#   separated by tabs

$taxon = '';

while (<>) {
    if (m{^\s*<rdfs:label\s+rdf:datatype="http://www.w3.org/2001/XMLSchema#string">([^<]+)</rdfs:label>\s*$}) {
	if ($taxon ne '') {
	    print "$taxon\t$1\n";
	    $taxon = '';
	}
    }
    if (m{^\s*<owl:Class\s*rdf:about="http://purl.obolibrary.org/obo/(NCBITaxon_\d+)">\s*$}) {
	$taxon = $1;
    }
}
