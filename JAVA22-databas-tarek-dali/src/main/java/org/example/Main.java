package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static int loggedInID;
    private static int loggedInPersonalNumber;
    private static ArrayList<Integer> loggedInAccountsIDsArr = new ArrayList<>();
    private static ArrayList<Integer> receiverAccountsIDsArr = new ArrayList<>();
    private static ArrayList<Integer> allUsersIDsExceptLoggedInArr = new ArrayList<>();

    static int getLoggedInID() {
        return loggedInID;
    }

    static void setLoggedInID(int newID) {
        loggedInID = newID;
    }

    static int getLoggedInPersonalNumber() {
        return loggedInPersonalNumber;
    }

    static void setLoggedInPersonalNumber(int newPersonalNumber) {
        loggedInPersonalNumber = newPersonalNumber;
    }

    static ArrayList<Integer> getLoggedInAccountsIDsArr() {
        return loggedInAccountsIDsArr;
    }

    static void setLoggedInAccountsIDsArr(int id) {
        loggedInAccountsIDsArr.add(id);
    }

    static void setToEmptyLoggedInAccountsIDsArr() {
        loggedInAccountsIDsArr.clear();
    }

    static ArrayList<Integer> getReceiverAccountsIDsArr() {
        return receiverAccountsIDsArr;
    }

    static void setReceiverAccountsIDsArr(int id) {
        receiverAccountsIDsArr.add(id);
    }

    static void setEmptyReceiverAccountsIDsArr() {
        receiverAccountsIDsArr.clear();
    }

    static ArrayList<Integer> getAllUsersIDsExceptLoggedInArr() {
        return allUsersIDsExceptLoggedInArr;
    }

    static void setAllUsersIDsExceptLoggedInArr(int id) {
        allUsersIDsExceptLoggedInArr.add(id);
    }

    static Scanner scan;
    static MysqlDataSource dataSource;
    static String url = "localhost";
    static int port = 3306;
    static String database = "finalProjectDatabase";
    static String username = "root";
    static String password = "";

    public static void main(String[] args) throws SQLException {

        InitializeDatabase();
        CreateUsersTable();
        CreateAccountsTable();
        CreateTransactionsTable();
        scan = new Scanner(System.in);

        boolean menu1 = true;
        boolean menu2;
        // Two menus, first one before logging in while the second is after logging in
        while (menu1) {
            System.out.println("Välj vad du vill göra.");
            System.out.println("1. Skapa user");
            System.out.println("2. Logga in");
            System.out.println("3. Avsluta");


            switch (scan.nextLine().trim()) {
                case "1":
                    CreateUser();
                    break;

                case "2":
                    System.out.println("1. Ange personnummer");
                    int personalNumber = scan.nextInt();
                    scan.nextLine();
                    System.out.println("2. Ange lösenord");
                    String password = scan.nextLine().trim();

                    if (CheckCredentials(personalNumber, password)) {
                        menu2 = true;
                        while (menu2) {
                            System.out.println("\nVälj vad du vill göra.");
                            System.out.println("1. Skapa konto");
                            System.out.println("2. Swosha någon");
                            System.out.println("3. Visa alla transaktioner på ett konto");
                            System.out.println("4. Visa dina uppgifter och konto");
                            System.out.println("5. Uppdatera dina uppgifter");
                            System.out.println("6. Ta bort konto");
                            System.out.println("7. Ta bort user");
                            System.out.println("8. Logga ut");

                            switch (scan.nextLine().trim()) {
                                case "1":
                                    CreateAccount();
                                    break;

                                case "2":
                                    MakeTransaction();
                                    scan.nextLine();
                                    break;

                                case "3":
                                    ShowAccountAndTransactions();
                                    break;

                                case "4":
                                    ShowLoggedUserInfo();
                                    ShowLoggedInAccounts();
                                    break;

                                case "5":
                                    UpdateUserInfo();
                                    break;

                                case "6":
                                    DeleteAccount();
                                    break;

                                case "7":
                                    DeleteUser();
                                    menu2 = false;
                                    break;

                                case "8":
                                    menu2 = false;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("\nPersonnummer eller lösenord var felaktig\n");
                    }
                    break;

                case "3":
                    menu1 = false;
                    break;

                default:
                    break;
            }
        }
    }


    // Configures connections to the database
    public static void InitializeDatabase() {
        try {
            dataSource = new MysqlDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
            dataSource.setUseSSL(false);
        } catch (SQLException e) {
            System.out.println("failed!");
            PrintSQLException(e);
            System.exit(0);
        }
    }

    // Creates a temporary connection to the database
    public static Connection GetConnection() {
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            PrintSQLException(e);
            System.out.println("Connection failed");
            System.exit(0);
            return null;
        }
    }

    public static void PrintSQLException(SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }

    // Creates users table if it does not exist
    public static void CreateUsersTable() throws SQLException {
        Connection connection = GetConnection();
        Statement statement = connection.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(50), " +
                "personal_number INT," +
                "password VARCHAR(50), " +
                "email VARCHAR(255)," +
                " phone VARCHAR(20)," +
                " address VARCHAR(100)," +
                " created DATE Default (CURRENT_DATE));";

        statement.executeUpdate(query);
        connection.close();
    }

    // Creates accounts table if it does not exist
    public static void CreateAccountsTable() throws SQLException {
        Connection connection = GetConnection();
        Statement statement = connection.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS accounts " +
                "(account_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id INT, " +
                "bban INT, " +
                "balance DOUBLE," +
                " created DATE Default (CURRENT_DATE));";

        statement.executeUpdate(query);
        connection.close();
    }

    // Creates transactions table if it does not exist
    public static void CreateTransactionsTable() throws SQLException {
        Connection connection = GetConnection();
        Statement statement = connection.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS transactions (transaction_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "senderAccount_id INT, " +
                "receiverAccount_id INT, " +
                "amount DOUBLE," +
                " created TIMESTAMP Default (CURRENT_TIMESTAMP));";

        statement.executeUpdate(query);
        connection.close();

    }

    // Creates user with the provided information
    public static void CreateUser() throws SQLException {
        System.out.print("Ange namn: ");
        String name = scan.nextLine().trim();
        System.out.print("Ange personnummer: ");
        int personalNumber = scan.nextInt();
        scan.nextLine();
        System.out.print("Ange lösenord: ");
        String password = scan.nextLine().trim();
        System.out.print("Ange email: ");
        String email = scan.nextLine().trim();
        System.out.print("Ange telefonnummer: ");
        String phone = scan.nextLine();
        System.out.print("Ange address: ");
        String address = scan.nextLine().trim();

        InsertToUsersTable(name, personalNumber, password, email, phone, address);
    }

    // Creates account with he provided bban and balance
    public static void CreateAccount() throws SQLException {
        System.out.print("Ange bban: ");
        int bban = scan.nextInt();
        System.out.print("Ange saldo: ");
        double balance = scan.nextDouble();
        scan.nextLine();

        InsertToAccountsTable(getLoggedInPersonalNumber(), bban, balance);
    }

    // Inserts user info to the users table
    public static void InsertToUsersTable(String name, int personalNumber, String password, String email, String phone, String address) throws SQLException {
        Connection connection = GetConnection();
        String query = "INSERT INTO users (name, personal_number, password, email, phone, address) VALUES (?,?,?,?,?,?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setInt(2, personalNumber);
        statement.setString(3, password);
        statement.setString(4, email);
        statement.setString(5, phone);
        statement.setString(6, address);
        statement.executeUpdate();
        connection.close();
    }

    // Inserts account info and user ID to the accounts table
    public static void InsertToAccountsTable(int personalNumber, int bban, Double balance) throws SQLException {
        Connection connection = GetConnection();

        int id = GetUserIdByPersonalNumber(personalNumber);

        String query = "INSERT INTO accounts (user_id, bban, balance) VALUES (?,?,?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.setInt(2, bban);
        statement.setDouble(3, balance);
        statement.executeUpdate();
        connection.close();
    }

    // Gets user ID and password, compares password to the one in the database, and saves ID and personal number with setters
    public static boolean CheckCredentials(int personalNumber, String password) throws SQLException {
        Connection connection = GetConnection();

        String query = "SELECT user_id, password FROM users WHERE personal_number = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, personalNumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int userId = resultSet.getInt("user_id");
            String passwordDatabase = resultSet.getString("password");
            connection.close();

            setLoggedInID(userId);
            setLoggedInPersonalNumber(personalNumber);
            return passwordDatabase.equals(password);
        } else {
            connection.close();
            return false;
        }
    }

    // Gets and returns user ID using personalNumber
    public static int GetUserIdByPersonalNumber(int personalNumber) throws SQLException {
        Connection connection = GetConnection();

        String query = "SELECT user_id FROM users WHERE personal_number = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, personalNumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int userId = resultSet.getInt("user_id");
            connection.close();
            return userId;
        } else {
            connection.close();
            return 0;
        }
    }

    // Show all users except the one logged-in
    public static void ShowAllUsers() throws SQLException {
        Connection connection = GetConnection();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM users ";

        ResultSet result = statement.executeQuery(query);

        while (result.next()) {
            int id = result.getInt("user_id");
            String name = result.getString("name");
            String phone = result.getString("phone");

            if (getLoggedInID() != id) {
                setAllUsersIDsExceptLoggedInArr(id);
                System.out.println("ID: " + id + " Namn: " + name + " Telefonnummer: " + phone);
            }
        }
        connection.close();
    }

    // Show all information of the logged-in user
    public static void ShowLoggedUserInfo() throws SQLException {
        Connection connection = GetConnection();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM users ";

        ResultSet result = statement.executeQuery(query);

        while (result.next()) {
            int id = result.getInt("user_id");
            String name = result.getString("name");
            String password = result.getString("password");
            String email = result.getString("email");
            String phone = result.getString("phone");
            String address = result.getString("address");

            if (getLoggedInID() == id) {
                System.out.println("Namn: " + name + " Lösenord: " + password + " Email: " + email + " Telefonnummer: " + phone + " Adress: " + address);
            }
        }
        connection.close();
    }

    // Show all accounts belonging to the chosen receiver
    public static void ShowReceiverAccounts(int id) throws SQLException {
        Connection connection = GetConnection();

        String query = "SELECT * FROM accounts WHERE user_id = ? ";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        setEmptyReceiverAccountsIDsArr();

        while (result.next()) {
            int accountId = result.getInt("account_id");
            int bban = result.getInt("bban");
            setReceiverAccountsIDsArr(accountId);
            System.out.println("Konto ID: " + accountId + " BBAN: " + bban);
        }
        connection.close();
    }

    // Show all accounts belonging to the logged-in user
    public static void ShowLoggedInAccounts() throws SQLException {
        Connection connection = GetConnection();

        String query = "SELECT * FROM accounts WHERE user_id = ? ";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, getLoggedInID());
        ResultSet result = statement.executeQuery();
        setToEmptyLoggedInAccountsIDsArr();

        while (result.next()) {
            int accountId = result.getInt("account_id");
            int bban = result.getInt("bban");
            double balance = result.getDouble("balance");
            setLoggedInAccountsIDsArr(accountId);
            System.out.println("Konto ID: " + accountId + " BBAN: " + bban + " Saldo: " + balance);
        }
        connection.close();
    }

    // Checks if the desired ID is in the array
    public static boolean IdIsInArray(ArrayList<Integer> array, int id) {
        for (int element : array) {
            if (element == id) {
                return true;
            }
        }
        return false;
    }

    // Transfer money from the chosen logged-in account to the chosen receiver's chosen account
    public static void MakeTransaction() throws SQLException {

        ShowLoggedInAccounts();
        System.out.println("Ange sändarens konto ID: ");
        int senderAccountId = scan.nextInt();

        if (IdIsInArray(getLoggedInAccountsIDsArr(), senderAccountId)) {

            System.out.println("\nDu kan inte välja ett ID som inte finns på listan");
            ShowAllUsers();
            System.out.println("Välj ett ID på personen som du vill swosha pengar till:");
            int receiverID = scan.nextInt();

            if (IdIsInArray(getAllUsersIDsExceptLoggedInArr(), receiverID)) {
                ShowReceiverAccounts(receiverID);
                if (!getReceiverAccountsIDsArr().isEmpty()) {

                    System.out.println("Ange mottagarens konto ID: ");
                    int receiverAccountId = scan.nextInt();

                    if (IdIsInArray(getReceiverAccountsIDsArr(), receiverAccountId)) {

                        System.out.println("Ange belopp: ");
                        double amount = scan.nextDouble();

                        boolean checker = BalanceChecker(senderAccountId, amount);

                        if (checker) {
                            Connection connection = GetConnection();
                            String query = "INSERT INTO transactions (senderAccount_id, receiverAccount_id, amount) VALUES (?,?,?)";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setInt(1, senderAccountId);
                            statement.setInt(2, receiverAccountId);
                            statement.setDouble(3, amount);
                            int result = statement.executeUpdate();

                            if (result > 0) {
                                UpdateBalance(senderAccountId, -amount);
                                UpdateBalance(receiverAccountId, amount);

                                System.out.println("Betalning genomförd");
                            } else {
                                System.out.println("Betalning misslyckades");
                            }
                            connection.close();
                        } else {
                            System.out.println("Du har inte tillräckligt med pengar");
                        }
                    } else {
                        System.out.println("ID:t som du har angett finns inte i listan");
                    }

                } else {
                    System.out.println("Mottagaren har inga konto");
                }

            } else {
                System.out.println("ID:t som du har angett finns inte i listan");
            }

        } else {
            System.out.println("ID:t som du har angett finns inte i listan");
        }
    }

    // Updates sender and receiver balance in accounts table
    public static void UpdateBalance(int account_id, double amount) throws SQLException {
        Connection connection = GetConnection();
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, amount);
        statement.setInt(2, account_id);
        statement.executeUpdate();
        connection.close();
    }

    // Checks if the balance has enough money to make the amount transfer
    public static boolean BalanceChecker(int id, double amount) throws SQLException {
        Connection connection = GetConnection();

        String query = "SELECT balance FROM accounts WHERE account_id = ? ";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            double balance = result.getDouble("balance");
            balance = balance - amount;

            connection.close();
            return balance >= 0.0;
        } else {
            connection.close();
            return false;
        }
    }

    // Deletes logged-in user and his accounts if there are any from the database tables
    public static void DeleteUser() throws SQLException {
        ShowLoggedUserInfo();
        Connection connection = GetConnection();
        System.out.print("Skriv ja för att bekräfta ");
        String confirmation = scan.nextLine();

        if (confirmation.equalsIgnoreCase("ja")) {
            String query = "DELETE users , accounts FROM users LEFT JOIN accounts ON users.user_id = accounts.user_id WHERE users.user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, getLoggedInID());
            statement.executeUpdate();
        } else {
            System.out.println("Raderingen avbröts");
        }
        connection.close();
    }

    // Deletes the chosen account belonging to the logged-in user
    public static void DeleteAccount() throws SQLException {
        Connection connection = GetConnection();
        ShowLoggedInAccounts();
        if (!getLoggedInAccountsIDsArr().isEmpty()) {
            System.out.print("Ange account ID: ");
            int account_id = Integer.parseInt(scan.nextLine().trim());
            if (IdIsInArray(getLoggedInAccountsIDsArr(), account_id)) {
                System.out.print("Skriv ja för att bekräfta ");
                String confirmation = scan.nextLine();

                if (confirmation.equalsIgnoreCase("ja")) {

                    String query = "DELETE FROM accounts WHERE account_id = ? AND user_id = ?";
                    PreparedStatement statement = connection.prepareStatement(query);

                    statement.setInt(1, account_id);
                    statement.setInt(2, getLoggedInID());
                    statement.executeUpdate();
                    System.out.println("Borttagning lyckades");
                } else {
                    System.out.println("Borttagning avbröts");
                }
            } else {
                System.out.println("ID:t som du har angett finns inte i listan");
            }
        } else {
            System.out.println("Du har inga konto");
        }
        connection.close();
    }

    // Logged-in user info is updates in users Table with the new info
    public static void UpdateUserInfo() throws SQLException {
        Connection connection = GetConnection();

        ShowLoggedUserInfo();
        System.out.print("Ange nytt användarnamn: ");
        String name = scan.nextLine().trim();
        System.out.print("Ange nytt lösenord: ");
        String password = scan.nextLine().trim();
        System.out.print("Ange ny email: ");
        String email = scan.nextLine().trim();
        System.out.print("Ange nytt telefonnummer: ");
        String phone = scan.nextLine();
        System.out.print("Ange ny address: ");
        String address = scan.nextLine().trim();

        String query = "UPDATE users SET name = ?, password = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setString(2, password);
        statement.setString(3, email);
        statement.setString(4, phone);
        statement.setString(5, address);
        statement.setInt(6, getLoggedInID());
        statement.executeUpdate();
        connection.close();
    }

    // Show all accounts and transactions between two chosen dates for the chosen account, if there are no transactions inform the user about it
    public static void ShowAccountAndTransactions() throws SQLException {
        Connection connection = GetConnection();

        ShowLoggedInAccounts();
        if (!getLoggedInAccountsIDsArr().isEmpty()) {
            System.out.print("Ange ID på kontot för att se dess transaktioner: ");
            int accountId = scan.nextInt();
            scan.nextLine();
            if (IdIsInArray(getLoggedInAccountsIDsArr(), accountId)) {

                System.out.print("Välj start datum (yyyy-mm-dd): ");
                String startDateStr = scan.nextLine();
                LocalDate startDate = LocalDate.parse(startDateStr);

                System.out.print("Välj slut datum (yyyy-mm-dd): ");
                String endDateStr = scan.nextLine();
                LocalDate endDate = LocalDate.parse(endDateStr);

                String query = "SELECT accounts.account_id , accounts.bban , accounts.balance , transactions.senderAccount_id , transactions.receiverAccount_id, transactions.amount, transactions.created FROM accounts INNER JOIN transactions ON accounts.account_id = transactions.senderAccount_id OR accounts.account_id = transactions.receiverAccount_id WHERE accounts.account_id = ? AND DATE(transactions.created) BETWEEN ? AND ? ORDER BY transactions.created DESC";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, accountId);
                statement.setObject(2, startDate);
                statement.setObject(3, endDate);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    int accountID = result.getInt("account_id");
                    int accountBBAN = result.getInt("bban");
                    double balance = result.getDouble("balance");

                    System.out.println("Konto ID: " + accountID + " BBAN: " + accountBBAN + " Saldo: " + balance + "\n");

                    do {
                        int senderID = result.getInt("senderAccount_id");
                        String receiverID = result.getString("receiverAccount_id");
                        String amount = result.getString("amount");
                        String created = result.getString("created");

                        System.out.println("Sändare konto ID: " + senderID + " Mottagare konto ID: " + receiverID + " Belopp:  " + amount + " Datum: " + created);

                    } while (result.next());
                } else {
                    System.out.println("Det finns inga transaktioner för detta kontot mellan de angivna datumen");
                }
            } else {
                System.out.println("ID:t som du har angett finns inte i listan");
            }
        } else {
            System.out.println("Du har inga konto");
        }
        connection.close();
    }
}