package com.hatcher.haemo.notification.application;

import com.hatcher.haemo.common.BaseResponse;
import com.hatcher.haemo.common.exception.BaseException;
import com.hatcher.haemo.notification.dto.NotificationDto;
import com.hatcher.haemo.notification.dto.NotificationListResponse;
import com.hatcher.haemo.notification.repository.NotificationRepository;
import com.hatcher.haemo.user.application.UserService;
import com.hatcher.haemo.user.domain.User;
import com.hatcher.haemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hatcher.haemo.common.constants.Constant.ACTIVE;
import static com.hatcher.haemo.common.enums.BaseResponseStatus.INTERNAL_SERVER_ERROR;
import static com.hatcher.haemo.common.enums.BaseResponseStatus.INVALID_USER_IDX;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    // 알림 목록 조회
    public BaseResponse<NotificationListResponse> getNotificationList() throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            List<NotificationDto> notificationDtoList = notificationRepository.findByUserAndStatusEquals(user, ACTIVE).stream()
                    .map(notification -> {
                        long activeParticipantsCount = notification.getRecruitment().getParticipants().stream()
                                .filter(participant -> participant.getStatus().equals(ACTIVE)).count();
                        return new NotificationDto(notification.getNotificationIdx(), notification.getRecruitment().getName(), notification.getRecruitment().getContactUrl(),
                                notification.getRecruitment().getLeader().getNickname(), activeParticipantsCount+1,
                                notification.getRecruitment().getParticipantLimit(), notification.getRecruitment().getDescription());}).toList();
            return new BaseResponse<>(new NotificationListResponse(notificationDtoList));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }
}
