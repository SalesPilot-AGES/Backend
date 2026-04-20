CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    tax_id VARCHAR,
    phone VARCHAR,
    address VARCHAR,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE subscription_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    description TEXT,
    price_cents INT NOT NULL,
    status VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE company_subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    plan_id UUID NOT NULL REFERENCES subscription_plans(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    starts_at TIMESTAMP,
    ends_at TIMESTAMP,
    renewal_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE collaborators (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    role VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    phone VARCHAR,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    preferences JSON,
    average_feeling INT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE prompt (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR NOT NULL,
    description TEXT,
    instructions TEXT NOT NULL,
    custom BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE prompt_assignments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prompt_id UUID NOT NULL REFERENCES prompt(id),
    collaborator_id UUID NOT NULL REFERENCES collaborators(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    collaborator_id UUID NOT NULL REFERENCES collaborators(id),
    name VARCHAR NOT NULL,
    client_company_name VARCHAR,
    sector VARCHAR,
    overall_sentiment INT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE meetings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    collaborator_id UUID NOT NULL REFERENCES collaborators(id),
    client_id UUID NOT NULL REFERENCES clients(id),
    title VARCHAR,
    status VARCHAR NOT NULL,
    duration_seconds INT,
    objective VARCHAR,
    meeting_type VARCHAR,
    client_needs TEXT,
    previous_interactions TEXT,
    competitors_involved VARCHAR,
    scheduled_for TIMESTAMP,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE meeting_pre_analysis (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id UUID NOT NULL REFERENCES meetings(id),
    recommended_strategy JSON,
    key_points JSON,
    possible_objections JSON,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE meeting_realtime_insights (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id UUID NOT NULL REFERENCES meetings(id),
    content TEXT NOT NULL,
    type VARCHAR NOT NULL,
    description JSON,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE meeting_post_analysis (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id UUID NOT NULL REFERENCES meetings(id),
    summary TEXT,
    action_items JSON,
    sentiment_analysis JSON,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
