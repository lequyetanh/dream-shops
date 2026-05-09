package com.dailycodework.dreamshops.rabbitmq.consumer;

import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.entity.TaskLog;
import com.dailycodework.dreamshops.payload.dto.taskLog.Content;
import com.dailycodework.dreamshops.repository.order.IOrderRepository;
import com.dailycodework.dreamshops.repository.takeLog.ITaskLogRepository;
import com.dailycodework.dreamshops.service.order.OrderService;
import com.dailycodework.dreamshops.service.taskLog.TaskLogService;
import com.dailycodework.dreamshops.util.Common;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;


@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final Logger log = LoggerFactory.getLogger("sds.ep.AsyncGetTaxMachineListener");
    private final OrderService orderService;
    private final IOrderRepository orderRepository;
    private final TaskLogService taskLogService;
    private final ITaskLogRepository taskLogRepository;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    @RabbitListener(queues = "create-order-queue")
    public void handle(Message message, Integer taskLogId, Channel channel) throws InterruptedException, IOException, ExecutionException {
        System.out.println("Handle message: " + taskLogId);
        Thread.sleep(1000L);
        TaskLog taskLog = getTaskLogById(taskLogId).get();
        if(taskLog == null) {
            log.error("Tasklog {} is not found", taskLogId);
            return;
        }
        try{
            Content content = new Content();
            content = Common.fromJsonString(taskLog.getContent(), Content.class);
            Order order = new Order();
            List<Order> listOrder = orderRepository.findByIdIn(content.getBillIds());
            System.out.println(listOrder);
//            ==========================================================
        }catch(Exception e){
            log.error(
                    "user-get-tax-machine failed to ACK message, taskLogId = {}, queueMessageInfo = {}",
                    taskLogId,
                    message.getMessageProperties()
            );
        }
        finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    public CompletableFuture<TaskLog> getTaskLogById(Integer id) throws IOException {
        CompletableFuture<TaskLog> futureResponse = new CompletableFuture<>();
        Runnable task = new Runnable() {
            private int attempts = 0;

            @Override
            public void run() {
                try {
                    int maxAttempts = 5;
                    if (attempts < maxAttempts) {
                        Optional<TaskLog> taskLogOptional = taskLogRepository.findById(id);
                        if (taskLogOptional.isPresent()) {
                            futureResponse.complete(taskLogOptional.get());
                        } else {
                            attempts++;
                            scheduler.schedule(this, 1, TimeUnit.SECONDS);
                        }
                    } else {
                        futureResponse.complete(null);
                    }
                } catch (Exception e) {
//                    log.error("Error when getTaskLogById: {}", e.getMessage());
                } finally {
                    SecurityContextHolder.clearContext();
                }
            }
        };

        scheduler.schedule(task, 1, TimeUnit.SECONDS);
        return futureResponse;
    }
}
