package DavisBase.Util;

public class Settings {
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ARROW = " \u276F" + ANSI_RESET + " ";
    static String prompt = ANSI_PURPLE_BACKGROUND + "bunker" + ARROW;
    static String version = "v1.7";
    static String copyright = "©2022 Bunker Team";
    static boolean isExit = false;
    static String dataBaseName = "";
    static boolean dataBaseSelected = false;
    static String sytaxErrorString = ANSI_RED_BACKGROUND + "Oops! There is a syntax error in your query/command"
            + ANSI_RESET;
    static String createDatabaseSuccess = ANSI_GREEN_BACKGROUND + "Data Base created sucessfully" + ANSI_RESET;
    static String createDatabaseFailure = ANSI_YELLOW_BACKGROUND
            + "Data Base already exists/ data base cannot be created" + ANSI_RESET;
    static String droppedSucessfully = ANSI_GREEN_BACKGROUND + "Database dropped" + ANSI_RESET;
    static String droppedUnSucessfully = ANSI_RED_BACKGROUND + "Error when dropping database" + ANSI_RESET;
    static String databaseNotSelected = ANSI_RED_BACKGROUND + "Database not selected, for this operation" + ANSI_RESET;
    static String dataBaseTableNotFound = ANSI_RED_BACKGROUND + "Database/table not found" + ANSI_RESET;
    static String querySucessfulString = ANSI_GREEN_BACKGROUND + "Query ran sucessfully" + ANSI_RESET;
    static String queryUnSucessfulString = ANSI_RED_BACKGROUND + "Query un-sucessfull" + ANSI_RESET;
    public static String columnNameError = ANSI_RED_BACKGROUND + "Table column names does not match"
            + ANSI_RESET;
    /*
     * Page size for all files is 512 bytes by default.
     * You may choose to make it user modifiable
     */
    static int pageSize = 512;

    public static boolean isExit() {
        return isExit;
    }

    public static void setExit(boolean e) {
        isExit = e;
    }

    public static String getPrompt() {
        return prompt;
    }

    public static void setPrompt(String s) {
        prompt = s;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        Settings.version = version;
    }

    public static String getCopyright() {
        return copyright;
    }

    public static void setCopyright(String copyright) {
        Settings.copyright = copyright;
    }

    public static int getPageSize() {
        return pageSize;
    }

    public static void setPageSize(int pageSize) {
        Settings.pageSize = pageSize;
    }

    public static void setDataBaseName(String dataBaseName) {
        Settings.dataBaseName = dataBaseName;
    }

    public static String getDataBaseName() {
        return ANSI_CYAN_BACKGROUND + dataBaseName;
    }

    public static void setDataBaseSelected(Boolean dataBaseSelected) {
        Settings.dataBaseSelected = dataBaseSelected;
    }

    public static Boolean getDataBaseSelected() {
        return dataBaseSelected;
    }

    public static String getSyntaxError() {
        return sytaxErrorString;
    }

    /**
     * ***********************************************************************
     * Static method definitions
     */

    /**
     * @param s   The String to be repeated
     * @param num The number of time to repeat String s.
     * @return String A String object, which is the String s appended to itself num
     *         times.
     */
    public static String line(String s, int num) {
        String a = "";
        for (int i = 0; i < num; i++) {
            a += s;
        }
        return a;
    }

    public static String getCreateDatabaseSuccess() {
        return createDatabaseSuccess;
    }

    public static String getCreateDatabaseAlreadyExists() {
        return createDatabaseFailure;
    }

    public static String getDroppedSucessfully() {
        return droppedSucessfully;
    }

    public static String getDroppedUnSucessfully() {
        return droppedUnSucessfully;
    }

    public static String getDataBaseNotSelected() {
        return databaseNotSelected;
    }

    public static String getdataBaseTableNotFound() {
        return dataBaseTableNotFound;
    }

    public static String getquerySucessfulString() {
        return querySucessfulString;
    }

    public static String getqueryUnSucessfulString() {
        return queryUnSucessfulString;
    }
}