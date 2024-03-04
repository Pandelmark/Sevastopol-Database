/**
 *
 * @author Pandelmark
 * 
 * part 2: Playing with Sockets
 */
package part2_sockets;

import static part2_sockets.streamHandler.*;
import config.DatabaseConfig;

import java.io.*;
import java.sql.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    
    private static boolean executeQuery(Connection conn, String query, Socket sock) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            StringBuilder resultBuilder = new StringBuilder();
            while (rs.next()) {
                // Process the result and append it to the StringBuilder
            resultBuilder.append("|")
                    .append(rs.getString(1)).append("|")
                    .append(rs.getString(2)).append("|")
                    .append(rs.getString(3)).append("|")
                    .append(rs.getString(4)).append("|")
                    .append(rs.getString(5)).append("|")
                    .append(rs.getString(6)).append("|")
                    .append(rs.getString(7)).append("|")
                    .append("\n");
            }
            
            //System.out.println(resultBuilder.toString());
            
            // In case of a SELECT statement send the results back to the client
            sendMessage(sock, resultBuilder.toString());

            rs.close();
            statement.close();
            return true;
        }
        catch (SQLException ex) {
            System.out.println("SQL exception: " + ex.getMessage());
            return false;
            }
    }
    

    public static void main(String[] args) throws SQLException, IOException {
        String dbTable = DatabaseConfig.TABLE_NAME;
        String dbUser = DatabaseConfig.DB_USER;
        String dbPass = DatabaseConfig.DB_PASS;
        String dbUrl = DatabaseConfig.DB_URL;
        
        // Database Connection
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            
        // Server socket creation
        ServerSocket serverSock = new ServerSocket(6969);
        
        while(true){
            System.out.println("Server Running... ");
            
            // Server listening for Clients
            Socket sock = serverSock.accept();
            
            System.out.println("Client accepted");
            System.out.println("Local Socket: " + sock.getLocalSocketAddress());
            System.out.println("Remote Socket: " + sock.getLocalSocketAddress());
                        
            PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
            writer.println("Welcome to the Sevastopol Socket Server!");
            
            int choice = Integer.parseInt(receiveMessage(sock));
            System.out.println("Client's choice: " + choice);
            
            switch(choice){
                case 1:
                    // Data received from Client (lastname)
                    String inputLastname = receiveMessage(sock);
                    executeQuery(conn, "SELECT * FROM " + dbTable + " WHERE lastname LIKE '" + inputLastname + "'", sock);
                    // Τώρα πρέπει να τυπωθούν τα αποτελέσματα του Query στον Client.
                    break;
                case 2:
                    String firstname = receiveMessage(sock);
                    String lastname = receiveMessage(sock);
                    String department = receiveMessage(sock);
                    String stay_time = receiveMessage(sock);
                    String salary = receiveMessage(sock);
                    
                    //System.out.println(firstname + " | " + lastname + " | " + department + " | " + stay_time + " | " + salary);
                    System.out.println( "INSERT INTO " + dbTable + " (firstname, lastname, department, stay_time, salary) VALUES ('" + firstname + "', '" + lastname + "', '" + department + "'," + stay_time + "," + salary + ")");
                    
                    if(executeQuery(conn, "INSERT INTO " + dbTable + " (firstname, lastname, department, stay_time, salary) VALUES ('" + 
                            firstname + "', '" +
                            lastname + "', '" +
                            department + "'," +
                            stay_time + "," +
                            salary + ")", sock)){
                        sendMessage(sock, "Resident registry has been successfully inserted.");
                    }
                    else
                        sendMessage(sock, "Registry insertion was unsuccesful.");
                    break;
                    
                case 3:
                    break;
                default:
                    // Socket termination
                    sock.close();
                    System.out.println("Bye!");
                    System.exit(0);
            }
                        
            // Socket termination
            sock.close();
        }
        //serverSock.close();
    }
}
