import json

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

    queue = {

    }

    # curationQueue
    # ID
    # list summaryRecord
    # summaryRecord
    # list indivdualRecord
    # list
    # individualRecord term|cell line|cell type|anatomy|species|disease|note


    individualRecords = [
        {
            "id": 12,
            "cell line": {"source": "HeLa", "annotation": ""},
            "cell type": {"source": "epf tm", "annotation": ""},
            "cell treatment": {"source": "firboblast", "annotation": ""},
            "anatomy": {"source": "Folderol", "annotation": ""},
            "species": {"source": "human", "annotation": ""},
            "disease": {"source": "gout", "annotation": ""},
            "note": ""
        },
        {
            "id": 1234,
            "cell line": {"source": "epf-1", "annotation": ""},
            "cell type": {"source": "epf tm", "annotation": ""},
            "cell treatment": {"source": "firboblast", "annotation": ""},
            "anatomy": {"source": "Folderol", "annotation": ""},
            "species": {"source": "human", "annotation": ""},
            "disease": {"source": "gout", "annotation": ""},
            "note": ""
        },
        {
            "id": 12345,
            "cell line": {"source": "epf-1", "annotation": ""},
            "cell type": {"source": "epf tm", "annotation": ""},
            "cell treatment": {"source": "firboblast", "annotation": ""},
            "anatomy": {"source": "Folderol", "annotation": ""},
            "species": {"source": "human", "annotation": ""},
            "disease": {"source": "gout", "annotation": ""},
            "note": ""
        }
        {
            "id": 12,
            "cell line": {"source": "epf-1", "annotation": ""},
            "cell type": {"source": "epf tm", "annotation": ""},
            "cell treatment": {"source": "firboblast", "annotation": ""},
            "anatomy": {"source": "Folderol", "annotation": ""},
            "species": {"source": "human", "annotation": ""},
            "disease": {"source": "gout", "annotation": ""},
            "note": ""
        },
        {
            "id": 123,
            "cell line": {"source": "epf-1", "annotation": ""},
            "cell type": {"source": "epf tm", "annotation": ""},
            "cell treatment": {"source": "firboblast", "annotation": ""},
            "anatomy": {"source": "Folderol", "annotation": ""},
            "species": {"source": "human", "annotation": ""},
            "disease": {"source": "gout", "annotation": ""},
            "note": ""
        },
        {
            "id": 12345,
            "cell line": {"source": "epf-1", "annotation": ""},
            "cell type": {"source": "epf tm", "annotation": ""},
            "cell treatment": {"source": "firboblast", "annotation": ""},
            "anatomy": {"source": "Folderol", "annotation": ""},
            "species": {"source": "human", "annotation": ""},
            "disease": {"source": "gout", "annotation": ""},
            "note": ""
        }
    ]
