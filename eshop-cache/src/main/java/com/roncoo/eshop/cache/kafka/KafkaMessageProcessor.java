package com.roncoo.eshop.cache.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * kafka消息处理线程
 * @author Administrator
 *
 */
public class KafkaMessageProcessor implements Runnable{
    private KafkaStream kafkaStream;
//    private CacheService cacheService;

    public KafkaMessageProcessor(KafkaStream kafkaStream) {
        this.kafkaStream = kafkaStream;
//        this.cacheService = SpringContext.get;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()) {
            String message = new String(it.next().message());



        }
    }
}
