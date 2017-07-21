package cn.sinjinsong.server.request.dispatcher.impl;

import cn.sinjinsong.server.constant.CharsetProperties;
import cn.sinjinsong.server.enumeration.HTTPStatus;
import cn.sinjinsong.server.exception.ResourceNotFoundException;
import cn.sinjinsong.server.exception.base.ServletException;
import cn.sinjinsong.server.request.Request;
import cn.sinjinsong.server.request.dispatcher.RequestDispatcher;
import cn.sinjinsong.server.resource.ResourceHandler;
import cn.sinjinsong.server.response.Response;
import cn.sinjinsong.server.util.IOUtil;
import cn.sinjinsong.server.util.MimeTypeUtil;
import cn.sinjinsong.server.template.TemplateResolver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/7/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequestDispatcher implements RequestDispatcher {
    private String url;
    
    @Override
    public void forward(Request request, Response response) throws ServletException, IOException {
        if (ResourceHandler.class.getResource(url) == null) {
            throw new ResourceNotFoundException();
        }
        String body = TemplateResolver.resolve(new String(IOUtil.getBytesFromFile(url), CharsetProperties.UTF_8_CHARSET),request);
        response.header(HTTPStatus.OK, MimeTypeUtil.getTypes(url)).body(body.getBytes(CharsetProperties.UTF_8_CHARSET));
    }
}
