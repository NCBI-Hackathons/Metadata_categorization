#author: Lena Pons
#date: 4-Feb-2016
#script to modify solr schema
name="test" 		#this is the name of your new field
core="AnnotationsDev" 	#this is the solr core you want to modify	
indexed=true	 	#set to true for all modifiable fields
stored=true		#MUST be true for modifiable fields
multiValued=false	#Solr defults this true, so set false	
default="0"		#this is default value, omit this field if not used
datatype="text_general"	#data type of the field 
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field":	{"name":"'$name'","type":"'$datatype'",	"indexed":"'$indexed'",	"multiValued":"'$multiValued'","stored":"'$stored'"}}' http://localhost:8983/solr/$core/schema
