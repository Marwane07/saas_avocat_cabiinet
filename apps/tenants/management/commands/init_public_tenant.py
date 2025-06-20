from django.core.management.base import BaseCommand
from django.db import transaction
from apps.tenants.models import Tenant, Domain
from django.conf import settings

class Command(BaseCommand):
    help = 'Create the public tenant with domains for local development'
    
    @transaction.atomic
    def handle(self, *args, **options):
        # Check if public tenant already exists
        if Tenant.objects.filter(schema_name=settings.PUBLIC_SCHEMA_NAME).exists():
            self.stdout.write(self.style.WARNING(f"Public tenant already exists with schema '{settings.PUBLIC_SCHEMA_NAME}'"))
            tenant = Tenant.objects.get(schema_name=settings.PUBLIC_SCHEMA_NAME)
        else:
            # Create public tenant
            tenant = Tenant(
                schema_name=settings.PUBLIC_SCHEMA_NAME,
                name='Public Tenant',
            )
            tenant.save()
            self.stdout.write(self.style.SUCCESS(f"Created public tenant with schema '{settings.PUBLIC_SCHEMA_NAME}'"))
        
        # Add domains for local development - include common port variations
        domains = [
            '127.0.0.1', 'localhost',  # Default without port
            '127.0.0.1:8000', 'localhost:8000'  # Common Django development port
        ]
        
        for domain_name in domains:
            if not Domain.objects.filter(domain=domain_name).exists():
                domain = Domain(
                    domain=domain_name,
                    tenant=tenant,
                    is_primary=(domain_name == '127.0.0.1')
                )
                domain.save()
                self.stdout.write(self.style.SUCCESS(f"Added domain '{domain_name}' to public tenant"))
            else:
                self.stdout.write(self.style.WARNING(f"Domain '{domain_name}' already exists"))
        
        self.stdout.write(self.style.SUCCESS("Public tenant initialization complete"))
