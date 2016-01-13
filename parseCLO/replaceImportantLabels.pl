#!/usr/bin/perl

# This script creates a dictionary of 

$dictFile = shift @ARGV;

open ($FH, "<", $dictFile) or die "Could not open $dictFile: $!\n";

while (<$FH>) {
    chomp;
    @_ = split /\t/;
    $tr{$_[0]} = $_[1];
}

close $FH;

@keys = sort keys %tr;

while (<>) {
    for $key (@keys) {
	$_ =~ s/$key/$tr{$key}/;
    }
    print $_;
}
