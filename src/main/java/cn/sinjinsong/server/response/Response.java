package cn.sinjinsong.server.response;

import cn.sinjinsong.server.enumeration.ResponseStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static cn.sinjinsong.server.constant.CharConstant.BLANK;
import static cn.sinjinsong.server.constant.CharConstant.CRLF;
import static cn.sinjinsong.server.constant.CharsetProperties.UTF_8_CHARSET;
import static cn.sinjinsong.server.constant.Context.DEFAULT_CONTENT_TYPE;

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
 * ＜body＞
 * ＜!-- body goes here --＞
 * ＜/body＞
 * ＜/html＞
 */
@Data
@Slf4j
public class Response {

    private byte[] bytes;

    public Response(byte[] bytes) {
        this.bytes = bytes;
    }

    public static class ResponseBuilder {
        
        private StringBuilder header;
        private byte[] body;

        public ResponseBuilder header(ResponseStatus status) {
            return header(status, DEFAULT_CONTENT_TYPE);
        }
        
        public ResponseBuilder header(ResponseStatus status, String contentType) {
            if (contentType == null) {
                contentType = DEFAULT_CONTENT_TYPE;
            }
            header = new StringBuilder();
            //HTTP/1.1 200 OK
            header.append("HTTP/1.1").append(BLANK).append(status.getCode()).append(BLANK).append(status).append(CRLF);
            //Date: Sat, 31 Dec 2005 23:59:59 GMT
            header.append("Date:").append(BLANK).append(new Date()).append(CRLF);
            header.append("Content-Type:").append(BLANK).append(contentType).append(CRLF);
            header.append("Content-Length:").append(BLANK);
            return this;
        }

        public ResponseBuilder body(byte[] body) {
            this.header.append(body.length).append(CRLF).append(CRLF);
            this.body = body;
            return this;
        }
        
        public Response build() {
            byte[] header = this.header.toString().getBytes(UTF_8_CHARSET);
            byte[] response = new byte[header.length + body.length];
            System.arraycopy(header, 0, response, 0, header.length);
            System.arraycopy(body, 0, response, header.length, body.length);
            return new Response(response);
        }
    }

}
