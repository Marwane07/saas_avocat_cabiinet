from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import ClientViewSet, IndividualClientsView, CompanyClientsView

router = DefaultRouter()
router.register(r'', ClientViewSet)

urlpatterns = [
    path('', include(router.urls)),
    path('individuals/', IndividualClientsView.as_view(), name='individual-clients'),
    path('companies/', CompanyClientsView.as_view(), name='company-clients'),
]
