INSERT INTO subscription_plans (id, name, description, price_cents, status) VALUES
('11111111-1111-1111-1111-111111111111', 'BASIC',      'Plano básico',      4900,  'ACTIVE'),
('22222222-2222-2222-2222-222222222222', 'PRO',        'Plano profissional', 9900,  'ACTIVE'),
('33333333-3333-3333-3333-333333333333', 'ENTERPRISE', 'Plano enterprise',   19900, 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO companies (id, name, tax_id, active) VALUES
('a1b2c3d4-e5f6-7890-1234-56789abcdef0', 'Tech Solutions Ltda',   '12.345.678/0001-99', TRUE),
('f0e1d2c3-b4a5-6789-0123-456789abcdef', 'Enterprise Tech Brasil', '98.765.432/0001-11', TRUE),
('b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Digital Sales',          '12.345.678/0001-90', TRUE),
('c2d3e4f5-a6b7-8901-3456-7890abcdef12', 'InovaCorp',              '11.222.333/0001-44', TRUE),
('d3e4f5a6-b7c8-9012-4567-890abcdef123', 'ProVendas',              '22.333.444/0001-55', FALSE),
('e4f5a6b7-c8d9-0123-5678-90abcdef1234', 'Smart Vendas',           '33.444.555/0001-66', TRUE),
('f5a6b7c8-d9e0-1234-6789-0abcdef12345', 'Tech Solutions',         '44.555.666/0001-77', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO company_subscriptions (id, company_id, plan_id, active, starts_at) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', '33333333-3333-3333-3333-333333333333', TRUE, NOW()),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', '22222222-2222-2222-2222-222222222222', TRUE, NOW()),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', '11111111-1111-1111-1111-111111111111', TRUE, NOW()),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'c2d3e4f5-a6b7-8901-3456-7890abcdef12', '22222222-2222-2222-2222-222222222222', TRUE, NOW()),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'd3e4f5a6-b7c8-9012-4567-890abcdef123', '11111111-1111-1111-1111-111111111111', TRUE, NOW()),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'e4f5a6b7-c8d9-0123-5678-90abcdef1234', '33333333-3333-3333-3333-333333333333', TRUE, NOW()),
('99999999-9999-9999-9999-999999999999', 'f5a6b7c8-d9e0-1234-6789-0abcdef12345', '11111111-1111-1111-1111-111111111111', TRUE, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO collaborators (id, company_id, name, email, role, active, preferences) VALUES
('b2c3d4e5-f6a7-8901-2345-67890abcdef1', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Ana Costa',      'ana@digitalsales.com',     'SYSTEM_ADMIN', TRUE, '{"theme":"dark","default_model":"gpt-4o"}'),
('c3d4e5f6-a7b8-9012-3456-7890abcdef12', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Gabriel Ribeiro', 'gabriel@digitalsales.com', 'MANAGER',      TRUE, '{"theme":"light","default_model":"gpt-4o"}'),
('d4e5f6a7-b8c9-0123-4567-890abcdef123', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Laura Silva',    'laura@digitalsales.com',   'SELLER',       FALSE, '{}'),
('e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Saulo Souza',    'saulo@digitalsales.com',   'SELLER',       TRUE, '{"theme":"dark","default_model":"gpt-3.5"}')
ON CONFLICT (id) DO NOTHING;

INSERT INTO clients (id, company_id, collaborator_id, name, client_company_name, sector, overall_sentiment, created_at, updated_at) VALUES
('11111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'Marina Lima', 'Alfa Industrial', 'Manufacturing', 8, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('99999999-8888-7777-6666-555555555555', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '11111111-2222-3333-4444-555555555555', 'Reuniao de descoberta', 'SCHEDULED', 1800, 'Entender dores e objetivos', 'ONLINE', 'Melhorar eficiencia', 'Contato inicial via email', 'Concorrente X', NOW(), NULL, NULL, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO meeting_pre_analysis (id, meeting_id, recommended_strategy, key_points, possible_objections, created_at) VALUES
('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '99999999-8888-7777-6666-555555555555', '{"focus":"exploration"}'::jsonb, '["Entender processos atuais","Mapear stakeholders"]'::jsonb, '["Orcamento limitado","Prazo curto"]'::jsonb, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO meeting_post_analysis (id, meeting_id, summary, action_items, sentiment_analysis, created_at) VALUES ('a1a2b3c4-d5e6-7890-1234-56789abcdef0', '99999999-8888-7777-6666-555555555555', 'Cliente demonstrou interesse na proposta e pediu retorno em 7 dias.', '[{"text": "Enviar proposta revisada", "done": false}, {"text": "Agendar próxima etapa", "done": false}]', '{"overall": "positive", "score": 0.81}', '2024-06-10T16:00:00Z');
