from rest_framework import serializers
from .models import Case, Audience
from apps.clients.serializers import ClientSerializer
from apps.authentication.serializers import UserSerializer

class AudienceSerializer(serializers.ModelSerializer):
    class Meta:
        model = Audience
        fields = ['id', 'case', 'date', 'location', 'notes', 'decision', 
                 'next_audience_date', 'created_at', 'updated_at']

class CaseSerializer(serializers.ModelSerializer):
    client_details = ClientSerializer(source='client', read_only=True)
    lawyer_details = UserSerializer(source='assigned_lawyer', read_only=True)
    audiences = AudienceSerializer(many=True, read_only=True)
    
    class Meta:
        model = Case
        fields = ['id', 'reference', 'title', 'description', 'case_type', 
                 'status', 'client', 'client_details', 'assigned_lawyer', 
                 'lawyer_details', 'filing_date', 'audiences', 
                 'created_at', 'updated_at']
