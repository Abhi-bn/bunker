package DavisBase.Util;

public class Settings {

    static String prompt = "davisql> ";
    static String version = "v1.2";
    static String copyright = "Â©2020 Chris Irwin Davis";
    static boolean isExit = false;
    static String dataBaseName = "";
    static boolean dataBaseSelected = false;
    static String sytaxErrorString = "Oops! There is a syntax error in your query/command";
    static String createDatabaseSuccess = "Data Base created sucessfully";
    static String createDatabaseFailure = "Data Base already exists/ data base cannot be created";
    static String droppedSucessfully = "Database dropped";
    static String droppedUnSucessfully = "Error when dropping database";
    static String databaseNotSelected = "Database not selected, for this operation";
    static String dataBaseTableNotFound = "Database/table not found";
    static String querySucessfulString = "Query ran sucessfully";
    static String queryUnSucessfulString = "Query un-sucessfull";
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
        return dataBaseName;
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