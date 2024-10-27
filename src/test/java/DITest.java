import com.nwp.app.controller.TestController1;
import com.nwp.framework.discovery.DiscoveryEngine;
import com.nwp.framework.discovery.Route;
import com.nwp.server.ServerThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static org.mockito.Mockito.*;

public class DITest {

    private Socket socket;
    private BufferedReader reader;
    private DiscoveryEngine discoveryEngine;
    private ServerThread serverThread;

    @BeforeEach
    public void setUp() throws IOException {
        socket = mock(Socket.class);
        reader = mock(BufferedReader.class);
        discoveryEngine = mock(DiscoveryEngine.class);

        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream("GET /test11 HTTP/1.1\n\n".getBytes()));
        when(socket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        serverThread = new ServerThread(socket);
        serverThread = spy(serverThread);
    }

    @Test
    public void testGetRequest() throws Exception {
        when(reader.readLine()).thenReturn("GET /test11 HTTP/1.1", "");
        when(discoveryEngine.getRoutes()).thenReturn(List.of(new Route("/test11", "GET", TestController1.class, "testMethod11")));

        serverThread.run();
    }

}
