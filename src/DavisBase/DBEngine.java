package DavisBase;

import java.io.File;

public class DBEngine {
    public final static String DBPath = "davisbase";
    final File DBFile = new File(DBPath);

    public DBEngine() {
        if (initialized())
            return;
        initializeNewDB();
    }

    public void createDB(String database) {

    }

    public boolean initialized() {
        // TODO: adding database validity checks also
        return DBFile.exists();
    }

    public void initializeNewDB() {
        DBFile.mkdir();
    }
}
