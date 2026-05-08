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
('d4e5f6a7-b8c9-0123-4567-890abcdef123', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Laura Silva',    'laura@digitalsales.com',    'SELLER', FALSE, '{}'),
('e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Saulo Souza',    'saulo@digitalsales.com',    'SELLER', TRUE,  '{"theme":"dark","default_model":"gpt-3.5"}'),
('f6a7b8c9-d0e1-2345-6789-0abcdef12345', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Marcos Pereira', 'marcos@digitalsales.com',   'SELLER', TRUE,  '{"theme":"light","default_model":"gpt-4o"}'),
('a7b8c9d0-e1f2-3456-7890-abcdef123456', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'Julia Fernandes','julia@digitalsales.com',    'SELLER', TRUE,  '{"theme":"dark","default_model":"gpt-4o"}')
ON CONFLICT (id) DO NOTHING;

INSERT INTO clients (id, company_id, collaborator_id, name, client_company_name, sector, overall_sentiment, created_at, updated_at) VALUES
('11111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'Marina Lima',     'Alfa Industrial',  'Manufacturing', 8,  NOW(), NOW()),
('21111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', 'Carlos Mendes',   'BetaTech',         'Technology',    7,  NOW(), NOW()),
('31111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'Patricia Rocha',  'Nexus Logística',  'Logistics',     6,  NOW(), NOW()),
('41111111-2222-3333-4444-555555555555', 'b1c2d3e4-f5a6-7890-2345-67890abcdef1', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', 'Roberto Campos',  'Sigma Varejo',     'Retail',        5,  NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO meetings (id, collaborator_id, client_id, title, status, duration_seconds, objective, meeting_type, client_needs, previous_interactions, competitors_involved, scheduled_for, started_at, ended_at, created_at) VALUES
('99999999-8888-7777-6666-555555555555', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '11111111-2222-3333-4444-555555555555', 'Reunião de descoberta',         'SCHEDULED',   1800, 'Entender dores e objetivos',            'ONLINE',      'Melhorar eficiência operacional',       'Contato inicial via email',         'Concorrente X',              NOW() + interval '3 days',  NULL,                                  NULL,                                  NOW()),
('aaaaaaaa-1111-2222-3333-444444444444', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '21111111-2222-3333-4444-555555555555', 'Apresentação de proposta',       'COMPLETED',   3600, 'Apresentar solução customizada',        'IN_PERSON',   'Reduzir custo de infraestrutura',       'Demo realizada na semana anterior', 'Concorrente Y, Concorrente Z', NOW() - interval '10 days', NOW() - interval '10 days',            NOW() - interval '10 days' + interval '1 hour', NOW() - interval '10 days'),
('aaaaaaaa-1111-2222-3333-555555555555', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '31111111-2222-3333-4444-555555555555', 'Negociação de contrato',         'COMPLETED',   2700, 'Fechar contrato anual',                 'ONLINE',      'Flexibilidade no prazo de pagamento',   'Duas reuniões anteriores de demo',  'Nenhum',                     NOW() - interval '5 days',  NOW() - interval '5 days',             NOW() - interval '5 days' + interval '45 minutes',  NOW() - interval '5 days'),
('aaaaaaaa-1111-2222-3333-666666666666', 'a7b8c9d0-e1f2-3456-7890-abcdef123456', '41111111-2222-3333-4444-555555555555', 'Follow-up pós-demonstração',     'IN_PROGRESS', 1200, 'Tirar dúvidas técnicas pós-demo',       'ONLINE',      'Entender modelo de precificação',       'Demo realizada ontem',              'Concorrente W',              NOW(),                      NOW(),                                 NULL,                                  NOW()),
('aaaaaaaa-1111-2222-3333-777777777777', 'f6a7b8c9-d0e1-2345-6789-0abcdef12345', '21111111-2222-3333-4444-555555555555', 'Kickoff de implementação',       'SCHEDULED',   5400, 'Alinhar escopo e cronograma',           'IN_PERSON',   'Garantir suporte durante a migração',   'Contrato assinado na semana passada', 'Nenhum',                   NOW() + interval '7 days',  NULL,                                  NULL,                                  NOW()),
('aaaaaaaa-1111-2222-3333-888888888888', 'e5f6a7b8-c9d0-1234-5678-90abcdef1234', '11111111-2222-3333-4444-555555555555', 'Revisão trimestral de resultados', 'CANCELLED',   3600, 'Avaliar resultados do último trimestre', 'ONLINE',     'Relatório de desempenho atualizado',    'Reunião mensal recorrente',         'Nenhum',                     NOW() - interval '2 days',  NULL,                                  NULL,                                  NOW() - interval '2 days')
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
('a1a2b3c4-d5e6-7890-1234-56789abcdef0', '99999999-8888-7777-6666-555555555555', 'Cliente demonstrou interesse na proposta e pediu retorno em 7 dias.',
 '[{"text": "Enviar proposta revisada", "done": false}, {"text": "Agendar próxima etapa", "done": false}]'::jsonb,
 '{"overall": "positive", "score": 0.81}'::jsonb, NOW() - interval '20 days'),
('a2b3c4d5-e6f7-8901-2345-67890abcdef1', 'aaaaaaaa-1111-2222-3333-444444444444', 'Reunião muito positiva. Cliente aprovou a proposta técnica e solicitou ajuste no prazo de entrega. Concorrentes foram descartados após comparativo de funcionalidades.',
 '[{"text": "Ajustar cronograma de entrega para 45 dias", "done": true}, {"text": "Enviar contrato para assinatura", "done": false}, {"text": "Incluir cláusula de suporte estendido", "done": false}]'::jsonb,
 '{"overall": "very_positive", "score": 0.93}'::jsonb, NOW() - interval '10 days'),
('a3b4c5d6-e7f8-9012-3456-7890abcdef12', 'aaaaaaaa-1111-2222-3333-555555555555', 'Contrato negociado com sucesso. Cliente aceitou parcelamento em 3x sem juros e assinou LOI. Implementação prevista para início do próximo mês.',
 '[{"text": "Enviar LOI assinada para o jurídico", "done": true}, {"text": "Agendar kickoff de implementação", "done": false}, {"text": "Provisionar ambiente de homologação", "done": false}]'::jsonb,
 '{"overall": "positive", "score": 0.87}'::jsonb, NOW() - interval '5 days'),
('a4b5c6d7-e8f9-0123-4567-890abcdef123', 'aaaaaaaa-1111-2222-3333-666666666666', 'Dúvidas técnicas esclarecidas. Cliente confirmou que a equipe de desenvolvimento iniciará a integração na próxima sprint.',
 '[{"text": "Enviar documentação técnica da API", "done": false}, {"text": "Criar canal de suporte dedicado no Slack", "done": false}]'::jsonb,
 '{"overall": "positive", "score": 0.78}'::jsonb, NOW()),
('a5b6c7d8-e9f0-1234-5678-90abcdef1234', 'aaaaaaaa-1111-2222-3333-777777777777', 'Escopo e cronograma do kickoff alinhados. Equipe do cliente confirmou disponibilidade e marcos do projeto foram definidos em conjunto.',
 '[{"text": "Compartilhar acesso ao ambiente de homologação", "done": false}, {"text": "Enviar cronograma detalhado por e-mail", "done": false}]'::jsonb,
 '{"overall": "neutral", "score": 0.65}'::jsonb, NOW() - interval '1 day'),
('a6b7c8d9-e0f1-2345-6789-0abcdef12345', 'aaaaaaaa-1111-2222-3333-888888888888', 'Reunião cancelada pelo cliente por conflito de agenda. Reagendamento solicitado para a próxima semana.',
 '[{"text": "Reagendar reunião de revisão trimestral", "done": false}]'::jsonb,
 '{"overall": "neutral", "score": 0.50}'::jsonb, NOW() - interval '2 days')
ON CONFLICT (id) DO NOTHING;

INSERT INTO meeting_realtime_insights (id, meeting_id, content, type, description, created_at) VALUES
-- Reunião de descoberta (99999999)
('b1b2b3b4-c5d6-7890-1234-56789abcdef1', '99999999-8888-7777-6666-555555555555', 'Cliente mencionou necessidade de integração com ERP existente',        'KEY_POINT',   '{"text": "Requisito técnico crítico que deve constar na proposta"}'::jsonb,           NOW()),
('b2b3b4b5-c6d7-8901-2345-67890abcdef2', '99999999-8888-7777-6666-555555555555', 'Enviar comparativo de planos até sexta-feira',                          'ACTION_ITEM', '{"text": "Prazo definido pelo cliente durante a reunião"}'::jsonb,                   NOW()),
('b3b4b5b6-c7d8-9012-3456-7890abcdef13', '99999999-8888-7777-6666-555555555555', 'Orçamento aprovado pela diretoria para Q3',                             'KEY_POINT',   '{"text": "Janela de compra confirmada para o terceiro trimestre"}'::jsonb,            NOW()),
('b4b5b6b7-c8d9-0123-4567-890abcdef124', '99999999-8888-7777-6666-555555555555', 'Agendar demonstração técnica com o time de TI do cliente',              'ACTION_ITEM', '{"text": "Necessário envolver engenheiro de soluções"}'::jsonb,                      NOW()),
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
-- Kickoff de implementação (aaaaaaaa-1111-...-777)
('f1f2f3f4-a5b6-7890-1234-56789abcdef1', 'aaaaaaaa-1111-2222-3333-777777777777', 'Marcos do projeto definidos: entrega em 3 fases de 15 dias cada',       'KEY_POINT',   '{"text": "Cronograma aprovado por ambas as partes na reunião"}'::jsonb,              NOW() - interval '1 day'),
('f2f3f4f5-a6b7-8901-2345-67890abcdef2', 'aaaaaaaa-1111-2222-3333-777777777777', 'Compartilhar credenciais do ambiente de homologação até amanhã',        'ACTION_ITEM', '{"text": "Equipe do cliente aguarda acesso para iniciar testes"}'::jsonb,            NOW() - interval '1 day'),
-- Revisão trimestral cancelada (aaaaaaaa-1111-...-888)
('f3f4f5f6-a7b8-9012-3456-7890abcdef13', 'aaaaaaaa-1111-2222-3333-888888888888', 'Reunião cancelada por conflito de agenda do lado do cliente',           'KEY_POINT',   '{"text": "Cliente solicitou reagendamento via e-mail antes do horário"}'::jsonb,     NOW() - interval '2 days'),
('f4f5f6f7-a8b9-0123-4567-890abcdef124', 'aaaaaaaa-1111-2222-3333-888888888888', 'Reagendar revisão trimestral para a semana seguinte',                   'ACTION_ITEM', '{"text": "Prioridade alta para não perder a janela do trimestre"}'::jsonb,           NOW() - interval '2 days')
ON CONFLICT (id) DO NOTHING;