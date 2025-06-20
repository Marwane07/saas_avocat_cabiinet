from django.contrib import admin
from django.urls import path, include, re_path
from django.views.generic.base import RedirectView
from drf_yasg.views import get_schema_view
from drf_yasg import openapi
from rest_framework import permissions

schema_view = get_schema_view(
    openapi.Info(
        title="Law Firm API",
        default_version='v1',
        description="API for managing law firm cases and clients",
    ),
    public=True,
    permission_classes=[permissions.AllowAny],
)

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/auth/', include('apps.authentication.urls')),
    path('api/tenants/', include('apps.tenants.urls')),
    path('api/cases/', include('apps.cases.urls')),
    path('api/clients/', include('apps.clients.urls')),
    
    # Swagger URLs
    path('swagger/', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
    path('swagger', RedirectView.as_view(url='/swagger/', permanent=True)),
    path('redoc/', schema_view.with_ui('redoc', cache_timeout=0), name='schema-redoc'),
    path('redoc', RedirectView.as_view(url='/redoc/', permanent=True)),
]