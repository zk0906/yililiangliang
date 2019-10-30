package com.roncoo.eshop.cache.kafka;

import com.alibaba.fastjson.JSONObject;
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

            // 首先将message转换成json对象
            JSONObject messageJSONObject = JSONObject.parseObject(message);

            // 从这里提取出消息对应的服务的标识
            String serviceId = messageJSONObject.getString("serviceId");

            // 如果是商品信息服务
            if("productInfoService".equals(serviceId)) {
                processProductInfoChangeMessage(messageJSONObject);
            } else if("shopInfoService".equals(serviceId)) {
                processShopInfoChangeMessage(messageJSONObject);
            }

        }
    }

    /**
     * 处理商品信息变更的消息
     * @param messageJSONObject
     */
    private void processProductInfoChangeMessage(JSONObject messageJSONObject) {

    }


    /**
     * 处理店铺信息变更的消息
     * @param messageJSONObject
     */
    private void processShopInfoChangeMessage(JSONObject messageJSONObject) {


    }
}
