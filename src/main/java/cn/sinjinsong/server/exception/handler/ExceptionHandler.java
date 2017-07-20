package cn.sinjinsong.server.exception.handler;

import cn.sinjinsong.server.exception.base.BaseWebException;
import cn.sinjinsong.server.response.Response;
import cn.sinjinsong.server.util.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

import static cn.sinjinsong.server.constant.Context.ERROR_PAGE;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class ExceptionHandler {

    public void handle(BaseWebException e, OutputStream os) {
        try {
            os.write(new Response.ResponseBuilder()
                    .header(e.getStatus())
                    .body(IOUtil.getBytesFromFile(String.format(ERROR_PAGE, String.valueOf(e.getStatus().getCode()))))
                    .build()
                    .getBytes()
            );
            os.flush();
            log.info("错误消息已写入输出流");
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
