package com.hatcher.haemo.notification.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hatcher.haemo.common.constants.RequestURI.notification;


@RestController
@RequiredArgsConstructor
@RequestMapping(notification)
public class NotificationController {


}
