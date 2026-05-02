package com.salespilot.api.infrastructure.persistence.jpa.entity;

import com.salespilot.api.domain.valueobject.PreAnalysisRecommendedStrategy;
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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meeting_pre_analysis")
@Getter
@Setter
@NoArgsConstructor
public class MeetingPreAnalysisEntity implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recommended_strategy", columnDefinition = "jsonb")
    private PreAnalysisRecommendedStrategy preAnalysisRecommendedStrategy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "key_points", columnDefinition = "jsonb")
    private List<String> preAnalysisKeyPoints;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "possible_objections", columnDefinition = "jsonb")
    private List<String> preAnalysisPossibleObjections;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
