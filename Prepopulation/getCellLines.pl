#!/usr/bin/perl

while (<>) {
    if (/^\s+<owl:Class\s+rdf:about="([^"]*)">/) {
#	print STDERR "Yay!\n";
	if ($1 =~ m/&obo;(.*)/) {
	    print "\n$1";
	}
    }
    if (/^\s+<rdfs:label[^>]*>([^<]*)<\/rdfs:label>/) {
	print "\t$1";
    }
}
