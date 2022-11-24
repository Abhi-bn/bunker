package DavisBase;

import java.io.File;

import DavisBase.DDL.MetaData;
import DavisBase.DDL.Table;
import DavisBase.Util.Settings;

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
        if (!checkIfPathExists(DBFile)) {
            return DBFile.mkdir();
        }
        return false;
    }

    public boolean checkIfPathExists(File file) {
        return file.exists();
    }

    public void describe() {
        Table r = new Table(DBPath + "/", "MetaData");
        r.describe();
    }

    public boolean dropDataBase(String databaseName) {
        DBFile = new File(databaseName);
        if (checkIfPathExists(DBFile)) {
            return deleteDirectory(DBFile);
        } else {
            System.out.println("DataBase doesnt exist");
            return false;
        }
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
