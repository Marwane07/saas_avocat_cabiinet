from django.db import models
from django_tenants.models import TenantMixin, DomainMixin

class Tenant(TenantMixin):
    name = models.CharField(max_length=255)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    auto_create_schema = True
    
    # Override save to ensure schema_name is properly set
    def save(self, *args, **kwargs):
        # Make sure schema_name is set and valid
        if not self.schema_name:
            # Generate schema name from the tenant name (lowercase, underscore)
            self.schema_name = self.name.lower().replace(' ', '_')
        super().save(*args, **kwargs)

    def __str__(self):
        return self.name

class Domain(DomainMixin):
    # We need to explicitly reference the tenant field for django-tenants to work properly
    tenant = models.ForeignKey(Tenant, on_delete=models.CASCADE, related_name='domains')
    
    def __str__(self):
        return self.domain

# Keep the Cabinet model if needed
class Cabinet(models.Model):
    tenant = models.ForeignKey(Tenant, on_delete=models.CASCADE, related_name='cabinets')
    owner_name = models.CharField(max_length=255)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.owner_name} - {self.tenant.name}"