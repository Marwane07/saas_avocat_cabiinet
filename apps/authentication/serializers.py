from rest_framework import serializers
from .models import Cabinet, User

class CabinetSerializer(serializers.ModelSerializer):
    class Meta:
        model = Cabinet
        fields = ['id', 'name', 'domain', 'created_at', 'updated_at']

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'cabinet', 'username', 'email', 'first_name', 'last_name', 'is_assistant', 'created_at', 'updated_at']