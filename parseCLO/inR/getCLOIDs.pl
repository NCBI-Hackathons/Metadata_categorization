#!/usr/bin/perl

# This script pulls out all the CLO IDs in the CLO.owl file.
# Probably should be used as: ./getCLOIDs.pl CLO.owl | sort | uniq
#   to get a list of all the unique CLO IDs in the .owl file

while (<>) {
    if (/owl:Class/ and /(CLO_\d+)/) {
#	print "$1\n";
    } elsif (/(CLO_\d+)/) {
	print "$1\n";
    }
}
