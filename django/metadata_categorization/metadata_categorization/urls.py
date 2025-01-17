from django.conf.urls import include, url
from django.contrib import admin

from . import views

urlpatterns = [
    url('^$', views.IndexView.as_view(), name='index'),
    url('^queue/(?P<queueId>[0-9]+)', views.QueueView.as_view(), name='queue'),
    url('^record/(?P<recordId>[0-9]+)', views.RecordView.as_view(), name='record'),
    url('^records', views.RecordsView.as_view(), name='records')
]
