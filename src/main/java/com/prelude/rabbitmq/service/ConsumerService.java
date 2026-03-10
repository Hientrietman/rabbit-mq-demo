package com.prelude.rabbitmq.service;

import com.prelude.rabbitmq.configuration.RabbitMQ.RBMQMainConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class ConsumerService {

    @RabbitListener(queues = RBMQMainConfig.QUEUE_NAME, containerFactory = "autoFactory")
    public void receiveMessage(Map<String, Object> message) {
        System.out.println("Đã nhận tin nhắn từ Queue: " + message.get("message"));
        // Bạn có thể xử lý logic CRUD, gửi mail... ở đây
    }
    // Lắng nghe Queue A của Fanout
    @RabbitListener(queues = RBMQMainConfig.QUEUE_A, containerFactory = "autoFactory")
    public void consumeA(Map<String, Object> message) {
        System.out.println("Consumer A nhận được: " + message);
    }

    // Lắng nghe Queue B của Fanout
    @RabbitListener(queues = RBMQMainConfig.QUEUE_B, containerFactory = "autoFactory")
    public void consumeB(Map<String, Object> message) {
        System.out.println("Consumer B nhận được: " + message);
    }

    // Lắng nghe Queue Manual ACK
    @RabbitListener(queues = RBMQMainConfig.MANUAL_ACK_QUEUE, containerFactory = "manualFactory")
    public void handleManual(Map<String, Object> message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            // 1. Thực hiện logic nghiệp vụ
            System.out.println("Đang xử lý đơn hàng: " + message);

            // Giả sử có lỗi logic không thể cứu vãn
            if (message.get("data") == null) throw new IllegalArgumentException("Dữ liệu trống!");

            // 2. Thành công -> Xóa tin nhắn
            channel.basicAck(tag, false);

        } catch (IllegalArgumentException e) {
            // 3. Lỗi logic (Ví dụ: dữ liệu sai định dạng)
            // -> NACK và requeue = false (đẩy đi DLX hoặc xóa bỏ, không thử lại vô ích)
            System.err.println("Lỗi dữ liệu, không thử lại: " + e.getMessage());
            channel.basicNack(tag, false, false);

        } catch (Exception e) {
            // 4. Lỗi hệ thống (Ví dụ: Timeout DB, sập mạng)
            // -> NACK và requeue = true (để RabbitMQ đẩy lại vào hàng đợi thử lại sau)
            System.err.println("Lỗi hệ thống, đang thử lại...");
            channel.basicNack(tag, false, true);
        }
    }

}
