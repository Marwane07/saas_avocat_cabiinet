from django.db import models

class Client(models.Model):
    CLIENT_TYPES = [
        ('INDIVIDUAL', 'Particulier'),
        ('COMPANY', 'Société'),
    ]

    type = models.CharField(max_length=20, choices=CLIENT_TYPES)
    name = models.CharField(max_length=255)
    identification = models.CharField(max_length=50, help_text="CIN or RC number")
    address = models.TextField()
    phone = models.CharField(max_length=20)
    email = models.EmailField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.name} ({self.get_type_display()})"
