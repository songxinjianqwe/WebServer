package com.sinjinsong.webserver.core.network.endpoint;

import org.springframework.util.StringUtils;

/**
 * @author sinjinsong
 * @date 2018/5/4
 */
public abstract class Endpoint {
    /**
     * 启动服务器
     * @param port
     */
    public abstract void start(int port);

    /**
     * 关闭服务器
     */
    public abstract void close();

    /**
     * 根据传入的bio、nio、aio获取相应的Endpoint实例
     * @param connector
     * @return
     */
    public static Endpoint getInstance(String connector) {
        StringBuilder sb = new StringBuilder();
        sb.append("com.sinjinsong.webserver.core.network.endpoint")
                .append(".")
                .append(connector)
                .append(".")
                .append(StringUtils.capitalize(connector))
                .append("Endpoint");
        try {
            return (Endpoint) Class.forName(sb.toString()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(connector);
    }
}
