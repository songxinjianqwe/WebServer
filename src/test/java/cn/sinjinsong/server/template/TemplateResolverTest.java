package cn.sinjinsong.server.template;

import cn.sinjinsong.server.constant.CharsetProperties;
import cn.sinjinsong.server.util.IOUtil;
import org.junit.Test;

/**
 * Created by SinjinSong on 2017/7/21.
 */
public class TemplateResolverTest {
    @Test
    public void resolve() throws Exception {
        byte[] rawBody = IOUtil.getBytesFromFile("/views/success.html");
        String body = new String(rawBody, CharsetProperties.UTF_8_CHARSET);
        TemplateResolver.resolve(body,null);
    }

}