package com.hatcher.haemo.notification.dto;

public record NotificationDto(Long notificationIdx,
                              String name,
                              String contactUrl,
                              String leader,
                              long participantNumber,
                              Integer participantLimit,
                              String description) {}
