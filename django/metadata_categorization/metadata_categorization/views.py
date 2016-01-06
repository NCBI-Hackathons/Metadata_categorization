import json
import urllib

from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from django.views import generic
from django.shortcuts import render, redirect
from django.core.files.storage import default_storage
from django.views.decorators.csrf import csrf_exempt
from django.core.files.base import ContentFile
from django.conf import settings
from django.core.urlresolvers import reverse

class IndexView(generic.TemplateView):
    template_name = 'metadata_categorization/index.html'

class QueueView(generic.TemplateView):
    template_name = 'metadata_categorization/queue.html'

    # curationQueue
    # ID
    # list summaryRecord
    # summaryRecord
    # list indivdualRecord
    # list
    # individualRecord term|cell line|cell type|anatomy|species|disease|note
    def get_context_data(self, **kwargs):

        context = super(QueueView, self).get_context_data()

        summaryRecords = [
            {
                "sourceCellLine": "HeLa",
                "sourceCellType": "epf tm",
                "sourceCellTreatment": "firboblast",
                "sourceCellAnatomy": "firboblast",
                "sourceTreatment": "Folderol",
                "sourceSpecies": "human",
                "sourceDisease": "gout",
                "annotatedCellLine": "",
                "annotatedCellType": "",
                "annotatedCellTreatment": "",
                "annotatedCellAnatomy": "",
                "annotatedSpecies": "",
                "annotatedSpecies": "",
                "annotatedDisease": "",
                "note": "",
                "individualRecords": [
                    {
                        "id": 12,
                        "sourceCellLine": "HeLa",
                        "sourceCellType": "epf tm",
                        "sourceCellTreatment": "firboblast",
                        "sourceCellAnatomy": "foot",
                        "sourceSpecies": "Folderol",
                        "sourceSpecies": "human",
                        "sourceDisease": "gout",
                        "annotatedCellLine": "",
                        "annotatedCellType": "",
                        "annotatedCellTreatment": "",
                        "annotatedCellAnatomy": "",
                        "annotatedSpecies": "",
                        "annotatedSpecies": "",
                        "annotatedDisease": "",
                        "note": ""
                    }
                ]
            },
            {
                "sourceCellLine": "epf-1",
                "sourceCellType": "epf tm",
                "sourceCellTreatment": "firboblast",
                "sourceCellAnatomy": "foot",
                "sourceSpecies": "Folderol",
                "sourceSpecies": "human",
                "sourceDisease": "gout",
                "annotatedCellLine": "",
                "annotatedCellType": "",
                "annotatedCellTreatment": "",
                "annotatedCellAnatomy": "",
                "annotatedSpecies": "",
                "annotatedSpecies": "",
                "annotatedDisease": "",
                "note": "",
                "individualRecords": [
                    {
                        "id": 1234,
                        "sourceCellLine": "epf-1",
                        "sourceCellType": "epf tm",
                        "sourceCellTreatment": "firboblast",
                        "sourceCellAnatomy": "firboblast",
                        "sourceSpecies": "Folderol",
                        "sourceSpecies": "human",
                        "sourceDisease": "gout",
                        "annotatedCellLine": "",
                        "annotatedCellType": "",
                        "annotatedCellTreatment": "",
                        "annotatedCellAnatomy": "",
                        "annotatedSpecies": "",
                        "annotatedSpecies": "",
                        "annotatedDisease": "",
                        "note": ""
                    },
                    {
                        "id": 12345,
                        "sourceCellLine": "epf-1",
                        "sourceCellType": "",
                        "sourceCellTreatment": "",
                        "sourceCellAnatomy": "firboblast",
                        "sourceSpecies": "",
                        "sourceSpecies": "human",
                        "sourceDisease": "gout",
                        "annotatedCellLine": "",
                        "annotatedCellType": "",
                        "annotatedCellTreatment": "",
                        "annotatedCellAnatomy": "",
                        "annotatedSpecies": "",
                        "annotatedSpecies": "",
                        "annotatedDisease": "",
                        "note": ""
                    }
                ]
            }
        ]

        # TODO:
        # Refactor into modular methods and models

        # Call Solr
        solr_host = "http://localhost:8983/solr/annotation"

        # %3A is :
        # sampleName%3A* is sampleName:*"
        url = (solr_host + "/select?" +
              "q=*%3A*&" +
              "start=0&" +
              "rows=100&" +
              "wt=json&" +
              "indent=true&")

        request = urllib.request.Request(url)

        response = urllib.request.urlopen(request)

        str_response = response.readall().decode('utf-8')

        solr_data = json.loads(str_response)["response"]["docs"]

        context['solr_data'] = solr_data

        context["id"] = 42
        context["summaryRecords"] = summaryRecords

        return context
