package com.salespilot.api.infrastructure.persistence.jpa.entity;

import java.time.OffsetDateTime;
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
@Table(name = "meeting_post_analysis")
public class MeetingPostAnalysisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @Column(name = "summary", columnDefinition = "jsonb")
    private String summary;

    @Column(name = "action_items", columnDefinition = "jsonb")
    private String actionItems;

    @Column(name = "sentiment_analysis", columnDefinition = "jsonb")
    private String sentimentAnalysis;

    @Column(name = "generated_at")
    private OffsetDateTime generatedAt;
}
