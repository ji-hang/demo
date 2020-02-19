package com.example.demo.rabbitmq.provider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;


@RestController
@RequestMapping("/")
public class RabbitProvider {

	@Autowired
	private RabbitTemplate rabbitTemplate; // 使用RabbitTemplate,这提供了接收/发送等等方法
	
	
	@GetMapping("sendDirectMessage")
    public String sendDirectMessage() {
		System.out.println("调用...");
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JSONObject json= new JSONObject();
        json.put("messageId",messageId);
        json.put("messageData",messageData);
        json.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        //使用convertAndSend方式发送消息，消息默认就是持久化的
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", JSONObject.toJSONString(json));
        return "ok";
    }
	
}
