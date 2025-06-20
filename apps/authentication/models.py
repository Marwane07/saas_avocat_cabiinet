from django.contrib.auth.models import AbstractUser
from django.db import models

class Cabinet(models.Model):
    name = models.CharField(max_length=255)
    domain = models.CharField(max_length=255, unique=True)

    def __str__(self):
        return self.name

class User(AbstractUser):
    cabinet = models.ForeignKey(Cabinet, on_delete=models.CASCADE, related_name='users')
    is_assistant = models.BooleanField(default=False)

    def __str__(self):
        return self.username