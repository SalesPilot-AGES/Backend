package com.salespilot.api.infrastructure.persistence.jpa.entity;

import com.salespilot.api.domain.valueobject.PostAnalysisActionItem;
import com.salespilot.api.domain.valueobject.PostAnalysisSentimentAnalysis;
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
@Table(name = "meeting_post_analysis")
@Getter
@Setter
@NoArgsConstructor
public class MeetingPostAnalysisEntity implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @Column(name = "summary")
    private String summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "action_items", columnDefinition = "jsonb")
    private List<PostAnalysisActionItem> actionItems;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sentiment_analysis", columnDefinition = "jsonb")
    private PostAnalysisSentimentAnalysis sentimentAnalysis;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
