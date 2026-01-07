package com.dailycodework.dreamshops.rabbitmq;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQProperties {
    private String addresses;
    private Integer port;
    private String username;
    private String password;

    private Consumer consumer;
    private Producer producer;

    public static class Consumer {

        private Integer maxConcurrentConsumer;
        private Integer concurrentConsumer;
        private Integer prefetchCount;

        public Integer getMaxConcurrentConsumer() {
            return maxConcurrentConsumer;
        }

        public void setMaxConcurrentConsumer(Integer maxConcurrentConsumer) {
            this.maxConcurrentConsumer = maxConcurrentConsumer;
        }

        public Integer getConcurrentConsumer() {
            return concurrentConsumer;
        }

        public void setConcurrentConsumer(Integer concurrentConsumer) {
            this.concurrentConsumer = concurrentConsumer;
        }

        public Integer getPrefetchCount() {
            return prefetchCount;
        }

        public void setPrefetchCount(Integer prefetchCount) {
            this.prefetchCount = prefetchCount;
        }
    }

    public static class Producer {

        private Integer replyTimeout;
        private String directExchange;

        public Integer getReplyTimeout() {
            return replyTimeout;
        }

        public void setReplyTimeout(Integer replyTimeout) {
            this.replyTimeout = replyTimeout;
        }

        public String getDirectExchange() {
            return directExchange;
        }

        public void setDirectExchange(String directExchange) {
            this.directExchange = directExchange;
        }
    }

    @Getter
    @Setter
    public static class OrderPublish {
        private String CreateOrderQueue;
        private String CreateOrderRoutingKey;
    }
}


