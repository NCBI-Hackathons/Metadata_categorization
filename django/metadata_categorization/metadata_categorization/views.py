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

    def get_summary_records(self, individual_records):
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

        source_fields = {
            "sampleName": "",
            "sampleTitle": "",
            "sourceCellLine": "",
            "sourceCellType": "",
            "sourceCellTreatment": "",
            "sourceDevStage": "",
            "sourceSex": "",
            "sourceAnatomy": "",
            "harvestSite": "",
            "sourceSpecies": "",
            "sourceDisease": "",
            "note": ""
        }

        annot_fields = {
            "sampleName": "",
            "sampleTitle": "",
            "annotCellLine": "",
            "annotCellType": "",
            "annotCellTreatment": "",
            "annotDevStage": "",
            "annotSex": "",
            "annotAnatomy": "",
            "harvestSite": "",
            "annotSpecies": "",
            "annotSpecies": "",
            "annotDisease": "",
            "note": ""
        }

        summary_records = []

        summary_record = {"individualRecords": []}
        summary_record.update(source_fields)
        summary_record.update(annot_fields)

        ir_fields = summary_record.copy()
        ir_fields.update({
            'id': '',
            'taxId': ''
        })
        del ir_fields['individualRecords']

        individual_records = sorted(
                        individual_records,
                        key=lambda k: k['sourceCellLine']
                    )

        tmp_IRs = []
        s = []
        for i, individual_record in enumerate(individual_records):
            tmp_IR = {}
            for field in ir_fields:
                #if field == 'sourceDisease' or field == 'annotDisease':
                #    continue
                if field not in individual_record:
                    individual_record[field] = ''
                this_field = individual_record[field]
                tmp_IR[field] = this_field
                if this_field == '0' or this_field == "   ":
                    individual_records[i][field] = ""
                    individual_record[field] = ""
                    tmp_IR[field] = ""

            individual_record = tmp_IR
            tmp_IRs.append(individual_record)

        individual_records = tmp_IRs

        for i, individual_record in enumerate(individual_records):
            if "sourceCellLine" not in individual_record:
                individual_records[i]["sourceCellLine"] = ""

            cellLine = individual_records[i]["sourceCellLine"]

            prev_cell_line = individual_records[i-1]["sourceCellLine"]

            if cellLine == prev_cell_line or i == 0:
                summary_record["individualRecords"].append(individual_record)
            else:
                recordsCount = summary_record["individualRecords"]
                summary_record['recordsCount'] = len(recordsCount)

                if len(summary_record["individualRecords"]) > 0:
                    summary_record['sourceCellLine'] = prev_cell_line

                summary_records.append(summary_record)
                summary_record = {
                    "individualRecords": [individual_record],
                    "index": i
                }
                summary_record.update(source_fields)
                summary_record.update(annot_fields)

        new_SRs = []
        for i, summary_record in enumerate(summary_records):
            prev_fields = {}
            new_SR = summary_record
            individual_records = summary_record['individualRecords']

            if len(individual_records) == 1:
                new_SR.update(individual_records[0])
                new_SRs.append(new_SR)
                continue

            for j, individual_record in enumerate(individual_records):
                for field in individual_record:
                    if j == 0:
                        prev_fields[field] = individual_record[field]
                    else:
                        if (
                            prev_fields[field] != "" and
                            prev_fields[field] == individual_record[field]
                        ):
                            new_SR[field] = prev_fields[field]
                        else:
                            new_SR[field] = ""
            new_SRs.append(new_SR)
        summary_records = new_SRs

        return summary_records

    # curationQueue
    # ID
    # list summary_record
    # summary_record
    # list indivdualRecord
    # list
    # individual_record term|cell line|cell type|anatomy|species|disease|note
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

        context['solr_data'] = self.get_summary_records(solr_data)

        summary_records = self.get_summary_records(solr_data)

        '''
        # TODO
        # There's a bug in the summary sourceCellLine
        # Remove this hack once fixed
        tmp = []
        for summary_record in summary_records:
            if summary_record['sourceCellLine'] != '':
                tmp.append(summary_record)
        summary_records = tmp
        '''

        # Demo'd URL:
        # http://localhost:8000/queue/6

        context["id"] = queueId
        context["summary_records"] = summary_records

        return context


class RecordView(generic.TemplateView):

    def transform_record(self, id, data, is_summary):
        # Transforms data to record format used by this app's Solr core

        if is_summary:
            record = {
                'annotCellLine': data['annotCellLine'],
                'annotCellType': data['annotCellType'],
                'annotAnatomy': data['annotAnatomy'],
                'annotSpecies': data['annotSpecies'],
                'note': data['note']
            }
        else:
            record = {
                'annotCellLine': data['annotCellLine'],
                'annotCellType': data['annotCellType'],
                'annotCellTreatment': data['annotCellTreatment'],
                'annotAnatomy': data['annotAnatomy'],
                'harvestSite': data['harvestSite'],
                'annotSpecies': data['annotSpecies'],
                'annotDisease': data['annotDisease'],
                'note': data['note']
            }

        nr = {'id': id} # new record
        for key in record:
            value = record[key]
            nr[key] = {'set': value}

        return nr

    def update_solr(self, records):
        # POST request body, representing an edited individual record
        body = str(records).encode('utf-8')

        solr_host = "http://localhost:8983/solr/annotation"
        #solr_host = "http://localhost:8983/solr/AnnotationsDev"
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

    def post(self, request, *args, **kwargs):

        data = request.POST

        id = kwargs['recordId']

        data = json.loads(data['records'])
        record = self.transform_record(id, data, False)
        record = [record]

        self.update_solr(record)

        return HttpResponse()


class RecordsView(RecordView):

    def post(self, request, *args, **kwargs):

        data = request.POST
        records = json.loads(data['records'])

        transformed_records = []
        for record in records:
            id = record['id']
            transformed_record = self.transform_record(id, record, True)
            transformed_records.append(transformed_record)

        self.update_solr(transformed_records)

        return HttpResponse()
