from rest_framework import generics, permissions
from rest_framework.response import Response
from rest_framework_simplejwt.tokens import RefreshToken
from django.contrib.auth import get_user_model
from .models import Cabinet
from .serializers import CabinetSerializer, UserSerializer

User = get_user_model()

class CabinetLoginView(generics.GenericAPIView):
    serializer_class = CabinetSerializer
    permission_classes = [permissions.AllowAny]

    def post(self, request, *args, **kwargs):
        cabinet_name = request.data.get('cabinet_name')
        password = request.data.get('password')
        
        try:
            cabinet = Cabinet.objects.get(name=cabinet_name)
            if cabinet.check_password(password):
                refresh = RefreshToken.for_user(cabinet)
                return Response({
                    'refresh': str(refresh),
                    'access': str(refresh.access_token),
                    'tenant': cabinet_name
                })
            else:
                return Response({'detail': 'Invalid credentials'}, status=400)
        except Cabinet.DoesNotExist:
            return Response({'detail': 'Cabinet not found'}, status=404)

class UserLoginView(generics.GenericAPIView):
    serializer_class = UserSerializer
    permission_classes = [permissions.AllowAny]

    def post(self, request, *args, **kwargs):
        username = request.data.get('username')
        password = request.data.get('password')
        
        try:
            user = User.objects.get(username=username)
            if user.check_password(password):
                refresh = RefreshToken.for_user(user)
                return Response({
                    'refresh': str(refresh),
                    'access': str(refresh.access_token),
                    'user': username
                })
            else:
                return Response({'detail': 'Invalid credentials'}, status=400)
        except User.DoesNotExist:
            return Response({'detail': 'User not found'}, status=404)