#!/usr/bin/perl

# this script takes in a Cell Line owl file and flattens it to CL_ID - Label pairs in a TSV format

$cellLine = '';

while (<>) {
    if (m{^\s*<rdfs:label\s+rdf:datatype="http://www.w3.org/2001/XMLSchema#string">([^<]+)</rdfs:label>\s*$}) {
	if ($cellLine ne '') {
	    print "$cellLine\t$1\n";
	    $cellLine = '';
	}
    }
    if (m{^\s*<owl:Class\s+rdf:about="http://purl.obolibrary.org/obo/(CL_\d+)">\s*$}) {
	$cellLine = $1;
    }
}
