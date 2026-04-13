BEGIN;

INSERT INTO public.profiles (id, full_name, email, role, is_active, created_at, updated_at) VALUES
('10000000-0000-0000-0000-000000000001','Ana Gestora','ana@salespilot.dev','manager',true,now(),now()),
('10000000-0000-0000-0000-000000000002','Bruno Vendedor','bruno@salespilot.dev','seller',true,now(),now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.companies (id, name, tax_id, status, max_sellers, max_managers, notes, created_at, updated_at) VALUES
('20000000-0000-0000-0000-000000000001','Acme Corp','12.345.678/0001-99','active',10,3,'Empresa mock',now(),now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.subscription_plans (id, name, description, price_cents, max_sellers, max_managers, status, is_active, created_at) VALUES
('30000000-0000-0000-0000-000000000001','Pro','Plano Pro',19900,10,3,'active',true,now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.company_memberships (id, company_id, profile_id, manager_membership_id, job_title, phone, is_active, preferences, created_at, updated_at) VALUES
('40000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000001','10000000-0000-0000-0000-000000000001',NULL,'Sales Manager','51999990001',true,'{"theme":"dark"}',now(),now()),
('40000000-0000-0000-0000-000000000002','20000000-0000-0000-0000-000000000001','10000000-0000-0000-0000-000000000002','40000000-0000-0000-0000-000000000001','Account Executive','51999990002',true,'{"theme":"light"}',now(),now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.company_subscriptions (id, company_id, plan_id, status, starts_at, renewal_date, created_at, updated_at) VALUES
('50000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000001','30000000-0000-0000-0000-000000000001','active',now(),now() + interval '30 day',now(),now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.ai_prompts (id, company_id, name, description, instructions, scope, created_by, is_active, created_at) VALUES
('60000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000001','Discovery Call','Prompt de descoberta','Faca perguntas objetivas','company','10000000-0000-0000-0000-000000000001',true,now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.prompt_assignments (id, prompt_id, company_id, membership_id, sequence_number, is_active, created_at) VALUES
('70000000-0000-0000-0000-000000000001','60000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000001','40000000-0000-0000-0000-000000000002',1,true,now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.meetings (
  id, company_id, seller_membership_id, manager_membership_id, title, objective, meeting_type,
  client_name, client_company_name, status, result, has_realtime_insights, has_post_meeting_summary,
  created_at, updated_at
) VALUES
('80000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000001','40000000-0000-0000-0000-000000000002','40000000-0000-0000-0000-000000000001',
 'Reuniao Inicial','Mapear dores','discovery','Carlos Cliente','Cliente SA','completed','positive',true,true,now(),now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.meeting_realtime_insights (id, meeting_id, type, description, content, sequence_number, is_active, created_at) VALUES
('90000000-0000-0000-0000-000000000001','80000000-0000-0000-0000-000000000001','insight','Momento importante','Cliente citou urgencia',1,true,now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.meeting_pre_analysis (id, meeting_id, recommended_strategy, key_points, possible_objections, generated_at) VALUES
('a0000000-0000-0000-0000-000000000001','80000000-0000-0000-0000-000000000001','{"approach":"consultive"}','["dor A","dor B"]','["preco","prazo"]',now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.meeting_post_analysis (id, meeting_id, summary, action_items, sentiment_analysis, generated_at) VALUES
('b0000000-0000-0000-0000-000000000001','80000000-0000-0000-0000-000000000001','{"summary":"reuniao positiva"}','["enviar proposta","agendar demo"]','{"sentiment":"positive"}',now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.audit_logs (id, actor_profile_id, company_id, action, entity_type, entity_id, payload, created_at) VALUES
('c0000000-0000-0000-0000-000000000001','10000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000001','CREATE','meeting','80000000-0000-0000-0000-000000000001','{"source":"seed"}',now())
ON CONFLICT (id) DO NOTHING;

COMMIT;
