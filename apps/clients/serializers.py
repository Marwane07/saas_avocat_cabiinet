from rest_framework import serializers
from .models import Client

class ClientSerializer(serializers.ModelSerializer):
    class Meta:
        model = Client
        fields = ['id', 'type', 'name', 'identification', 'address', 
                 'phone', 'email', 'created_at', 'updated_at']
