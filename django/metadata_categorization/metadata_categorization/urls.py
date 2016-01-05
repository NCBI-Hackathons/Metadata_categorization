from django.conf.urls import include, url
from django.contrib import admin

from . import views

urlpatterns = [
    url('^$', views.IndexView.as_view(), name='index'),
    url('^queue/', views.QueueView.as_view(), name='queue'),
]
