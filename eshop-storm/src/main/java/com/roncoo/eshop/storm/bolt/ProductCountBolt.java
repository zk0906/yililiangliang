package com.roncoo.eshop.storm.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品访问次数统计bolt
 * @author Administrator
 *
 */
public class ProductCountBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;

    private LRUMap<Long,Long> productCountMap = new LRUMap<Long,Long>(1000);


    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        new Thread(new ProductCountThread()).start();
        // 1、将自己的taskid写入一个zookeeper node中，形成taskid的列表
        // 2、然后每次都将自己的热门商品列表，写入自己的taskid对应的zookeeper节点
        // 3、然后这样的话，并行的预热程序才能从第一步中知道，有哪些taskid
        // 4、然后并行预热程序根据每个taskid去获取一个锁，然后再从对应的znode中拿到热门商品列表

    }




    @Override
    public void execute(Tuple tuple) {
        Long  productId = tuple.getLongByField("productId");
        Long count = productCountMap.get(productId);
        if(count == null){
            count = 0L;
        }
        count++;

        //某个产品，被请求了多少次，构成下一波LRU数据集合的数据来源
        productCountMap.put(productId,count);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    /**
     * 使用LRU算法，生成热点数据集合
     */
    private class ProductCountThread implements Runnable {

        @Override
        public void run() {
            //本次最终目标是：定时从productCountMap集合中，算出一个排名前n的目标集合topnProductList
            List<Map.Entry<Long,Long>> topnProductList = new ArrayList<Map.Entry<Long, Long>>();
            while(true){
                topnProductList.clear();

                int topn = 3;
                for(Map.Entry<Long,Long> productCountEntry :productCountMap.entrySet()){
                    //目标集合为空时，先放一批数据进去
                    //然后，非空时，将每一个productCountMap中的数据（productCountEntry）与topnProductList的数据计算，剔除不合格的
                    if(topnProductList.size() == 0){
                        topnProductList.add(productCountEntry);
                    }else {
                        // 比较大小，生成最热topn的算法有很多种
                        // 但是我这里为了简化起见，不想引入过多的数据结构和算法的的东西
                        // 很有可能还是会有漏洞，但是我已经反复推演了一下了，而且也画图分析过这个算法的运行流程了
                        boolean bigger = false;
                        for(int i = 0; i < topnProductList.size(); i++){
                            Map.Entry<Long, Long> topnProductCountEntry = topnProductList.get(i);

                            if(productCountEntry.getValue() > topnProductCountEntry.getValue()) {
                                int lastIndex = topnProductList.size() < topn ? topnProductList.size() - 1 : topn - 2;
                                for(int j = lastIndex; j >= i; j--) {
                                    topnProductList.set(j + 1, topnProductList.get(j));//原先的那个top该索引后的所有数据都往后挪一位
                                }
                                topnProductList.set(i, productCountEntry);
                                bigger = true;
                                break;
                            }
                        }

                        if(!bigger) {
                            if(topnProductList.size() < topn) {
                                topnProductList.add(productCountEntry);
                            }
                        }
                    }
                }
            }
        }
    }
}
