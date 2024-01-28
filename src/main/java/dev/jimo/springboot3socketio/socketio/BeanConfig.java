package dev.jimo.springboot3socketio.socketio;

import dev.jimo.springboot3socketio.socketio.listener.CostomListener;
import dev.jimo.springboot3socketio.socketio.listener.MainListener;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.socketio.server.SocketIoServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig extends MainListener {

    @Bean
    EngineIoServer engineIoServer() {
        var opt = EngineIoServerOptions.newFromDefault();
        opt.setCorsHandlingDisabled(true);
        opt.setPingInterval(300);
        opt.setPingTimeout(200);
        var eioServer = new EngineIoServer(opt);
        return eioServer;
    }

    @Bean
    SocketIoServer socketIoServer(EngineIoServer eioServer) {
        var sioServer = new SocketIoServer(eioServer);

        // https://socket.io/docs/v4/socket-io-protocol/#test-suite
        var namespace = sioServer.namespace("/");
        var custom = sioServer.namespace("/custom");

        namespace.on("connection", new MainListener());
        custom.on("connection", new CostomListener());
        return sioServer;
    }

}
