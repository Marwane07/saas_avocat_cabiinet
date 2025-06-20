from rest_framework import generics
from rest_framework.permissions import IsAuthenticated
from .models import Court
from .serializers import CourtSerializer

class CourtListView(generics.ListCreateAPIView):
    queryset = Court.objects.all()
    serializer_class = CourtSerializer
    permission_classes = [IsAuthenticated]

class CourtDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Court.objects.all()
    serializer_class = CourtSerializer
    permission_classes = [IsAuthenticated]
