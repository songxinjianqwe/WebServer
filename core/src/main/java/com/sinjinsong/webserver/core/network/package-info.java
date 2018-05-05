/**
 * @author sinjinsong
 * @date 2018/5/5
 * 
 * 网络模块，支持BIO，NIO，AIO，下面用${IO}来代指不同IO模型；
 * 入口是${IO}Endpoint,它对应着唯一的dispatcher实例，以及不同IO模型所需的${IO}Acceptor；
 * ${IO}Acceptor是用来接收客户端连接请求的，接收之后往往会交给${IO}Dispatcher，它往往持有一个业务线程池；
 * ${IO}Dispatcher会先读取客户端数据（IO线程），然后将读到的数据交给${IO}RequestHandler,放入到线程池中去执行；
 * 线程池中会先执行filter，然后执行servlet；
 */
package com.sinjinsong.webserver.core.network;