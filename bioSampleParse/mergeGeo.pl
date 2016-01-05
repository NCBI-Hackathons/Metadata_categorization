#!/usr/bin/perl

# This script merges the results of the old parser.pl with the results of getSamp_GEO_SRA.pl
# This takes two command line arguments: 1st the output of getSamp_GEO_SRA.pl, 2nd the result of parser.pl

my %geos;
my %sras;
my %accs;

$file = shift @ARGV;
open ($fh, "<", $file) or die "Couldn't find file $file: $!\n";

while (<$fh>) {
    $counter++;
    chomp;
    @_ = split /\t/, $_, 4;
    $accs{$_[0]} = $_[1];
    $geos{$_[0]} = $_[2];
    $sras{$_[0]} = $_[3];
}

while (<>) {
    if (/^SRA_ID/) {
	@_ = split /\t+/, $_, 4;
	print "$_[1]\t$_[0]\tGEO_Label\t$_[2]\t$_[3]";
	next;
    }
    @_ = split /\t/, $_, 4;
    next if ($_[1] eq '.');
    if (exists($geos{$_[1]}) or exists($sras{$_[1]})) {
	if ($sras{$_[1]} ne $_[0]) {
	    print STDERR "Mismatch! Large parse SRA: ($_[0]) Small parse SRA: ($sras{$_[1]})\n";
	}
	next if (($sras{$_[1]} eq '.') and ($geos{$_[1]} eq '.'));
	print "$_[1]\t$sras{$_[1]}\t$geos{$_[1]}\t$_[2]\t$_[3]";
    } else {
	$filtered++;
    }
}

print STDERR "$filtered samples didn't have SRA or GEO ID\n";
