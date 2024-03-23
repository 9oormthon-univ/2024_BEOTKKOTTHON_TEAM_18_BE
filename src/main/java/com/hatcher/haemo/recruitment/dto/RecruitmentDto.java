package com.hatcher.haemo.recruitment.dto;

public record RecruitmentDto(Long recruitmentIdx,
                             String type,
                             String name,
                             String leader,
                             Integer participantNumber,
                             Integer participantLimit,
                             String description,
                             boolean isLeader,
                             boolean isDone) {}
