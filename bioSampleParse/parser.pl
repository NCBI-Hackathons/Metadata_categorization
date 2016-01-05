#!/usr/bin/perl

binmode STDOUT, ":encoding(UTF-8)";

use XML::LibXML;
use XML::LibXML::Iterator;

$organism = 'Homo Sapiens';

$header = "SRA_ID\tBioSampleID\tBioSampleAccession\tExperimentTitle\tExperimentParagraph\tOrganism\tSample_Name\tSample_Title\tTissue\tCell_Line\tCell_Type\tCell_Subtype\tDisease\tHealth_State\tPhenotype\tTreatment\n";
print $header;

$counter = 0;
$humanCounter = 0;

while (<>) {
    if (/^<BioSample.*/) {
	$counter++;
	if (($counter % 1000) == 0) {
	    print STDERR "Looked at $counter BioSamples, $humanCounter humans encountered so far\n";
	}
	$soFar = '';
    }
    $soFar .= $_;
    if (/^<\/BioSample>/) {
	# if it's not human, don't bother parsing it
	next unless ($soFar =~ /Organism taxonomy_id="9606"/);

	$humanCounter++;
	
	$doc  = undef;
	$iter = undef;
	$doc  = XML::LibXML->new->parse_string($soFar);
	$iter = XML::LibXML::Iterator->new($doc);
	
	# not sure what this does
#	$iter->iterator_function ( \&iterator_function );

	$BioSampleAccession    = '.';
	$BioSampleID           = '.';
	$SRA_ID                = '.';
	$sample_name           = '.';
	$sample_title          = '.';
	$tissue                = '.';
	$cell_line             = '.';
	$cell_type             = '.';
	$cell_subtype          = '.';
	$disease               = '.';
	$health_state          = '.';
	$phenotype             = '.';
	$treatment             = '.';
	$description_title     = '.';
	$description_paragraph = '.';

	while ($cur = $iter->nextNode) {
	    $wholeStr = $cur;
	    if ($wholeStr =~ /^\s*<BioSample.*?id="(\d+)"\s+accession="([A-Z\d]+)"/) {
		$BioSampleID = $1;
		$BioSampleAccession = $2;
	    } elsif ($wholeStr =~ /^\s*<Id\sdb="SRA">([A-Z\d]+)<\/Id>\s*$/) {
		$SRA_ID = $1;
	    } elsif ($wholeStr =~ /^\s*<Attribute\s+attribute_name="([^"]+)"[^>]*>([^<]+)<\/Attribute>\s*$/) {
		$att_name = $1;
		$att_field = $2;
		if ($att_name =~ /sample_name/i) {  $sample_name = $att_field; }
		elsif ($att_name =~ /sample_title/i) { $sample_title = $att_field; }
		elsif ($att_name =~ /tissue/ or $att_name =~ /tissue_type/i) { $tissue = $att_field }
		elsif ($att_name =~ /cell(-|_|\s)line/i) { $cell_line = $att_field }
		elsif ($att_name =~ /cell(-|_|\s)type/i) { $cell_type = $att_field }
		elsif ($att_name =~ /cell(-|_|\s)subtype/i) { $cell_subtype = $att_field }
		elsif ($att_name =~ /disease(s|\sstate)?/i) { $disease = $att_field }
		elsif ($att_name =~ /health(-|_|\s)state/i) { $health_state = $att_field }
		elsif ($att_name =~ /phenotype/i) { $phenotype = $att_field }
		elsif ($att_name =~ /treatment/i) { $treatment = $att_field }
	    # if the beginning and end match
	    } elsif ($wholeStr =~ /^\s*<Description>(.*)<\/Description>\s*$/s) { 
		$description_paragraph = '';
		while ($wholeStr =~ s/<Paragraph>([^<]*)<\/Paragraph>//) {
		    $description_paragraph .= $1;
		}
		if ($description_paragraph eq '') {
		    $description_paragraph = '';
		}
		if ($wholeStr =~ /<Title>([^<]*)<\/Title>/) {
		    $description_title = $1;
		}
	    }
	}

	$outputStr = sprintf("%s\t%d\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
			     $SRA_ID, $BioSampleID, $BioSampleAccession, $description_title,
			     $description_paragraph, $organism, $sample_name, $sample_title,
			     $tissue, $cell_line, $cell_type, $cell_subtype, $disease, $health_state,
			     $phenotype, $treatment);
	
	print $outputStr;
    }
}



