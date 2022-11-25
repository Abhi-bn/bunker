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
        return true;
    }

    public boolean select(String table_name, String... columns) {
        Table tb = new Table(DBPath + "/", table_name);
        tb.select(table_name);
        return true;
    }
}
