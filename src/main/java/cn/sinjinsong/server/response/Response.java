package cn.sinjinsong.server.response;

import cn.sinjinsong.server.constant.CharsetProperties;
import cn.sinjinsong.server.enumeration.ResponseStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static cn.sinjinsong.server.constant.CharConstant.BLANK;
import static cn.sinjinsong.server.constant.CharConstant.CRLF;

/**
 * Created by SinjinSong on 2017/7/20.
 * <p>
 * HTTP/1.1 200 OK
 * Date: Sat, 31 Dec 2005 23:59:59 GMT
 * Content-Type: text/html;constant=ISO-8859-1
 * Content-Length: 122
 * <p>
 * ＜html＞
 * ＜head＞
 * ＜title＞Wrox Homepage＜/title＞
 * ＜/head＞
 * ＜body＞
 * ＜!-- body goes here --＞
 * ＜/body＞
 * ＜/html＞
 */
@Data
@Slf4j
public class Response {
    private String response;
    
    public Response(String response) {
        this.response = response;
    }
        
    public static class ResponseBuilder {

        
        private StringBuilder header;
        private StringBuilder body;
        
        public ResponseBuilder header(ResponseStatus status) {
            header = new StringBuilder();
            //HTTP/1.1 200 OK
            header.append("HTTP/1.1").append(BLANK).append(status.getCode()).append(BLANK).append(status).append(CRLF);
            //Date: Sat, 31 Dec 2005 23:59:59 GMT
            header.append("Date:").append(BLANK).append(new Date()).append(CRLF);
            header.append("Content-Type:").append(BLANK).append("text/html;constant=UTF-8").append(CRLF);
            header.append("Content-Length:").append(BLANK).append(CRLF);
            return this;
        }

        public ResponseBuilder body(String body) {
            this.header.append(body.getBytes(CharsetProperties.charset).length);
            this.body = new StringBuilder(body);
            return this;
        }

        public Response build() {
            return new Response(new StringBuilder().append(header).append(CRLF).append(body).toString());
        }
    }

}
