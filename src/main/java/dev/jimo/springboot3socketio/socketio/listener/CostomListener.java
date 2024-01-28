package dev.jimo.springboot3socketio.socketio.listener;

import io.socket.engineio.server.Emitter;
import io.socket.socketio.server.SocketIoSocket;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class CostomListener implements Emitter.Listener  {

    @Override
    public void call(Object... args) {
        var socket = (SocketIoSocket) args[0];
        log.info("Client {} has connected.", socket.getId());
        JSONObject authJsonObject = socket.getConnectData() == null ? new JSONObject() : (JSONObject) socket.getConnectData();

        try {
            socket.send("auth", authJsonObject);
        }catch (Exception e) {
            log.error("error: {}", e.getMessage(),e);
        }
    }
}
