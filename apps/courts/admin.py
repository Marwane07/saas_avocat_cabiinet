from django.contrib import admin
from .models import Court

@admin.register(Court)
class CourtAdmin(admin.ModelAdmin):
    list_display = ('name', 'court_type', 'city', 'address')
    list_filter = ('court_type', 'city')
    search_fields = ('name', 'city')
