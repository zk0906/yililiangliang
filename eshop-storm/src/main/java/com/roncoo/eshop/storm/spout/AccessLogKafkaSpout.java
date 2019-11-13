package com.roncoo.eshop.storm.spout;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * kafka消费数据的spout
 */
public class AccessLogKafkaSpout extends BaseRichSpout{
    private static final long serialVersionUID = 1L;

    private SpoutOutputCollector collector;
    private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue(1000);

    private final  String topic = "access-log";;

    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = collector;
        //建立kafka的连接
        startKafkaConsumer();
    }

    //从eshop-cache中copy出kafka的消费端代码
    private void startKafkaConsumer() {
        Properties props = new Properties();
        props.put("zookeeper.connect","139.9.105.242:2181,49.234.235.150:2181,115.28.211.17:2181");
        props.put("group.id","eshop-cache-group");
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");

        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        for (final KafkaStream stream : streams) {
            new Thread(new KafkaMessageProcessor(stream)).start();
        }
    }

    private class KafkaMessageProcessor implements Runnable {
        @SuppressWarnings("rawtypes")
        private KafkaStream kafkaStream;

        @SuppressWarnings("rawtypes")
        public KafkaMessageProcessor(KafkaStream stream) {
            this.kafkaStream = stream;
        }

        @Override
        public void run() {
            ConsumerIterator<Byte[],byte[]> it = kafkaStream.iterator();
            while (it.hasNext()){
                String message = new String(it.next().message());
                try{
                    queue.put(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void nextTuple() {
        if(queue.size() > 0){
            try {
                String message = queue.take();
                collector.emit(new Values(message));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            Utils.sleep(100);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));


    }


}
