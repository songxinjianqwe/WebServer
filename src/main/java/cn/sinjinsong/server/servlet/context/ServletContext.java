package cn.sinjinsong.server.servlet.context;

import cn.sinjinsong.server.model.Cookie;
import cn.sinjinsong.server.model.HTTPSession;
import cn.sinjinsong.server.response.Response;
import cn.sinjinsong.server.servlet.base.HTTPServlet;
import cn.sinjinsong.server.util.UUIDUtil;
import cn.sinjinsong.server.util.XMLUtil;
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
    private Map<String, HTTPServlet> servlet;
    //一个Servlet可以对应多个URL，一个URL只能对应一个Servlet
    //URL->Servlet别名
    private Map<String, String> mapping;
    private Map<String, Object> attributes;
    private Map<String, HTTPSession> sessions;

    public ServletContext() {
        init();
    }

    //由URL得到对应的Servlet类
    public HTTPServlet dispatch(String url) {
        return servlet.get(mapping.get(url));
    }

    //从web.xml读到servlet映射
    public void init() {
        this.servlet = new HashMap<>();
        this.mapping = new HashMap<>();
        this.attributes = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
        Document doc = XMLUtil.getDocument(ServletContext.class.getResource("/WEB-INF/web.xml").getFile());
        Element root = doc.getRootElement();
        List<Element> servlets = root.elements("servlet");
        for (Element servlet : servlets) {
            String key = servlet.element("servlet-name").getText();
            String value = servlet.element("servlet-class").getText();
            HTTPServlet httpServlet = null;
            try {
                httpServlet = (HTTPServlet) Class.forName(value).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.servlet.put(key, httpServlet);
        }

        List<Element> mappings = root.elements("servlet-mapping");
        for (Element mapping : mappings) {
            String key = mapping.element("url-pattern").getText();
            String value = mapping.element("servlet-name").getText();
            this.mapping.put(key, value);
        }
    }

    public HTTPSession getSession(String JSESSIONID) {
        return sessions.get(JSESSIONID);
    }
    
    public HTTPSession createSession(Response response){
        HTTPSession session = new HTTPSession(UUIDUtil.uuid());
        sessions.put(session.getId(),session);
        response.addCookie(new Cookie("JSESSIONID",session.getId()));
        return session;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

}
