from rest_framework import viewsets, generics
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.decorators import action
from .models import Client
from .serializers import ClientSerializer

class ClientViewSet(viewsets.ModelViewSet):
    queryset = Client.objects.all()
    serializer_class = ClientSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        queryset = Client.objects.all()
        
        # Filter by client type if provided in query params
        client_type = self.request.query_params.get('type', None)
        if client_type:
            queryset = queryset.filter(type=client_type)
            
        return queryset
    
    @action(detail=True, methods=['get'])
    def cases(self, request, pk=None):
        from apps.cases.models import Case
        from apps.cases.serializers import CaseSerializer
        
        client = self.get_object()
        cases = Case.objects.filter(client=client)
        serializer = CaseSerializer(cases, many=True)
        
        return Response(serializer.data)

class IndividualClientsView(generics.ListAPIView):
    serializer_class = ClientSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        return Client.objects.filter(type='INDIVIDUAL')

class CompanyClientsView(generics.ListAPIView):
    serializer_class = ClientSerializer
    permission_classes = [IsAuthenticated]
    
    def get_queryset(self):
        return Client.objects.filter(type='COMPANY')
