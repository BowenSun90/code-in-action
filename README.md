# code-in-action
Sample projects for various language in programing

[toc]

## 1.springboot
- 1.1 Springboot with Swagger   
[spring-with-swagger](https://github.com/BowenSun90/code-in-action/tree/master/springboot-in-action/spring-with-swagger)   
启动程序，访问http://127.0.0.1:8080/swagger-ui.html  
>The Best APIs   
https://swagger.io/   


- 1.2 Springboot with Kotlin    
[spring-with-Kotlin](https://github.com/BowenSun90/code-in-action/tree/master/springboot-in-action/spring-with-kotlin)   
Kotlin和Java混合编程  


- 1.3 Springboot with security   
[spring-with-security](https://github.com/BowenSun90/code-in-action/tree/master/springboot-in-action/spring-with-security)    
Springboot权限控制   


- 1.4 Springboot support Spring batch  
[spring-batch](https://github.com/BowenSun90/code-in-action/tree/master/springboot-in-action/spring-batch)


- 1.5 Springboot webservice sampel program**       
[spring-web](https://github.com/BowenSun90/code-in-action/tree/master/springboot-in-action/spring-boot-web)

配置文件 `application.properties`
```
# spring application config
spring.application.name=webservice-template
spring.profiles.active=${spring.profiles.active}
server.context-path=/
server.port=${port}

# mybatis mapper config
mybatis.mapper-locations=classpath*:mapper/**Mapper.xml

# guava memory cache config
guava.cache.maximumSize=1000
guava.cache.duration=1
# DAYS |HOURS |MINUTES |SECONDS |MILLISECONDS |MICROSECONDS |NANOSECONDS
guava.cache.unit=DAYS
```

数据库配置 `application-{spring.profiles.active}.properties`
```
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://${host}:${port}/${database}
spring.datasource.username=${username}
spring.datasource.password=${password}

spring.datasource.max-idle=50
spring.datasource.max-wait=10000
spring.datasource.min-idle=10
spring.datasource.initial-size=10
```

启动类   
[WebserviceApplication](https://github.com/BowenSun90/client-collection/blob/master/spring-boot-web/src/main/java/com/alex/space/springboot/WebserviceApplication.java)   

Profile     
DEV：使用内嵌Tomcat容器启动     
PROD：不打包内嵌Tomcat，打包为war放到Jetty中启动   
[pom.xml](https://github.com/BowenSun90/client-collection/blob/master/spring-boot-web/pom.xml)   

Swagger APIs   
[SwaggerConfig](https://github.com/BowenSun90/client-collection/blob/master/spring-boot-web/src/main/java/com/alex/space/springboot/cfg/SwaggerConfig.java)  

Guava memory cache   
[UserService](https://github.com/BowenSun90/client-collection/blob/master/spring-boot-web/src/main/java/com/alex/space/springboot/service/UserService.java)   



## 2.Python爬虫
- 2.1 Python scrapy sample program   
[tutorial](https://github.com/BowenSun90/code-in-action/tree/master/python-scrapy-collection/tutorial)
```
> cd tutorial
> scrapy crawl quotes
```


- 2.2 拉勾网职位信息  
[lagou](https://github.com/BowenSun90/code-in-action/tree/master/python-scrapy-collection/lagou)  
```
> cd lagou
> scrapy crawl lagouposition
> head ./lagou.csv
```


- 2.3 豆瓣文章信息  
[doubanbook](https://github.com/BowenSun90/code-in-action/tree/master/python-scrapy-collection/doubanbook)  
```
> cd doubanbook
> scrapy crawl dbbook
> head ./douban.csv
```


- 2.4 城市编码  
[citycode](https://github.com/BowenSun90/code-in-action/tree/master/python-scrapy-collection/citycode)  
```
> cd citycode
> scrapy crawl city
> head ./city.csv
```


- 2.5 甲级医院信息
[hospital](https://github.com/BowenSun90/code-in-action/tree/master/python-scrapy-collection/hospitalcrawler)  
```
> cd hospitalcrawler
> scrapy crawl hospital
> head ./hospital.csv
```


- 2.6 学校信息
[school](https://github.com/BowenSun90/code-in-action/tree/master/python-scrapy-collection/schoolcrawler)  
```
> cd schoolcrawler
> scrapy crawl school
> head ./school.csv
```



## 3.Python
- 3.1 查看被删的微信好友  
[wechat-deleted-friends](https://github.com/BowenSun90/code-in-action/tree/master/python-in-action/wechat-deleted-friends)



## 4.Storm
- 4.1 Wordcount   
[Wordcount](https://github.com/BowenSun90/code-in-action/tree/master/storm-in-action/Wordcount)   


- 4.2 Log Analysis   
[calllog-analysis](https://github.com/BowenSun90/code-in-action/tree/master/storm-in-action/calllog-analysis)   



## 5.Flink
- 5.1 Streaming-Wordcount With Socket    
[Streaming-wordcount](https://github.com/BowenSun90/code-in-action/tree/master/flink-in-action/streaming-wordcount)   


- 5.2 Streaming-Wordcount With Kafka   
[Streaming-kafka](https://github.com/BowenSun90/code-in-action/tree/master/flink-in-action/streaming-kafka)


- 5.3 Streaming-Window operation      
[Streaming-window](https://github.com/BowenSun90/code-in-action/tree/master/flink-in-action/streaming-window)   

## 6.HBase
- 6.1 HBase Bulk load via Spark    
[CreateAndBulkloadHFile.scala](https://github.com/BowenSun90/code-in-action/blob/master/hbase-in-action/src/main/scala/com/alex/space/hbase/spark/CreateAndBulkloadHFile.scala)

- 6.2 HBase Bulk load via Flink    
// todo


