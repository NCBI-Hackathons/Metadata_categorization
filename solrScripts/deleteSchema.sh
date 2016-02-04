#author: Lena Pons
#date: 4-Feb-2016
#script to delete a field in solr schema
name="test"			#name of the field to delete
core="AnnotationsDev"		#name of the core to modify
curl -X POST -H 'Content-type:application/json' --data-binary '{"delete-field":{"name":"'$name'"}}' http://localhost:8983/solr/$core/schema
