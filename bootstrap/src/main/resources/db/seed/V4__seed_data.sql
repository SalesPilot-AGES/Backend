INSERT INTO subscription_plans (id, name, description, price_cents, status) VALUES
('11111111-1111-1111-1111-111111111111', 'BASIC',      'Plano básico',      4900,  'ACTIVE'),
('22222222-2222-2222-2222-222222222222', 'PRO',        'Plano profissional', 9900,  'ACTIVE'),
('33333333-3333-3333-3333-333333333333', 'ENTERPRISE', 'Plano enterprise',   19900, 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO companies (id, name, tax_id, active) VALUES
('a1b2c3d4-e5f6-7890-1234-56789abcdef0', 'Tech Solutions Ltda',    '12.345.678/0001-99', TRUE),
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
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'd3e4f5a6-b7c8-9012-4567-890abcdef123', '11111111-1111-1111-1111-111111111111', FALSE, NOW()),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'e4f5a6b7-c8d9-0123-5678-90abcdef1234', '33333333-3333-3333-3333-333333333333', TRUE, NOW()),
('99999999-9999-9999-9999-999999999999', 'f5a6b7c8-d9e0-1234-6789-0abcdef12345', '11111111-1111-1111-1111-111111111111', TRUE, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO collaborators (id, company_id, name, email, role, active, preferences, password_hash) VALUES
-- Dev password for all seeded collaborators: password
('b2c3d4e5-f6a7-8901-2345-67890abcdef1', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Ana Costa',      'ana@digitalsales.com',     'SYSTEM_ADMIN', TRUE, '{"theme":"dark","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('c3d4e5f6-a7b8-9012-3456-7890abcdef12', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Gabriel Ribeiro', 'gabriel@digitalsales.com', 'MANAGER',      TRUE, '{"theme":"light","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('d4e5f6a7-b8c9-0123-4567-890abcdef123', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Laura Silva',    'laura@digitalsales.com',    'SELLER', FALSE, '{}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Saulo Souza',    'saulo@digitalsales.com',    'SELLER', TRUE,  '{"theme":"dark","default_model":"gpt-3.5"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('f6a7b8c9-d0e1-2345-6789-0abcdef12345', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Marcos Pereira', 'marcos@digitalsales.com',   'SELLER', TRUE,  '{"theme":"light","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('a7b8c9d0-e1f2-3456-7890-abcdef123456', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Julia Fernandes','julia@digitalsales.com',    'SELLER', TRUE,  '{"theme":"dark","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy')
ON CONFLICT (id) DO NOTHING;

INSERT INTO clients (id, company_id, collaborator_id, name, client_company_name, sector, overall_sentiment, created_at, updated_at) VALUES
('11111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'Marina Lima',     'Alfa Industrial',  'Manufacturing', 8,  NOW(), NOW()),
('21111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', 'Carlos Mendes',   'BetaTech',         'Technology',    7,  NOW(), NOW()),
('31111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'Patricia Rocha',  'Nexus Logística',  'Logistics',     6,  NOW(), NOW()),
('41111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', 'Roberto Campos',  'Sigma Varejo',     'Retail',        5,  NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('99999999-8888-7777-6666-555555555555', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '11111111-2222-3333-4444-555555555555', 'Reunião de descoberta',         'SCHEDULED',   0, 'Entender dores e objetivos',            'ONLINE',      'Melhorar eficiência operacional',       'Contato inicial via email',         'Concorrente X',              NOW() + interval '3 days',  NULL,                                  NULL,                                  NOW()),
('aaaaaaaa-1111-2222-3333-444444444444', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '21111111-2222-3333-4444-555555555555', 'Apresentação de proposta',       'COMPLETED',   3600, 'Apresentar solução customizada',        'IN_PERSON',   'Reduzir custo de infraestrutura',       'Demo realizada na semana anterior', 'Concorrente Y, Concorrente Z', NOW() - interval '10 days', NOW() - interval '10 days',            NOW() - interval '10 days' + interval '1 hour', NOW() - interval '10 days'),
('aaaaaaaa-1111-2222-3333-555555555555', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '31111111-2222-3333-4444-555555555555', 'Negociação de contrato',         'COMPLETED',   2700, 'Fechar contrato anual',                 'ONLINE',      'Flexibilidade no prazo de pagamento',   'Duas reuniões anteriores de demo',  'Nenhum',                     NOW() - interval '5 days',  NOW() - interval '5 days',             NOW() - interval '5 days' + interval '45 minutes',  NOW() - interval '5 days'),
('aaaaaaaa-1111-2222-3333-666666666666', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '41111111-2222-3333-4444-555555555555', 'Follow-up pós-demonstração',     'IN_PROGRESS', 0, 'Tirar dúvidas técnicas pós-demo',       'ONLINE',      'Entender modelo de precificação',       'Demo realizada ontem',              'Concorrente W',              NOW(),                      NOW(),                                 NULL,                                  NOW()),
('aaaaaaaa-1111-2222-3333-777777777777', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '21111111-2222-3333-4444-555555555555', 'Kickoff de implementação',       'SCHEDULED',   0, 'Alinhar escopo e cronograma',           'IN_PERSON',   'Garantir suporte durante a migração',   'Contrato assinado na semana passada', 'Nenhum',                   NOW() + interval '7 days',  NULL,                                  NULL,                                  NOW()),
('aaaaaaaa-1111-2222-3333-888888888888', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '11111111-2222-3333-4444-555555555555', 'Revisão trimestral de resultados', 'CANCELLED',   0, 'Avaliar resultados do último trimestre', 'ONLINE',     'Relatório de desempenho atualizado',    'Reunião mensal recorrente',         'Nenhum',                     NOW() - interval '2 days',  NULL,                                  NULL,                                  NOW() - interval '2 days')
ON CONFLICT (id) DO NOTHING;

-- Additional meetings for January 2026
INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('10000001-0001-0001-0001-000000000001', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '11111111-2222-3333-4444-555555555555', 'Prospecção inicial',              'COMPLETED', 1800, 'Apresentar produtos e serviços', 'ONLINE', 'Otimizar operações', 'Indicação via LinkedIn', 'Nenhum', '2026-01-08'::timestamp, '2026-01-08 14:00'::timestamp, '2026-01-08 14:30'::timestamp, '2026-01-08'::timestamp),
('10000001-0001-0001-0001-000000000002', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '21111111-2222-3333-4444-555555555555', 'Reunião técnica',                'COMPLETED', 2700, 'Validar requisitos técnicos', 'IN_PERSON', 'Integração com sistemas legados', 'Reunião comercial anterior', 'Concorrente A', '2026-01-15'::timestamp, '2026-01-15 09:00'::timestamp, '2026-01-15 09:45'::timestamp, '2026-01-15'::timestamp),
('10000001-0001-0001-0001-000000000003', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '31111111-2222-3333-4444-555555555555', 'Diagnóstico de necessidades',     'COMPLETED', 3600, 'Mapear dores e desafios', 'IN_PERSON', 'Reduzir custos operacionais', 'Primeira reunião com cliente', 'Nenhum', '2026-01-22'::timestamp, '2026-01-22 10:00'::timestamp, '2026-01-22 11:00'::timestamp, '2026-01-22'::timestamp),
('10000001-0001-0001-0001-000000000004', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '41111111-2222-3333-4444-555555555555', 'Demo executiva',                 'COMPLETED', 2400, 'Apresentar solução para C-level', 'ONLINE', 'Entender modelo de negócio', 'Três reuniões preparatórias', 'Concorrente B, Concorrente C', '2026-01-29'::timestamp, '2026-01-29 15:00'::timestamp, '2026-01-29 15:40'::timestamp, '2026-01-29'::timestamp)
ON CONFLICT (id) DO NOTHING;

-- Additional meetings for February 2026
INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('10000002-0002-0002-0002-000000000001', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '11111111-2222-3333-4444-555555555555', 'Proposta comercial',             'COMPLETED', 3600, 'Apresentar proposta com valores', 'IN_PERSON', 'Decisão de compra em 2 semanas', 'Duas reuniões técnicas anteriores', 'Nenhum', '2026-02-05'::timestamp, '2026-02-05 14:00'::timestamp, '2026-02-05 15:00'::timestamp, '2026-02-05'::timestamp),
('10000002-0002-0002-0002-000000000002', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '21111111-2222-3333-4444-555555555555', 'Alinhamento de expectativas',    'COMPLETED', 1800, 'Confirmar escopo e prazos', 'ONLINE', 'Garantir cronograma realista', 'Contato com cliente há 2 meses', 'Concorrente D', '2026-02-12'::timestamp, '2026-02-12 11:00'::timestamp, '2026-02-12 11:30'::timestamp, '2026-02-12'::timestamp),
('10000002-0002-0002-0002-000000000003', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '31111111-2222-3333-4444-555555555555', 'Negociação de termos',          'COMPLETED', 2700, 'Acordar preço e condições', 'ONLINE', 'Parcelamento em 6x', 'Quatro reuniões anteriores', 'Nenhum', '2026-02-19'::timestamp, '2026-02-19 16:00'::timestamp, '2026-02-19 16:45'::timestamp, '2026-02-19'::timestamp),
('10000002-0002-0002-0002-000000000004', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '41111111-2222-3333-4444-555555555555', 'Assinatura de contrato',         'COMPLETED', 1200, 'Validar e assinar contrato', 'IN_PERSON', 'Iniciar implementação em março', 'Ciclo de vendas de 3 meses', 'Nenhum', '2026-02-26'::timestamp, '2026-02-26 10:00'::timestamp, '2026-02-26 10:20'::timestamp, '2026-02-26'::timestamp)
ON CONFLICT (id) DO NOTHING;

-- Additional meetings for March 2026
INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('10000003-0003-0003-0003-000000000001', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '11111111-2222-3333-4444-555555555555', 'Kickoff do projeto',             'COMPLETED', 5400, 'Alinhar equipes e cronograma', 'IN_PERSON', 'Garantir sucesso da implementação', 'Contrato assinado em fevereiro', 'Nenhum', '2026-03-03'::timestamp, '2026-03-03 09:00'::timestamp, '2026-03-03 10:30'::timestamp, '2026-03-03'::timestamp),
('10000003-0003-0003-0003-000000000002', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '21111111-2222-3333-4444-555555555555', 'Reunião de acompanhamento',      'COMPLETED', 2400, 'Revisar progresso da implementação', 'ONLINE', 'Resolver bloqueios técnicos', 'Kickoff realizado na semana anterior', 'Nenhum', '2026-03-10'::timestamp, '2026-03-10 14:00'::timestamp, '2026-03-10 14:40'::timestamp, '2026-03-10'::timestamp),
('10000003-0003-0003-0003-000000000003', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '31111111-2222-3333-4444-555555555555', 'Treinamento de usuários',        'COMPLETED', 7200, 'Capacitar equipe do cliente', 'IN_PERSON', 'Garantir adoção rápida', 'Implementação em fase avançada', 'Nenhum', '2026-03-17'::timestamp, '2026-03-17 09:00'::timestamp, '2026-03-17 11:00'::timestamp, '2026-03-17'::timestamp),
('10000003-0003-0003-0003-000000000004', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '41111111-2222-3333-4444-555555555555', 'Revisão de funcionalidades',     'COMPLETED', 3600, 'Demonstrar features implementadas', 'ONLINE', 'Validar alinhamento com requisitos', 'Reunião de acompanhamento', 'Nenhum', '2026-03-24'::timestamp, '2026-03-24 15:00'::timestamp, '2026-03-24 16:00'::timestamp, '2026-03-24'::timestamp),
('10000003-0003-0003-0003-000000000005', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '11111111-2222-3333-4444-555555555555', 'Resolução de incidentes',        'COMPLETED', 1800, 'Suportar ao cliente em problemas urgentes', 'ONLINE', 'Minimizar downtime', 'Suporte durante fase crítica', 'Nenhum', '2026-03-31'::timestamp, '2026-03-31 11:00'::timestamp, '2026-03-31 11:30'::timestamp, '2026-03-31'::timestamp)
ON CONFLICT (id) DO NOTHING;

-- Additional meetings for April 2026
INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('10000004-0004-0004-0004-000000000001', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '21111111-2222-3333-4444-555555555555', 'Ajustes finais',                 'COMPLETED', 2700, 'Refinar configurações conforme feedback', 'ONLINE', 'Go-live em uma semana', 'Diversas reuniões técnicas', 'Nenhum', '2026-04-07'::timestamp, '2026-04-07 10:00'::timestamp, '2026-04-07 10:45'::timestamp, '2026-04-07'::timestamp),
('10000004-0004-0004-0004-000000000002', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '31111111-2222-3333-4444-555555555555', 'Go-live assessment',            'COMPLETED', 3600, 'Validar readiness para produção', 'IN_PERSON', 'Garantir transição suave', 'Preparação de 4 semanas', 'Nenhum', '2026-04-14'::timestamp, '2026-04-14 09:00'::timestamp, '2026-04-14 10:00'::timestamp, '2026-04-14'::timestamp),
('10000004-0004-0004-0004-000000000003', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '41111111-2222-3333-4444-555555555555', 'Go-live support',               'COMPLETED', 4800, 'Monitorar produção no primeiro dia', 'ONLINE', 'Suportar usuários durante migração', 'Go-live assessment anterior', 'Nenhum', '2026-04-21'::timestamp, '2026-04-21 08:00'::timestamp, '2026-04-21 09:20'::timestamp, '2026-04-21'::timestamp),
('10000004-0004-0004-0004-000000000004', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '11111111-2222-3333-4444-555555555555', 'Reunião pós go-live',           'COMPLETED', 2400, 'Avaliar implementação e próximos passos', 'ONLINE', 'Planejamento de melhorias', 'Go-live realizado há 3 dias', 'Nenhum', '2026-04-28'::timestamp, '2026-04-28 14:00'::timestamp, '2026-04-28 14:40'::timestamp, '2026-04-28'::timestamp)
ON CONFLICT (id) DO NOTHING;

-- Additional meetings for May 2026 (diverse companies and sellers)
INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('10000005-0005-0005-0005-000000000001', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '21111111-2222-3333-4444-555555555555', 'Reunião de upsell',              'COMPLETED', 1800, 'Apresentar novos módulos disponíveis', 'ONLINE', 'Expandir uso do produto', 'Contato regular há 5 meses', 'Nenhum', '2026-05-05'::timestamp, '2026-05-05 15:00'::timestamp, '2026-05-05 15:30'::timestamp, '2026-05-05'::timestamp),
('10000005-0005-0005-0005-000000000002', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '31111111-2222-3333-4444-555555555555', 'Check-in de satisfação',        'COMPLETED', 1200, 'Avaliar satisfação e sugestões', 'ONLINE', 'Garantir renovação do contrato', 'Reunião mensal recorrente', 'Nenhum', '2026-05-12'::timestamp, '2026-05-12 11:00'::timestamp, '2026-05-12 11:20'::timestamp, '2026-05-12'::timestamp),
('10000005-0005-0005-0005-000000000003', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '41111111-2222-3333-4444-555555555555', 'Discussão de roadmap',          'SCHEDULED', 0, 'Apresentar visão de produto para H2', 'IN_PERSON', 'Influenciar prioridades de desenvolvimento', 'Contato regular de 4 meses', 'Nenhum', NOW() + interval '5 days', NULL, NULL, NOW()),
('10000005-0005-0005-0005-000000000004', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '11111111-2222-3333-4444-555555555555', 'Suporte técnico programado',     'IN_PROGRESS', 0, 'Otimizar performance do sistema', 'ONLINE', 'Reduzir tempo de resposta', 'Suporte contínuo desde o go-live', 'Nenhum', NOW(), NOW(), NULL, NOW()),
('10000005-0005-0005-0005-000000000005', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '21111111-2222-3333-4444-555555555555', 'Planejamento de renovação',      'SCHEDULED', 0, 'Definir termos de renovação anual', 'IN_PERSON', 'Renovação com desconto volume', 'Contato contínuo há 6 meses', 'Nenhum', NOW() + interval '30 days', NULL, NULL, NOW() + interval '30 days')
ON CONFLICT (id) DO NOTHING;

-- Collaborators for Tech Solutions Ltda (Company: a1b2c3d4-e5f6-7890-1234-56789abcdef0)
INSERT INTO collaborators (id, company_id, name, email, role, active, preferences, password_hash) VALUES
('12345678-1111-1111-1111-111111111111', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', 'Ricardo Santos', 'ricardo@techsolutions.com', 'SYSTEM_ADMIN', TRUE, '{"theme":"dark","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('23456789-2222-2222-2222-222222222222', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', 'Patricia Oliveira', 'patricia@techsolutions.com', 'MANAGER', TRUE, '{"theme":"light","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('34567890-3333-3333-3333-333333333333', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', 'Felipe Costa', 'felipe@techsolutions.com', 'SELLER', TRUE, '{"theme":"dark","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('45678901-4444-4444-4444-444444444444', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', 'Camila Rocha', 'camila@techsolutions.com', 'SELLER', TRUE, '{"theme":"light","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy')
ON CONFLICT (id) DO NOTHING;

-- Clients for Tech Solutions Ltda
INSERT INTO clients (id, company_id, collaborator_id, name, client_company_name, sector, overall_sentiment, created_at, updated_at) VALUES
('51111111-2222-3333-4444-555555555555', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', '34567890-3333-3333-3333-333333333333', 'Fernanda Silva', 'FinanceFlow', 'Financial Services', 8, NOW(), NOW()),
('52111111-2222-3333-4444-555555555555', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', '45678901-4444-4444-4444-444444444444', 'Bruno Alves', 'CloudSys', 'Technology', 7, NOW(), NOW()),
('53111111-2222-3333-4444-555555555555', 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', '34567890-3333-3333-3333-333333333333', 'Tatiana Ferreira', 'VendasMax', 'Consulting', 9, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Meetings for Tech Solutions Ltda
INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('20000001-1111-1111-1111-111111111111', '34567890-3333-3333-3333-333333333333', '51111111-2222-3333-4444-555555555555', 'Prospecting call', 'COMPLETED', 1800, 'Initial contact', 'ONLINE', 'Understand needs', 'Cold call', 'None', NOW() - interval '20 days', NOW() - interval '20 days', NOW() - interval '20 days' + interval '30 minutes', NOW() - interval '20 days'),
('20000002-1111-1111-1111-111111111111', '45678901-4444-4444-4444-444444444444', '52111111-2222-3333-4444-555555555555', 'Product demo', 'COMPLETED', 3600, 'Show capabilities', 'IN_PERSON', 'Check compatibility', 'Previous email', 'Competitor A', NOW() - interval '15 days', NOW() - interval '15 days', NOW() - interval '15 days' + interval '1 hour', NOW() - interval '15 days'),
('20000003-1111-1111-1111-111111111111', '34567890-3333-3333-3333-333333333333', '53111111-2222-3333-4444-555555555555', 'Contract negotiation', 'COMPLETED', 2700, 'Close deal', 'ONLINE', 'Payment terms', 'Multiple demos', 'None', NOW() - interval '10 days', NOW() - interval '10 days', NOW() - interval '10 days' + interval '45 minutes', NOW() - interval '10 days'),
('20000004-1111-1111-1111-111111111111', '45678901-4444-4444-4444-444444444444', '51111111-2222-3333-4444-555555555555', 'Implementation kickoff', 'IN_PROGRESS', 0, 'Start implementation', 'IN_PERSON', 'Setup and training', 'Contract signed', 'None', NOW(), NOW(), NULL, NOW()),
('20000005-1111-1111-1111-111111111111', '34567890-3333-3333-3333-333333333333', '52111111-2222-3333-4444-555555555555', 'Follow-up meeting', 'SCHEDULED', 0, 'Check progress', 'ONLINE', 'Address concerns', 'Previous implementation', 'None', NOW() + interval '5 days', NULL, NULL, NOW()),
('20000006-1111-1111-1111-111111111111', '45678901-4444-4444-4444-444444444444', '53111111-2222-3333-4444-555555555555', 'Training session', 'COMPLETED', 5400, 'Train users', 'IN_PERSON', 'User adoption', 'Implementation phase', 'None', NOW() - interval '3 days', NOW() - interval '3 days', NOW() - interval '3 days' + interval '1.5 hours', NOW() - interval '3 days'),
('20000007-1111-1111-1111-111111111111', '34567890-3333-3333-3333-333333333333', '51111111-2222-3333-4444-555555555555', 'Go-live support', 'COMPLETED', 3600, 'Support go-live', 'ONLINE', 'Ensure smooth launch', 'Training completed', 'None', NOW() - interval '2 days', NOW() - interval '2 days', NOW() - interval '2 days' + interval '1 hour', NOW() - interval '2 days'),
('20000008-1111-1111-1111-111111111111', '45678901-4444-4444-4444-444444444444', '52111111-2222-3333-4444-555555555555', 'Post-launch review', 'SCHEDULED', 0, 'Evaluate launch', 'ONLINE', 'Identify improvements', 'Go-live completed', 'None', NOW() + interval '10 days', NULL, NULL, NOW()),
('20000009-1111-1111-1111-111111111111', '34567890-3333-3333-3333-333333333333', '53111111-2222-3333-4444-555555555555', 'Renewal discussion', 'SCHEDULED', 0, 'Discuss renewal', 'IN_PERSON', 'Multi-year agreement', 'Annual relationship', 'None', NOW() + interval '30 days', NULL, NULL, NOW())
ON CONFLICT (id) DO NOTHING;

-- Collaborators for Enterprise Tech Brasil (Company: f0e1d2c3-b4a5-6789-0123-456789abcdef)
INSERT INTO collaborators (id, company_id, name, email, role, active, preferences, password_hash) VALUES
('56789012-5555-5555-5555-555555555555', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', 'Gustavo Mendes', 'gustavo@enterprisetech.com', 'SYSTEM_ADMIN', TRUE, '{"theme":"dark","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('67890123-6666-6666-6666-666666666666', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', 'Alessandra Lima', 'alessandra@enterprisetech.com', 'MANAGER', TRUE, '{"theme":"light","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('78901234-7777-7777-7777-777777777777', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', 'Diego Martins', 'diego@enterprisetech.com', 'SELLER', TRUE, '{"theme":"dark","default_model":"gpt-3.5"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'),
('89012345-8888-8888-8888-888888888888', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', 'Vanessa Teixeira', 'vanessa@enterprisetech.com', 'SELLER', TRUE, '{"theme":"light","default_model":"gpt-4o"}', '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy')
ON CONFLICT (id) DO NOTHING;

-- Clients for Enterprise Tech Brasil
INSERT INTO clients (id, company_id, collaborator_id, name, client_company_name, sector, overall_sentiment, created_at, updated_at) VALUES
('61111111-2222-3333-4444-555555555555', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', '78901234-7777-7777-7777-777777777777', 'Rodrigo Nunes', 'Logística Brasil', 'Logistics', 8, NOW(), NOW()),
('62111111-2222-3333-4444-555555555555', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', '89012345-8888-8888-8888-888888888888', 'Monica Santos', 'MegaRetail', 'Retail', 7, NOW(), NOW()),
('63111111-2222-3333-4444-555555555555', 'f0e1d2c3-b4a5-6789-0123-456789abcdef', '78901234-7777-7777-7777-777777777777', 'Henrique Rocha', 'FactoryAI', 'Manufacturing', 9, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Meetings for Enterprise Tech Brasil
INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('30000001-1111-1111-1111-111111111111', '78901234-7777-7777-7777-777777777777', '61111111-2222-3333-4444-555555555555', 'Initial discovery', 'COMPLETED', 2400, 'Understand requirements', 'ONLINE', 'Process automation', 'LinkedIn recommendation', 'Competitor B', NOW() - interval '25 days', NOW() - interval '25 days', NOW() - interval '25 days' + interval '40 minutes', NOW() - interval '25 days'),
('30000002-1111-1111-1111-111111111111', '89012345-8888-8888-8888-888888888888', '62111111-2222-3333-4444-555555555555', 'Solution presentation', 'COMPLETED', 3600, 'Present solution', 'IN_PERSON', 'Cost reduction', 'Discovery meeting', 'Competitor C, Competitor D', NOW() - interval '18 days', NOW() - interval '18 days', NOW() - interval '18 days' + interval '1 hour', NOW() - interval '18 days'),
('30000003-1111-1111-1111-111111111111', '78901234-7777-7777-7777-777777777777', '63111111-2222-3333-4444-555555555555', 'Technical deep-dive', 'COMPLETED', 5400, 'Verify technical fit', 'IN_PERSON', 'Integration capability', 'Presentation completed', 'None', NOW() - interval '12 days', NOW() - interval '12 days', NOW() - interval '12 days' + interval '1.5 hours', NOW() - interval '12 days'),
('30000004-1111-1111-1111-111111111111', '89012345-8888-8888-8888-888888888888', '61111111-2222-3333-4444-555555555555', 'Pricing negotiation', 'IN_PROGRESS', 0, 'Finalize pricing', 'ONLINE', 'Budget alignment', 'Technical review done', 'None', NOW(), NOW(), NULL, NOW()),
('30000005-1111-1111-1111-111111111111', '78901234-7777-7777-7777-777777777777', '62111111-2222-3333-4444-555555555555', 'Contract review', 'SCHEDULED', 0, 'Review contract terms', 'ONLINE', 'Legal compliance', 'Pricing agreed', 'None', NOW() + interval '3 days', NULL, NULL, NOW()),
('30000006-1111-1111-1111-111111111111', '89012345-8888-8888-8888-888888888888', '63111111-2222-3333-4444-555555555555', 'Project planning', 'SCHEDULED', 0, 'Plan implementation', 'IN_PERSON', 'Timeline definition', 'Contract ready', 'None', NOW() + interval '8 days', NULL, NULL, NOW()),
('30000007-1111-1111-1111-111111111111', '78901234-7777-7777-7777-777777777777', '61111111-2222-3333-4444-555555555555', 'Team alignment', 'SCHEDULED', 0, 'Align with stakeholders', 'IN_PERSON', 'Stakeholder buy-in', 'Planning phase', 'None', NOW() + interval '15 days', NULL, NULL, NOW()),
('30000008-1111-1111-1111-111111111111', '89012345-8888-8888-8888-888888888888', '62111111-2222-3333-4444-555555555555', 'Success metrics review', 'SCHEDULED', 0, 'Define KPIs', 'ONLINE', 'Performance tracking', 'Team alignment done', 'None', NOW() + interval '20 days', NULL, NULL, NOW()),
('30000009-1111-1111-1111-111111111111', '78901234-7777-7777-7777-777777777777', '63111111-2222-3333-4444-555555555555', 'Upsell opportunity', 'SCHEDULED', 0, 'Discuss additional services', 'ONLINE', 'Revenue growth', 'Active customer', 'None', NOW() + interval '35 days', NULL, NULL, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO meeting_pre_analysis (id, meeting_id, recommended_strategy, key_points, possible_objections, created_at) VALUES
('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '99999999-8888-7777-6666-555555555555', '{"focus":"exploration"}'::jsonb,      '["Entender processos atuais","Mapear stakeholders"]'::jsonb,                            '["Orçamento limitado","Prazo curto"]'::jsonb,                      NOW()),
('bbbbbbbb-1111-cccc-dddd-eeeeeeeeeeee', 'aaaaaaaa-1111-2222-3333-444444444444', '{"focus":"value_selling"}'::jsonb,   '["Destacar ROI da solução","Apresentar casos de sucesso similares","Demo ao vivo"]'::jsonb, '["Preço acima do esperado","Preferência por fornecedor atual"]'::jsonb, NOW() - interval '11 days'),
('cccccccc-1111-dddd-eeee-ffffffffffff', 'aaaaaaaa-1111-2222-3333-555555555555', '{"focus":"negotiation"}'::jsonb,     '["Negociar desconto por volume","Propor contrato de 12 meses","Validar SLA"]'::jsonb,    '["Condições de pagamento","Cláusulas de rescisão"]'::jsonb,        NOW() - interval '6 days'),
('dddddddd-1111-eeee-ffff-111111111111', 'aaaaaaaa-1111-2222-3333-666666666666', '{"focus":"technical_clarification"}'::jsonb, '["Responder dúvidas sobre API","Explicar modelo de licenciamento"]'::jsonb,       '["Complexidade de integração","Custo de manutenção"]'::jsonb,      NOW()),
('eeeeeeee-1111-ffff-aaaa-222222222222', 'aaaaaaaa-1111-2222-3333-777777777777', '{"focus":"onboarding"}'::jsonb,      '["Apresentar equipe de suporte","Definir marcos do projeto","Revisar escopo contratado"]'::jsonb, '["Disponibilidade interna da equipe do cliente"]'::jsonb,     NOW() - interval '1 day'),
('ffffffff-1111-aaaa-bbbb-333333333333', 'aaaaaaaa-1111-2222-3333-888888888888', '{"focus":"retention"}'::jsonb,       '["Revisar métricas de uso","Identificar oportunidades de upsell"]'::jsonb,               '["Insatisfação com tempo de resposta do suporte"]'::jsonb,        NOW() - interval '3 days')
ON CONFLICT (id) DO NOTHING;

INSERT INTO meeting_post_analysis (id, meeting_id, summary, action_items, sentiment_analysis, created_at) VALUES
('a1a2b3c4-d5e6-7890-1234-56789abcdef0', '10000001-0001-0001-0001-000000000004', 'Cliente demonstrou interesse na proposta e pediu retorno em 7 dias.',
 '[{"text": "Enviar proposta revisada", "done": false}, {"text": "Agendar próxima etapa", "done": false}]'::jsonb,
 '{"overall": "positive", "score": 0.81}'::jsonb, NOW() - interval '20 days'),
('a2b3c4d5-e6f7-8901-2345-67890abcdef1', 'aaaaaaaa-1111-2222-3333-444444444444', 'Reunião muito positiva. Cliente aprovou a proposta técnica e solicitou ajuste no prazo de entrega. Concorrentes foram descartados após comparativo de funcionalidades.',
 '[{"text": "Ajustar cronograma de entrega para 45 dias", "done": true}, {"text": "Enviar contrato para assinatura", "done": false}, {"text": "Incluir cláusula de suporte estendido", "done": false}]'::jsonb,
 '{"overall": "very_positive", "score": 0.93}'::jsonb, NOW() - interval '10 days'),
('a3b4c5d6-e7f8-9012-3456-7890abcdef12', 'aaaaaaaa-1111-2222-3333-555555555555', 'Contrato negociado com sucesso. Cliente aceitou parcelamento em 3x sem juros e assinou LOI. Implementação prevista para início do próximo mês.',
 '[{"text": "Enviar LOI assinada para o jurídico", "done": true}, {"text": "Agendar kickoff de implementação", "done": false}, {"text": "Provisionar ambiente de homologação", "done": false}]'::jsonb,
 '{"overall": "positive", "score": 0.87}'::jsonb, NOW() - interval '5 days'),
('a4b5c6d7-e8f9-0123-4567-890abcdef123', '10000001-0001-0001-0001-000000000002', 'Dúvidas técnicas esclarecidas. Cliente confirmou que a equipe de desenvolvimento iniciará a integração na próxima sprint.',
 '[{"text": "Enviar documentação técnica da API", "done": false}, {"text": "Criar canal de suporte dedicado no Slack", "done": false}]'::jsonb,
 '{"overall": "positive", "score": 0.78}'::jsonb, NOW()),
('a5b6c7d8-e9f0-1234-5678-90abcdef1234', '10000003-0003-0003-0003-000000000001', 'Escopo e cronograma do kickoff alinhados. Equipe do cliente confirmou disponibilidade e marcos do projeto foram definidos em conjunto.',
 '[{"text": "Compartilhar acesso ao ambiente de homologação", "done": false}, {"text": "Enviar cronograma detalhado por e-mail", "done": false}]'::jsonb,
 '{"overall": "neutral", "score": 0.65}'::jsonb, NOW() - interval '1 day'),
('a6b7c8d9-e0f1-2345-6789-0abcdef12345', 'aaaaaaaa-1111-2222-3333-888888888888', 'Reunião cancelada pelo cliente por conflito de agenda. Reagendamento solicitado para a próxima semana.',
 '[{"text": "Reagendar reunião de revisão trimestral", "done": false}]'::jsonb,
 '{"overall": "neutral", "score": 0.50}'::jsonb, NOW() - interval '2 days')
ON CONFLICT (id) DO NOTHING;

INSERT INTO meeting_realtime_insights (id, meeting_id, content, type, description, created_at) VALUES
-- Reunião de descoberta (30000001)
('b1b2b3b4-c5d6-7890-1234-56789abcdef1', '30000001-1111-1111-1111-111111111111', 'Cliente mencionou necessidade de integração com ERP existente',        'KEY_POINT',   '{"text": "Requisito técnico crítico que deve constar na proposta"}'::jsonb,           NOW()),
('b2b3b4b5-c6d7-8901-2345-67890abcdef2', '30000001-1111-1111-1111-111111111111', 'Enviar comparativo de planos até sexta-feira',                          'ACTION_ITEM', '{"text": "Prazo definido pelo cliente durante a reunião"}'::jsonb,                   NOW()),
('b3b4b5b6-c7d8-9012-3456-7890abcdef13', '30000001-1111-1111-1111-111111111111', 'Orçamento aprovado pela diretoria para Q3',                             'KEY_POINT',   '{"text": "Janela de compra confirmada para o terceiro trimestre"}'::jsonb,            NOW()),
('b4b5b6b7-c8d9-0123-4567-890abcdef124', '30000001-1111-1111-1111-111111111111', 'Agendar demonstração técnica com o time de TI do cliente',              'ACTION_ITEM', '{"text": "Necessário envolver engenheiro de soluções"}'::jsonb,                      NOW()),
-- Apresentação de proposta (aaaaaaaa-1111-...-444)
('c1c2c3c4-d5e6-7890-1234-56789abcdef1', 'aaaaaaaa-1111-2222-3333-444444444444', 'Cliente rejeitou Concorrente Y após comparativo de funcionalidades',    'KEY_POINT',   '{"text": "Diferencial competitivo confirmado pelo próprio cliente"}'::jsonb,          NOW() - interval '10 days'),
('c2c3c4c5-d6e7-8901-2345-67890abcdef2', 'aaaaaaaa-1111-2222-3333-444444444444', 'Ajustar prazo de entrega de 30 para 45 dias no contrato',               'ACTION_ITEM', '{"text": "Negociação de prazo solicitada pelo gerente de projetos"}'::jsonb,          NOW() - interval '10 days'),
('c3c4c5c6-d7e8-9012-3456-7890abcdef13', 'aaaaaaaa-1111-2222-3333-444444444444', 'Decisor final é o CTO, não o gerente de TI presente na reunião',       'KEY_POINT',   '{"text": "Mapear aprovação do CTO antes de enviar contrato"}'::jsonb,               NOW() - interval '10 days'),
('c4c5c6c7-d8e9-0123-4567-890abcdef124', 'aaaaaaaa-1111-2222-3333-444444444444', 'Enviar proposta revisada com novo prazo para aprovação do CTO',         'ACTION_ITEM', '{"text": "Incluir carta de cases de sucesso no setor de tecnologia"}'::jsonb,        NOW() - interval '10 days'),
-- Negociação de contrato (aaaaaaaa-1111-...-555)
('d1d2d3d4-e5f6-7890-1234-56789abcdef1', 'aaaaaaaa-1111-2222-3333-555555555555', 'Cliente aceitou parcelamento em 3x sem juros como condição para fechar', 'KEY_POINT',   '{"text": "Condição determinante para o fechamento do contrato"}'::jsonb,             NOW() - interval '5 days'),
('d2d3d4d5-e6f7-8901-2345-67890abcdef2', 'aaaaaaaa-1111-2222-3333-555555555555', 'Enviar LOI assinada para o time jurídico ainda hoje',                   'ACTION_ITEM', '{"text": "Urgência solicitada pelo cliente para garantir início em novembro"}'::jsonb, NOW() - interval '5 days'),
('d3d4d5d6-e7f8-9012-3456-7890abcdef13', 'aaaaaaaa-1111-2222-3333-555555555555', 'SLA de 99,5% de uptime é requisito contratual obrigatório',             'KEY_POINT',   '{"text": "Deve constar explicitamente no anexo técnico do contrato"}'::jsonb,        NOW() - interval '5 days'),
-- Follow-up pós-demo (aaaaaaaa-1111-...-666) — em andamento
('e1e2e3e4-f5a6-7890-1234-56789abcdef1', 'aaaaaaaa-1111-2222-3333-666666666666', 'Cliente tem dúvida sobre limites de requisições na API',                'KEY_POINT',   '{"text": "Verificar plano contratado e enviar documentação de rate limits"}'::jsonb,  NOW()),
('e2e3e4e5-f6a7-8901-2345-67890abcdef2', 'aaaaaaaa-1111-2222-3333-666666666666', 'Enviar documentação técnica da API REST com exemplos de integração',    'ACTION_ITEM', '{"text": "Cliente tem equipe de devs pronta para iniciar integração"}'::jsonb,       NOW()),
-- Kickoff de implementação (20000004-1111-...-111)
('f1f2f3f4-a5b6-7890-1234-56789abcdef1', '20000004-1111-1111-1111-111111111111', 'Marcos do projeto definidos: entrega em 3 fases de 15 dias cada',       'KEY_POINT',   '{"text": "Cronograma aprovado por ambas as partes na reunião"}'::jsonb,              NOW() - interval '1 day'),
('f2f3f4f5-a6b7-8901-2345-67890abcdef2', '20000004-1111-1111-1111-111111111111', 'Compartilhar credenciais do ambiente de homologação até amanhã',        'ACTION_ITEM', '{"text": "Equipe do cliente aguarda acesso para iniciar testes"}'::jsonb,            NOW() - interval '1 day')
ON CONFLICT (id) DO NOTHING;

UPDATE collaborators
SET password_hash = '$2a$10$nNZ7b1NRjkrq9muzmMm/BOCu7G6L7DPxKU4XsYxISQj5Uqj4AQYZy'
WHERE password_hash IS NULL;