import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Scanner;

public class BadCode {

    // Hardcoded credentials (Sensitive Data Exposure)
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "password123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Command Injection
        System.out.print("Enter a command to execute: ");
        String userCommand = scanner.nextLine();
        try {
            Runtime.getRuntime().exec(userCommand); // Vulnerable to command injection
        } catch (IOException e) {
            e.printStackTrace();
        }

        // SQL Injection
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users WHERE username = '" + username + "'"; // Vulnerable to SQL injection
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println("User found: " + resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insecure HTTP Request
        try {
            URL url = new URL("http://insecure-api.com/data"); // Insecure HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Insecure File Handling
        System.out.print("Enter the file path to read: ");
        String filePath = scanner.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { // No validation of file path
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}