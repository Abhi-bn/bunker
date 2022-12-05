package DavisBase.Pages;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import DavisBase.DDL.MetaData;
import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.DavisBaseExceptions;

public class BPlusTreeController {
    RandomAccessFile access_file;
    boolean create_new = false;

    public BPlusTreeController(RandomAccessFile access_file, boolean create_new) {
        this.access_file = access_file;
        this.create_new = create_new;
    }

    public ArrayList<ValueField[]> select_all_data(ColumnField[] column) {
        ArrayList<ValueField[]> arr = new ArrayList<>();
        long pos = 0;
        try {
            while (true) {
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

    public ArrayList<ValueField[]> select_data(ValueField[] data, ValueField[] req_col, ColumnField[] column) {
        ArrayList<ValueField[]> arr = select_all_data(column);
        ArrayList<Integer> _to_data = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            int match = 0;
            for (int j = 0; j < data.length; j++) {
                ValueField val = MetaData.getMeColumnFromName(arr.get(i), data[j].getName());
                if (val.compare(data[j]))
                    match += 1;
            }
            if (match == data.length)
                _to_data.add(i);
        }
        ArrayList<ValueField[]> filtered_data = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (!_to_data.contains(i))
                continue;
            filtered_data.add(arr.get(i));
        }
        ArrayList<ValueField[]> filter = new ArrayList<>();
        for (int i = 0; i < filtered_data.size(); i++) {
            ValueField[] fil = new ValueField[req_col.length];
            for (int j = 0; j < req_col.length; j++) {
                ValueField val = MetaData.getMeColumnFromName(filtered_data.get(i), req_col[j].getName());
                fil[j] = new ValueField(val.getValue(), req_col[j]);
            }
            filter.add(fil);
        }

        return filter;
    }

    public void insert_data(ValueField[][] data) {
        int pos = 0;
        int rows_inserted = 0;
        while (true) {
            if (rows_inserted == data.length)
                break;
            try {
                Page pg = null;

                Boolean extend = pos >= access_file.length();

                if (extend) {
                    // extend the file first
                    long old = access_file.length();
                    access_file.setLength(access_file.length() + 512);
                    access_file.seek(old);
                    pg = PageGenerator.generatePage(Page.PageType.TableLeaf, access_file, true);
                } else {
                    access_file.seek(pos);
                    pg = PageGenerator.generatePage(Page.PageType.TableLeaf, access_file, false);
                }

                pos += pg.GetPageEndSize();
                for (int i = rows_inserted; i < data.length; i++) {
                    rows_inserted += pg.insertData(data[i]) ? 1 : 0;
                }
            } catch (DavisBaseExceptions.PageOverflow e) {
            } catch (EOFException e) {
            } catch (IOException e) {
            }
        }
    }

    public int delete_data(ValueField[] data, ColumnField[] columns) {
        int pos = 0;
        int rows_deleted = 0;
        while (true) {
            try {
                if (pos >= access_file.length())
                    break;
                Page pg = null;

                pg = PageGenerator.generatePage(Page.PageType.TableLeaf, access_file, false);
                pos += pg.GetPageEndSize();
                rows_deleted += pg.removeTheseData(data, columns) ? 1 : 0;
                access_file.seek(pos);
            } catch (DavisBaseExceptions.PageOverflow e) {
                System.out.println(e);
            } catch (EOFException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return rows_deleted;
    }

    public void update_data(ValueField[] data, ValueField[] condition, ColumnField[] columns) {
        ValueField[] dum = ValueField.ConvertToValue(columns);

        ArrayList<ValueField[]> to_be_updated = select_data(condition, dum, columns);
        for (int i = 0; i < to_be_updated.size(); i++) {
            for (int j = 0; j < data.length; j++) {
                to_be_updated.get(i)[data[j].getOrder()].setValue(data[j].getValue());
            }
        }
        ValueField[][] values = new ValueField[to_be_updated.size()][columns.length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                values[i][j] = to_be_updated.get(i)[j];
            }
        }
        // Resetting the seeker
        try {
            access_file.seek(0);
        } catch (IOException e) {

        }
        delete_data(condition, columns);
        try {
            access_file.seek(0);
        } catch (IOException e) {

        }
        insert_data(values);
    }

    public void insert_data_index(ValueField[][] data) {
        System.out.println(access_file);
        int pos = 0;
        int rows_inserted = 0;
        while (true) {
            if (rows_inserted == data.length)
                break;
            try {
                Page pg = null;
                System.out.println(access_file.length());
                boolean extend = pos >= access_file.length();
                if (extend) {
                    extend = false;
                    // extend the file first
                    long old = access_file.length();
                    access_file.setLength(access_file.length() + 512);
                    access_file.seek(old);
                    pg = PageGenerator.generatePage(Page.PageType.IndexLeaf, access_file, true);
                } else {
                    access_file.seek(pos);
                    pg = PageGenerator.generatePage(Page.PageType.IndexLeaf, access_file, false);
                }

                pos += pg.GetPageEndSize();
                for (int i = rows_inserted; i < data.length; i++) {
                    rows_inserted += pg.insertDataIndex(data[i]) ? 1 : 0;
                }
            } catch (DavisBaseExceptions.PageOverflow e) {
            } catch (EOFException e) {
            } catch (IOException e) {
            }
        }
    }
}
