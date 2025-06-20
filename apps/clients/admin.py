from django.contrib import admin
from .models import Client

@admin.register(Client)
class ClientAdmin(admin.ModelAdmin):
    list_display = ('name', 'type', 'identification', 'phone', 'email')
    list_filter = ('type', 'created_at')
    search_fields = ('name', 'identification', 'email', 'phone')
    date_hierarchy = 'created_at'
