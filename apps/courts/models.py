from django.db import models

class Court(models.Model):
    COURT_TYPES = [
        ('FIRST_INSTANCE', 'Tribunal de Première Instance'),
        ('COMMERCE', 'Tribunal de Commerce'),
        ('ADMINISTRATIVE', 'Tribunal Administratif'),
        ('APPEAL', 'Cour d\'Appel'),
        ('APPEAL_COMMERCE', 'Cour d\'Appel de Commerce'),
        ('APPEAL_ADMINISTRATIVE', 'Cour d\'Appel Administrative'),
        ('CASSATION', 'Cour de Cassation'),
    ]
    
    name = models.CharField(max_length=255)
    court_type = models.CharField(max_length=30, choices=COURT_TYPES)
    city = models.CharField(max_length=100)
    address = models.TextField()
    phone = models.CharField(max_length=20, blank=True, null=True)
    email = models.EmailField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        ordering = ['city', 'court_type']
        
    def __str__(self):
        return f"{self.name} - {self.city}"
