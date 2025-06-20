from django.urls import reverse
from rest_framework import status
from rest_framework.test import APITestCase
from .models import Cabinet, User

class AuthenticationTests(APITestCase):

    def setUp(self):
        self.cabinet_data = {
            'name': 'El Ouahidi',
            'domain': 'elouahidi.jure.ma',
            'email': 'contact@elouahidi.jure.ma',
            'password': 'securepassword123'
        }
        self.user_data = {
            'username': 'assistant',
            'email': 'assistant@elouahidi.jure.ma',
            'password': 'securepassword123'
        }
        self.cabinet = Cabinet.objects.create(**self.cabinet_data)
        self.user = User.objects.create_user(username=self.user_data['username'], email=self.user_data['email'], password=self.user_data['password'], cabinet=self.cabinet)

    def test_cabinet_authentication(self):
        response = self.client.post(reverse('cabinet-auth'), {'email': self.cabinet_data['email'], 'password': self.cabinet_data['password']})
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertIn('token', response.data)

    def test_user_authentication(self):
        response = self.client.post(reverse('user-auth'), {'username': self.user_data['username'], 'password': self.user_data['password']})
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertIn('token', response.data)

    def test_invalid_cabinet_authentication(self):
        response = self.client.post(reverse('cabinet-auth'), {'email': 'wrongemail@jure.ma', 'password': 'wrongpassword'})
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

    def test_invalid_user_authentication(self):
        response = self.client.post(reverse('user-auth'), {'username': 'wronguser', 'password': 'wrongpassword'})
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)