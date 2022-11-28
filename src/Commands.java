import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import DavisBase.DBEngine;
import DavisBase.DDL.Table;
import DavisBase.Util.CommonUse;
import DavisBase.Util.Settings;
import DavisBase.Util.WelcomeScreen;

public class Commands {

    /*
     * This method determines what type of command the userCommand is and
     * calls the appropriate method to parse the userCommand String.
     */
    public static void parseUserCommand(String userCommand) {

        /* Clean up command string so that each token is separated by a single space */
        userCommand = userCommand.replaceAll("\n", " "); // Remove newlines
        userCommand = userCommand.replaceAll("\r", " "); // Remove carriage returns
        userCommand = userCommand.replaceAll(",", " , "); // Tokenize commas
        userCommand = userCommand.replaceAll("\\(", " ( "); // Tokenize left parentheses
        userCommand = userCommand.replaceAll("\\)", " ) "); // Tokenize right parentheses
        userCommand = userCommand.replaceAll("( )+", " "); // Reduce multiple spaces to a single space

        /*
         * commandTokens is an array of Strings that contains one lexical token per
         * array
         * element. The first token can be used to determine the type of command
         * The other tokens can be used to pass relevant parameters to each
         * command-specific
         * method inside each case statement
         */
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));

        /*
         * This switch handles a very small list of hard-coded commands from SQL syntax.
         * You will want to rewrite this method to interpret more complex commands.
         */
        switch (commandTokens.get(0).toLowerCase()) {
            case "use":
                System.out.println("Case: USE");
                use(commandTokens);
                break;
            case "show":
                System.out.println("Case: SHOW");
                show(commandTokens);
                break;
            case "select":
                System.out.println("Case: SELECT");
                parseQuery(userCommand);
                break;
            case "create":
                System.out.println("Case: CREATE");
                if (userCommand.toLowerCase().contains("database")) {
                    parseCreateDatabase(userCommand);
                } else {
                    parseCreateTable(userCommand);
                }
                break;
            case "insert":
                System.out.println("Case: INSERT INTO");
                parseInsert(userCommand);
                break;
            case "delete":
                System.out.println("Case: DELETE");
                parseDelete(userCommand);
                break;
            case "update":
                System.out.println("Case: UPDATE");
                parseUpdate(userCommand);
                break;
            case "drop":
                System.out.println("Case: DROP");
                if (commandTokens.get(1).equalsIgnoreCase("database")) {
                    dropDatabase(commandTokens);
                }
                if (commandTokens.get(1).equalsIgnoreCase("table")) {
                    dropTable(commandTokens);
                }
                break;
            case "describe":
                System.out.println("Case: display data of database");
                describe(commandTokens);
                break;
            case "help":
                help();
                break;
            case "version":
                displayVersion();
                break;
            case "exit":
                Settings.setExit(true);
                break;
            case "quit":
                Settings.setExit(true);
                break;
            default:
                System.out.println("I didn't understand the command: \"" + userCommand + "\"");
                break;
        }
    }

    static DBEngine db = new DBEngine();

    private static void use(ArrayList<String> commandTokens) {
        if (commandTokens.size() != 2 || !db.checkIfDbExists(commandTokens.get(1))) {
            String error = (commandTokens.size() != 2) ? Settings.getSyntaxError()
                    : Settings.getdataBaseTableNotFound();
            System.out.println(error);
            return;
        }
        Settings.setDataBaseSelected(true);
        Settings.setDataBaseName(commandTokens.get(1));
        db.connect(commandTokens.get(1));
    }

    private static void describe(ArrayList<String> commandTokens) {
        db.describe();
    }

    public static void displayVersion() {
        System.out.println("DavisBaseLite Version " + Settings.getVersion());
        System.out.println(Settings.getCopyright());
    }

    public static void parseCreateDatabase(String command) {

        System.out.println("Stub: parseCreateDatabase method");
        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (commandTokens.size() != 3) {
            System.out.println(Settings.getSyntaxError());
        } else {
            if (db.createDB(commandTokens.get(2))) {
                System.out.println(Settings.getCreateDatabaseSuccess());
            } else {
                System.out.println(Settings.getCreateDatabaseAlreadyExists());
            }
        }
    }

    public static void parseCreateTable(String command) {

        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (commandTokens.size() < 4) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (!Settings.getDataBaseSelected()) {
            System.out.println(Settings.getDataBaseNotSelected());
            return;
        } else {
            Table table = new Table(Settings.getDataBaseName(), commandTokens.get(2));
            if (table.exists()) {
                System.out.println(Settings.getCreateDatabaseAlreadyExists());
                return;
            }
            String[] columnString = CommonUse.createQueryString(command, 2);
            db.createTable(commandTokens.get(1), columnString);
        }
    }

    public static void show(ArrayList<String> commandTokens) {
        if (commandTokens.size() != 2) {
            System.out.println(Settings.getSyntaxError());
            return;
        } else {
            if (commandTokens.get(1).equalsIgnoreCase("tables")) {
                if (Settings.getDataBaseSelected())
                    db.showTables(Settings.getDataBaseName());
                else {
                    System.out.println(Settings.getDataBaseNotSelected());
                }
            } else if (commandTokens.get(1).equalsIgnoreCase("databases")) {
                db.showDatabases();
            }
        }
    }

    /*
     * Stub method for inserting a new record into a table.
     */
    public static void parseInsert(String command) {
        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (commandTokens.size() < 4) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(2))) {
            String[] insertCommand = CommonUse.createQueryString(command, 3);
            db.insertInto(commandTokens.get(2), insertCommand);
        }
    }

    public static void parseDelete(String command) {
        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(2))) {
            db.delete(commandTokens.get(2), CommonUse.deletePrep(command));
        } else {
            System.out.println(Settings.getdataBaseTableNotFound());
        }
    }

    /**
     * Stub method for dropping tables
     */
    public static void dropDatabase(ArrayList<String> commandTokens) {
        System.out.println("Command: " + tokensToCommandString(commandTokens));
        System.out.println("Stub: This is the dropTable method.");
        if (commandTokens.size() != 3) {
            System.out.println(Settings.getSyntaxError());
        } else {
            if (db.dropDataBase(commandTokens.get(2))) {
                System.out.println(Settings.getDroppedSucessfully());
                Settings.setDataBaseName(null);
                Settings.setDataBaseSelected(false);
            } else {
                System.out.println(Settings.getDroppedUnSucessfully());
            }
        }
    }

    // TODO need to test this
    public static void dropTable(ArrayList<String> commandTokens) {
        System.out.println("Command: " + tokensToCommandString(commandTokens));
        System.out.println("Stub: This is the dropTable method.");
        if (commandTokens.size() != 3) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (!Settings.getDataBaseSelected()) {
            System.out.println(Settings.getDataBaseNotSelected());
            return;
        }
        Table table = new Table(Settings.getDataBaseName(), commandTokens.get(2));
        if (table.exists()) {
            if (table.delete()) {
                System.out.println(Settings.getquerySucessfulString());
                return;
            } else {
                System.out.println(Settings.getqueryUnSucessfulString());
                return;
            }
        }
        System.out.println(Settings.getdataBaseTableNotFound());
        return;
    }

    /**
     * Stub method for executing queries
     */
    public static void parseQuery(String command) {
        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (commandTokens.size() < 3) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (commandTokens.contains("*")) {
            db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(3));
            // db.select(commandTokens.get(3));
        } else if (!commandTokens.contains("where")) {
            db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(commandTokens.size() - 1));
            command = CommonUse.removeBegning(command, 1);
            command = CommonUse.removeEnd(command, 2);
            String selectColumnString[] = CommonUse.splitGenerator(command, ",");
            db.select(commandTokens.get(commandTokens.size() - 1), selectColumnString);
        }
    }

    /**
     * Stub method for updating records
     * 
     * @param updateString is a String of the user input
     */
    public static void parseUpdate(String command) {
        String[] values = { "col1", "1000" };
        String[] where = { "col1", "10" };
        db.updateInfo("details", values, where);
        // ArrayList<String> commandTokens = commandStringToTokenList(command);
        // if (!commandTokens.contains("set") || !commandTokens.contains("where")) {
        // System.out.println(Settings.getSyntaxError());
        // } else {
        // if (db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(1)))
        // {
        // Map<String, String[]> updateValues = CommonUse.updateQueryPrep(command);
        // db.updateInfo(commandTokens.get(1), updateValues.get("values"),
        // updateValues.get("where"));
        // } else {
        // System.out.println(Settings.getdataBaseTableNotFound());
        // }
        // }
    }

    public static String tokensToCommandString(ArrayList<String> commandTokens) {
        String commandString = "";
        for (String token : commandTokens)
            commandString = commandString + token + " ";
        return commandString;
    }

    public static ArrayList<String> commandStringToTokenList(String command) {
        command.replace("\n", " ");
        command.replace("\r", " ");
        command.replace(",", " , ");
        command.replace("\\(", " ( ");
        command.replace("\\)", " ) ");
        ArrayList<String> tokenizedCommand = new ArrayList<String>(Arrays.asList(command.split(" ")));
        return tokenizedCommand;
    }

    /**
     * Help: Display supported commands
     */
    public static void help() {
        out.println(WelcomeScreen.printSeparator("*", 80));
        out.println("SUPPORTED COMMANDS\n");
        out.println("All commands below are case insensitive\n");
        out.println("SHOW TABLES;");
        out.println("\tDisplay the names of all tables.\n");
        out.println("SELECT âŸ¨column_listâŸ© FROM table_name [WHERE condition];\n");
        out.println("\tDisplay table records whose optional condition");
        out.println("\tis <column_name> = <value>.\n");
        out.println("INSERT INTO (column1, column2, ...) table_name VALUES (value1, value2, ...);\n");
        out.println("\tInsert new record into the table.");
        out.println("UPDATE <table_name> SET <column_name> = <value> [WHERE <condition>];");
        out.println("\tModify records data whose optional <condition> is\n");
        out.println("DROP TABLE table_name;");
        out.println("\tRemove table data (i.e. all records) and its schema.\n");
        out.println("VERSION;");
        out.println("\tDisplay the program version.\n");
        out.println("HELP;");
        out.println("\tDisplay this help information.\n");
        out.println("EXIT;");
        out.println("\tExit the program.\n");
        out.println(WelcomeScreen.printSeparator("*", 80));
    }
}