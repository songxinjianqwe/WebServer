//package com.sinjinsong.webserver.sample.web.listener;
//
//import cn.sinjinsong.orderquery.constant.CounterConstant;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.servlets.ServletContextEvent;
//import javax.servlets.annotation.WebListener;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author sinjinsong
// * @date 2017/12/24
// */
//@WebListener
//@Slf4j
//public class ServletContextListener implements javax.servlets.ServletContextListener {
//    
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        sce.getServletContext().setAttribute(CounterConstant.TOTAL_NAME, new AtomicInteger(0));
//        sce.getServletContext().setAttribute(CounterConstant.ONLINE_NAME,new AtomicInteger(0));
//        sce.getServletContext().setAttribute(CounterConstant.VISITOR_NAME,new AtomicInteger(0));
//        log.info("contextInitialized,total:{}", 0);
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//    }
//   
//}
