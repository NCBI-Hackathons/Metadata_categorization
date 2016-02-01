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
            "sourceSpecies": "",
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

        individualRecords = sorted(
                        individualRecords,
                        key=lambda k: k['sourceCellLine']
                    )

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

        for i, individualRecord in enumerate(individualRecords):
            if "sourceCellLine" not in individualRecord:
                print('"sourceCellLine" not in individualRecord')
                individualRecords[i]["sourceCellLine"] = ""

            cellLine = individualRecords[i]["sourceCellLine"]

            prevCellLine = individualRecords[i-1]["sourceCellLine"]

            if cellLine == prevCellLine or i == 0:
                summaryRecord["individualRecords"].append(individualRecord)
            else:
                recordsCount = summaryRecord["individualRecords"]
                summaryRecord['recordsCount'] = len(recordsCount)

                if len(summaryRecord["individualRecords"]) > 0:
                    summaryRecord['sourceCellLine'] = prevCellLine

                summaryRecords.append(summaryRecord)
                summaryRecord = {
                    "individualRecords": [individualRecord],
                    "index": i
                }
                summaryRecord.update(sourceFields)
                summaryRecord.update(annotFields)

        newSRs = []
        for i, summaryRecord in enumerate(summaryRecords[:10]):
            prevFields = {}
            newSR = summaryRecord
            individualRecords = summaryRecord['individualRecords']

            if len(individualRecords) == 1:
                newSR.update(individualRecords[0])
                newSRs.append(newSR)
                continue

            for j, individualRecord in enumerate(individualRecords):
                for field in individualRecord:
                    if j == 0:
                        prevFields[field] = individualRecord[field]
                    else:
                        if (
                            prevFields[field] != "" and
                            prevFields[field] == individualRecord[field]
                        ):
                            newSR[field] = prevFields[field]
                        else:
                            newSR[field] = ""
            newSRs.append(newSR)
        summaryRecords = newSRs

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

    def transform_record(self, id, data):
        # Transforms data to record format used by this app's Solr core
        record = {
            'annotCellLine': data.get('annotCellLine'),
            'annotCellType': data.get('annotCellType'),
            'annotAnatomy': data.get('annotAnatomy'),
            'annotSpecies': data.get('annotSpecies'),
            'annotDisease': data.get('annotDisease')
        }

        nr = {'id': id} # new record
        for key in record:
            value = record[key]
            if value == '':
                # We store empty strings as "0" in Solr.  I forget exactly why.
                # TODO: Investigate why.  If needed, document, else refactor.
                nr[key] = {'set': '0'}
            else:
                nr[key] = {'set': value}

        return [nr]

    def post(self, request, *args, **kwargs):

        data = request.POST

        id = kwargs['recordId']

        record = self.transform_record(id, data)

        # POST request body, representing an edited individual record
        body = str(record).encode('utf-8')

        solr_host = "http://localhost:8983/solr/annotation"
        url = solr_host + "/update?commit=true"

        # Example POST to update part of a Solr document in "annotation" core:
        # curl 'http://localhost:8983/solr/annotation/update?commit=true' -d "[{'annotAnatomy': {'set': '0'}, 'annotDisease': {'set': '0'}, 'annotSpecies': {'set': '0'}, 'id': '1090570', 'annotCellType': {'set': '0'}, 'annotCellLine': {'set': 'test'}}]"
        # Example read:
        # curl 'http://localhost:8983/solr/annotation/select?wt=json&q=id:1090570'
        # Another POST / partial update example:
        # curl 'http://localhost:8983/solr/annotation/update?commit=true' -d '[{"id": 3854415, "sourceCellLine": {"set": "testfoo"}}]'
        request = urllib.request.Request(url, body)
        request.add_header('Content-Type', 'application/json')
        response = urllib.request.urlopen(request)
        str_response = response.readall().decode('utf-8')

        return HttpResponse()
