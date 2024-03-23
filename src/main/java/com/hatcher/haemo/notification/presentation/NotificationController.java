package com.hatcher.haemo.notification.presentation;

import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.notification.application.NotificationService;
import com.hatcher.haemo.notification.dto.NotificationListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hatcher.haemo.common.constants.RequestURI.notification;


@RestController
@RequiredArgsConstructor
@RequestMapping(notification)
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public BaseResponse<NotificationListResponse> getNotificationList() {
        try {
            return notificationService.getNotificationList();
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
