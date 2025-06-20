from django.urls import path
from .views import TenantCreateView, TenantListView

urlpatterns = [
    path('', TenantListView.as_view(), name='tenant-list'),
    path('create/', TenantCreateView.as_view(), name='tenant-create'),
]