package com.nwp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static com.nwp.config.Config.TCP_PORT;

@SuppressWarnings("InfiniteLoopStatement")
public class Server {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("Server is running at http://localhost:" + TCP_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    try {
                        new ServerThread(socket).run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}