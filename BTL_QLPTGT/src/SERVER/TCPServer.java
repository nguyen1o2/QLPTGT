package SERVER;
import btl_qlptgt.util.CSDL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mindrot.jbcrypt.BCrypt;
public class TCPServer {
    private static final int PORT = 3456;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
    try {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            // Create a new thread to handle the client's request
            Runnable clientHandler = new ClientHandler(clientSocket);

            // Execute the thread in the thread pool
            executorService.execute(clientHandler);

            // Get the client information
            String clientName = ((ClientHandler) clientHandler).getClientName();
            String ipAddress = ((ClientHandler) clientHandler).getIpAddress();
            int port = ((ClientHandler) clientHandler).getPort();
            String time = ((ClientHandler) clientHandler).getTime();
            String status = ((ClientHandler) clientHandler).getStatus();status = "RUNNING";
                
            // Display the client information
            System.out.println("Client Name: " + clientName);
            System.out.println("IP Address: " + ipAddress);
            System.out.println("Port: " + port);
            System.out.println("Time: " + time);
            System.out.println("Status: " + status);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String clientName;
    private final String ipAddress;
    private final int port;
    private final String time;
    private String status;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientName = "Client"; // Set the client name based on your requirements
        this.ipAddress = clientSocket.getInetAddress().getHostAddress();
        this.port = clientSocket.getPort();
        this.time = getCurrentTime(); // Set the time based on your requirements
    }

    public String getClientName() {
        return clientName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void run() {
        try {
            // Get the input and output streams for reading from and writing to the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read the username and password from the client
            String username = in.readLine();
            String password = in.readLine();

            // Perform the necessary authentication logic and set the status
            if (isValidCredentials(username, password)) {
                status = "RUNNING";
                out.println("RUNNING");
            } else {
                status = "FAILURE";
                out.println("FAILURE");
            }

            // Close the client socket
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isValidCredentials(String username, String password) {
    // SQL query to retrieve the hashed password from the user table
    String sql = "SELECT password FROM Account WHERE username = ?";

    try {
        Connection conn = CSDL.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                // Compare the provided password with the hashed password stored in the database
                return BCrypt.checkpw(password, hashedPassword);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
    }
    private String getCurrentTime() {
        // Return the current time in a desired format
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentTime.format(formatter);
    }
}
