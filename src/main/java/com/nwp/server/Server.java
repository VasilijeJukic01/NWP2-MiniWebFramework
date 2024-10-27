package com.nwp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static com.nwp.config.Config.*;

/**
 * The main server class that listens for incoming connections.
 */
@SuppressWarnings("InfiniteLoopStatement")
public class Server {

    public static void main(String[] args) {
        printBanner();
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

    private static void printBanner() {
        System.out.println(COL_BRIGHT_CYAN + " __      __    "+ COL_DEFAULT +"       "+ COL_BRIGHT_PURPLE +"__________                      __    " + COL_DEFAULT);
        System.out.println(COL_BRIGHT_CYAN + "/  \\    /  \\ "+ COL_DEFAULT +"         "+ COL_BRIGHT_PURPLE +"\\______   \\   ____     ____   _/  |_  " + COL_DEFAULT);
        System.out.println(COL_BRIGHT_CYAN + "\\   \\/\\/   /"+ COL_DEFAULT +"   ______"+ COL_BRIGHT_PURPLE +"  |    |  _/  /  _ \\   /  _ \\  \\   __\\ " + COL_DEFAULT);
        System.out.println(COL_BRIGHT_CYAN + " \\        /   "+ COL_DEFAULT +"/_____/  "+ COL_BRIGHT_PURPLE +"|    |   \\ (  <_> ) (  <_> )  |  |   " + COL_DEFAULT);
        System.out.println(COL_BRIGHT_CYAN + "  \\__/\\  /   "+ COL_DEFAULT +"         "+ COL_BRIGHT_PURPLE +" |______  /  \\____/   \\____/   |__|   " + COL_DEFAULT);
        System.out.println(COL_BRIGHT_CYAN + "       \\/     "+ COL_DEFAULT +"         "+ COL_BRIGHT_PURPLE +"       \\/                           " + COL_DEFAULT);
    }
}