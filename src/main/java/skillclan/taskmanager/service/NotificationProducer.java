package skillclan.taskmanager.service;

import skillclan.taskmanager.dto.TaskNotificationDto;

public interface NotificationProducer {
    /**
     * Sends a message to the Kafka topic
     * @param taskNotificationDto - object with information (DTO) that will be serialized in JSON (message value).
     */
    void sendMessage(TaskNotificationDto taskNotificationDto);
}
