package config;

import java.util.Scanner;

/**
 *
 * @author Pandelmark
 */
public class inputMethods {
    public static String getUserInput(String prompt) {
    System.out.println("+---------------------------------------------+");
    System.out.print(prompt);
    Scanner scanner = new Scanner(System.in);
    return scanner.nextLine();
    } 
    
    public static boolean checkUserInput(String var){
        if("q".equals(var))
            return true;
        return false;
    }
}
