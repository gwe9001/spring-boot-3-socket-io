package dev.jimo.springboot3socketio.socketio.listener;

import io.socket.engineio.server.Emitter;
import io.socket.socketio.server.SocketIoSocket;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.Arrays;

@Slf4j
public class MainListener implements Emitter.Listener  {

    @Override
    public void call(Object... args) {
        var socket = (SocketIoSocket) args[0];
        log.info("Client {} has connected.", socket.getId());

        socket.on("message", args1 -> {
            System.out.println("[Client " + socket.getId() + "] event " + "message");
            socket.send("message-back", args1);
        });
        socket.on("message-with-ack", args1 -> {
            System.out.println("[Client " + socket.getId() + "] event " + "message-with-ack");
            int length = args1.length;
            if (length > 0 && args1[length - 1] instanceof SocketIoSocket.ReceivedByLocalAcknowledgementCallback ack) {
                try {
                    Object[] array = Arrays.copyOf(args1, length - 1);
                    ack.sendAcknowledgement(array);
                } catch (Exception e) {
                    log.error("error: {}", e.getMessage(),e);
                }
            }
        });
        JSONObject authJsonObject = socket.getConnectData() == null ? new JSONObject() : (JSONObject) socket.getConnectData();
        try {
            socket.send("auth", authJsonObject);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(),e);
        }
    }
}
