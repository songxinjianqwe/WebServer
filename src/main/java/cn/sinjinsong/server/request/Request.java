package cn.sinjinsong.server.request;

import cn.sinjinsong.server.constant.CharConstant;
import cn.sinjinsong.server.constant.CharsetProperties;
import cn.sinjinsong.server.enumeration.RequestMethod;
import cn.sinjinsong.server.enumeration.ResponseStatus;
import cn.sinjinsong.server.exception.RequestParseException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SinjinSong on 2017/7/20.
 * <p>
 * GET /search?hl=zh-CN&source=hp&q=domety&aq=f&oq= HTTP/1.1
 * Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint,
 * application/msword, application/x-silverlight
 * Referer: <a href="http://www.google.cn/">http://www.google.cn/</a>
 * Accept-Language: zh-cn
 * Accept-Encoding: gzip, deflate
 * User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; TheWorld)
 * Host: <a href="http://www.google.cn">www.google.cn</a>
 * Connection: Keep-Alive
 * Cookie: PREF=ID=80a06da87be9ae3c:U=f7167333e2c3b714:NW=1:TM=1261551909:LM=1261551917:S=ybYcq2wpfefs4V9g;
 * NID=31=ojj8d-IygaEtSxLgaJmqSjVhCspkviJrB6omjamNrSm8lZhKy_yMfO2M4QMRKcH1g0iQv9u-2hfBW7bUFwVh7pGaRUb0RnHcJU37y-
 * FxlRugatx63JLv7CWMD6UB_O_r
 * <p>
 * <p>
 * <p>
 * POST /search HTTP/1.1
 * Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint,
 * application/msword, application/x-silverlight
 * Referer: <a href="http://www.google.cn/">http://www.google.cn/</a>
 * Accept-Language: zh-cn
 * Accept-Encoding: gzip,deflate
 * User-Agent: Mozilla/4.0(compatible;MSIE6.0;Windows NT5.1;SV1;.NET CLR2.0.50727;TheWorld)
 * Host: <a href="http://www.google.cn">www.google.cn</a>
 * Connection: Keep-Alive
 * Cookie: PREF=ID=80a06da87be9ae3c:U=f7167333e2c3b714:NW=1:TM=1261551909:LM=1261551917:S=ybYcq2wpfefs4V9g;
 * NID=31=ojj8d-IygaEtSxLgaJmqSjVhCspkviJrB6omjamNrSm8lZhKy_yMfO2M4QMRKcH1g0iQv9u-2hfBW7bUFwVh7pGaRUb0RnHcJU37y-
 * FxlRugatx63JLv7CWMD6UB_O_r
 * <p>
 * hl=zh-CN&source=hp&q=domety
 */

@Data
@Slf4j
public class Request {
    private RequestMethod method;
    private String url;
    private Map<String, List<String>> params;
    private Map<String, List<String>> headers;

    /**
     * 读取请求体只能使用字节流，使用字符流读不到
     *
     * @param in
     * @throws RequestParseException
     */
    public void build(InputStream in) throws RequestParseException {
        log.info("开始读取Request");
        BufferedInputStream bin = new BufferedInputStream(in);
        byte[] buf = null;
        try {
            buf = new byte[bin.available()];
            int len = bin.read(buf);
            if (len <= 0) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lines = new String(buf, CharsetProperties.UTF_8_CHARSET).split(CharConstant.CRLF);
        log.info("Request读取完毕");
        log.info("{}", Arrays.toString(lines));
        try {
            parseHeaders(lines);
            if (headers.containsKey("Content-Length") && !headers.get("Content-Length").get(0).equals("0")) {
                parseBody(lines[lines.length - 1]);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RequestParseException(ResponseStatus.BAD_REQUEST);
        }
    }

    private void parseHeaders(String[] lines) {
        log.info("解析请求头");
        String firstLine = lines[0];
        //解析方法
        String[] firstLineSlices = firstLine.split(CharConstant.BLANK);
        this.method = RequestMethod.valueOf(firstLineSlices[0]);
        log.debug("method:{}", this.method);

        //解析URL
        String rawURL = firstLineSlices[1];
        String[] urlSlices = rawURL.split("\\?");
        this.url = urlSlices[0];
        log.debug("url:{}", this.url);

        //解析URL参数
        if (urlSlices.length > 1) {
            parseParams(urlSlices[1]);
        }
        log.debug("params:{}", this.params);

        //解析请求头
        String header;
        this.headers = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            header = lines[i];
            if (header.equals("")) {
                break;
            }
            int colonIndex = header.indexOf(':');
            String key = header.substring(0, colonIndex);
            String[] values = header.substring(colonIndex + 2).split(",");
            headers.put(key, Arrays.asList(values));
        }
        log.debug("headers:{}", this.headers);
    }

    private void parseBody(String body) {
        log.info("解析请求体");
        parseParams(body);
        if (this.params == null) {
            this.params = new HashMap<>();
        }
    }

    private void parseParams(String params) {
        String[] urlParams = params.split("&");
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        for (String param : urlParams) {
            String[] kv = param.split("=");
            String key = kv[0];
            String[] values = kv[1].split(",");
            this.params.put(key, Arrays.asList(values));
        }
    }
}
