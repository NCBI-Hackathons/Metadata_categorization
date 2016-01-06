#!/usr/bin/perl

if ($ARGV[0] =~ /(CLO_\d+)\.xml/) {
    $name = $1;
}

$flag = 0;
$data = '';
$diseaseID = '.';
$diseaseLabel = '.';
$uberonID = '.';
$uberonLabel = '.';
$taxonID = '.';
$taxonLabel = '.';
$cellTypeID = '.';
$cellTypeLabel = '.';
$CLOLabel = '.';

$allData = '';

while (<>) {
    chomp;
    $allData .= $_;
    if (/^\s*Superclasses\s*&amp;\s*Asserted\s*Axioms/) {
	$flag = 1;
    }
    if ($flag) {
	$data .= $_;
    }
}

if ($allData =~ /"section-title-value">\s*([^<]*?)\s*</s) {
    $CLOLabel = $1;
    s/\s+/ /g;
}    

$data =~ s/\s+/ /g; # condense whitespace


if ($data =~ /(CL_\d+)">\s*([^<]*)</) {
    $cellTypeID = $1;
    $cellTypeLabel = $2;
}
if ($data =~ /(DOID_\d+)">\s*([^<]*)</) {
    $diseaseID = $1;
    $diseaseLabel = $2;
}
if ($data =~ /(UBERON_\d+)">\s*([^<]*)</) {
    $uberonID = $1;
    $uberonLabel = $2;
}
if ($data =~ /(NCBITaxon_\d+)">\s*([^<]*)</) {
    $taxonID = $1;
    $taxonLabel = $2;
}

print "$name\t$CLOLabel\t$diseaseID\t$diseaseLabel\t$uberonID\t$uberonLabel\t$taxonID\t$taxonLabel\t$cellTypeID\t$cellTypeLabel\n";
