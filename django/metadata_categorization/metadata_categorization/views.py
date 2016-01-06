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

        annotFields = {
            "annotCellLine": "",
            "annotCellType": "",
            "annotCellTreatment": "",
            "annotCellAnatomy": "",
            "annotSpecies": "",
            "annotSpecies": "",
            "annotDisease": ""
        }

        summaryRecords = []

        summaryRecord = {"individualRecords": []}
        summaryRecord.update(sourceFields)
        summaryRecord.update(annotFields)

        #print(len(individualRecords))

        tmpIRs = []
        for i, individualRecord in enumerate(individualRecords):
            tmpIR = {}
            for field in individualRecord:
                thisField = individualRecord[field]
                tmpIR[field] = thisField
                if thisField == '0' or thisField == "   ":
                    individualRecords[i][field] = ""
                    individualRecord[field] = ""
                    tmpIR[field] = ""

            individualRecord = tmpIR
            tmpIRs.append(individualRecord)

        individualRecords = tmpIRs

        #print(individualRecords)

        for i, individualRecord in enumerate(individualRecords):
            if "sourceCellLine" not in individualRecord:
                print('"sourceCellLine" not in individualRecord')
                individualRecords[i]["sourceCellLine"] = ""

            cellLine = individualRecords[i]["sourceCellLine"]

            if i == 0:
                prevCellLine = ""
            else:
                prevCellLine = individualRecords[i-1]["sourceCellLine"]

            if cellLine == prevCellLine:
                summaryRecord["individualRecords"].append(individualRecord)
            else:
                recordsCount = summaryRecord["individualRecords"]
                summaryRecord['recordsCount'] = len(recordsCount)

                if len(summaryRecord["individualRecords"]) > 0:
                    summaryRecord['sourceCellLine'] = cellLine

                summaryRecords.append(summaryRecord)
                summaryRecord = {"individualRecords": []}
                summaryRecord.update(sourceFields)
                summaryRecord.update(annotFields)

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
                "annotCellLine": "",
                "annotCellType": "",
                "annotCellTreatment": "",
                "annotCellAnatomy": "",
                "annotSpecies": "",
                "annotSpecies": "",
                "annotDisease": "",
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
                        "annotCellLine": "",
                        "annotCellType": "",
                        "annotCellTreatment": "",
                        "annotCellAnatomy": "",
                        "annotSpecies": "",
                        "annotSpecies": "",
                        "annotDisease": "",
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
                "annotCellLine": "",
                "annotCellType": "",
                "annotCellTreatment": "",
                "annotCellAnatomy": "",
                "annotSpecies": "",
                "annotSpecies": "",
                "annotDisease": "",
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
                        "annotCellLine": "",
                        "annotCellType": "",
                        "annotCellTreatment": "",
                        "annotCellAnatomy": "",
                        "annotSpecies": "",
                        "annotDisease": "",
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
                        "annotCellLine": "",
                        "annotCellType": "",
                        "annotCellTreatment": "",
                        "annotCellAnatomy": "",
                        "annotSpecies": "",
                        "annotDisease": "",
                        "note": ""
                    }
                ]
            }
        ]

        # TODO:
        # Refactor into modular methods and models

        # Call Solr
        #solr_host = "http://localhost:8983/solr/AnnotationsDev"
        solr_host = "http://localhost:8983/solr/annotation"

        # %3A is :
        # sampleName%3A* is sampleName:*"

        '''
        url = (solr_host + "/select?" +
              #"q=queueId%3A42&" +
              'queueId%3A5+AND+NOT+sourceCellLine%3A"0"&'
              #"start=0&" +
              #"rows=9999&" +
              "wt=json&" +
              "indent=true&")
        '''
        url = 'http://localhost:8983/solr/annotation/select?q=queueId%3A5+AND+NOT+sourceCellLine%3A%220%22&rows=10000&wt=json&indent=true'

        request = urllib.request.Request(url)

        response = urllib.request.urlopen(request)

        str_response = response.readall().decode('utf-8')

        solr_data = json.loads(str_response)["response"]["docs"]

        #print(solr_data)

        context['solr_data'] = self.getSummaryRecords(solr_data)

        summaryRecords = self.getSummaryRecords(solr_data)

        context["id"] = 42
        context["summaryRecords"] = summaryRecords

        return context
