package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "meeting_pre_analysis")
@Getter
@Setter
@NoArgsConstructor
public class MeetingPreAnalysis implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meetings meetings;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recommended_strategy", columnDefinition = "json")
    private String recommendedStrategy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "key_points", columnDefinition = "json")
    private String keyPoints;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "possible_objections", columnDefinition = "json")
    private String possibleObjections;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
