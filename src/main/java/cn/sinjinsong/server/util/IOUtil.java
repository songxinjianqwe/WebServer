package cn.sinjinsong.server.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class IOUtil {

    public static byte[] getBytesFromFile(String fileName) throws IOException {
        URL url = IOUtil.class.getResource(fileName);
        if (url == null) {
            log.info("file:{}",fileName);
            throw new FileNotFoundException();
        }
        log.info("正在读取文件:{}",url.getFile());
        return getBytesFromStream(new FileInputStream(url.getFile()));
    }

    public static byte[] getBytesFromStream(FileInputStream in) throws IOException {
        FileChannel channel = in.getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) channel.size());
        channel.read(buf);
        channel.close();
        in.close();
        return buf.array();
    }

   
}
