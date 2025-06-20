from django.contrib import admin
from .models import Case, Audience

class AudienceInline(admin.TabularInline):
    model = Audience
    extra = 1

@admin.register(Case)
class CaseAdmin(admin.ModelAdmin):
    list_display = ('reference', 'title', 'client', 'assigned_lawyer', 'case_type', 'status', 'filing_date')
    list_filter = ('status', 'case_type', 'filing_date')
    search_fields = ('reference', 'title', 'client__name', 'assigned_lawyer__username')
    date_hierarchy = 'filing_date'
    inlines = [AudienceInline]

@admin.register(Audience)
class AudienceAdmin(admin.ModelAdmin):
    list_display = ('case', 'date', 'location', 'next_audience_date')
    list_filter = ('date', 'location')
    search_fields = ('case__reference', 'case__title')
    date_hierarchy = 'date'
