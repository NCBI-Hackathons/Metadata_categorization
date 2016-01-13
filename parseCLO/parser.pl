#!/usr/bin/perl

# A script to parse "useful" portions of the CLO.owl file
# This makes fairly strong assumptions about the CLO.owl file,
#   which may not be true in future versions of the file (or 
#   this version of the file)

binmode STDOUT, ":encoding(UTF-8)";

use XML::LibXML;
use XML::LibXML::Iterator;
use Try::Tiny;

$header = "CLO_ID\tLabel\tSynonyms\tCell_Line\tDisease\tNCBI_Taxon\tUberon\tSnome\n";
print $header;

$counter = 0;
$humanCounter = 0;
$classDepth = 0;

$inXMLHeader = 1;

# read in the file, one line at a time
while (<>) {
    if ($inXMLHeader) {
	$XMLHeader .= $_;
	if (/\s*<\/owl:Ontology>\s*/) {
	    $inXMLHeader = 0;
	}
    }
    # if we're in a new ObjectProperty
    if (/^\s*<owl:ObjectProperty(?:\s+rdf:about=[^>]*[^\/])?>/) {
	$objPropDepth++;
    }
    if (/^\s*<\/owl:ObjectProperty>\s*$/){
	$objPropDepth--;
    }
    # if we find a new Class
    if (/^\s*<owl:Class[^>]*>\s*$/) {
	if (/Class\s*rdf:about="[^"]*">/) {
	    $soFar = $XMLHeader;
	}
	$classDepth++;
    }
    # append this line to our data so far
    $soFar .= $_;
    if (/^\s*<\/owl:Class>\s*$/) {
	$classDepth--;
	if (($classDepth == 0) and ($objPropDepth == 0)) {
	    try {
		$counter++;
		$soFar .= "</rdf:RDF>\n";
		$doc  = undef;
		$iter = undef;
		$doc  = XML::LibXML->new->parse_string($soFar);
		$iter = XML::LibXML::Iterator->new($doc);
	    } catch {
		die "Caught error: $_\nProblem parsing: \n$soFar";
	    };


	    $clo_id = '.';
	    $label = '.';
	    $synonyms = '"';
	    $comment_disease = '.';
	    $cell_line = '.';
	    $ncbi_taxon = '.';
	    $uberon = '.';
	    $snomedct = '.';


	    # iterate through the XML nodes (the root node appears as a string containing the entire record
	    while ($cur = $iter->nextNode) {
		$wholeStr = $cur;
		# if we're at the smallest portion to extract data, do it
		if ($wholeStr =~ /^\s*<obo:IAO_0000118[^>]*>([^<]+)<\/obo:IAO_0000118>\s*$/) { $synonyms .= "$1;"; }
		elsif ($wholeStr =~ /^\s*<rdfs:seeAlso[^>]*>[^:<]+:\s+([^<]+)<\/rdfs:seeAlso>\s*$/) { $synonyms .= "$1;"; }
		elsif ($wholeStr =~ /^\s*<owl:Class\s*rdf:about="[^>]*(CLO_\d+)">/) { $clo_id = $1; }
		elsif ($wholeStr =~ /^\s*<rdfs:comment[^>]*>disease:\s*([^<]+)<\/rdfs:comment>\s*$/) { $comment_disease = $1; }
		elsif ($wholeStr =~ /^\s*<owl:someValuesFrom\s+rdf:resource="[^>]*(NCBITaxon_\d+)"\/>\s*$/) { $ncbi_taxon = $1; }
		elsif ($wholeStr =~ /^\s*<rdf:Description rdf:about="[^>]*(CL_\d+)"\/>\s*$/) { $cell_line = $1; }
		elsif ($wholeStr =~ /^\s*<rdfs:label(?:\s+[^>]+=[^>]*)?>([^<]*)<\/rdfs:label>\s*$/) { $label = $1; }
		elsif ($wholeStr =~ /^\s*<rdf:Description\s+rdf:about="[^>]*(UBERON_\d+)"\/>\s*$/) { $uberon = $1; }
		elsif ($wholeStr =~ /^\s*<rdf:Description\s+rdf:about="[^>]*ihtsdo.org\/snomedct\/anatomy#(\d+)"\/>\s*$/) { $snomedct = "SNOMEID_$1"; }
	    }

	    chop ($synonyms); # chop of either the trailing semi-colon OR the only quote
	    if ($synonyms eq '') {
		$synonyms = '.';
	    } else {
		$synonyms .= '"';
	    }

	    # output this record if it has a CLO_ID
	    unless ($clo_id eq '.') {
		$outputStr = sprintf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
				     $clo_id, $label, $synonyms, $cell_line, $comment_disease, $ncbi_taxon, $uberon, $snomedct);
	    
		print $outputStr;
	    }
	    $soFar = '';
	}


    }
}



