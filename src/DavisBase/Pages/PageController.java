package DavisBase.Pages;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.DavisBaseExceptions;

public class PageController {
    RandomAccessFile access_file;
    boolean create_new = false;

    public PageController(RandomAccessFile access_file, boolean create_new) {
        this.access_file = access_file;
        this.create_new = create_new;
    }

    public ArrayList<ValueField[]> get_me_data(ColumnField[] column) {
        ArrayList<ValueField[]> arr = new ArrayList<>();
        long pos = 0;
        try {
            while (true) {
                System.out.println(access_file.length());
                if (pos >= access_file.length())
                    break;
                Page pg = PageGenerator.generatePage(Page.PageType.TableLeaf, access_file, create_new);
                ValueField[][] for_this_page = pg.getAllData(column);
                for (int i = 0; i < for_this_page.length; i++) {
                    arr.add(for_this_page[i]);
                }
                pos += pg.GetPageEndSize();
                access_file.seek(pos);
            }
        } catch (DavisBaseExceptions.PageOverflow e) {
            throw new IllegalStateException(e);
        } catch (EOFException e) {

        } catch (IOException e) {

        }
        return arr;
    }

    public void insert_data(ValueField[][] data) {
        int pos = 0;
        int rows_inserted = 0;
        boolean extend = false;
        while (true) {
            if (rows_inserted == data.length)
                break;
            try {
                Page pg = null;
                if (extend) {
                    extend = false;
                    // extend the file first
                    long old = access_file.length();
                    access_file.setLength(access_file.length() + 512);
                    access_file.seek(old);
                    pg = PageGenerator.generatePage(Page.PageType.TableLeaf, access_file, true);
                } else {
                    pg = PageGenerator.generatePage(Page.PageType.TableLeaf, access_file, false);
                }

                pos += pg.GetPageEndSize();
                for (int i = rows_inserted; i < data.length; i++) {
                    rows_inserted += pg.insertData(data[i]) ? 1 : 0;
                }

                access_file.seek(pos);
            } catch (DavisBaseExceptions.PageOverflow e) {
                extend = true;
            } catch (EOFException e) {
                extend = true;
            } catch (IOException e) {
                extend = true;
            }
        }
    }

    public void insert_data_index(ValueField[][] data) {

    }

}
