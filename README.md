# RabbitMQ教程

### 一、 工作模式

1. 一对一
一个生产者，一个消费者

2. 工作队列：
多个消费者竞争消费同一个队列的消息

3. 发布订阅
原理是使用匿名队列绑定到一个`fanout`交换机, 生产者给这个交换机发送消息时，每个匿名机队列的消费者都会同等的收到消息。

4. 路由模式
使用一个消费队列绑定多个不同路由键的消息，接收端和发送端的路由键完全一致时，消费者就会收到这个消息。

5. topics模式
指定一个topic类型的交换机，队列和交换机绑定时，可以使用通配符通过`*`和`#`通配符来，接收端和发送端的路由键匹配时，消费者就会收到这个消息

### 二、交换机类型
#### 1.  direct 直连交换机

发送端指定路由键`routingKey`，接收端`bindingKey`,如果bindingKey和`routingKey`匹配时，则可以接收该消息。

#### 2. fanout 扇形交换机

使用匿名队列 + 扇形交换机，可以实现消息的订阅发布工作模式，扇形交换机能够把消息同等的发送到每一个队列，
使用时可以忽略发送和接收端的routingKey

#### 3. topic 话题交换机

bindingKey通常是用`.`分割的一系列单词，最大支持255bytes, 如`a.b.c`,可以认为有三个维度a.b.c来描述这个消息，和其他交换机的比较，就是可以使用bindingKey模糊匹配,
基于不同的维度订阅消息。

* `*` 精确表示一个单词
* `#` 表示一个或多个单词

- 当使用`#`绑定一个队列时，它会收到所有的消息，就像fanout交换机一样
- 当没有使用`*`和`#`绑定队列时，他的行为就像direct交换机一样。

#### 4. header 头部交换机

todo: 待补充

### 三、数据安全保证机制

#### 1. 使用事务

todo 待补充

#### 2. 生产者发布确认
方向：broker ---> publisher, 服务端`basic.ack` / `basic.nack`
概念: 生成者投递消息到broker这个过程并不一定成功，broker可能会由于某些原因，拒绝接收投递的消息，所以会有一个投递确认的状态表示接收成功或者失败，状态分为ack和nack两种。

生产者发送消息到交换机后， broker会发送一个Ack消息到生产者，用来表示消息已收到。 默认情况下，发布确认是关闭的，需要在Channel上通过`channel.confirmSelect`开启。
基于消息的确认可以单个确认一次，也可以批量确认一次，

##### 反向确认`basic.nack`;
交换机仅在由于某些原因内部进程错误无法接收消息时会返回nack,经测试nack会在消息找不到交换机时会返回。

##### 正向确认`basic.ack`
表示消息已到达broker（这里确切来说是交换机）, 但**不检查消息是否已经进入队列**，此时会返回一个`basic.ack`到客户端。在开启了`publish-return`,并且如果消息设置为了强制的`mandatory`，
如果消息无法从交换机路由到任何队列，则会返回`basic.return`到发送端,这个return消息包含了一些状态码之类的解释内容。

下面是消息发布确认的客户端确认两种处理方式 :

（1）单个消息阻塞式确认

```
while (thereAreMessagesToPublish()) {
    byte[] body = ...;
    BasicProperties properties = ...;
    channel.basicPublish(exchange, queue, properties, body);
    // uses a 5 second timeout
    channel.waitForConfirmsOrDie(5_000);
}
```

(2)批量消息阻塞式确认：

```
    int batchSize = 100;
    int outstandingMessageCount = 0;
    while (thereAreMessagesToPublish()) {
        byte[] body = ...;
        BasicProperties properties = ...;
        channel.basicPublish(exchange, queue, properties, body);
        outstandingMessageCount++;
        if (outstandingMessageCount == batchSize) {
            channel.waitForConfirmsOrDie(5_000);
            outstandingMessageCount = 0;
        }
    }
    if (outstandingMessageCount > 0) {
        channel.waitForConfirmsOrDie(5_000);
    }
```


* 对于broker拒绝的消息，不要在投递确认回调中再次通过这个channel发送这个消息，这个时候IO线程不支持做这些操作。
更好地解决方案是在内存队列中对消息进行排队，该队列由发布线程轮询。


#### 3. 队列持久化
首次定义队列时需要指定

#### 4. 消息持久化
消息发送时指定消息的是否是要持久化，对应的`deliveryMode = 2`



### 四、消费端消息确认
方向：consumer --->  broker
概念：broker将队列中的消息投递给消费者时，消息处于Untracked状态，消费端处理完成并且没有发生异常，需要返回一个消息的处理的确认状态给broker，表示这条消息已经被正确处理，或拒绝处理，
客户端依据返回的确认状态删除该消息，或者否需要重新入队交由其他消费者处理。

##### 1. 自动确认
创建消费端消息时，可以指定自动确认`autoAck`来决定是否开启自动或手动确认。自动确认比较适合高性能的场景，对消费端的消费水平要求也较高，需要注意消费端的过载，也容易造成消息丢失。

##### 2. 手动确认
手动确认通常配合prefetch一起使用，用来限制这个通道上未完成的消息数量。 手动消费时，如果消费失败，或者丢失连接时，消息会自动重入队列。


### 五、其他概念
##### `Prefectch` QoS -- 未确认消息的滑动窗口大小

消息投递给消费者时，无论是自动确认还是手动确认，由消费者返回的投递确认ACK都是有延迟的。投递时，broker并不需要等到ACK收到后才能继续投递，而是会任继续投递，这个prefectchCount的大小即是这个未收到ACK的消息的
最大值，这个channel上的消息达到过这个`prefetchCount`值时，broker不会再往这个channel投递消息，而是等待其他消费者来消费。一旦这个直到收到这个通道的ACK后才会继续投递。

* prefetch可以在通道、消费者、全局这三个维度上设置。
* 如果客户端使用pullApi即basic.get, prefetch设置的值将不会生效

参考文章：
* RabbitMq-可靠性:<https://www.rabbitmq.com/reliability.html>

