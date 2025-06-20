from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import CaseViewSet, AudienceViewSet, CasesByClientView, UpcomingAudiencesView

router = DefaultRouter()
router.register(r'cases', CaseViewSet)
router.register(r'audiences', AudienceViewSet)

urlpatterns = [
    path('', include(router.urls)),
    path('client/<int:client_id>/cases/', CasesByClientView.as_view(), name='client-cases'),
    path('audiences/upcoming/', UpcomingAudiencesView.as_view(), name='upcoming-audiences'),
]
