import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import DavisBase.DBEngine;
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
                use(commandTokens);
                break;
            case "show":
                show(commandTokens);
                break;
            case "select":
                parseQuery(userCommand);
                break;
            case "create":
                if (userCommand.toLowerCase().contains("database")) {
                    parseCreateDatabase(userCommand);
                } else if (userCommand.toLowerCase().contains("table")) {
                    parseCreateTable(userCommand);
                } else {
                    System.out.println(Settings.getSyntaxError());
                }
                break;
            case "insert":
                parseInsert(userCommand);
                break;
            case "delete":
                parseDelete(userCommand);
                break;
            case "update":
                parseUpdate(userCommand);
                break;
            case "drop":
                if (commandTokens.get(1).equalsIgnoreCase("database")) {
                    dropDatabase(commandTokens);
                }
                if (commandTokens.get(1).equalsIgnoreCase("table")) {
                    dropTable(commandTokens);
                }
                break;
            case "describe":
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
                System.out.println(
                        Settings.ANSI_RED_BACKGROUND + "I didn't understand the command: \"" + userCommand + "\""
                                + Settings.ANSI_RESET);
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
        if (!db.connect(commandTokens.get(1)))
            System.out.println("Error Connecting to Database");
    }

    private static void describe(ArrayList<String> commandTokens) {
        if (!Settings.getDataBaseSelected()) {
            System.out.println(Settings.getDataBaseNotSelected());
            return;
        }

        if (commandTokens.size() == 1) {
            db.describe("");
            return;
        }
        if (commandTokens.size() == 3) {
            db.describe(commandTokens.get(2));
            return;
        }

        System.out.println(Settings.getdataBaseTableNotFound());
    }

    public static void displayVersion() {
        System.out.println("DavisBaseLite Version " + Settings.getVersion());
        System.out.println(Settings.getCopyright());
    }

    public static void parseCreateDatabase(String command) {
        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (commandTokens.size() != 3) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (!db.createDB(commandTokens.get(2))) {
            System.out.println(Settings.getCreateDatabaseAlreadyExists());
            return;
        }
        System.out.println(Settings.getCreateDatabaseSuccess());
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
        }
        if (db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(2))) {
            System.out.println(Settings.getCreateDatabaseAlreadyExists());
            return;
        }
        String[] columnString = CommonUse.createQueryString(command, 3);
        db.createTable(commandTokens.get(2), columnString);
        System.out.println(Settings.getCreateDatabaseSuccess());
    }

    public static void show(ArrayList<String> commandTokens) {
        if (commandTokens.size() != 2) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (commandTokens.get(1).equalsIgnoreCase("databases")) {
            db.showDatabases();
            return;
        }
        if (!Settings.getDataBaseSelected()) {
            System.out.println(Settings.getDataBaseNotSelected());
            return;
        }
        db.showTables(Settings.getDataBaseName());
    }

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
            db.delete(commandTokens.get(2), CommonUse.wherePrep(command));
        } else {
            System.out.println(Settings.getdataBaseTableNotFound());
        }
    }

    public static void dropDatabase(ArrayList<String> commandTokens) {
        if (commandTokens.size() != 3) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (!db.dropDataBase(commandTokens.get(2))) {
            System.out.println(Settings.getDroppedUnSucessfully());
        }
        Settings.setDataBaseName(null);
        Settings.setDataBaseSelected(false);
        System.out.println(Settings.getDroppedSucessfully());
    }

    public static void dropTable(ArrayList<String> commandTokens) {
        System.out.println("Command: " + tokensToCommandString(commandTokens));
        if (commandTokens.size() != 3) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (!Settings.getDataBaseSelected()) {
            System.out.println(Settings.getDataBaseNotSelected());
            return;
        }
        String table_name = commandTokens.get(2);
        if (db.checkIfTableExists(Settings.getDataBaseName(), table_name)) {
            db.dropTable(table_name);
        } else {
            System.out.println(Settings.getdataBaseTableNotFound());
        }
        return;
    }

    public static void parseQuery(String command) {
        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (commandTokens.size() < 3) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        String[] array = new String[0];

        if (commandTokens.contains("where") && commandTokens.contains("*")) {
            if (db.checkIfTableExists(Settings.getDataBaseName(),
                    commandTokens.get(commandTokens.indexOf("from") + 1))) {
                String[] whereSelectClause = CommonUse.wherePrep(command);
                db.select(commandTokens.get(commandTokens.indexOf("from")
                        + 1), whereSelectClause,
                        array);
            } else {
                System.out.println(Settings.getdataBaseTableNotFound());
                return;
            }
        } else if (commandTokens.contains("where") && !commandTokens.contains("*")) {
            if (db.checkIfTableExists(Settings.getDataBaseName(),
                    commandTokens.get(commandTokens.indexOf("from") + 1))) {
                command = CommonUse.removeBegning(command, 1);
                String selectColumnString[] = CommonUse.selectWhereCols(command);
                String[] whereSelectClause = CommonUse.wherePrep(command);
                db.select(commandTokens.get(commandTokens.indexOf("from") + 1), whereSelectClause,
                        selectColumnString);
            } else {
                System.out.println(Settings.getdataBaseTableNotFound());
                return;
            }
        } else if (commandTokens.contains("*")) {
            if (db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(3))) {
                db.select(commandTokens.get(3), array, array);
            } else {
                System.out.println(Settings.getdataBaseTableNotFound());
                return;
            }

        } else if (!commandTokens.contains("where")) {
            if (db.checkIfTableExists(Settings.getDataBaseName(),
                    commandTokens.get(commandTokens.size() - 1))) {
                command = CommonUse.removeBegning(command, 1);
                command = CommonUse.removeEnd(command, 2);
                String selectColumnString[] = CommonUse.splitGenerator(command, ",");
                db.select(commandTokens.get(commandTokens.size() - 1), array,
                        selectColumnString);
            } else {
                System.out.println(Settings.getdataBaseTableNotFound());
                return;
            }
        }
    }

    public static void parseUpdate(String command) {
        ArrayList<String> commandTokens = commandStringToTokenList(command);
        if (!commandTokens.contains("set") || !commandTokens.contains("where")) {
            System.out.println(Settings.getSyntaxError());
            return;
        }
        if (!db.checkIfTableExists(Settings.getDataBaseName(), commandTokens.get(1))) {
            System.out.println(Settings.getdataBaseTableNotFound());
            return;
        }
        Map<String, String[]> updateValues = CommonUse.updateQueryPrep(command);
        db.updateInfo(commandTokens.get(1), updateValues.get("values"), updateValues.get("where"));
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
        command.replace(" FROM", " from");
        command.replace(" WHERE", " where");
        ArrayList<String> tokenizedCommand = new ArrayList<String>(Arrays.asList(command.split(" ")));
        return tokenizedCommand;
    }

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