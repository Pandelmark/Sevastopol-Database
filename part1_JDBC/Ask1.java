/**
 *
 * @author Pandelmark
 * 
 * part 1: JDBC
 */
package part1_JDBC;

import config.DatabaseConfig; 
import config.getStuff;
import static config.inputMethods.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Ask1 {
    public static int start_menu(){
        Scanner logIn = new Scanner(System.in);
        int choice;
        
        System.out.println("+------------------- Start Menu -------------------+");
        System.out.println("Hello! This is the Sevastopol Database pre-entry Interface");
        System.out.println("1. Log-in");
        System.out.println("2. Exit");
        while(true){
            System.out.print("Choice: ");
            choice = logIn.nextInt();
            switch(choice) {
                case 1:
                    return choice;
                case 2:
                    System.exit(0);
                default:
                    System.out.println("Please enter a valid option.");
            }
        }
    }
    
    private static int main_menu(){
        Scanner input = new Scanner(System.in);
        int userChoice;
        System.out.println("+-------------- MAIN MENU --------------+");
        System.out.println("1. Retrieval of all documents.");
        System.out.println("2. Search by lastname");
        System.out.println("3. Search by stay time");
        System.out.println("4. Update registry");
        System.out.println("5. Delete registry");
        System.out.println("6. Delete everything (Not Advised) ");
        System.out.println("Any other number to exit the program");
        do{
            System.out.print("Choice: ");
            userChoice = input.nextInt();
            
            if(userChoice>=1 && userChoice<=7)
                return userChoice;
            else{
                System.out.println("Bye!");
                System.exit(0);
            }
                
        }while(true);
    }
    
    private static List<String> searchRecords(Connection conn, String lastname, String dbTable) {
        // Execute SELECT query to retrieve records based on 'lastname'
        List<String> searchResults = new ArrayList<>();
        // Execute your SELECT query and populate searchResults with record IDs
        // For simplicity, I assume a single column 'id' in the result set
        // Modify this part based on your table structure

        String query = "SELECT * FROM " + dbTable + " WHERE lastname = '" + lastname + "'";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, lastname);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                searchResults.add(rs.getString("id"));
                searchResults.add(rs.getString("firstname"));
                searchResults.add(rs.getString("lastname"));
                searchResults.add(rs.getString("department"));
                searchResults.add(rs.getString("stay_time"));
                searchResults.add(rs.getString("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResults;
    }
    
    private static String recordChoiceUpdate(List<String> records) {
        // Display the records and ask the user to choose one by ID
        System.out.println("Please choose one by ID.\nRegistries meeting input criteria: ");
        
        int i=0;
        for(String record : records) {
            System.out.printf(record + " | ");
            i++;
            if(i==6){
                i=0;
                System.out.println("");
            }
        }

        String chosenId = getUserInput("Enter the ID of the record you want to update (or press 'q' to cancel):");
        
        // Validate user input (check if the entered ID is in the list)
        System.out.println("chosenId: " + chosenId);
        if(records.contains(chosenId) || chosenId.isEmpty()) {
            return chosenId;
        } 
        else {
            System.out.println("Invalid ID. Update canceled.");
            return null;
        }
    }
    
        private static String recordChoiceDelete(List<String> records) {
        // Display the records and ask the user to choose one by ID
        System.out.println("Please choose one by ID.\nRegistries meeting input criteria:");
        
        int i=0;
        for(String record : records) {
            System.out.printf(record + " | ");
            i++;
            if(i==6){
                i=0;
                System.out.println("");
            }
        }

        String chosenId = getUserInput("Enter the ID of the record you want to delete (or press 'q' to cancel):");
        
        // Validate user input (check if the entered ID is in the list)
        System.out.println("chosenId: " + chosenId);
        if(records.contains(chosenId) || chosenId.isEmpty()) {
            return chosenId;
        } 
        else {
            System.out.println("Invalid ID. Deletion canceled.");
            return null;
        }
    }
    
    private static void passOrSkip(PreparedStatement preparedStatement, int parameterIndex, String value) throws SQLException {
        if (!value.isEmpty()) {
            preparedStatement.setString(parameterIndex, value);
        }
    }
    
    private static void executeQuery(Connection conn, String query) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                // Process the result
                System.out.printf("|%3s| %-10s| %-15s| %-8s| %-4s| %-4s| %-20s|\n",
                        rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
            }
            
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("SQL exception: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) throws SQLException {
        try{
            String dbTable = DatabaseConfig.TABLE_NAME;
            String dbUser = DatabaseConfig.DB_USER;
            String dbPass = DatabaseConfig.DB_PASS;
            String dbUrl = DatabaseConfig.DB_URL;
    
            //2. *Loading the appropriate Driver for the Netbeans project* (mariadb-java-client-3.2.0.jar)
            
            int choice = start_menu();
            
            //3. Create the connection to the DB using your dbname, dbusername and password
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            System.out.println("");
            System.out.println("Establishing Connection...");
            System.out.println("Welcome to the Sevastopol Database System!");
            System.out.printf("           Seegson Inc. All rights Reserved\n\n");
            choice = main_menu();
            
            //4. Create an SQL statement
            Statement st = conn.createStatement();
            Scanner search = new Scanner(System.in);
            
            switch(choice){
                case 1:
                    //5. Provide the SQL Query and execute the statement 
                    executeQuery(conn, "SELECT * FROM " + dbTable);
                    break;
                    
                case 2:
                    System.out.println("Enter a lastname to search for: ");
                    String lastname = search.nextLine();
                    executeQuery(conn, "SELECT * FROM residents WHERE lastname LIKE '" + lastname + "'");
                    break;
                    
                case 3:
                    System.out.printf("Enter stay time number of a person to search for: ");
                    String stayTime = search.nextLine();
                    executeQuery(conn, "SELECT * FROM residents WHERE stay_time LIKE '" + stayTime + "'");
                    break;
                    
                case 4:
                    //getStuff firstname = new getStuff();
                    // Collect user input for search criteria
                    String searchLastname = getUserInput("Enter the lastname to search for: ");
                    List<String> searchResults = searchRecords(conn, searchLastname, dbTable);
                    
                    // Check if any records match the search criteria
                    if(!searchResults.isEmpty()){
                        // Display search results and get user choice
                        String recordToUpdateId = recordChoiceUpdate(searchResults);
                        
                        //System.out.println("Record to update: " + recordToUpdateId);
                        
                        // If the user chose a record, proceed with updating
                        if(recordToUpdateId != null && !"q".equals(recordToUpdateId)){
                            // To avoid wrong values entering the table registries the program reassigns the previous ones to the ones
                            // not updated (pressed 'q' by the user).
                            String query = "SELECT firstname, lastname, department, stay_time, salary FROM " + dbTable + " WHERE id = " + recordToUpdateId;
                            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                                preparedStatement.setString(1, recordToUpdateId);

                                try (ResultSet rs = preparedStatement.executeQuery()) {
                                    if (rs.next()) {
                                        // Reading values from Query
                                        getStuff.firstname = rs.getString("firstname");
                                        getStuff.lastname = rs.getString("lastname");
                                        getStuff.department = rs.getString("department");
                                        getStuff.stayTime = rs.getString("stay_time");
                                        getStuff.salary = rs.getString("salary");
                                        /*
                                        System.out.println("Firstname: " + getStuff.firstname);
                                        System.out.println("Lastname: " + getStuff.lastname);
                                        System.out.println("Department: " + getStuff.department);
                                        System.out.println("Stay Time: " + getStuff.stayTime);
                                        System.out.println("Salary: " + getStuff.salary); */
                                    } 
                                    else {
                                        System.out.println("No entry with the given ID was found: " + recordToUpdateId);
                                    }
                                }
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                            String tmp;
                            String newFirstname;
                            String newLastname;
                            String newDepartment;
                            String newStayTime;
                            String newSalary;
                            
                            // Collect user input for new values
                            tmp = getUserInput("Enter new first-name (press 'q' to be unchanged): ");
                            //System.out.println(checkUserInput(tmp));
                            if(checkUserInput(tmp))
                                newFirstname = getStuff.firstname;
                            else
                                newFirstname = tmp;
                            
                            tmp = getUserInput("Enter new last-name (press 'q' to be unchanged): ");
                            //System.out.println(checkUserInput(tmp));
                            if(checkUserInput(tmp))
                                newLastname = getStuff.lastname;
                            else
                                newLastname = tmp;
                            
                            tmp = getUserInput("Enter new Department of work (press 'q' to be unchanged): ");
                            if(checkUserInput(tmp))
                                newDepartment = getStuff.department;
                            else
                                newDepartment = tmp;
                                    
                            tmp = getUserInput("Enter new number of stay time (press 'q' to be unchanged): ");
                            if(checkUserInput(tmp))
                                newStayTime = getStuff.stayTime;
                            else
                                newStayTime = tmp;
                                    
                            tmp = getUserInput("Enter new salary (press 'q' to be unchanged): ");
                            if(checkUserInput(tmp))
                                newSalary = getStuff.salary;
                            else
                                newSalary = tmp;
                                
                            // Prepare and execute the SQL UPDATE statement
                            String updateQuery = "UPDATE " + dbTable + 
                                    " SET firstname='" + newFirstname + 
                                    "', lastname='" + newLastname +
                                    "', department='" + newDepartment +
                                    "', stay_time='" + newStayTime +
                                    "', salary ='" + newSalary +
                                    "', created_at = CURRENT_TIMESTAMP WHERE id = " + recordToUpdateId;
                            
                            try(PreparedStatement pr = conn.prepareStatement(updateQuery)){
                                passOrSkip(pr, 1, newFirstname);
                                passOrSkip(pr, 2, newLastname);
                                passOrSkip(pr, 3, newDepartment);
                                passOrSkip(pr, 4, newStayTime);
                                passOrSkip(pr, 5, newSalary);
                                
                                int parametersCount = 5;
                                int rowsAffected = pr.executeUpdate();
                                
                                pr.setString(parametersCount+1, recordToUpdateId);
                                // Table update check and temrinal indication
                                if (rowsAffected > 0)
                                    System.out.println("Registry updated successfully!");
                                else
                                    System.out.println("No records matching the criteria were found.");
                                
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                            System.out.println("Update canceled by the user.");
                    }
                    else
                        System.out.println("No records matching the criteria were found.");
                    break;
                    
                case 5:
                    System.out.printf("Enter the last name of the person's regitry you wish to delete: ");
                    getStuff.lastname = search.nextLine();
                    List<String> searchResultsDel = searchRecords(conn, getStuff.lastname, dbTable);
                    
                    // Check if any records match the search criteria
                    if(!searchResultsDel.isEmpty()){
                        // Display search results and get user choice
                        String recordToDeleteId = recordChoiceDelete(searchResultsDel);
                        
                        if(recordToDeleteId != null && !"q".equals(recordToDeleteId)){
                            String deleteQuery = "DELETE FROM " + dbTable + " WHERE id = " + recordToDeleteId;
                            
                            try(PreparedStatement pr = conn.prepareStatement(deleteQuery)){
                                int parametersCount = 5;
                                int rowsAffected = pr.executeUpdate();
                                
                                pr.setString(parametersCount+1, recordToDeleteId);
                                // Table update check and temrinal indication
                                if (rowsAffected > 0)
                                    System.out.println("Registry deleted successfully!");
                                else
                                    System.out.println("No records matching the criteria were found.");
                                
                            }
                            catch (SQLException e){
                                e.printStackTrace();
                            }
                        }
                        else
                            System.out.println("Delete canceled by the user.");
                    }
                    else
                        System.out.println("No records matching the criteria were found.");
                    break;
                    
                case 6:
                    executeQuery(conn, "DELETE * FROM " + dbTable);
                    break;
                    
                default:
                    System.out.println("ERROR");
                    break;
            }
            st.close();
            conn.close();
            System.out.println("+------+");
            System.out.println("  Bye!");
            System.out.println("+------+");
            System.exit(0);           
        }
        catch(SQLException ex){
            System.out.println("--SQL exception--" + ex);
            ex.printStackTrace();
        }
    }    
}
