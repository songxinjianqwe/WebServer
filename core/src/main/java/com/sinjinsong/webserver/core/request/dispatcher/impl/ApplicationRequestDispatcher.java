package com.sinjinsong.webserver.core.request.dispatcher.impl;

import com.sinjinsong.webserver.core.constant.CharsetProperties;
import com.sinjinsong.webserver.core.exception.ResourceNotFoundException;
import com.sinjinsong.webserver.core.exception.base.ServletException;
import com.sinjinsong.webserver.core.request.Request;
import com.sinjinsong.webserver.core.request.dispatcher.RequestDispatcher;
import com.sinjinsong.webserver.core.resource.ResourceHandler;
import com.sinjinsong.webserver.core.response.Response;
import com.sinjinsong.webserver.core.template.TemplateResolver;
import com.sinjinsong.webserver.core.util.IOUtil;
import com.sinjinsong.webserver.core.util.MimeTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 * 请求转发器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ApplicationRequestDispatcher implements RequestDispatcher {
    private String url;
    
    @Override
    public void forward(Request request, Response response) throws ServletException, IOException {
        if (ResourceHandler.class.getResource(url) == null) {
            throw new ResourceNotFoundException();
        }
        log.info("forward至 {} 页面",url);
        String body = TemplateResolver.resolve(new String(IOUtil.getBytesFromFile(url), CharsetProperties.UTF_8_CHARSET),request);
        response.setContentType(MimeTypeUtil.getTypes(url));
        response.setBody(body.getBytes(CharsetProperties.UTF_8_CHARSET));
    }
}
