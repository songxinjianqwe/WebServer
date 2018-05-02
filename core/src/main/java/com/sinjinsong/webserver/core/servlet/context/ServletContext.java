package com.sinjinsong.webserver.core.servlet.context;

import com.sinjinsong.webserver.core.filter.Filter;
import com.sinjinsong.webserver.core.model.Cookie;
import com.sinjinsong.webserver.core.model.HTTPSession;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.servlet.base.HTTPServlet;
import com.sinjinsong.webserver.core.util.UUIDUtil;
import com.sinjinsong.webserver.core.util.XMLUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Data
@Slf4j
public class ServletContext {
    //别名->类名
    //一个Servlet类只能有一个Servlet别名，一个Servlet别名只能对应一个Servlet类
    private Map<String, HTTPServlet> servlets;
    
    //一个Servlet可以对应多个URL，一个URL只能对应一个Servlet
    //URL Pattern -> Servlet别名
    private Map<String, String> servletMapping;
    
    
    //
    private Map<String, Filter> filters;
    private Map<String, String> filterMapping;
    private Map<String, Object> attributes;
    private Map<String, HTTPSession> sessions;


    public ServletContext() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        init();
    }

    //由URL得到对应的Servlet类
    public HTTPServlet map(String url) {
        return servlets.get(servletMapping.get(url));
    }


    //从web.xml读到servlet映射
    public synchronized void init() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        this.servlets = new HashMap<>();
        this.servletMapping = new HashMap<>();
        this.attributes = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
        this.filters = new HashMap<>();
        this.filterMapping = new HashMap<>();
        parseConfig();
    }

    /**
     * 解析web.xml配置文件
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void parseConfig() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Document doc = XMLUtil.getDocument(ServletContext.class.getResourceAsStream("/web.xml"));
        Element root = doc.getRootElement();
        List<Element> servlets = root.elements("HTTPServlet");
        for (Element servletEle : servlets) {
            String key = servletEle.element("HTTPServlet-name").getText();
            String value = servletEle.element("HTTPServlet-class").getText();
            HTTPServlet HTTPServlet = (HTTPServlet) Class.forName(value).newInstance();
            this.servlets.put(key, HTTPServlet);
        }

        List<Element> servletMapping = root.elements("HTTPServlet-mapping");
        for (Element mapping : servletMapping) {
            String key = mapping.element("url-pattern").getText();
            String value = mapping.element("HTTPServlet-name").getText();
            this.servletMapping.put(key, value);
        }

        List<Element> filters = root.elements("filter");
        for (Element filterEle : filters) {
            String key = filterEle.element("filter-name").getText();
            String value = filterEle.element("filter-class").getText();
            Filter filter = (Filter) Class.forName(value).newInstance();
            this.filters.put(key, filter);
        }

        List<Element> filterMapping = root.elements("filter-mapping");
        for (Element mapping : filterMapping) {
            String key = mapping.element("url-pattern").getText();
            String value = mapping.element("filter-name").getText();
            this.filterMapping.put(key, value);
        }
    }


    public HTTPSession getSession(String JSESSIONID) {
        return sessions.get(JSESSIONID);
    }

    public synchronized HTTPSession createSession(Response response) {
        HTTPSession session = new HTTPSession(UUIDUtil.uuid());
        sessions.put(session.getId(), session);
        response.addCookie(new Cookie("JSESSIONID", session.getId()));
        return session;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

}
