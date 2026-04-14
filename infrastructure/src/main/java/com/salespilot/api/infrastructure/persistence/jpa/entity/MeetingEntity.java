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
@Table(name = "meetings")
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_membership_id", nullable = false)
    private CompanyMembershipEntity sellerMembership;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_membership_id")
    private CompanyMembershipEntity managerMembership;

    @Column(name = "title")
    private String title;

    @Column(name = "objective")
    private String objective;

    @Column(name = "meeting_type")
    private String meetingType;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_company_name")
    private String clientCompanyName;

    @Column(name = "client_sector")
    private String clientSector;

    @Column(name = "competitors", columnDefinition = "jsonb")
    private String competitors;

    @Column(name = "previous_interactions")
    private String previousInteractions;

    @Column(name = "customer_pains")
    private String customerPains;

    @Column(name = "transcript_storage_path")
    private String transcriptStoragePath;

    @Column(name = "summary_storage_path")
    private String summaryStoragePath;

    @Column(name = "audio_storage_path")
    private String audioStoragePath;

    @Column(name = "scheduled_for")
    private OffsetDateTime scheduledFor;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @Column(name = "has_realtime_insights", nullable = false)
    private boolean realtimeInsights;

    @Column(name = "has_post_meeting_summary", nullable = false)
    private boolean postMeetingSummary;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
