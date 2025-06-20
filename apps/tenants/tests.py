from django.test import TestCase
from .models import Tenant

class TenantModelTest(TestCase):
    def setUp(self):
        self.tenant = Tenant.objects.create(name='El Ouahidi', domain='elouahidi.jure.ma')

    def test_tenant_creation(self):
        self.assertEqual(self.tenant.name, 'El Ouahidi')
        self.assertEqual(self.tenant.domain, 'elouahidi.jure.ma')

    def test_tenant_str(self):
        self.assertEqual(str(self.tenant), 'El Ouahidi')