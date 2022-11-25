package DavisBase.DDL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import DavisBase.DBEngine;
import DavisBase.Pages.Page;
import DavisBase.Pages.PageController;
import DavisBase.Pages.PageGenerator;
import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.DavisBaseExceptions;
import DavisBase.Util.Draw;
import DavisBase.Util.Log;

public class Table {
    static boolean verbose = true;
    String name;
    String path;

    public Table(String base_path, String table_name) {
        this.path = base_path;
        this.name = table_name;
    }

    ColumnField[] generateMeta(String... columns) {
        ColumnField[] meta_data = new ColumnField[columns.length + 1];
        String[] id_ = { "_id", "INT", "UNIQUE" };
        meta_data[0] = new ColumnField(id_, 0);
        for (int i = 1; i < columns.length + 1; i++) {
            String[] eachCol = columns[i - 1].split(" ");
            ColumnField tf = new ColumnField(eachCol, i);
            meta_data[i] = tf;
            Log.DEBUG(verbose, meta_data.toString());
        }
        return meta_data;
    }

    public boolean create(String... columns) {
        File f = new File(path + name + ".tbl");
        if (f.exists())
            return false;
        try {
            f.createNewFile();
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            DBEngine.__metadata.insert(name, generateMeta(columns));
            Page pg = PageGenerator.generatePage(Page.PageType.TableLeaf, rf, true);

            rf.close();
        } catch (DavisBaseExceptions.PageOverflow e) {

        } catch (IOException e) {
        }
        return true;
    }

    public String getFilePath() {
        return path + name + ".tbl";
    }

    public boolean insertInto(String... cols) {
        ValueField[] table_info = DBEngine.__metadata.tables_info.get(this.name);
        if (cols.length != table_info.length - 1) {
            System.out.println("INVALID DATA");
            return false;
        }
        ValueField[][] new_data = new ValueField[1][table_info.length];
        new_data[0][0] = new ValueField(0, table_info[0]);
        for (int i = 1; i < table_info.length; i++) {
            ValueField fd = new ValueField(cols[i - 1], table_info[i]);
            new_data[0][i] = fd;
        }
        insert(new_data);
        return true;
    }

    public void insert(ValueField[][] fields) {
        File f = new File(getFilePath());
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            PageController pc = new PageController(rf, false);
            pc.insert_data(fields);
            rf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void select(String table_name) {
        File f = new File(getFilePath());
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            PageController pc = new PageController(rf, false);
            ValueField[] table_info = DBEngine.__metadata.tables_info.get(this.name);
            ArrayList<ValueField[]> data = pc.get_me_data(table_info);
            Draw.drawTable(table_info, data);
            rf.close();
        } catch (IOException e) {
        }
    }

    public void describe() {
        MetaData tb = new MetaData(path);
        tb.selectRows();
    }
}
