package cn.sinjinsong.server.util;

import org.junit.Test;

/**
 * Created by SinjinSong on 2017/7/20.
 */
public class MimeTypeUtilTest {
    @Test
    public void getMineTypes() throws Exception {
        MimeTypeUtil.getTypes("/static/img/cat.jpeg");
    }

}