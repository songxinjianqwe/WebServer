package com.sinjinsong.webserver.core.network.endpoint;

import org.springframework.util.StringUtils;

/**
 * @author sinjinsong
 * @date 2018/5/4
 */
public abstract class Endpoint {
    public abstract void start(int port);

    public abstract void close();

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
