
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.profiles (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name text NOT NULL,
    email text NOT NULL UNIQUE,
    avatar_url text,
    role text NOT NULL DEFAULT 'seller',
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now()
);

CREATE TABLE public.companies (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    tax_id text UNIQUE,
    status text NOT NULL DEFAULT 'active',
    max_sellers integer NOT NULL DEFAULT 0,
    max_managers integer NOT NULL DEFAULT 0,
    notes text,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now()
);

CREATE TABLE public.subscription_plans (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    description text,
    price_cents integer NOT NULL DEFAULT 0,
    max_sellers integer NOT NULL DEFAULT 0,
    max_managers integer NOT NULL DEFAULT 0,
    status text NOT NULL DEFAULT 'active',
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamp with time zone NOT NULL DEFAULT now()
);

CREATE TABLE public.company_memberships (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id uuid NOT NULL,
    profile_id uuid NOT NULL,
    manager_membership_id uuid,
    job_title text,
    phone text,
    is_active boolean NOT NULL DEFAULT true,
    preferences jsonb,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT company_memberships_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(id) ON DELETE CASCADE,
    CONSTRAINT company_memberships_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES public.profiles(id) ON DELETE CASCADE,
    CONSTRAINT company_memberships_manager_membership_id_fkey FOREIGN KEY (manager_membership_id) REFERENCES public.company_memberships(id) ON DELETE SET NULL
);

CREATE TABLE public.company_subscriptions (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id uuid NOT NULL,
    plan_id uuid NOT NULL,
    status text NOT NULL DEFAULT 'active',
    starts_at timestamp with time zone,
    ends_at timestamp with time zone,
    renewal_date timestamp with time zone,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT company_subscriptions_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(id) ON DELETE CASCADE,
    CONSTRAINT company_subscriptions_plan_id_fkey FOREIGN KEY (plan_id) REFERENCES public.subscription_plans(id)
);

CREATE TABLE public.ai_prompts (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id uuid,
    name text NOT NULL,
    description text,
    instructions text NOT NULL,
    scope text NOT NULL DEFAULT 'global',
    created_by uuid,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT ai_prompts_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(id) ON DELETE CASCADE,
    CONSTRAINT ai_prompts_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.profiles(id) ON DELETE SET NULL
);

CREATE TABLE public.prompt_assignments (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    prompt_id uuid NOT NULL,
    company_id uuid NOT NULL,
    membership_id uuid NOT NULL,
    sequence_number integer,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT prompt_assignments_prompt_id_fkey FOREIGN KEY (prompt_id) REFERENCES public.ai_prompts(id) ON DELETE CASCADE,
    CONSTRAINT prompt_assignments_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(id) ON DELETE CASCADE,
    CONSTRAINT prompt_assignments_membership_id_fkey FOREIGN KEY (membership_id) REFERENCES public.company_memberships(id) ON DELETE CASCADE
);

CREATE TABLE public.meetings (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id uuid NOT NULL,
    seller_membership_id uuid NOT NULL,
    manager_membership_id uuid,
    title text,
    objective text,
    meeting_type text,
    client_name text,
    client_company_name text,
    client_sector text,
    competitors jsonb,
    previous_interactions text,
    customer_pains text,
    transcript_storage_path text,
    summary_storage_path text,
    audio_storage_path text,
    scheduled_for timestamp with time zone,
    started_at timestamp with time zone,
    ended_at timestamp with time zone,
    duration_seconds integer,
    status text NOT NULL DEFAULT 'scheduled',
    result text NOT NULL DEFAULT 'unknown',
    processed_at timestamp with time zone,
    has_realtime_insights boolean NOT NULL DEFAULT false,
    has_post_meeting_summary boolean NOT NULL DEFAULT false,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT meetings_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(id) ON DELETE CASCADE,
    CONSTRAINT meetings_seller_membership_id_fkey FOREIGN KEY (seller_membership_id) REFERENCES public.company_memberships(id),
    CONSTRAINT meetings_manager_membership_id_fkey FOREIGN KEY (manager_membership_id) REFERENCES public.company_memberships(id)
);

CREATE TABLE public.meeting_realtime_insights (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id uuid NOT NULL,
    type text NOT NULL,
    description text,
    content text NOT NULL,
    sequence_number integer,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT meeting_realtime_insights_meeting_id_fkey FOREIGN KEY (meeting_id) REFERENCES public.meetings(id) ON DELETE CASCADE
);

CREATE TABLE public.meeting_pre_analysis (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id uuid NOT NULL,
    recommended_strategy jsonb,
    key_points jsonb,
    possible_objections jsonb,
    generated_at timestamp with time zone,
    CONSTRAINT fk_meeting_pre_analysis_meeting FOREIGN KEY (meeting_id) REFERENCES public.meetings(id) ON DELETE CASCADE
);

CREATE TABLE public.meeting_post_analysis (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id uuid NOT NULL,
    summary jsonb,
    action_items jsonb,
    sentiment_analysis jsonb,
    generated_at timestamp with time zone,
    CONSTRAINT fk_meeting_post_analysis_meeting FOREIGN KEY (meeting_id) REFERENCES public.meetings(id) ON DELETE CASCADE
);

CREATE TABLE public.audit_logs (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_profile_id uuid,
    company_id uuid,
    action text NOT NULL,
    entity_type text NOT NULL,
    entity_id uuid,
    payload jsonb,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT audit_logs_actor_profile_id_fkey FOREIGN KEY (actor_profile_id) REFERENCES public.profiles(id) ON DELETE SET NULL,
    CONSTRAINT audit_logs_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.companies(id) ON DELETE CASCADE
);

CREATE INDEX idx_ai_prompts_company_id ON public.ai_prompts (company_id);
CREATE INDEX idx_audit_logs_actor_profile_id ON public.audit_logs (actor_profile_id);
CREATE INDEX idx_audit_logs_company_id ON public.audit_logs (company_id);
CREATE INDEX idx_company_memberships_company_id ON public.company_memberships (company_id);
CREATE INDEX idx_company_memberships_profile_id ON public.company_memberships (profile_id);
CREATE INDEX idx_company_memberships_manager_membership_id ON public.company_memberships (manager_membership_id);
CREATE INDEX idx_company_subscriptions_company_id ON public.company_subscriptions (company_id);
CREATE INDEX idx_company_subscriptions_plan_id ON public.company_subscriptions (plan_id);
CREATE INDEX idx_meeting_post_analysis_meeting_id ON public.meeting_post_analysis (meeting_id);
CREATE INDEX idx_meeting_pre_analysis_meeting_id ON public.meeting_pre_analysis (meeting_id);
CREATE INDEX idx_meeting_realtime_insights_meeting_id ON public.meeting_realtime_insights (meeting_id);
CREATE INDEX idx_meetings_company_id ON public.meetings (company_id);
CREATE INDEX idx_meetings_manager_membership_id ON public.meetings (manager_membership_id);
CREATE INDEX idx_meetings_seller_membership_id ON public.meetings (seller_membership_id);
CREATE INDEX idx_prompt_assignments_company_id ON public.prompt_assignments (company_id);
CREATE INDEX idx_prompt_assignments_membership_id ON public.prompt_assignments (membership_id);
CREATE INDEX idx_prompt_assignments_prompt_id ON public.prompt_assignments (prompt_id);

