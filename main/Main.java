package bhashaforge.main;

import bhashaforge.lexer.Lexer;
import bhashaforge.parser.Parser;
import bhashaforge.interpreter.Interpreter;
import bhashaforge.ast.Node;
import bhashaforge.storage.DatabaseStorage;
import bhashaforge.token.Token;
import bhashaforge.user.User;
import bhashaforge.user.UserDAO;
import bhashaforge.storage.DBConnection;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner      = new Scanner(System.in);
    private static DatabaseStorage db   = new DatabaseStorage();
    private static Interpreter interp   = new Interpreter();
    private static UserDAO userDAO      = new UserDAO();
    private static User currentUser     = null;
    private static String currentCode   = "";

    public static void main(String[] args) {
        printBanner();
        showLoginMenu();
        DBConnection.closeConnection();
    }

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║          BhashaForge             ║");
        System.out.println("║   Banglish Mini Compiler/IDE     ║");
        System.out.println("║     with Database Support!       ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println();
    }

    private static void showLoginMenu() {
        while (true) {
            System.out.println("┌─────────────────────────────┐");
            System.out.println("│        WELCOME MENU         │");
            System.out.println("├─────────────────────────────┤");
            System.out.println("│  1. Login                   │");
            System.out.println("│  2. Register                │");
            System.out.println("│  3. Exit                    │");
            System.out.println("└─────────────────────────────┘");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (login()) showMenu();
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    System.out.println("\nGoodbye!\n");
                    return;
                default:
                    System.out.println("Invalid option!\n");
            }
        }
    }

    private static void register() {
        System.out.println("\nRegister New Account");
        System.out.println("───────────────────────");

        System.out.print("Choose username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!\n");
            return;
        }

        if (userDAO.usernameExists(username)) {
            System.out.println("Username already taken!\n");
            return;
        }

        System.out.print("Choose password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty!\n");
            return;
        }

        boolean success = userDAO.register(username, password);
        if (success) {
            System.out.println("Account created! Please login.\n");
        } else {
            System.out.println("Registration failed!\n");
        }
    }

    private static boolean login() {
        System.out.println("\nLogin to BhashaForge");
        System.out.println("───────────────────────");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        int userId = userDAO.login(username, password);

        if (userId == -1) {
            System.out.println("Wrong username or password!\n");
            return false;
        }

        currentUser = new User(userId, username);
        System.out.println("Welcome back, " + currentUser.getUsername() + "!\n");
        return true;
    }

    private static void showMenu() {
        currentCode = "";
        interp.reset();

        while (true) {
            System.out.println("┌──────────────────────────────────┐");
            System.out.println("│    User: "+
                    padRight(currentUser.getUsername(), 23) +  "│");
            System.out.println("├──────────────────────────────────┤");
            System.out.println("│  1. Write Code                   │");
            System.out.println("│  2. Run Code                     │");
            System.out.println("│  3. View Current Code            │");
            System.out.println("│  4. Save Code to Database        │");
            System.out.println("│  5. View My Saved Codes          │");
            System.out.println("│  6. Delete a Saved Code          │");
            System.out.println("│  7. Show Variables               │");
            System.out.println("│  8. Clear Code                   │");
            System.out.println("│  9. Logout                       │");
            System.out.println("│  10. Exit                        │");
            System.out.println("└──────────────────────────────────┘");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":  writeCode();      break;
                case "2":  runCode();        break;
                case "3":  viewCurrentCode();break;
                case "4":  saveCode();       break;
                case "5":  viewSavedCodes(); break;
                case "6":  deleteCode();     break;
                case "7":  showVariables();  break;
                case "8":  clearCode();      break;
                case "9":
                    System.out.println("Logged out!\n");
                    return; // go back to login menu
                case "10":
                    System.out.println("\nThank you for using BhashaForge!");
                    DBConnection.closeConnection();
                    System.exit(0);
                default:
                    System.out.println("Invalid option!\n");
            }
        }
    }

    private static void writeCode() {
        System.out.println("\nWrite your BhashaForge code.");
        System.out.println("   'sesh' lekho shesh korte.");
        System.out.println("   'badh' lekho pichone jete.\n");
        System.out.println("── Keywords ────────────────────────");
        System.out.println("  dhoro x = 5         (variable)");
        System.out.println("  dekhao(x)            (print)");
        System.out.println("  jodi (x > 5) { }     (if)");
        System.out.println("  nahole { }           (else)");
        System.out.println("  jotokkhon (x < 10){ }(while)");
        System.out.println("────────────────────────────────────\n");

        StringBuilder code = new StringBuilder();

        while (true) {
            System.out.print(">>> ");
            String line = scanner.nextLine();

            if (line.trim().equalsIgnoreCase("sesh"))   break;
            if (line.trim().equalsIgnoreCase("badh")) {
                System.out.println("Badh dewa holo.\n");
                return;
            }
            code.append(line).append("\n");
        }

        if (code.toString().trim().isEmpty()) {
            System.out.println("No code written.\n");
            return;
        }

        currentCode = code.toString();
        System.out.println("Code ready! Choose '2' to run.\n");
    }

    private static void runCode() {
        if (currentCode.isEmpty()) {
            System.out.println("No code to run!\n");
            return;
        }

        System.out.println("\nRunning BhashaForge code...");
        System.out.println("────────────────────────────────");

        try {
            Lexer lexer        = new Lexer(currentCode);
            List<Token> tokens = lexer.tokenize();
            Parser parser      = new Parser(tokens);
            List<Node> ast     = parser.parseAll();
            interp.reset();
            interp.executeAll(ast);
            System.out.println("────────────────────────────────");
            System.out.println("Executed successfully!\n");
        } catch (RuntimeException e) {
            System.out.println("────────────────────────────────");
            System.out.println(e.getMessage());
            System.out.println("Execution stopped.\n");
        }
    }


    private static void viewCurrentCode() {
        if (currentCode.isEmpty()) {
            System.out.println("No code written yet.\n");
            return;
        }
        System.out.println("\nCurrent Code:");
        System.out.println("────────────────────────────────");
        System.out.println(currentCode);
        System.out.println("────────────────────────────────\n");
    }

    private static void saveCode() {
        if (currentCode.isEmpty()) {
            System.out.println("No code to save!\n");
            return;
        }

        System.out.print("Enter a title for this code: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) title = "Untitled";

        db.saveCode(
                currentUser.getUsername(),
                currentCode,
                title,
                currentUser.getId()
        );
        System.out.println();
    }

    private static void viewSavedCodes() {
        System.out.println("\nSaved codes for: " +
                currentUser.getUsername());
        System.out.println("────────────────────────────────");

        List<String> codes = db.loadCodes(currentUser.getUsername());

        if (codes.isEmpty()) {
            System.out.println("No saved codes yet.\n");
            return;
        }

        for (int i = 0; i < codes.size(); i++) {
            System.out.println("Code #" + (i + 1) + ":");
            System.out.println(codes.get(i));
            System.out.println("────────────────────────────────");
        }
        System.out.println();
    }

    private static void deleteCode() {
        List<String> codes  = db.loadCodes(currentUser.getUsername());
        List<Integer> ids   = db.getCodeIds(currentUser.getUsername());

        if (codes.isEmpty()) {
            System.out.println("No saved codes to delete!\n");
            return;
        }

        System.out.println("\nYour Saved Codes:");
        System.out.println("────────────────────────────────");
        for (int i = 0; i < codes.size(); i++) {
            System.out.println("#" + (i + 1) + " → " +
                    codes.get(i).split("\n")[0]);
        }
        System.out.println("────────────────────────────────");
        System.out.print("Enter number to delete (0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;

            if (choice < 1 || choice > ids.size()) {
                System.out.println("Invalid number!\n");
                return;
            }

            int codeId = ids.get(choice - 1);
            boolean deleted = db.deleteCode(currentUser.getId(), codeId);

            if (deleted) {
                System.out.println("Code deleted!\n");
            } else {
                System.out.println("Could not delete!\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number!\n");
        }
    }

    private static void showVariables() {
        System.out.println();
        interp.showVariables();
        System.out.println();
    }


    private static void clearCode() {
        currentCode = "";
        interp.reset();
        System.out.println("Cleared!\n");
    }
    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}