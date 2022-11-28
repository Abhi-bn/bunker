package DavisBase;

import java.io.File;

import DavisBase.DDL.MetaData;
import DavisBase.DDL.Table;

public class DBEngine {
    private static String DBPath;
    private static File DBFile;
    public static MetaData __metadata;

    public DBEngine() {
    }

    public boolean createDB(String database) {
        DBPath = database;
        if (!initializeNewDB())
            return false;
        this.connect(database);
        return true;
    }

    public boolean createTable(String table_name, String... columns) {
        Table tb = new Table(DBPath + "/", table_name);
        tb.create(columns);
        return true;
    }

    public boolean connect(String database) {
        DBPath = database;

        if (!initialized())
            return false;

        __metadata = new MetaData(DBPath + "/");
        return true;
    }

    public boolean initialized() {
        DBFile = new File(DBPath);
        return DBFile.exists();
    }

    public boolean initializeNewDB() {
        DBFile = new File(DBPath);
        // TODO: adding database validity checks also
        return DBFile.mkdir();
    }

    public void describe() {
        Table r = new Table(DBPath + "/", "MetaData");
        r.describe();
    }

    public boolean insertInto(String table_name, String... columns) {
        Table tb = new Table(DBPath + "/", table_name);
        tb.insertInto(columns);
        // Index index = new Index(DBPath, table_name, "row_id");
        // index.create();
        return true;
    }

    public boolean select(String table_name, String... columns) {
        Table tb = new Table(DBPath + "/", table_name);
        tb.select(table_name, columns);
        return true;
    }

    public int updateInfo(String table_name, String[] data, String[] columns) {
        Table tb = new Table(DBPath + "/", table_name);
        return tb.updateInfo(data, columns);
    }

    public int delete(String table_name, String... columns) {
        Table tb = new Table(DBPath + "/", table_name);
        return tb.deleteTableValues(columns);
    }

    public boolean createIndex(String table_name, String columnName) {
        // TODO:CODE TO TRAVERSE ALL RECORDS IN THE TABLE AND ADD TO INDEX FILE
        Table tb = new Table(DBPath + "/", table_name);
        tb.insertIntoIndex(table_name, columnName, /* columnName */ "");
        return true;
    }
}
