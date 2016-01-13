#!/usr/bin/perl

# This script grabs the ID-label pairs from the non-CLO ontologies in clo_merged.owl

$id = '';

while (<>) {
    chomp;
    # look for IDs we want to find labels for
    if (/^\s*<owl:Class\s+rdf:about="[^"]*(CL_\d+)">\s*$/) { $id = $1; }
    elsif (/^\s*<owl:Class\s+rdf:about="[^"]*(NCBITaxon_\d+)">\s*$/) { $id = $1; }
    elsif (/^\s*<owl:Class\s+rdf:about="[^"]*(UBERON_\d+)">\s*$/) { $id = $1; }
    elsif (/^\s*<owl:Class\s+rdf:about="[^"]*(DOID_\d+)">\s*$/) { $id = $1; }
    elsif (/^\s*<owl:Class\s+rdf:about="[^"]*snomedct\/anatomy#(\d+)">\s*$/) { $id = "SNOMEID_$1"; }

    if (/^\s*<rdfs:label\s+[^>]*>([^<]+)<\/rdfs:label>\s*$/ and ($id ne '')) {
	print "$id\t$1\n";
	$id = '';
    }
}
