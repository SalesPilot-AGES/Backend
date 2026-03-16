package com.salespilot.api.domain.repository;

import com.salespilot.api.domain.entity.SystemStatus;

public interface SystemStatusRepository {
    SystemStatus getCurrentStatus();
}
