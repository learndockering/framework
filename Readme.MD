# Lemon framework 使用指南

##  规范

+   除接口外，其他各业务模块原则上不允许直接使用第三方jar包，如果必须使用，需要封装后再使用
+   日期，时间使用java提供的LocalDate和LocalDateTime，平台Spring mvc 、 mybatis 已提供支持
+   任何暴露的接口返回类型必须是[GenericRspDTO](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-framework/src/main/java/com/hisun/lemon/framework/data/GenericRspDTO.java)类型对象，输入参数中必须🈶️一个参数是[GenericDTO]()类型或其子类型，建议就一个GenericDTO类型参数
+   暴露接口仅仅只需要返回消息码时，返回对象应定义为 ```GenericRspDTO<NoBody>```
+   在application.yml 增加业务参数的时候，必须以模块名开头. 如用户模块：
+   redis缓存所有的KEY必须加前缀，如 id生成是IDGEN，缓存是CACHE，累计是CUMULATIVE.原则上应用不允许直接用RedisTemplete操作redis

```yaml
usr :
    test : testparam
    
```
+   Controller 类必须继承基类 [BaseController](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-framework/src/main/java/com/hisun/lemon/framework/service/BaseService.java)
+   Service 类必须继承基类 [BaseService](http://120.76.22.56/microservices-platform/framework/tree/master/lemon-framework/src/main/java/com/hisun/lemon/framework/service)
+   Redis 缓存的使用，任何KEY必须带前缀，如ID生成前缀是IDGEN，缓存的前缀是CACHE，累计的前缀是CUMULATIVE；原则上不允许使用框架提供之外的方法访问redis
+   所有的Feign interface 开发只引入下面的包,防止第三方引入接口时包冲突

```gradle
dependencies {
    compile("com.hisun:lemon-interface")
}
```



---------------------------------------

##  建议
+   暴露接口输入输出参数用[GenericDTO](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-framework/src/main/java/com/hisun/lemon/framework/data/GenericDTO.java) 的body 属性传递
+   传输对象命名为DTO，持久化对象DO， 业务对象BO
+   先获取分布式锁后再调用Service层服务，避免因等待分布式锁而长时间占用数据库连接资源；解决方法见分布式锁

---------------------------------------

##  工具类
+   日期、时间工具类
    见[com.hisun.lemon.common.utils.DateTimeUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/DateTimeUtils.java)

+   Bean 相关、如对象属性拷贝
    见[com.hisun.lemon.common.utils.BeanUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/BeanUtils.java)

+   判断类，如交易调用是否成功的消息码判断，null、空判断等。
    见[com.hisun.lemon.common.utils.JudgeUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/JudgeUtils.java)

+   validate 
    见[com.hisun.lemon.common.utils.Validate](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/Validate.java)

+   字符串操作
    见[com.hisun.lemon.common.utils.StringUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/StringUtils.java)

+   Number操作
    见[com.hisun.lemon.common.utils.NumberUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/NumberUtils.java)

+   资源类
    见[com.hisun.lemon.common.utils.ResourceUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/ResourceUtils.java)

+   随机数、随机字符串、固定长度随机数、固定长度随机字符串
    见[com.hisun.lemon.common.utils.RandomUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/RandomUtils.java)

+   反射相关
    见[com.hisun.lemon.common.utils.ReflectionUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/ReflectionUtils.java)

+   注解相关
    见[com.hisun.lemon.common.utils.AnnotationUtils](http://120.76.22.56/microservices-platform/framework/tree/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/AnnotationUtils.java)

+   ID、序列号生成
    见[com.hisun.lemon.framework.utils.IdGenUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-framework/src/main/java/com/hisun/lemon/framework/utils/IdGenUtils.java)

+   流水号、消息号、登录用户号、配置参数、记账日期 等平台相关数据
    见[com.hisun.lemon.framework.utils.LemonUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-framework/src/main/java/com/hisun/lemon/framework/utils/LemonUtils.java)

+   分页
    见[com.hisun.lemon.framework.utils.PageUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-framework/src/main/java/com/hisun/lemon/framework/utils/PageUtils.java)

+   SHA-1/MD5消息摘要的工具类
    见[com.hisun.lemon.common.security.Digests](http://120.76.22.56/microservices-platform/framework/tree/master/lemon-common/src/main/java/com/hisun/lemon/common/security/Digests.java)

+   HMAC-SHA1消息签名 及 DES/AES对称加密的工具类
    见[com.hisun.lemon.common.security.Cryptos](http://120.76.22.56/microservices-platform/framework/tree/master/lemon-common/src/main/java/com/hisun/lemon/common/security/Cryptos.java)

+   编码解码工具类
    见[com.hisun.lemon.common.utils.Encodes](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-common/src/main/java/com/hisun/lemon/common/utils/Encodes.java)

---------------------------------------

##  日志

+   slf4j + logback
+   配置文件    src/main/resource/config/logback-spring.xml
+   生产设置日志级别INFO，建议调试都统一设置为DEBUG 或以下级别
+   日志使用方式

```java
    public class UserController {
        private static final Logger logger = LoggerFactory.getLogger(UserController.class);
        
        public GenericDTO<NoBody> testException(GenericDTO<NoBody> object) {
            LemonException.throwBusinessException("PRD00001");
            if(logger.isErrorEnabled()) {
                logger.error("throw a exception, msg cd is {}.", "PRD00001");
            }
            return null;
        }
    }

```
    


---------------------------------------

##  异常

+   平台对异常做统一处理
+   业务异常就抛出com.hisun.lemon.common.exception.LemonException, 抛出方式

```java
    public GenericDTO<NoBody> testException(GenericDTO<NoBody> object) {
        LemonException.throwBusinessException("PRD00001");
        return null;
    }

```
+   错误码建议用enum

+   平台错误码

>
>   **SYS00001** 平台级异常
>
>   **SYS00002** 访问数据库异常
>
>   **SYS00404** 404异常
>
>   **SYS00401** 401异常
>
>   **SYS00004** 线程池满拒绝
>
>   **SYS00005** 任务调度异常
>
>   **SYS00004** 服务端404异常，如请求资源不存在
>
>   **SYS10001** bean validation 异常
>
>   **SYS20000** feign client 异常
>
>   **SYS20001** feign client UnknownHostException
>
>   **SYS99999** 业务代码没有设置msgCd
>

---------------------------------------

##  ID Generator

+   数据持久化对象ID自动生成

```java
public class UserDO extends BaseDO{
    @AutoIdGen(key="USER_ID", prefix="US")
    private String userId;
    private String name;
    private String sex;
    //.......
}
```

+   ID、序列号生成
    见[com.hisun.lemon.framework.utils.IdGenUtils](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-framework/src/main/java/com/hisun/lemon/framework/utils/IdGenUtils.java)
 
+   配置

```ymal
lemon :
  idgen :
    #每次从reids申请Id的数量
    delta : 
      default : 500
      MSGID_ : 1000
      REQUESTID_ : 1000
    #在redis里Id key 的前缀，prefix+"."+ applicationName,申请ID key作为hash key.
    prefix : IDGEN
    #ID sequence 最大值，默认值无穷大，配置方式是在max-value 下配置key 及长度 
    max-value :
      #msgId key
      MSGID_ : 999999999
      REQUESTID_ : 999999999
      USER_ID : 999999  
    autogen :
      #DO目录
      DOPackage : com.hisun.lemon.*.entity
```

---------------------------------------

##  动态数据源

+   见配置  application.yml

```yaml
lemon :
  #dynamic datasource
  dynamicDataSource : 
    enabled : true
    #生效数据源
    dataSources : lemon,primary
    #默认数据源
    defaultDataSource : primary
  
#druid 数据源配置   
dataSource :
  lemon :
    type : com.alibaba.druid.pool.DruidDataSource
    driverClassName : com.mysql.cj.jdbc.Driver
    url : jdbc:mysql://119.23.126.119:3306/lemon?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
    username : lemon
    password : lemon@123
  primary :
    type : com.alibaba.druid.pool.DruidDataSource
    driverClassName : com.mysql.cj.jdbc.Driver
    url : jdbc:mysql://localhost:3306/seatelpay?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
    username : seatelpay
    password : seatelpay

```

+   切换到非默认数据源, 注解@com.hisun.lemon.framework.datasource.TargetDataSource

```java
    //切换到lemon数据源
    @Transactional(readOnly=true)
    @TargetDataSource("lemon")
    public MsgInfoDO getMsgInfo(String msgCd, String language) {
        if(JudgeUtils.isBlank(msgCd)) {
            return null;
        }
        if(JudgeUtils.isBlank(language)) {
            if(JudgeUtils.isBlank(this.defaultLanguage)){
                this.defaultLanguage = this.defaultLocale.split("_")[0];
            }
            language = this.defaultLanguage;
        }
        return this.msgInfoDao.getMsgInfo(msgCd, language);
    }
```


---------------------------------------

##  分页
```java
List<UserDO> userDOs = PageUtils.pageQuery(userQueryDTO.getPageNum(), userQueryDTO.getPageSize(), () -> {
            return this.userService.findUser(queryUserDO);
            });
            
或
            
List<UserDO> userDOs = PageUtils.pageQuery(userQueryDTO.getPageNum(), userQueryDTO.getPageSize(), false, () -> {
            return this.userService.findUser(queryUserDO);
            });
```

---------------------------------------

## 事务
+   Spring 注解事务
+   事务必须在Service层控制

---------------------------------------

## 缓存

+   有redis缓存和ehcache3.X(主要用做堆内缓存)
+   ehcache3.0 实际是JCache 的provider，实现了JSR107

### Redis 缓存
+   redis 主要用作分布式缓存
+   用注解@RedisCacheable 方法即可以实现redis缓存操作，原理是：先查找缓存，缓存没有数据执行方法得到数据，返回得到的数据并将数据存入缓存
+   注解@RedisCacheable 的cacheName 前缀必须为 ${lemon.cache.cacheName.prefix}
+   支持@Cacheable、@CachePut、@CacheEvict；但需要将cacheResolver 指定为 "redisCacheResolver"
+   默认的keyGenerator 是 "CACHE." + simple class name + method name + args；可以按@Cacheable 的key 属性指定key，但key 必须带前缀 “CACHE.”， 支持EL表达式
+   原则上不允许用RedisTemplate 直接操作redis
+   可以根据缓存名设置过期时间，不设置使用默认过期时间：spring.redis.default-expiration

```yaml

lemon :
  cache :
    cacheName :
      prefix : CACHENAME.${spring.application.name}
    redis :
      expires :
        #redis cache expire time; cacheName : expireTime/s; 0 - never expire
        ${lemon.cache.cacheName.prefix}.user : 140 
        
```

```java

    @RedisCacheable(cacheNames="${lemon.cache.cacheName.prefix}.user")
    @Transactional(readOnly=true)
    @Override
    public UserDO findUser(String userId) {
        return this.userDao.findByUserId(userId);
    }
    
```

### Ehcache3 缓存， JCache 缓存， ehcache3 作为JCache 的provider，JSR107实现

+   Ehcache3 主要用作堆内缓存，如消息码信息等
+   使用@JCacheCacheable 注解方法，即可以使用 ehcahce缓存，原理同redis
+   ehcache 的配置见config/ehcache3.xml, 新增缓存必须配置
+   cacheName 可以不指定前缀
+   key生成策略同redis

```java

    @JCacheCacheable("lemonMsgInfo")
    @Transactional(readOnly=true)
    @TargetDataSource("lemon")
    public MsgInfoDO getMsgInfo(String msgCd, String language) {
        if(JudgeUtils.isBlank(msgCd)) {
            return null;
        }
        if(JudgeUtils.isBlank(language)) {
            if(JudgeUtils.isBlank(this.defaultLanguage)){
                this.defaultLanguage = this.defaultLocale.split("_")[0];
            }
            language = this.defaultLanguage;
        }
        return this.msgInfoDao.getMsgInfo(msgCd, language);
    }
    

```

```xml

    <cache alias="lemonMsgInfo">
	    <expiry>
	      <ttl unit="minutes">5</ttl>
	    </expiry>
	    <heap unit="entries">10000</heap>
	    <heap-store-settings>
	       <max-object-size unit="kB">10</max-object-size>
	    </heap-store-settings>
    </cache>

```

---------------------------------------

##  分布式锁

+   使用注解@Locked实现分布式锁
    
```java

    @Locked(lockName = "testLock", leaseTime=40, waitTime=10)
    @Scheduled(fixedRate=10000)
    public void testLock() {
        String str = "testLock1234567890..";
        for(char c : str.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("");
    }
    
```
+   分布式锁不要用在Service层，因为这样会先获取数据库连接，再来等待应用锁；可以用一个非Service层(或者叫XX服务层)方法带起Service层方法，XX服务层注解@Lock,XX服务层不含事务
+   @Locked 注解还有一些属性，见代码
+   如果不用注解，可以写java代码使用分布式锁,可以自己控制异常；如果非必须，还是使用注解
+   如果是Schedule等后台调起的服务，建议忽略调所有异常，业务方法里异常不要往外抛，抛出到线程池的线程也没法处理。

```java

    @Autowired
    private DistributedLocker distributedLocker;

    @Around("@annotation(locked)")
    public void lock(ProceedingJoinPoint pjp, Locked locked) {
        Validate.notEmpty(locked.lockName());
        try {
            distributedLocker.lock(locked.lockName(), locked.leaseTime(), locked.waitTime(), 
            () -> {return proceed(pjp);});
        } catch (UnableToAquireLockException e) {
            if(!(locked.ignoreUnableToAquireLockException() || locked.ignoreException())) {
                LemonException.throwLemonException(e);
            }
            
        } catch (LemonException e) {
            if(! locked.ignoreException()) {
                throw e;
            }
           
        } catch (Throwable e) {
            if(! locked.ignoreException()) {
                LemonException.throwLemonException(e);
            }
           
        }
    }

```


---------------------------------------

##  日累计、月累计

+   利用redis实现
+   接口Cumulative 实现了日累计、月累计、 日月累计、日累计查询、月累计查询
+   支持多维度的累计; new Dimension("K1","1") 对象，K1 表示维度、“1” 表示累计值，可以同时设置多个维度
+   日累计、月累计、 日月累计 使用LUA 脚本实现，原子性操作
+   使用方法见下面例子

```java
   
    @Autowired
    private Cumulative cumulative;

    @GetMapping("/testCumulative/{mode}")
    public GenericDTO<NoBody> testCumulative(@PathVariable String mode) {
        if(JudgeUtils.equals(mode, "0")) {
            this.cumulative.countByDay("TEST", new Dimension("K1","1"),new Dimension("K2","2"),new Dimension("K3","3"));
        } else if (JudgeUtils.equals(mode, "1")) {
            this.cumulative.countByMonth("TEST", new Dimension("K1","1"),new Dimension("K2","2"),new Dimension("K3","3"));
        } else if(JudgeUtils.equals(mode, "2")) {
            this.cumulative.countByDayAndMonth("TEST", new Dimension("K1","1"),new Dimension("K2","2"),new Dimension("K3","3"));
        }
        return GenericDTO.newSuccessInstance();
    }
    
    @GetMapping("/queryCumulative/{mode}/{dimension}")
    public GenericDTO<String> testCumulative(@PathVariable String mode, @PathVariable String dimension) {
        String rst = "";
        if(JudgeUtils.equals(mode, "0")) {
           rst = this.cumulative.queryByDay("TEST", dimension);
        } else if (JudgeUtils.equals(mode, "1")) {
           rst = this.cumulative.queryByMonth("TEST", dimension);
        }
        return GenericDTO.newSuccessInstance(rst);
    }

```

---------------------------------------

## Bean validation JSR303 JSR349

+   feign client bean validate, 注解 @com.hisun.lemon.framework.validation.ClientValidated

```java
//在客户端进行输入参数的校验，只需要在输入DTO注解 @com.hisun.lemon.framework.validation.ClientValidated

@ClientValidated
public class UserQueryDTO{
    @NotEmpty(message="DM310001")
    private String name;
    
    private Integer pageNum;
    
```
```yaml
同时配置文件需要开启客户端验证：
feign :
  validation :
    enabled : true

```

+   服务端 mvc 方法级别 bean validate, 注解@Validated

```java
    //在controller层的方法输入参数注解 @Validated
    @PostMapping("/addUser")
    public GenericDTO<NoBody> addUser(@Validated @RequestBody UserDTO userDTO) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userDO, userDTO);
        this.userService.addUser(userDO);
        return GenericDTO.newSuccessInstance();
    }

```

---------------------------------------

##  轮询、定时任务

+   org.springframework.scheduling.annotation.Scheduled

+   例如：

```java

    @Scheduled(fixedRateString = "${feign.httpclient.clearConnectionsRate}", initialDelay = 30000)
    public void clearExpiredAndIdleConnections() {
        if(JudgeUtils.isNotNull(poolingHttpClientConnectionManager)) {
            poolingHttpClientConnectionManager.closeExpiredConnections();
            poolingHttpClientConnectionManager.closeIdleConnections(feignHttpclientProperties.getIdleTimeoutMillis(), TimeUnit.MILLISECONDS);
        }
    }
    
      
```

+   线程池配置

```yaml

  schedule :
    threadPool :
      poolSize : 10
      waitForTasksToCompleteOnShutdown : true
      awaitTerminaltionSeconds : 30
      
```

+   方法调用前会自动初始化LemonData，可以从LemonUtils获取

---------------------------------------

##  批处理

+   com.hisun.lemon.framework.schedule.batch.BatchScheduled， 注解参数同@Scheduled

+   示例

```java

    @BatchScheduled(fixedRate=10000)
    public void schedule() {
        System.out.println("@BatchScheduled test..................");
    }
    
```
+   线程池配置同 “轮询、定时任务”

+   批处理模式下，@Schedule 不会处理

+   批处理模式下，MQ消费者不会启动

+   开启批处理模式,默认未开启

>
>   方式一：
>
>   java参数形式"-Dlemon.batch.enabled=true"
>
>   方式二：
>   
>   yml配置 "lemon.batch.enabled=true"
>

+   方法调用前会自动初始化LemonData，可以从LemonUtils获取

---------------------------------------

##  国际化

+   消息码已提供支持，消息码表：lemon.lemon_msg_info
+   com.hisun.lemon.framework.i18n.LocaleMessageSource，该类在BaseController 已注入，读取配置文件**i18n/message.properties**
+   系统取不到区域信息、或区域信息不在平台识别的范围类是，默认的区域配置

```yaml
lemon:
  locale :
    default : zh_cn
```

---------------------------------------

##  异步及订阅发布

+   spring cloud stream rabbit 实现

+   配置文件

```yaml

spring :
  rabbitmq :
    addresses : 192.168.1.38:5672
    virtualHost : /lemon
    username : rabbitmq
    password : Rabbitmq123
    requestedHeartbeat : 10
    publisherConfirms : true
    publisherReturns : true
    connectionTimeout : 10000
    cache:
      channel :
        size : 5
    listener :
      concurrency : 1
      maxConcurrency : 5
      idleEventInterval : 60000
      prefetch : 10
      transactionSize : 10
  cloud :
    stream :
      defaultBinder : rabbit
      bindings :
        # 输入通道，即消费者消费的通道
        input :
          destination : ${spring.application.name}
          group : ${spring.application.name}
          consumer :
            #   是否启动消费者
            enable : false
            concurrency : 1
            maxConcurrency : 5
            maxAttempts : 1
            durableSubscription : true
            prefetch : 10
            txSize : 10
          producer :
            deliveryMode : PERSISTENT
        # 输出通道
        output :
          #是否启动该通道
          enable : true
          #通道绑定的topic， rabbitmq 对应 exchange； kafka对应主题；我们在此处配置实例名，即将消息发送到哪个实例（订阅者）
          destination : USR
        output1 :
          enable : true
          destination : output1-test

```

+   生产者（订阅发布的发布者）

```java
//框架会自动将方法的返回对象（即主题、Hello对象）通过通道（channelName）发送出去
@Component
public class TestProducer {
    @Producers({
        @Producer(beanName="helloMessageHandler", channelName=MultiOutput.OUTPUT_DEFAULT),          //beanName 为消费主题(Hello)的spring bean name
        @Producer(beanName="helloMessageHandler2", channelName=MultiOutput.OUTPUT_DEFAULT)          //channelName 为将主题发送出去的通道名，如配置文件中的output
    })
    public Hello sendHello() {
        return new Hello("hello-->",40);
    }
}

//系统默认可以启动8个通道
    public static final String OUTPUT_DEFAULT = "output";
    public static final String OUTPUT_ONE = "output1";
    public static final String OUTPUT_TWO = "output2";
    public static final String OUTPUT_THREE = "output3";
    public static final String OUTPUT_FOUR = "output4";
    public static final String OUTPUT_FIVE = "output5";
    public static final String OUTPUT_SIX = "output6";
    public static final String OUTPUT_SEVEN = "output7";

```

+   消费者(订阅发布中的订阅者)，框架默认配置一个消费者消费input通道

+   消费者中的pring bean 必须实现 com.hisun.lemon.framework.stream.MessageHandler 接口

```java

@Component("helloMessageHandler")
public class HelloMessageHandler implements MessageHandler<Hello> {
    private static final Logger logger = LoggerFactory.getLogger(HelloMessageHandler.class);
    
    @Override
    public void onMessageReceive(GenericCmdDTO<Hello> genericCmdDTO) {
        logger.info("Receive msg hand {}", genericCmdDTO.getBody());
    }
}

```

---------------------------------------

##  随机数

+   示例

```java

    @Resource("bindingTokenRandomTemplete")
    private RandomTemplete randomTemplete;
    
    @GetMapping("/testRandomTemplete")
    public GenericRspDTO<NoBody> testRandomTemplete() {
        String random = randomTemplete.apply("Test", 30*60*1000, RandomType.NUMERIC_LETTER, 15);
        logger.info("random is {}", random);
        randomTemplete.validateOnce("Test", random);
        return GenericRspDTO.newSuccessInstance();
    }

```

+   具体见接口 [com.hisun.lemon.framework.utils.RandomTemplete]()

+   实现类com.hisun.lemon.framework.redis.random.BindingTokenRandomTemplete; 与用户sessionId绑定


---------------------------------------

##  编译、打包、启动服务

+   运行gradle 命令： gradle clean build dist
+   在工程目录下的build/target的目录下生成如下文件及文件夹

>   bin
>
>   lemon-prd-1.0.0-SNAPSHOT.jar
>
>   config
>
>   运行bin/start.sh 即可以启动服务

+   在工程目录下的build/distributions目录下生成如下文件

>
>   lemon-prd-1.0.0-SNAPSHOT.zip
>
>   把此zip文件上传到服务器解压可以得到build/target的目录结构

---------------------------------------

##  通道

+   项目引入通道框架

```gradle

    compile("com.hisun:channel-integration-starter:1.0.0-SNAPSHOT")

```

+   引入自己开发的通道项目，参考:channel:channel-cmsb

```gradle

    compile("com.hisun:channel-cmsb:1.0.0-SNAPSHOT")
    compile("com.hisun:channel-cgb:1.0.0-SNAPSHOT")

```

+   通道开发详情见 [《通道开发指南.docx》](http://120.76.22.56/microservices-platform/channel/blob/master/%E9%80%9A%E9%81%93%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97.docx)
    
>
>   修改：
>    1， 各子通道配置文件不需要在config/channel-spring.xml文件中import, 配置文件命名规则改为“项目名-channel-spring.xml”
>    

+   客户端代码

```java

    
    @Autowired
    private IChannelClient channelClient;
    
    @GetMapping("/testChannel")
    public GenericRspDTO<?> testChannel(GenericDTO<NoBody> genericDTO) {
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
        PayReq.TranReq tranReq = new PayReq.TranReq();
        tranReq.setMchntCd("MCH0000000000001");
        tranReq.setTranDate(localDate.format(formatterDate));
        tranReq.setTranTime(localDate.format(formatterTime));
        tranReq.setTranId("1602061010000000");
        tranReq.setCurrency("RMB");
        tranReq.setPayAcctNo("PAYACCNO0000002");
        tranReq.setPayAccName("yuzhou");
        tranReq.setPayBankType("1");
        tranReq.setPayBankName("中国银行");
        tranReq.setAccNo("6226192914678983");
        tranReq.setAccName("YUZHOU");
        tranReq.setBankType("105100000017");
        tranReq.setBankName("招行");
        tranReq.setTransAmt(new BigDecimal("99.99"));
        tranReq.setRemarkCd("90804");
        tranReq.setRemark("case115");
        tranReq.setResv("case115");
        
        PayReq payReq = new PayReq();
        payReq.setCooperationCd("1000000000001");
        payReq.setTxCd("3002");
        payReq.setTranReq(tranReq);
        
        Request request = new Request();
        request.setRoute("CMSB");
        request.setBusiType("pay");
        request.setSource("tst");
        request.setTarget(payReq);
        request.setRequestId(genericDTO.getMsgId());
        Response response = channelClient.request(request);
        logger.info("channel rsp code {}", response.getMsgCode());
        logger.info("channel rsp info {}", response.getMsgInfo());
        logger.info("channel rsp object {}", response.getResult());
        return GenericRspDTO.newInstance(response.getMsgCode(), response.getResult());
    }

```

---------------------------------------

##  踩过的坑
+   ribbon 默认会对超时的GET请求retry一次。加上以下配置，覆盖ribbon默认配置

```yaml

ribbon :
  #retry next Server times
  MaxAutoRetriesNextServer : 0
  #retry same Server times
  MaxAutoRetries : 0
  ReadTimeout : 20000
  ConnectTimeout : 5000

```

+   在Feign client 中使用@PathVariable 注解，必须指定value值,  ```否则会报：java.lang.IllegalStateException: PathVariable annotation was empty on param 0. ```

```java

@GetMapping(value = "/user/{id}")  
User findById(@PathVariable("id") Long id);  

```

+   关于GET请求参数绑定不支持GenericDTO<T>

>
>   **原因：**
>
>   1, spring mvc 的URL参数绑定不支持泛型
>
>   2, feign 对象GET请求也是用Body(Json)发送
>

解决方法一：参数继承```GenericDTO<NoBody>``` ， GenericDTO或其子类 必须 @LemonBody注解
```java
        @GetMapping("/findUser2")
    public GenericRspDTO<List<User>> findUser2(@Validated @LemonBody UserQueryDTO queryUserDTO) {
        List<User> users = null;
        //省略
        return GenericRspDTO.newSuccessInstance(users);
    }
    
    @ClientValidated
    @ApiModel(value="UserQueryDTO", description="用户查询传输对象")
    public class UserQueryDTO extends GenericDTO<NoBody>{
        @ApiModelProperty(value="姓名", required= true)
        @NotEmpty(message="DM310001")
        private String name;
    }
```

解决方法二: 多个参数，其中有一个为```GenericDTO<NoBody>``` ;GenericDTO 必须 @LemonBody注解 当参数比较固定的时候，可以使用该方案
```java
    @GetMapping("/findUser3")
    public GenericRspDTO<List<User>> findUser3(@RequestParam String name, @RequestParam int pageNum, 
        @RequestParam int pageSize, @LemonBody GenericDTO<NoBody> genericDTO) {
        List<User> users = null;
        //省略
        return GenericRspDTO.newSuccessInstance(users);
    }
```

feign 接口也需要@LemonBody注解

```java

    @GetMapping("/user/findUser2")
    public GenericRspDTO<List<UserQueryDTO.User>> findUser2(@LemonBody UserQueryDTO userQueryDTO);
    

    @GetMapping("/user/findUser3")
    public GenericRspDTO<List<UserQueryDTO.User>> findUser3(@RequestParam("name") String name, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNum") Integer pageNum, @Validated @LemonBody GenericDTO<NoBody> genericDTO);
    

```

GenericDTO 子类中属性忽略作为parameter通过feign接口发送，需要用@IgnoreLemonBody 注解

```java

    @ApiModelProperty(value="用户列表" )
    @IgnoreLemonBody
    private List<User> users;

```

---------------------------------------

##  mybatis 代码自动生成

+   mybatis 代码自动生成是根据mybatis-generator修改而成，见项目 [lemon-generator](http://120.76.22.56/microservices-platform/framework/tree/master/lemon-generator)

+   修改配置文件    [generatorConfig.xml](http://120.76.22.56/microservices-platform/framework/blob/master/lemon-generator/generatorConfig.xml)

>
+   修改数据库参数  **connectionURL** 
```xml
<jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql://localhost:3306/seatelpay" userId="seatelpay" password="seatelpay">  
</jdbcConnection>
```
> 
+   修改生成文件存放目录    **targetProject**   及工程目录结构  **targetPackage**  ，其他配置都不要修改
```xml
<javaModelGenerator targetPackage="com.hisun.lemon.prd.entity" targetProject="/Users/yuzhou/Documents/work/temp/java">
<sqlMapGenerator targetPackage="com.hisun.lemon.prd.mapper" targetProject="/Users/yuzhou/Documents/work/temp/resources"> 
<javaClientGenerator type="XMLMAPPER" targetPackage="com.hisun.lemon.prd.dao" targetProject="/Users/yuzhou/Documents/work/temp/java">
```
>

+   运行 org.mybatis.generator.api.ShellRunner


+   生成代码效果如下：

>   java dao interface  ,扩展的方法在此接口中定义

```java
    package com.hisun.lemon.prd.dao;

    import com.hisun.lemon.framework.dao.BaseDao;
    import com.hisun.lemon.prd.entity.UserDO;
    import org.apache.ibatis.annotations.Mapper;

    @Mapper
    public interface UserDao extends BaseDao<UserDO> {
    }
```
>

>   java entity

```java
package com.hisun.lemon.prd.entity;

import com.hisun.lemon.framework.data.BaseDO;
import java.time.LocalDate;

public class UserDO extends BaseDO {
    /**
     * @Fields userId 
     */
    private String userId;
    //.....省略
}

```
>

>  xml mapper   **xml没有自动生成BaseDAO的find 接口的代码，请自行实现** 可以参考[UserMapper.xml](http://120.76.22.56/microservices-platform/microservices-demo/blob/master/lemon-prd/src/main/resources/com/hisun/lemon/prd/mapper/UserMapper.xml)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.hisun.lemon.prd.dao.UserDao" >

    <resultMap id="BaseResultMap" type="com.hisun.lemon.prd.entity.UserDO" >
        <id column="user_id" property="userId" jdbcType="VARCHAR" />
        <result column="name" property="name" jdbcType="VARCHAR" />
        <result column="age" property="age" jdbcType="INTEGER" />
        <result column="birthday" property="birthday" jdbcType="DATE" />
        <result column="sex" property="sex" jdbcType="CHAR" />
        <result column="modify_time" property="modifyTime" jdbcType="TIMESTAMP" />
        <result column="create_time" property="createTime" jdbcType="TIMESTAMP" />
    </resultMap>

    <sql id="Base_Column_List" >
        user_id, name, age, birthday, sex, modify_time, create_time
    </sql>
```


