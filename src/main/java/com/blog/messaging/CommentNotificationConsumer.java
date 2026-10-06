package com.blog.messaging;

import com.blog.dto.message.CommentNotificationMessage;
import com.blog.model.Notification;
import com.blog.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentNotificationConsumer {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "comment.notification.queue")
    public void handleCommentNotification(CommentNotificationMessage message) {
        Notification notification = new Notification();
        notification.setRecipientUserId(message.getPostAuthorId());
        notification.setMessage(message.getCommentAuthor() + " sizning postingizga izoh qoldirdi");
        notificationRepository.save(notification);
        System.out.println(">>>Notification created for user " + message.getPostAuthorId());
    }


}
