-- Seed aligned with current JPA entity CompanyEntity -> table companies
INSERT INTO companies (id, name, tax_id, is_active, created_at) VALUES
(
    'a1b2c3d4-e5f6-7890-1234-56789abcdef0',
    'Tech Solutions Ltda',
    '12.345.678/0001-99',
    TRUE,
    '2024-05-10T14:30:00Z'
),
(
    'f0e1d2c3-b4a5-6789-0123-456789abcdef',
    'Enterprise Tech Brasil',
    '98.765.432/0001-11',
    TRUE,
    '2024-03-22T09:15:00Z'
),
(
    'b1c2d3e4-f5a6-7890-2345-67890abcdef1',
    'Digital Sales',
    '12.345.678/0001-90',
    TRUE,
    '2024-04-01T08:00:00Z'
),
(
    'c2d3e4f5-a6b7-8901-3456-7890abcdef12',
    'InovaCorp',
    '11.222.333/0001-44',
    TRUE,
    '2024-02-15T10:00:00Z'
),
(
    'd3e4f5a6-b7c8-9012-4567-890abcdef123',
    'ProVendas',
    '22.333.444/0001-55',
    FALSE,
    '2024-01-10T09:00:00Z'
),
(
    'e4f5a6b7-c8d9-0123-5678-90abcdef1234',
    'Smart Vendas',
    '33.444.555/0001-66',
    TRUE,
    '2024-03-05T11:00:00Z'
),
(
    'f5a6b7c8-d9e0-1234-6789-0abcdef12345',
    'Tech Solutions',
    '44.555.666/0001-77',
    TRUE,
    '2024-02-20T13:00:00Z'
)
ON CONFLICT (id) DO NOTHING;

-- Seed aligned with current JPA entity CollaboratorEntity -> table collaborators
INSERT INTO collaborators (id, company_id, name, email, is_active, preferences, created_at) VALUES
(
    'b2c3d4e5-f6a7-8901-2345-67890abcdef1',
    'b1c2d3e4-f5a6-7890-2345-67890abcdef1',
    'Ana Costa',
    'ana@digitalsales.com',
    TRUE,
    '{"theme":"dark","defaultModel":"gpt-4o"}'::jsonb,
    '2024-04-02T10:00:00Z'
),
(
    'c3d4e5f6-a7b8-9012-3456-7890abcdef12',
    'b1c2d3e4-f5a6-7890-2345-67890abcdef1',
    'Gabriel Ribeiro',
    'gabriel@digitalsales.com',
    TRUE,
    '{"theme":"light","defaultModel":"gpt-4o"}'::jsonb,
    '2024-04-02T10:01:00Z'
),
(
    'd4e5f6a7-b8c9-0123-4567-890abcdef123',
    'b1c2d3e4-f5a6-7890-2345-67890abcdef1',
    'Laura Silva',
    'laura@digitalsales.com',
    FALSE,
    '{}'::jsonb,
    '2024-04-02T10:02:00Z'
),
(
    'e5f6a7b8-c9d0-1234-5678-90abcdef1234',
    'b1c2d3e4-f5a6-7890-2345-67890abcdef1',
    'Saulo Souza',
    'saulo@digitalsales.com',
    TRUE,
    '{"theme":"dark","defaultModel":"gpt-3.5"}'::jsonb,
    '2024-04-02T10:03:00Z'
)
ON CONFLICT (id) DO NOTHING;
