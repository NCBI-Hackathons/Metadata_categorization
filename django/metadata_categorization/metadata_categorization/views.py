import json
import urllib

from django.views import generic
from django.shortcuts import render
from django.conf import settings
from django.core.urlresolvers import reverse
from django.http import HttpResponse


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
            "sourceAnatomy": "",
            "sourceTreatment": "",
            "sourceSpecies": "Homo Sapiens",
            "sourceDisease": ""
        }

        annotFields = {
            "annotCellLine": "",
            "annotCellType": "",
            "annotCellTreatment": "",
            "annotAnatomy": "",
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
                    summaryRecord['sourceCellLine'] = prevCellLine

                summaryRecords.append(summaryRecord)
                summaryRecord = {"individualRecords": [], "index": i}
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
    def get_context_data(self, queueId, **kwargs):

        context = super(QueueView, self).get_context_data()

        # TODO:
        # Refactor into modular methods and models

        # Call Solr
        #solr_host = "http://localhost:8983/solr/AnnotationsDev"
        solr_host = "http://localhost:8983/solr/annotation"

        # %3A is :
        # sampleName%3A* is sampleName:*"
        url = (solr_host + "/select?" +
              #"q=queueId%3A42&" +
              'q=queueId%3A' + queueId + '+AND+NOT+sourceCellLine%3A%220%22'
              "start=0&" +
              "rows=10000&" +
              "wt=json&" +
              "indent=true&")

        request = urllib.request.Request(url)
        response = urllib.request.urlopen(request)
        str_response = response.readall().decode('utf-8')

        solr_data = json.loads(str_response)["response"]["docs"]

        #print(solr_data)

        context['solr_data'] = self.getSummaryRecords(solr_data)

        summaryRecords = self.getSummaryRecords(solr_data)

        '''
        # TODO
        # There's a bug in the summary sourceCellLine
        # Remove this hack once fixed
        tmp = []
        for summaryRecord in summaryRecords:
            if summaryRecord['sourceCellLine'] != '':
                tmp.append(summaryRecord)
        summaryRecords = tmp
        '''

        # Demo'd URL:
        # http://localhost:8000/queue/6

        context["id"] = queueId
        context["summaryRecords"] = summaryRecords

        return context


class RecordView(generic.TemplateView):

    def post(self, request, *args, **kwargs):
        print(kwargs['recordId'])
        print(request.body)
        return HttpResponse()
