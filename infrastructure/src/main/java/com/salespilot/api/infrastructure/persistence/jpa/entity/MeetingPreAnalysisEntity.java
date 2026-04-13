package com.salespilot.api.infrastructure.persistence.jpa.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "meeting_pre_analysis")
public class MeetingPreAnalysisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @Column(name = "recommended_strategy", columnDefinition = "jsonb")
    private String recommendedStrategy;

    @Column(name = "key_points", columnDefinition = "jsonb")
    private String keyPoints;

    @Column(name = "possible_objections", columnDefinition = "jsonb")
    private String possibleObjections;

    @Column(name = "generated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime generatedAt;
}
