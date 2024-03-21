package com.hatcher.haemo.recruitment.dto;

public record RecruitmentPostRequest(String name,
                                     String type,
                                     Integer participantLimit,
                                     String contactUrl,
                                     String description) {}
