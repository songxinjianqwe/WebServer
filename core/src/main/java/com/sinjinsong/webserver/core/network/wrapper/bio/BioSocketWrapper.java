package com.sinjinsong.webserver.core.network.wrapper.bio;

import com.sinjinsong.webserver.core.network.wrapper.SocketWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * @author sinjinsong
 * @date 2018/5/4
 */
@Slf4j
@Getter
public class BioSocketWrapper implements SocketWrapper {
    private Socket socket;
    public BioSocketWrapper(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void close() throws IOException {
        socket.close();
    }

}
