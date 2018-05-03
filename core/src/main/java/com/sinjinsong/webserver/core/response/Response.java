package com.sinjinsong.webserver.core.response;

import com.sinjinsong.webserver.core.enumeration.HTTPStatus;
import com.sinjinsong.webserver.core.cookie.Cookie;
import com.sinjinsong.webserver.core.servlet.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sinjinsong.webserver.core.constant.CharConstant.BLANK;
import static com.sinjinsong.webserver.core.constant.CharConstant.CRLF;
import static com.sinjinsong.webserver.core.constant.CharsetProperties.UTF_8_CHARSET;
import static com.sinjinsong.webserver.core.constant.ContextConstant.DEFAULT_CONTENT_TYPE;

/**
 * Created by SinjinSong on 2017/7/20.
 * HTTP/1.1 200 OK
 * Date: Sat, 31 Dec 2005 23:59:59 GMT
 * Content-Type: text/html;constant=ISO-8859-1
 * Content-Length: 122
 * <p>
 * ＜html＞
 * ＜head＞
 * ＜title＞Wrox Homepage＜/title＞
 * ＜/head＞
 * ＜buildBody＞
 * ＜!-- buildBody goes here --＞
 * ＜/buildBody＞
 * ＜/html＞
 */
@Slf4j
public class Response {
    private StringBuilder headerAppender;
    private List<Cookie> cookies;
    private List<Header> headers;
    private HTTPStatus status = HTTPStatus.OK;
    private String contentType = DEFAULT_CONTENT_TYPE;
    private byte[] body = new byte[0];
    private SocketChannel socketChannel;
    private RequestHandler requestHandler;
    
    public Response(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.headerAppender = new StringBuilder();
        this.cookies = new ArrayList<>();
        this.headers = new ArrayList<>();
    }


    public void setStatus(HTTPStatus status) {
        this.status = status;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

    private Response buildHeader() {
        //HTTP/1.1 200 OK
        headerAppender.append("HTTP/1.1").append(BLANK).append(status.getCode()).append(BLANK).append(status).append(CRLF);
        //Date: Sat, 31 Dec 2005 23:59:59 GMT
        headerAppender.append("Date:").append(BLANK).append(new Date()).append(CRLF);
        headerAppender.append("Content-Type:").append(BLANK).append(contentType).append(CRLF);
        if (headers != null) {
            for (Header header : headers) {
                headerAppender.append(header.getKey()).append(":").append(BLANK).append(header.getValue()).append(CRLF);
            }
        }
        if (cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                headerAppender.append("Set-Cookie:").append(BLANK).append(cookie.getKey()).append("=").append(cookie.getValue()).append(CRLF);
            }
        }
        headerAppender.append("Content-Length:").append(BLANK);
        return this;
    }

    //一次性传入响应体
    public Response buildBody() {
        this.headerAppender.append(body.length).append(CRLF).append(CRLF);
        return this;
    }

    public void writeToClient() {
        //默认返回OK
        buildHeader();
        buildBody();
        byte[] header = this.headerAppender.toString().getBytes(UTF_8_CHARSET);
        ByteBuffer[] response = {ByteBuffer.wrap(header), ByteBuffer.wrap(body)};
        try {
            socketChannel.write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void sendRedirect(String url) {
        log.info("重定向至{}", url);
        addHeader(new Header("Location", url));
        setStatus(HTTPStatus.MOVED_TEMPORARILY);
        buildHeader();
        buildBody();
        requestHandler.finishRequest();
    }
}
