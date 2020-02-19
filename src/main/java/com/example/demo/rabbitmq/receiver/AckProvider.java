package com.example.demo.rabbitmq.receiver;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;

@Component
public class AckProvider {

	/**
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	@RabbitListener(queues = "TestDirectQueue")
	public void process(Message message, Channel channel) throws IOException {
		
		JSONObject msg = JSONObject.parseObject(new String(message.getBody()));
		System.out.println(("receive: " + msg));
		//channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		// 采用手动应答模式, 手动确认应答更为安全稳定
		//channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		//channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		
		//multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
		//requeue：重新放回队列
		//channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
		//拒绝消息
		//channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
	}

}
