package part2_sockets;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author Pandelmark
 */
public class streamHandler {
    
    public static String receiveMessage(Socket sock){
        try{
            InputStream input = sock.getInputStream();
            BufferedReader residentLastname = new BufferedReader(new InputStreamReader(input)); //reads a line of text
            return residentLastname.readLine();
        }
        catch(IOException ex){
            System.err.println("Error receiving message: " + ex.getMessage());
            return null;
        }
    }
    
    public static void sendMessage(Socket sock, String msgSend){
        try{
            //System.out.println(sock);
            PrintWriter sendLastname = new PrintWriter(sock.getOutputStream(), true);
            sendLastname.println(msgSend);
        }
        catch(IOException ex){
            System.err.println("Error sending message: " + ex.getMessage());
        }
    }
}
