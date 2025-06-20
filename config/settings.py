from pathlib import Path
from datetime import timedelta
import os

# Force UTF-8 encoding
os.environ['PYTHONUTF8'] = '1'
os.environ['PYTHONIOENCODING'] = 'utf-8'

BASE_DIR = Path(__file__).resolve().parent.parent

SECRET_KEY = 'django-insecure-a-strong-secret-key-here' # Hardcoded value

DEBUG = True # Hardcoded value

ALLOWED_HOSTS = ['*.jure.ma', 'localhost', '127.0.0.1']

# Application definition
SHARED_APPS = (
    'django_tenants',  # Mandatory, should be before django.contrib.admin
    'apps.authentication',  # Move this up to be processed before admin
    'apps.tenants',    # Your tenant app
    'django.contrib.contenttypes',  # This should come before auth and admin
    'django.contrib.auth',
    'django.contrib.admin',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'rest_framework',
    'rest_framework_simplejwt',
    'drf_yasg',
)

TENANT_APPS = (
    'django.contrib.contenttypes',  # Ensure correct order here too
    'django.contrib.auth',
    'django.contrib.admin',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'apps.cases',
    'apps.courts',
    'apps.clients',
)

INSTALLED_APPS = list(SHARED_APPS) + [app for app in TENANT_APPS if app not in SHARED_APPS]

MIDDLEWARE = [
    'django_tenants.middleware.main.TenantMainMiddleware',  # Should be first
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'config.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'config.wsgi.application'

# Database
# https://docs.djangoproject.com/en/3.2/ref/settings/#databases
DATABASE_ROUTERS = (
    'django_tenants.routers.TenantSyncRouter',
)

# Define a default database configuration
default_db = {
    'ENGINE': 'django_tenants.postgresql_backend',
    'NAME': 'jure_db',
    'USER': 'postgres',
    'PASSWORD': '123456',
    'HOST': 'localhost',
    'PORT': '5432',
    # Simplified encoding settings
    'OPTIONS': {
        'client_encoding': 'UTF8'
    },
}
DATABASES = {
    'default': default_db
}

# Django Tenants specific settings
TENANT_MODEL = 'tenants.Tenant'
TENANT_DOMAIN_MODEL = 'tenants.Domain'
PUBLIC_SCHEMA_NAME = 'public'
PUBLIC_SCHEMA_URLCONF = 'config.urls'
TENANT_USERS_DOMAIN = 'jure.ma'

# Ensure database backend is properly set up for django-tenants
DATABASE_ROUTERS = (
    'django_tenants.routers.TenantSyncRouter',
)

# Add support for tenant schemas
TENANT_CREATION_FAKES_MIGRATIONS = True

# Remove 'public' from PG_EXTRA_SEARCH_PATHS since it's already the default schema
# and cannot be included as an extra search path
PG_EXTRA_SEARCH_PATHS = []  # You can add other schemas here if needed, but not 'public'

# Add domain function to create domain URLs
def get_tenant_domain_model():
    return "apps.tenants.Domain"

# The rest of your settings...
AUTH_PASSWORD_VALIDATORS = [
    {
        'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
        'OPTIONS': {
            'MIN_LENGTH': 8,
        }
    },
    {
        'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
    },
]

LANGUAGE_CODE = 'fr-fr'

TIME_ZONE = 'Africa/Casablanca'

USE_I18N = True

USE_L10N = True

USE_TZ = True

STATIC_URL = '/static/'

REST_FRAMEWORK = {
    'DEFAULT_AUTHENTICATION_CLASSES': (
        'rest_framework_simplejwt.authentication.JWTAuthentication',
    ),
    'DEFAULT_PERMISSION_CLASSES': (
        'rest_framework.permissions.IsAuthenticated',
    ),
}

# Update JWT settings from environment variables
SIMPLE_JWT = {
    'ACCESS_TOKEN_LIFETIME': timedelta(minutes=30), # Hardcoded value
    'REFRESH_TOKEN_LIFETIME': timedelta(minutes=1440), # Hardcoded value
    'ROTATE_REFRESH_TOKENS': True,
    'BLACKLIST_AFTER_ROTATION': True,
    'ALGORITHM': 'HS256',
    'SIGNING_KEY': SECRET_KEY,
    'AUTH_HEADER_TYPES': ('Bearer',),
}

SWAGGER_SETTINGS = {
    'SECURITY_DEFINITIONS': {
        'Bearer': {
            'type': 'apiKey',
            'name': 'Authorization',
            'in': 'header'
        }
    }
}

AUTH_USER_MODEL = 'authentication.User'

# Tenant-specific settings
TENANT_BASE_SCHEMA = 'public'  # Schema name for the public tenant
PUBLIC_SCHEMA_NAME = 'public'  # Schema name for the public tenant
DEFAULT_FILE_STORAGE = 'django_tenants.files.storage.TenantFileSystemStorage'