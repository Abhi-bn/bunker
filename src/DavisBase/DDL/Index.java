package DavisBase.DDL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import DavisBase.Pages.Page;
import DavisBase.Pages.PageGenerator;
import DavisBase.Util.DavisBaseExceptions.PageOverflow;

public class Index {
    String name;
    String path;
    String column;

    public Index(String base_path, String table_name, String column_name) {
        this.path = base_path;
        this.name = table_name;
        this.column = column_name;
    }

    public boolean create() throws PageOverflow {
        File f = new File(path + "/" + name + "." + column + ".ndx");
        if (f.exists())
            return false;
        try {
            f.createNewFile();
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            Page pg = PageGenerator.generatePage(Page.PageType.IndexInterior, rf, true);
            pg.insertData(null);
            rf.close();
        } catch (IOException e) {
            System.out.println("Error when creating the index page");
        }
        return true;
    }

    public String getFilePath() {
        return path + "/" + name + "." + column + ".ndx";
    }

}
