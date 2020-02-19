package com.example.demo.rabbitmq.provider;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息持久化
 * @author admin
 *
 */
public class MessageHelper {

	public static Message objToMsg(Object obj) {
		if (null == obj) {
			return null;
		}

		Message message = MessageBuilder.withBody(JSONObject.toJSONString(obj).getBytes()).build();
		message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);// 消息持久化
		message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

		return message;
	}

	public static <T> T msgToObj(Message message, Class<T> clazz) {
		if (null == message || null == clazz) {
			return null;
		}

		String str = new String(message.getBody());
		T obj = JSONObject.parseObject(str, clazz);

		return obj;
	}
}
