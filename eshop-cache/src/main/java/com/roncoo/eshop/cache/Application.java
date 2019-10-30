package com.roncoo.eshop.cache;

import com.roncoo.eshop.cache.listener.InitListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * 2、编写业务逻辑
 （1）两种服务会发送来数据变更消息：商品信息服务，商品店铺信息服务，每个消息都包含服务名以及商品id
 （2）接收到消息之后，根据商品id到对应的服务拉取数据，这一步，我们采取简化的模拟方式，就是在代码里面写死，会获取到什么数据，不去实际再写其他的服务去调用了
 （3）商品信息：id，名称，价格，图片列表，商品规格，售后信息，颜色，尺寸
 （4）商品店铺信息：其他维度，用这个维度模拟出来缓存数据维度化拆分，id，店铺名称，店铺等级，店铺好评率
 （5）分别拉取到了数据之后，将数据组织成json串，然后分别存储到ehcache中，和redis缓存中

 3、测试业务逻辑
 （1）创建一个kafka topic
 （2）在命令行启动一个kafka producer
 （3）启动系统，消费者开始监听kafka topic
 C:\Windows\System32\drivers\etc\hosts
 （4）在producer中，分别发送两条消息，一个是商品信息服务的消息，一个是商品店铺信息服务的消息
 （5）能否接收到两条消息，并模拟拉取到两条数据，同时将数据写入ehcache中，并写入redis缓存中
 （6）ehcache通过打印日志方式来观察，redis通过手工连接上去来查询



 */

@SpringBootApplication
@MapperScan("com.roncoo.eshop.cache.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(){
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }

    /**
     * 数据源
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return  sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }


    //整合Jedis Cluster
    @Bean
    public JedisCluster JedisClusterFactory() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("49.234.235.150",7001));
        jedisClusterNodes.add(new HostAndPort("49.234.235.150",7002));
        jedisClusterNodes.add(new HostAndPort("49.234.235.150",7003));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);
        return jedisCluster;
    }

    /**
     * 注册监听器;将系统的初始化完成,如线程池的初始化。
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new InitListener());
        return servletListenerRegistrationBean;
    }

}
