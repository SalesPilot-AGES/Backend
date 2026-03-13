package com.salespilot.api.domain.repositories;

import com.salespilot.api.domain.entities.SystemStatus;

public interface SystemStatusRepository {
    SystemStatus getCurrentStatus();
}
