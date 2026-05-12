package com.salespilot.api.application.queryservice;

import com.salespilot.api.application.exception.MeetingNotFoundException;
import com.salespilot.api.domain.entity.Meeting;
import com.salespilot.api.domain.repository.MeetingRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeetingQueryService {
    private final MeetingRepository meetingRepository;

    public MeetingQueryService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public Meeting getOrThrowMeetingById(UUID id) {
        return meetingRepository.getMeetingById(id)
                .orElseThrow(() -> new MeetingNotFoundException(id));
    }
}
