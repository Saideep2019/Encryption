package simpleDatabase;

import java.sql.SQLException;
import java.util.Scanner;
import simpleDatabase.Article; // Adjust this line according to your package structure

/**
 * Main class for the CSE360 database application. This class manages the
 * application's workflow, including user registration, login, and article
 * management. It handles different flows for administrators and regular users,
 * and provides options for creating, viewing, deleting, backing up, and 
 * restoring articles in a simple in-memory database.
 */

public class StartCSE360 {

    private static DatabaseHelper databaseHelper;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            databaseHelper = new DatabaseHelper();
            databaseHelper.connectToDatabase();  // Connect to the database

            // Check if the database is empty (no users registered)
            if (databaseHelper.isDatabaseEmpty()) {
                System.out.println("In-Memory Database is empty");
                setupAdministrator();
            } else {
                System.out.println("If you are an administrator, then select A\nIf you are an instructor then select I\nEnter your choice:  ");
                String role = scanner.nextLine();

                switch (role) {
                    case "I":
                        instructorFlow();
                        break;
                    case "A":
                        adminFlow();
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 'A' or 'I'");
                        databaseHelper.closeConnection();
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Good Bye!!");
            databaseHelper.closeConnection();
        }
    }

    private static void setupAdministrator() throws Exception {
        System.out.println("Setting up the Administrator access.");
        System.out.print("Enter Admin Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        databaseHelper.register(email, password, "admin");
        System.out.println("Administrator setup completed.");
    }

    private static void instructorFlow() throws Exception {
        System.out.println("Instructor Flow");
        System.out.print("What would you like to do?\n1. Register\n2. Login\n3. Manage Articles\nEnter your choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                registerInstructor();
                break;
            case "2":
                loginInstructor();
                break;
            case "3":
                articleFlow();
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                break;
        }
    }
    //Method to register user into the database
    private static void registerInstructor() throws Exception {
        System.out.print("Enter Instructor Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Instructor Password: ");
        String password = scanner.nextLine();
        // Check if user already exists in the database
        if (!databaseHelper.doesUserExist(email)) {
            databaseHelper.register(email, password, "Instructor");
            System.out.println("Instructor setup completed.");
        } else {
            System.out.println("Instructor already exists.");
        }
    }
    //Method to create login feature
    private static void loginInstructor() throws Exception {
        System.out.print("Enter Instructor Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Instructor Password: ");
        String password = scanner.nextLine();
        if (databaseHelper.login(email, password, "Instructor")) {
            System.out.println("Instructor login successful.");
            // You can add more user functionalities here if needed.
        } else {
            System.out.println("Invalid Instructor credentials. Try again!!");
        }
    }
    //Method to provide the UI in the console.
    private static void articleFlow() throws Exception {
        while (true) {
            System.out.println("Article Management Menu:");
            System.out.println("1. Create Article");
            System.out.println("2. View Articles");
            System.out.println("3. Delete Article");
            System.out.println("4. Backup Articles");
            System.out.println("5. Restore Articles");
            System.out.println("6. Exit Article Management");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createArticle();
                    break;
                case "2":
                    databaseHelper.displayArticles();
                    break;
                case "3":
                    deleteArticle();
                    break;
                case "4":
                    backupArticles(); // Call method for backing up articles
                    break;
                case "5":
                    restoreArticles(); // Call method for restoring articles
                    break;
                case "6":
                    return; // Exit article management
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    // Method to handle article backup
    private static void backupArticles() {
        System.out.print("Enter backup file path (e.g., C:\\Users\\Saideep\\Documents\\article_backup.txt): ");
        String backupFilePath = scanner.nextLine();
        try {
            databaseHelper.backupArticles(backupFilePath);
            System.out.println("Articles backed up successfully to: " + backupFilePath);
        } catch (Exception e) {
            System.out.println("Error backing up articles: " + e.getMessage());
        }
    }

    // Method to handle article restoration
    private static void restoreArticles() {
        System.out.print("Enter restore file path (e.g., C:\\Users\\Saideep\\Documents\\article_backup.txt): ");
        String restoreFilePath = scanner.nextLine();
        try {
            databaseHelper.restoreArticles(restoreFilePath);
            System.out.println("Articles restored successfully from: " + restoreFilePath);
        } catch (Exception e) {
            System.out.println("Error restoring articles: " + e.getMessage());
        }
    }
    //Method to create an article.
    private static void createArticle() throws Exception {
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Authors (comma separated): ");
        String authors = scanner.nextLine();
        System.out.print("Enter Abstract: ");
        String abstractText = scanner.nextLine();
        System.out.print("Enter Keywords (comma separated): ");
        String keywords = scanner.nextLine();
        System.out.print("Enter Body: ");
        String body = scanner.nextLine();
        System.out.print("Enter References (comma separated): ");
        String references = scanner.nextLine();

        
        
        
        databaseHelper.createArticle(title, authors, abstractText, keywords, body, references);
        System.out.println("Article created successfully.");
    }
    //Method to delete article
    private static void deleteArticle() throws Exception {
        System.out.print("Enter Article ID to delete: ");
        int articleId = Integer.parseInt(scanner.nextLine());

        // Confirm deletion
        System.out.print("Are you sure you want to delete article with ID " + articleId + "? (y/n): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            databaseHelper.deleteArticle(articleId);
            System.out.println("Article deleted successfully.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }
    //Method to create the view for the admin.
    private static void adminFlow() throws Exception {
        System.out.println("Admin Flow");
        System.out.print("Enter Admin Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        if (databaseHelper.login(email, password, "admin")) {
            System.out.println("Admin login successful.");
            databaseHelper.displayUsersByAdmin();
        } else {
            System.out.println("Invalid admin credentials. Try again!!");
        }
    }
}
