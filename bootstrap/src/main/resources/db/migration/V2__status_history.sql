CREATE TABLE company_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    active BOOLEAN NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_company_status_history_company_changed
    ON company_status_history (company_id, changed_at DESC);

CREATE TABLE collaborator_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    collaborator_id UUID NOT NULL REFERENCES collaborators(id),
    active BOOLEAN NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_collaborator_status_history_collaborator_changed
    ON collaborator_status_history (collaborator_id, changed_at DESC);

INSERT INTO company_status_history (company_id, active, changed_at)
SELECT id, active, created_at FROM companies;

INSERT INTO collaborator_status_history (collaborator_id, active, changed_at)
SELECT id, active, created_at FROM collaborators;
