package com.microsservico.estoquepreco.connections;

import com.microsservico.estoquepreco.constants.RabbitMQConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConnection {

    private static final String EXCHANGE_NAME = "amq.direct";
    private AmqpAdmin amqpAdmin;

    public RabbitMQConnection(AmqpAdmin amqpAdmin){
        this.amqpAdmin = amqpAdmin;
    }

    private Queue queue(String queueName){
        return new Queue(queueName, true, false, false);
    }

    private DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    private Binding relationship(Queue queue, DirectExchange directExchange){
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, directExchange.getName(), queue.getName(), null);
    }

    @PostConstruct
    private void add(){
        Queue stockQueue = this.queue(RabbitMQConstants.STOCK_QUEUE);
        Queue priceQueue = this.queue(RabbitMQConstants.PRICE_QUEUE);

        DirectExchange exchange = this.directExchange();

        Binding stockBinding = this.relationship(stockQueue, exchange);
        Binding priceBinding = this.relationship(priceQueue, exchange);

        //criando as filas no RabbitMQ
        this.amqpAdmin.declareQueue(stockQueue);
        this.amqpAdmin.declareQueue(priceQueue);

        //criando as exchanges
        this.amqpAdmin.declareExchange(exchange);

        //criando os bindings
        this.amqpAdmin.declareBinding(stockBinding);
        this.amqpAdmin.declareBinding(priceBinding);
    }
}
