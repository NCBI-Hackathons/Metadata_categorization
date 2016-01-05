#!/usr/bin/perl

# This script parses the biosample xml data and grabs the GEO, SRA and BioSample IDs

$flag = 0;
$biosampleID = '.';
$biosampleAcc = '.';
$sra = '.';
$geo = '.';

print "BioSampleID\tBioSampleAcc\tGEO_label\tSRA\n";

while (<>) {
    if (/^\s*<BioSample\s[^>]*id="(\d+)"[^>]*>/) {
	$biosampleID = $1;
    }
    if (/^\s*<Ids>\s*$/) {
	if (($counter % 10000) == 0) {
	    print STDERR "$counter processed";
	}
	$flag = 1;
    }
    if ($flag) {
	if (/\s*<Id\s+db="([^"]+)"[^>]*>([^<]+)<\/Id>\s*$/) {
	    $name = $1;
	    $data = $2;
	    if ($name eq 'BioSample') { $biosampleAcc = $data; }
	    elsif ($name eq 'GEO') {$geo = $data; }
	    elsif ($name eq 'SRA') {$sra = $data; }
	}
    }
    if (/^\s*<\/Ids>\s*$/) {
	$counter++;
	print "$biosampleID\t$biosampleAcc\t$geo\t$sra\n";
	$biosampleID  = '.';
	$biosampleAcc = '.';
	$sra = '.';
	$geo = '.';
	$flag = 0;
    }
    
}
