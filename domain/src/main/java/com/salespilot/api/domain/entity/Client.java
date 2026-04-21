package com.salespilot.api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Client {
    private UUID id;
    private UUID companyId;
    private UUID collaboratorId;
    private String name;
    private String clientCompanyName;
    private String sector;
    private Integer overallSentiment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
