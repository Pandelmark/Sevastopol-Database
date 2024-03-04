/**
 *
 * @author Pandelmark
 * 
 * part 2: Playing with Sockets
 */
package part2_sockets;

import static part2_sockets.streamHandler.*;
import static config.inputMethods.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class SocketClient {
    
    private static int client_menu() throws InterruptedException{
        Scanner input = new Scanner(System.in);
        int userChoice;
        System.out.println("Establishing Conenction...");
        TimeUnit.SECONDS.sleep(1);
        
        System.out.println("+---------------- MAIN MENU ----------------+");
        System.out.println("--------------- SOCKET SERVER ---------------");
        System.out.println(" 1. Search by lastname");
        System.out.println(" 2. Insert a new Resident");
        System.out.println(" 3. Exit the program");
        
        do{
            System.out.print("Choice: ");
            userChoice = input.nextInt();
            
            switch (userChoice) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                default:
                    System.out.println("Please insert a valid value.");
                    break;
            }
        }while(true);
    }
    
    public static void main(String[] args) throws SQLException, IOException, InterruptedException{
        
        // Socket creation
        Socket sock = new Socket("localhost", 6969);
        
        // Establishing Connection with the Server
        InputStream input = sock.getInputStream();
        BufferedReader msgRcv = new BufferedReader(new InputStreamReader(input)); //reads line of text
        String line = msgRcv.readLine(); // In this part the Server welcomes the SocketClient with the corresponding message
        System.out.println(line);
        
        int choice = client_menu();
        String msgSend = Integer.toString(choice);
        sendMessage(sock,msgSend);
        
        String msg = null;
        switch(choice){
                case 1:
                    System.out.println("+---------------------------------------------+");
                    msg = getUserInput("Please insert a resident's last name: ");
                    System.out.println("+---------------------------------------------+");
                    if(msg != null){
                        // Sending lastname to Server
                        sendMessage(sock, msg);
                    }
                    else
                        System.err.println("Error no given lastname");
                    // Receive query result from server
                    msg = receiveMessage(sock);
                    System.out.println(msg);
                    break;
                case 2:
                    System.out.println("+---------------------------------------------+");
                    String firstname = getUserInput("Please resident's firstname: ");
                    String lastname = getUserInput("Please resident's lastname: ");
                    String department = getUserInput("Please resident's department of work: ");
                    String stay_time = getUserInput("Please resident's stay time: ");
                    String salary = getUserInput("Please resident's salary: ");
                    System.out.println("+---------------------------------------------+");
                    
                    sendMessage(sock, firstname);
                    sendMessage(sock, lastname);
                    sendMessage(sock, department);
                    sendMessage(sock, stay_time);
                    sendMessage(sock, salary);
                    
                    // Receive registry insertion update from the server
                    msg = receiveMessage(sock);
                    System.out.println(msg);
                    
                    // Message verification check
                    msg = receiveMessage(sock);
                    System.out.println(msg);
                    break;
                default:
                    break;
        }
             
        // Socket Client shutdown
        System.out.println("terminate connection..");
        sock.close();
        System.out.println("Connection closed");
    }
}