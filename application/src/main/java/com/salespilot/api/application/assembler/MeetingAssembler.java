package com.salespilot.api.application.assembler;

import com.salespilot.api.application.dto.ClientMeetingResponseDTO;
import com.salespilot.api.application.dto.MeetingResponseDTO;
import com.salespilot.api.application.dto.SellerMeetingResponseDTO;
import com.salespilot.api.domain.entity.Client;
import com.salespilot.api.domain.entity.Collaborator;
import com.salespilot.api.domain.entity.Meeting;
import org.springframework.stereotype.Component;

@Component
public class MeetingAssembler {
    public MeetingResponseDTO toDTO(Meeting meeting, Collaborator seller, Client client) {
        return new MeetingResponseDTO(
                meeting.getId(),
                meeting.getTitle(),
                SellerMeetingResponseDTO.from(seller),
                ClientMeetingResponseDTO.from(client),
                meeting.getMeetingType(),
                meeting.getScheduledFor(),
                meeting.getStartedAt(),
                meeting.getEndedAt(),
                meeting.getDurationSeconds(),
                meeting.getStatus()
        );
    }
}
