package cn.sinjinsong.server.resource;

import cn.sinjinsong.server.enumeration.ResponseStatus;
import cn.sinjinsong.server.exception.RequestParseException;
import cn.sinjinsong.server.exception.handler.ExceptionHandler;
import cn.sinjinsong.server.response.Response;
import cn.sinjinsong.server.util.MimeTypeUtil;
import cn.sinjinsong.server.util.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class ResourceHandler {
    private ExceptionHandler exceptionHandler;
    public ResourceHandler(ExceptionHandler exceptionHandler){
        this.exceptionHandler = exceptionHandler;
    }
    
    public void handle(String url, OutputStream os) {
        try {
            os.write(new Response.ResponseBuilder()
                    .header(ResponseStatus.OK, MimeTypeUtil.getTypes(url))
                    .body(IOUtil.getBytesFromFile(url))
                    .build().getBytes());
            os.flush();
            log.info("{}已写入输出流",url);
        } catch (IOException e1) {
            e1.printStackTrace();
            exceptionHandler.handle(new RequestParseException(ResponseStatus.BAD_REQUEST),os);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
