-- Insertion des habilitations
INSERT INTO habilitations (nom) VALUES
                                    ('USER_CREATE'), ('USER_READ'), ('USER_UPDATE'), ('USER_DELETE'),
                                    ('CABINET_CREATE'), ('CABINET_READ'), ('CABINET_UPDATE'), ('CABINET_DELETE'),
                                    ('DOSSIER_CREATE'), ('DOSSIER_READ'), ('DOSSIER_UPDATE'), ('DOSSIER_DELETE');

-- Insertion des rôles
INSERT INTO roles (nom) VALUES
                            ('ROLE_SUPER_ADMIN'), ('ROLE_CABINET_MANAGER'), ('ROLE_AVOCAT'), ('ROLE_ASSISTANT');

-- Association des habilitations aux rôles
-- SUPER_ADMIN a toutes les habilitations
INSERT INTO roles_habilitations (role_id, habilitation_id)
SELECT 1, id FROM habilitations;

-- CABINET_MANAGER peut gérer les utilisateurs et les dossiers
INSERT INTO roles_habilitations (role_id, habilitation_id)
SELECT 2, id FROM habilitations WHERE nom IN
                                      ('USER_CREATE', 'USER_READ', 'USER_UPDATE', 'USER_DELETE',
                                       'DOSSIER_CREATE', 'DOSSIER_READ', 'DOSSIER_UPDATE', 'DOSSIER_DELETE');

-- AVOCAT peut gérer les dossiers et voir les utilisateurs
INSERT INTO roles_habilitations (role_id, habilitation_id)
SELECT 3, id FROM habilitations WHERE nom IN
                                      ('DOSSIER_CREATE', 'DOSSIER_READ', 'DOSSIER_UPDATE', 'DOSSIER_DELETE', 'USER_READ');

-- ASSISTANT peut voir les dossiers et les utilisateurs
INSERT INTO roles_habilitations (role_id, habilitation_id)
SELECT 4, id FROM habilitations WHERE nom IN ('DOSSIER_READ', 'USER_READ');

-- Création d'un super admin par défaut (mot de passe: admin123)
INSERT INTO cabinets (nom_cabinet, password, tenant_id)
VALUES ('ADMIN_CABINET', '$2a$10$yfIHYCHVqyYQZMicjQMBWO.1Z7Zt2h9Mn9F5ocYzwWCnPvLeko3Aq', 'admin-tenant');

INSERT INTO users (email, password, nom_complet, role_id, cabinet_id, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES ('admin@jure.ma', '$2a$10$yfIHYCHVqyYQZMicjQMBWO.1Z7Zt2h9Mn9F5ocYzwWCnPvLeko3Aq', 'Super Admin', 1, 1, true, true, true, true);