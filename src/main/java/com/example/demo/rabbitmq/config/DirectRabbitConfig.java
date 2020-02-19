package com.example.demo.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 直连型交换机
 * @author admin
 *
 */
@Configuration
public class DirectRabbitConfig {
	
	@Bean
	public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /* (non-Javadoc)
             * 
             * ①消息推送到server，但是在server里找不到交换机
             * ②消息推送到server，找到交换机了，但是没找到队列  （ack为true,消息是推送成功到服务器了的，所以ConfirmCallback对消息确认情况是true）
             * ③消息推送到sever，交换机和队列啥都没找到 
             * ④消息推送成功
             * @see org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback#confirm(org.springframework.amqp.rabbit.support.CorrelationData, boolean, java.lang.String)
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
                System.out.println("ConfirmCallback:     "+"确认情况："+ack);
                System.out.println("ConfirmCallback:     "+"原因："+cause);
            }

        });
 
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /* (non-Javadoc)
             * ②消息推送到server，找到交换机了，但是没找到队列  
             * @see org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback#returnedMessage(org.springframework.amqp.core.Message, int, java.lang.String, java.lang.String, java.lang.String)
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallback:     "+"消息："+message);
                System.out.println("ReturnCallback:     "+"回应码："+replyCode);
                System.out.println("ReturnCallback:     "+"回应信息："+replyText);
                System.out.println("ReturnCallback:     "+"交换机："+exchange);
                System.out.println("ReturnCallback:     "+"路由键："+routingKey);
            }
        });

        return rabbitTemplate;
	}

	//队列 起名：TestDirectQueue
    @Bean
    public Queue TestDirectQueue() {
        return new Queue("TestDirectQueue",true, false, false);  //true 是否持久 化
    }
 
    //Direct交换机 起名：TestDirectExchange
    @Bean
    DirectExchange TestDirectExchange() {
        return new DirectExchange("TestDirectExchange");
    }
    
    /**
     * 1.交换机名称
     * 2.是否持久化
     * 3.自动删除
     * @return
     */
    @Bean
    DirectExchange TestDirectExchange1() {
    	//第二个参数durable: 是否持久化, 第三个参数autoDelete: 当所有绑定队列都不再使用时, 是否自动删除交换器, true: 删除, false: 不删除
        return new DirectExchange("TestDirectExchange1", true, false);
    }
 
    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("TestDirectRouting");
    }

   /* @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setQueues(TestDirectQueue());
        container.setMessageListener(directReceiver);
//        container.addQueues(fanoutRabbitConfig.queueA());
//        container.setMessageListener(fanoutReceiverA);
        return container;
    }*/

}
