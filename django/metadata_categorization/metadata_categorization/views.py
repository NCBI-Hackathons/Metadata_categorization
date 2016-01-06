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

    def getSummaryRecords(self, individualRecords):
        """
        Aggregates individual Biosample (SRA or GEO) records into
        "summary records" shown as top-level table rows in the UI.  Individual
        records are grouped into the same summary record only if they have
        the same 'sourceCellLine' value.

        Summary records have most of the same fields (columns) as
        the individual records.  Each summary record also has a list of the
        individual records that it contains/aggregates.

        It was unclear whether/how such aggregation was possible in Solr itself,
        so we're doing it in the web tier.
        """

        sourceFields = {
            "sourceCellLine": "",
            "sourceCellType": "",
            "sourceCellTreatment": "",
            "sourceCellAnatomy": "",
            "sourceTreatment": "",
            "sourceSpecies": "Homo Sapiens",
            "sourceDisease": ""
        }

        annotatedFields = {
            "annotatedCellLine": "",
            "annotatedCellType": "",
            "annotatedCellTreatment": "",
            "annotatedCellAnatomy": "",
            "annotatedSpecies": "",
            "annotatedSpecies": "",
            "annotatedDisease": ""
        }

        summaryRecords = []

        summaryRecord = {"individualRecords": []}
        summaryRecord.update(sourceFields)
        summaryRecord.update(annotatedFields)

        for i, individualRecord in enumerate(individualRecords):
            print(individualRecord)

            for field in individualRecord:
                if individualRecord[field] == 0:
                    individualRecord[field] = ""

            if "sourceCellLine" not in individualRecords[i]:
                individualRecords[i]["sourceCellLine"] = ""

            cellLine = individualRecords[i]["sourceCellLine"]

            if i == 0:
                prevCellLine = ""
            else:
                prevCellLine = individualRecords[-1]["sourceCellLine"]

            if cellLine == prevCellLine:
                summaryRecord["individualRecords"].append(individualRecord)
            else:
                summaryRecords.append(summaryRecord)
                summaryRecord = {"individualRecords": []}
                summaryRecord.update(sourceFields)
                summaryRecord.update(annotatedFields)

        return summaryRecords

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
        solr_host = "http://localhost:8983/solr/AnnotationsDev"

        # %3A is :
        # sampleName%3A* is sampleName:*"
        url = (solr_host + "/select?" +
              "q=queueId%3A5+AND+sourceCellLine%3A*&" +
              #"start=0&" +
              #"rows=100&" +
              "wt=json&" +
              "indent=true&")

        request = urllib.request.Request(url)

        response = urllib.request.urlopen(request)

        str_response = response.readall().decode('utf-8')

        solr_data = json.loads(str_response)["response"]["docs"]

        print(solr_data)

        context['solr_data'] = self.getSummaryRecords(solr_data)

        context["id"] = 42
        context["summaryRecords"] = summaryRecords

        return context
