package com.salespilot.api.application.assembler;

import com.salespilot.api.application.dto.ClientMeetingResponseDTO;
import com.salespilot.api.application.dto.MeetingContextMetadataResponseDTO;
import com.salespilot.api.application.dto.MeetingPreAnalysisResponseDTO;
import com.salespilot.api.application.dto.SellerMeetingResponseDTO;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.entity.MeetingPreAnalysis;
import org.springframework.stereotype.Component;

@Component
public class MeetingContextMetadataAssembler {
    public MeetingContextMetadataResponseDTO toDTO(Meeting meeting, Collaborator seller, Client client, MeetingPreAnalysis preAnalysis) {
        return new MeetingContextMetadataResponseDTO(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getStatus(),
                meeting.getMeetingType(),
                meeting.getObjective(),
                meeting.getClientNeeds(),
                meeting.getPreviousInteractions(),
                meeting.getCompetitorsInvolved(),
                meeting.getScheduledFor(),
                meeting.getStartedAt(),
                meeting.getEndedAt(),
                meeting.getDurationSeconds(),
                SellerMeetingResponseDTO.from(seller),
                ClientMeetingResponseDTO.from(client),
                preAnalysis == null ? null : MeetingPreAnalysisResponseDTO.from(preAnalysis),
                meeting.getCreatedAt()
        );
    }
}
