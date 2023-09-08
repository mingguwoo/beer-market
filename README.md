# plm-bom-configuration



## 模块说明

***

本服务一共有如下几个核心模块：

***

## Api

用户接入层，负责用户接口层负责接收外部的请求，请求包括但不限于Http请求或Kafka消息等。

- [ ] Controller

## Application

应用层，通常会采用CQRS的处理模式（也可以采用别的模式，但规范大同小异可以通用），将命令和查询进行分离，同时领域事件和一些复杂的跨领域的业务逻辑也可以在这一层处理。

- [ ] 命令
- [ ] 查询
- [ ] 应用服务
- [ ] 任务

## Domain

领域层，主要分为聚合、领域服务、领域事件三大部分和其他一些小的组成，聚合内包括一系列实体和值对象，每个聚合对应一个聚合根，由聚合根统一封装聚合内实体的状态变更，聚合根实现不了的操作，由领域服务负责实现，跨领域/聚合的操作发送领域事件进行流转，或者在应用层进行聚合编排。

- [ ] 聚合
- [ ] 领域服务
- [ ] 防腐接口
- [ ] 领域事件


## Infrastructure

基础设施层，负责数据存储、API网关、事件总线，防腐等，其中，数据存储为Repositiry，repository的接口定义在领域层，基础设施层提供repository实现，API网关通过防腐层ACL统一封装隔离变化，事件总线用于发送领域事件。总之，所有与平台，框架相关的实现都会在该层中，避免领域层被这些逻辑所污染。

- [ ] 配置
- [ ] 仓储实现
- [ ] 防腐实现
- [ ] 其他


***

除此之外，还有一些辅助模块：

***

## Common

公共模块，主要用来存放一些服务中通用的一些对象，工具类，常量，枚举等等

## Spi
Spi模块，是用来存放外部调用相关的Feign服务客户端以及Request，Response对象等

## Sdk
Sdk模块，是用来存放配置管理服务日后用来提供对外服务调用的Feign接口及其相关的Request，Response对象等