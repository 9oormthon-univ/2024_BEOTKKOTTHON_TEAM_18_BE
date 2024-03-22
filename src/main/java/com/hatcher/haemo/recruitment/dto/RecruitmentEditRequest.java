package com.hatcher.haemo.recruitment.dto;

public record RecruitmentEditRequest(String name,
                                     String type,
                                     Integer participantLimit,
                                     String contactUrl,
                                     String description) {}
