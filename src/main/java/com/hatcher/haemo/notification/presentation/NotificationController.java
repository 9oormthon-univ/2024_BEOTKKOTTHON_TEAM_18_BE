package com.hatcher.haemo.notification.presentation;

import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.notification.application.NotificationService;
import com.hatcher.haemo.notification.dto.NotificationListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.hatcher.haemo.common.constants.RequestURI.notification;


@RestController
@RequiredArgsConstructor
@RequestMapping(notification)
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 목록 조회
    @GetMapping("")
    public BaseResponse<NotificationListResponse> getNotificationList() {
        try {
            return notificationService.getNotificationList();
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 알림 삭제
    @DeleteMapping("/{notificationIdx}")
    public BaseResponse<String> deleteNotification(@PathVariable Long notificationIdx) {
        try {
            return notificationService.deleteNotification(notificationIdx);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
