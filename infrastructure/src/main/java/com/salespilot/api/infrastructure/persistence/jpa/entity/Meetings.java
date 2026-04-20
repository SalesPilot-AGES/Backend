package com.salespilot.api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "meetings", catalog = "salespilot")
@Getter
@Setter
@NoArgsConstructor
public class Meetings implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private Collaborators collaborators;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Clients clients;

    @Column(name = "title")
    private String title;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "objective")
    private String objective;

    @Column(name = "meeting_type")
    private String meetingType;

    @Column(name = "client_needs")
    private String clientNeeds;

    @Column(name = "previous_interactions")
    private String previousInteractions;

    @Column(name = "competitors_involved")
    private String competitorsInvolved;

    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meetings")
    private Set<MeetingPreAnalysis> meetingPreAnalyses = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meetings")
    private Set<MeetingRealtimeInsights> meetingRealtimeInsights = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meetings")
    private Set<MeetingPostAnalysis> meetingPostAnalyses = new HashSet<>(0);
}
