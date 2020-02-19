package com.example.demo.rabbitmq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

//@Component
//@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver  {

	@RabbitHandler
    public void process(JSONObject testMessage) {
        //System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
		
    }

}
