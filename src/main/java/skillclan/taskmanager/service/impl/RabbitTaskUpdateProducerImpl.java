package skillclan.taskmanager.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import skillclan.taskmanager.config.RabbitMQConfig;
import skillclan.taskmanager.dto.TaskNotificationDto;
import skillclan.taskmanager.service.NotificationProducer;

@Service
public class RabbitTaskUpdateProducerImpl implements NotificationProducer {

    private static final Logger loggger = LoggerFactory.getLogger(RabbitTaskUpdateProducerImpl.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitTaskUpdateProducerImpl(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(TaskNotificationDto taskNotificationDto) {
        loggger.info("Trying to send a JSON message to RabbitMQ. TaskID: {}", taskNotificationDto.getId());
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, taskNotificationDto);
            loggger.info("Message successfully sent to RabbitMQ: '{}' with Routing Key: '{}'", RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY);
        } catch (Exception e){
            loggger.error("Error while sending a message to RabbitMQ: {}", e.getMessage(), e);
        }
    }

}
