args <- commandArgs(trailingOnly = TRUE)

concept <- args[1];

library("Sxslt")
library("XML")

url=sprintf("http://www.ontobee.org/ontology/view/CLO?iri=http://purl.obolibrary.org/obo/%s",as.character(concept))
xslt=sprintf('http://www.ontobee.org/ontology/view/CLO?iri=http://purl.obolibrary.org/obo/%s',as.character(concept))

#    print (paste('Trying to get', url));
out <- xmlParse(url,xslt)
saveXML(out, paste(as.character(concept), '.xml', sep=''))
