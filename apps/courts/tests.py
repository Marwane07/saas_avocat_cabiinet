from django.test import TestCase
from .models import Court

class CourtModelTests(TestCase):
    def setUp(self):
        self.court = Court.objects.create(
            name="Tribunal de Première Instance de Casablanca",
            court_type="FIRST_INSTANCE",
            city="Casablanca",
            address="Bd Mohammed V, Casablanca",
            phone="+212522222222",
            email="tpi.casa@justice.gov.ma"
        )
        
    def test_court_creation(self):
        self.assertEqual(self.court.name, "Tribunal de Première Instance de Casablanca")
        self.assertEqual(self.court.court_type, "FIRST_INSTANCE")
        self.assertEqual(self.court.city, "Casablanca")
        
    def test_court_str(self):
        self.assertEqual(str(self.court), "Tribunal de Première Instance de Casablanca - Casablanca")
