from django.db import models
from apps.authentication.models import User
from apps.clients.models import Client

class Case(models.Model):
    CASE_TYPES = [
        ('CIVIL', 'Civil'),
        ('COMMERCIAL', 'Commercial'),
        ('CRIMINAL', 'Criminal'),
        ('ADMINISTRATIVE', 'Administrative'),
        ('FAMILY', 'Family'),
    ]

    STATUS_CHOICES = [
        ('PENDING', 'En cours'),
        ('WON', 'Gagné'),
        ('LOST', 'Perdu'),
        ('SETTLED', 'Réglé'),
    ]

    reference = models.CharField(max_length=50, unique=True)
    title = models.CharField(max_length=255)
    description = models.TextField()
    case_type = models.CharField(max_length=20, choices=CASE_TYPES)
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='PENDING')
    client = models.ForeignKey(Client, on_delete=models.PROTECT)
    assigned_lawyer = models.ForeignKey(User, on_delete=models.PROTECT)
    filing_date = models.DateField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.reference} - {self.title}"

class Audience(models.Model):
    case = models.ForeignKey(Case, related_name='audiences', on_delete=models.CASCADE)
    date = models.DateTimeField()
    location = models.CharField(max_length=255)
    notes = models.TextField(blank=True)
    decision = models.TextField(blank=True)
    next_audience_date = models.DateTimeField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.case.reference} - {self.date}"
