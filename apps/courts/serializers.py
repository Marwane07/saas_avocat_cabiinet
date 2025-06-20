from rest_framework import serializers
from .models import Court

class CourtSerializer(serializers.ModelSerializer):
    class Meta:
        model = Court
        fields = ['id', 'name', 'court_type', 'city', 'address', 'phone', 'email', 'created_at', 'updated_at']
