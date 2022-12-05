package DavisBase;

import java.io.File;
import java.util.ArrayList;

import DavisBase.DDL.MetaData;
import DavisBase.DDL.Table;
import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.Draw;
import DavisBase.Util.Settings;

public class DBEngine {
    private static String DBPath;
    private static String BasePath = "team_bunker/";
    public static MetaData __metadata;

    public DBEngine() {
        File DBFile = new File(BasePath);
        DBFile.mkdir();
    }

    public String getDBPath() {
        return BasePath + DBPath + "/";
    }

    public String makeFromBasePath(String db_name) {
        return BasePath + db_name;
    }

    public boolean createDB(String database) {
        DBPath = database;
        if (!initializeNewDB())
            return false;
        this.connect(database);
        return true;
    }

    public boolean createTable(String table_name, String... columns) {
        Table tb = new Table(getDBPath(), table_name);
        tb.create(columns);
        return true;
    }

    public boolean connect(String database) {
        DBPath = database;

        if (!initialized())
            return false;

        __metadata = new MetaData(getDBPath());
        return true;
    }

    public boolean initialized() {
        File DBFile = new File(getDBPath());
        return DBFile.exists();
    }

    public boolean initializeNewDB() {
        File DBFile = new File(getDBPath());
        return DBFile.mkdir();
    }

    public void describe(String table_name) {
        if (table_name.length() == 0) {
            __metadata.describe();
            return;
        }
        Table tb = new Table(getDBPath(), table_name);
        tb.describe();
    }

    public boolean insertInto(String table_name, String... columns) {
        Table tb = new Table(getDBPath(), table_name);
        tb.insertInto(columns);
        // Index index = new Index(DBPath, table_name, "row_id");
        // index.create();
        return true;
    }

    public boolean select(String table_name, String[] where, String[] columns) {
        Table tb = new Table(getDBPath(), table_name);
        tb.select(table_name, where, columns);
        return true;
    }

    public int updateInfo(String table_name, String[] data, String[] columns) {
        Table tb = new Table(getDBPath(), table_name);
        return tb.updateInfo(data, columns);
    }

    public int delete(String table_name, String... columns) {
        Table tb = new Table(getDBPath(), table_name);
        return tb.deleteTableValues(columns);
    }

    public boolean createIndex(String table_name, String columnName) {
        // TODO:CODE TO TRAVERSE ALL RECORDS IN THE TABLE AND ADD TO INDEX FILE
        Table tb = new Table(getDBPath(), table_name);
        tb.insertIntoIndex(table_name, columnName, /* columnName */ "");
        return true;
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

    public boolean checkIfPathExists(File file) {
        return file.exists();
    }

    public boolean dropDataBase(String databaseName) {
        File DBFile = new File(makeFromBasePath(databaseName));
        if (!checkIfPathExists(DBFile)) {
            System.out.println("DataBase does not exist");
            return false;
        }
        return deleteDirectory(DBFile);
    }

    public boolean showTables(String databaseName) {
        File DBFile = new File(getDBPath());

        if (!checkIfPathExists(DBFile)) {
            System.out.println(Settings.getdataBaseTableNotFound());
            return false;
        }

        int i = 0;
        String[][] c = { { "Sl.no", "INT" }, { "Tables", "Text" } };
        ColumnField[] col = { new ColumnField(c[0], 0), new ColumnField(c[1], 1) };
        ArrayList<ValueField[]> val = new ArrayList<ValueField[]>();
        for (String table_path : DBFile.list()) {
            String tableName = table_path.strip().replace(".tbl", "");
            ValueField[] vf = { new ValueField(i++, col[0]), new ValueField(tableName, col[1]) };
            val.add(vf);
        }
        Draw.drawTable(col, val);
        return true;
    }

    public void showDatabases() {
        File DBFile = new File(BasePath);
        int i = 0;
        String[][] c = { { "Sl.no", "INT" }, { "Databases", "Text" } };
        ColumnField[] col = { new ColumnField(c[0], 0), new ColumnField(c[1], 1) };
        ArrayList<ValueField[]> val = new ArrayList<ValueField[]>();
        for (String database : DBFile.list()) {
            ValueField[] vf = { new ValueField(i++, col[0]), new ValueField(database, col[1]) };
            val.add(vf);
        }
        Draw.drawTable(col, val);
    }

    public boolean checkIfDbExists(String databaseName) {
        File DBFile = new File(makeFromBasePath(databaseName));
        return checkIfPathExists(DBFile);
    }

    public boolean checkIfTableExists(String databaseName, String tableName) {
        Table tb = new Table(getDBPath(), tableName);
        return tb.exists();
    }

    public boolean dropTable(String table_name) {
        Table tb = new Table(getDBPath(), table_name);
        return tb.delete();
    }
}
