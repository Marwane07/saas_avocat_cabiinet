from rest_framework import viewsets, generics
from rest_framework.permissions import IsAuthenticated
from .models import Case, Audience
from .serializers import CaseSerializer, AudienceSerializer

class CaseViewSet(viewsets.ModelViewSet):
    queryset = Case.objects.all()
    serializer_class = CaseSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        queryset = Case.objects.all()
        
        # Filter by case type if provided in query params
        case_type = self.request.query_params.get('case_type', None)
        if case_type:
            queryset = queryset.filter(case_type=case_type)
            
        # Filter by status if provided in query params
        status = self.request.query_params.get('status', None)
        if status:
            queryset = queryset.filter(status=status)
            
        # Filter by client if provided in query params
        client_id = self.request.query_params.get('client_id', None)
        if client_id:
            queryset = queryset.filter(client_id=client_id)
            
        return queryset

class AudienceViewSet(viewsets.ModelViewSet):
    queryset = Audience.objects.all()
    serializer_class = AudienceSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        queryset = Audience.objects.all()
        
        # Filter by case if provided in query params
        case_id = self.request.query_params.get('case_id', None)
        if case_id:
            queryset = queryset.filter(case_id=case_id)
            
        return queryset

class CasesByClientView(generics.ListAPIView):
    serializer_class = CaseSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        client_id = self.kwargs['client_id']
        return Case.objects.filter(client_id=client_id)

class UpcomingAudiencesView(generics.ListAPIView):
    serializer_class = AudienceSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        from django.utils import timezone
        return Audience.objects.filter(date__gte=timezone.now()).order_by('date')
