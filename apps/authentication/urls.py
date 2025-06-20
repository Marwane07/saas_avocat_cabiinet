from django.urls import path
from .views import CabinetLoginView, UserLoginView
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView

urlpatterns = [
    path('cabinet/login/', CabinetLoginView.as_view(), name='cabinet-login'),
    path('user/login/', UserLoginView.as_view(), name='user-login'),
    path('token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
]