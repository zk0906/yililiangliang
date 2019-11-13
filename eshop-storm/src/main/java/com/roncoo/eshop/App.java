package com.roncoo.eshop;

/**
 * storm的作用，使用实时数据来预热redis
 * 1.nginx+lua 将请求流量打入kafka中
 * 2.使用storm实时分析kafka的数据，将结果导入LRU的队列中
 * 3.该结果，可以作为redis的预热数据来源
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
