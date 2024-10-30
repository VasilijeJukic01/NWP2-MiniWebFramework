package com.nwp.server;

import com.nwp.framework.di.DIEngine;
import com.nwp.framework.discovery.DiscoveryEngine;
import com.nwp.framework.request.Header;
import com.nwp.framework.request.Helper;
import com.nwp.framework.request.Request;
import com.nwp.framework.request.Method;
import com.nwp.framework.request.exceptions.RequestNotValidException;
import com.nwp.framework.response.JsonResponse;
import com.nwp.framework.response.Response;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private final DiscoveryEngine discoveryEngine;
    private final DIEngine diEngine;

    public ServerThread(Socket socket) throws IOException {
        this.discoveryEngine = DiscoveryEngine.getInstance();
        this.diEngine = DIEngine.getInstance();
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    @Override
    public void run() {
        try {
            // Read the request and process it
            generateRequest().ifPresentOrElse(req -> {
                discoveryEngine.getRoutes().stream()
                        .filter(route -> route.getRoute().equalsIgnoreCase(req.getLocation()) && route.getMethod().equalsIgnoreCase(req.getMethod().toString()))
                        .findFirst()
                        .ifPresent(route -> {
                            try {
                                diEngine.initialize(route.getController().getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                processRequest(req);
            }, this::closeConnection);

        } catch (IOException | RequestNotValidException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void processRequest(Request request) {
        Map<String, Object> responseMap = Map.of(
                "route_location", request.getLocation(),
                "route_method", request.getMethod().toString(),
                "parameters", request.getParameters()
        );
        Response response = new JsonResponse(responseMap);
        writer.println(response.render());
    }

    private Optional<Request> generateRequest() throws IOException, RequestNotValidException {
        return Optional.ofNullable(reader.readLine())
                .map(this::parseActionRow)
                .flatMap(this::parseRequestDetails);
    }

    private Optional<Request> parseRequestDetails(ActionRow actionRow) {
        try {
            Header header = readHeaders();
            Map<String, String> parameters = Helper.getParametersFromRoute(actionRow.route);
            if (actionRow.method.equals(Method.POST)) {
                parameters.putAll(readPostParameters(header));
            }
            return Optional.of(new Request(actionRow.method, actionRow.route, header, parameters));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Header readHeaders() throws IOException {
        Header header = new Header();
        reader.lines()
                .takeWhile(line -> !line.trim().isEmpty())
                .map(line -> line.split(": ", 2))
                .filter(headerRow -> headerRow.length == 2)
                .forEach(headerRow -> header.add(headerRow[0], headerRow[1]));
        return header;
    }

    private Map<String, String> readPostParameters(Header header) throws IOException {
        int contentLength = Integer.parseInt(header.get("Content-Length"));
        char[] buff = new char[contentLength];
        reader.read(buff, 0, contentLength);
        String parametersString = new String(buff);
        return Helper.getParametersFromString(parametersString);
    }

    // Helpers
    private void closeResources() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        closeResources();
    }

    private ActionRow parseActionRow(String command) {
        String[] actionRow = command.split(" ");
        Method method = Method.valueOf(actionRow[0]);
        String route = actionRow[1];
        return new ActionRow(method, route);
    }

    private record ActionRow(Method method, String route) { }
}
