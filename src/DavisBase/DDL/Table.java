package DavisBase.DDL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import DavisBase.DBEngine;
import DavisBase.Pages.Page;
import DavisBase.Pages.BPlusTreeController;
import DavisBase.Pages.PageGenerator;
import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.SupportedTypesConst;
import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.DavisBaseExceptions;
import DavisBase.Util.DavisBaseExceptions.DuplicateValueException;
import DavisBase.Util.DavisBaseExceptions.NullInsertException;
import DavisBase.Util.Draw;
import DavisBase.Util.Settings;

public class Table {
    static boolean verbose = true;
    String name;
    String path;

    public Table(String base_path, String table_name) {
        this.path = base_path;
        this.name = table_name.toUpperCase();
    }

    public boolean create(String... columns) {
        File f = new File(getFilePath());
        if (f.exists())
            return false;
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            DBEngine.__metadata.insert(name, columns);
            f.createNewFile();
            PageGenerator.generatePage(Page.PageType.TableLeaf, rf, true);
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
        new_data[0][0] = new ValueField(DBEngine.__metadata.tables_IDs.get(name) + 1, table_info[0]);
        for (int i = 1; i < table_info.length; i++) {
            ValueField fd = new ValueField(cols[i - 1], table_info[i]);
            // if (!fd.validate()) throw new ;
            new_data[0][i] = fd;
        }

        try {
            validateData(table_info, new_data[0]);
            insert(new_data);
            DBEngine.__metadata.updateID(name, DBEngine.__metadata.tables_IDs.get(name) + 1);
        } catch (NullInsertException e) {
            System.out.println("Trying to insert a null value to a not nullable field");
            return false;
        } catch (DuplicateValueException e) {
            System.out.println("Unique value constraint violated, data cannot be inserted");
            return false;
        }
        return true;
    }

    public void insert(ValueField[][] fields) {
        File f = new File(getFilePath());
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            BPlusTreeController pc = new BPlusTreeController(rf, false);
            pc.insert_data(fields);
            rf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void select(String table_name, String[] where, String[] cols) {
        ValueField[] table_info = DBEngine.__metadata.tables_info.get(this.name);

        if (!validateSelectFields(table_info, where, 3)) {
            System.out.println(Settings.columnNameError);
            return;
        }

        if (!validateSelectFields(table_info, cols, 1)) {
            System.out.println(Settings.columnNameError);
            return;
        }

        ValueField[] to_show = new ValueField[cols.length];
        for (int i = 0; i < cols.length; i += 1) {
            ValueField field = MetaData.getMeColumnFromName(table_info, cols[i].toUpperCase());
            to_show[i] = field;
        }
        ValueField[] filter = new ValueField[where.length / 3];
        if (cols.length == 0) {
            to_show = table_info;
        }

        for (int i = 0; i < where.length; i += 3) {
            ValueField field = MetaData.getMeColumnFromName(table_info, where[i].toUpperCase());
            field.setOp(where[i + 1]);
            field.setValue(where[i + 2]);
            filter[i / 2] = field;
        }
        select(table_name, filter, to_show, table_info);
    }

    public void select(String table_name, ValueField[] to_show, ValueField[] table_info, ColumnField[] column) {
        File f = new File(getFilePath());
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            BPlusTreeController pc = new BPlusTreeController(rf, false);
            ArrayList<ValueField[]> data = pc.select_data(to_show, table_info, column);
            Draw.drawTable(name, table_info, data);
            rf.close();
        } catch (IOException e) {

        }
    }

    public void describe() {
        ColumnField[] table_info = DBEngine.__metadata.tables_info.get(this.name);
        ArrayList<ValueField[]> values = new ArrayList<>();
        ValueField[] vf = new ValueField[table_info.length];
        String[] c = { "Tables", "Text" };
        for (int i = 0; i < table_info.length; i++) {
            ColumnField cf = table_info[i];
            vf[i] = new ValueField(SupportedTypesConst.TypesToString.get(cf.getType()), c, i);
        }
        values.add(vf);
        Draw.drawTable(name, table_info, values);
    }

    public int deleteTableValues(String[] cols) {
        ValueField[] table_info = DBEngine.__metadata.tables_info.get(this.name);

        if (!validateSelectFields(table_info, cols, 3)) {
            System.out.println(Settings.columnNameError);
            return 0;
        }

        ValueField[] to_delete = new ValueField[cols.length / 3];
        for (int i = 0; i < cols.length; i += 3) {
            ValueField field = MetaData.getMeColumnFromName(table_info, cols[i].toUpperCase());
            field.setValue(cols[i + 2]);
            field.setOp(cols[i + 1]);
            to_delete[i] = field;
        }
        int rows_deleted = deleteTableValues(to_delete, table_info);
        // DBEngine.__metadata.updateID(name, DBEngine.__metadata.tables_IDs.get(name) -
        // rows_deleted);
        return rows_deleted;
    }

    public int deleteTableValues(ValueField[] fields, ValueField[] columns) {
        File f = new File(getFilePath());
        int rows_deleted = 0;
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            BPlusTreeController pc = new BPlusTreeController(rf, false);
            rows_deleted = pc.delete_data(fields, columns);
            rf.close();
        } catch (IOException e) {
        }
        return rows_deleted;
    }

    public int updateInfo(String[] data, String[] cols) {
        ValueField[] table_info = DBEngine.__metadata.tables_info.get(this.name);
        if (!validateSelectFields(table_info, data, 2)) {
            System.out.println(Settings.columnNameError);
            return 0;
        }

        if (!validateSelectFields(table_info, cols, 2)) {
            System.out.println(Settings.columnNameError);
            return 0;
        }

        ValueField[] where = new ValueField[cols.length / 2];
        for (int i = 0; i < cols.length; i += 2) {
            ValueField field = MetaData.getMeColumnFromName(table_info, cols[i].toUpperCase());
            field.setValue(cols[i + 1]);
            where[i / 2] = field;
        }
        ValueField[] to_update_value = new ValueField[data.length / 2];
        for (int i = 0; i < data.length; i += 2) {
            ValueField field = MetaData.getMeColumnFromName(table_info, data[i].toUpperCase());
            field.setValue(data[i + 1]);
            to_update_value[i / 2] = field;
        }
        return updateInfo(to_update_value, where, table_info);
    }

    public int updateInfo(ValueField[] data, ValueField[] fields, ColumnField[] columns) {
        File f = new File(getFilePath());
        int rows_deleted = 0;
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            BPlusTreeController pc = new BPlusTreeController(rf, false);
            pc.update_data(data, fields, columns);
            rf.close();
        } catch (IOException e) {
        }
        return rows_deleted;
    }

    public String getIndexFilePath(String indexName) {
        return path + indexName + ".ndx";
    }

    public void insertIntoIndex(String tableName, String columnName, Object value) {
        ValueField[] cols = DBEngine.__metadata.tables_info.get(tableName);
        int colDataType = 0;
        boolean found = false;
        for (ValueField valueField : cols) {
            if (valueField.getName().equals(columnName)) {
                colDataType = valueField.getType();
                found = true;
                break;
            }
        }

        ValueField[][] valueField = new ValueField[1][4];
        if (found) {
            valueField[0][0] = new ValueField(123, cols[0]);
            valueField[0][0].setOrder(0);
            valueField[0][0].setName("numberOfIndexes");
            valueField[0][0].setType(3);
            valueField[0][0].setValue(0);

            valueField[0][1] = new ValueField(123, cols[0]);
            valueField[0][1].setOrder(1);
            valueField[0][1].setName("indexType");
            valueField[0][1].setType(3);
            valueField[0][1].setValue(colDataType);

            valueField[0][2] = new ValueField(123, cols[0]);
            valueField[0][2].setOrder(2);
            valueField[0][2].setName("indexValue");
            valueField[0][2].setType(colDataType);
            valueField[0][2].setValue(value);

            valueField[0][3] = new ValueField(123, cols[0]);
            valueField[0][3].setOrder(3);
            valueField[0][3].setName("indexRowId");
            valueField[0][3].setType(3);
            valueField[0][3].setValue(0);
        }

        else {
            System.out.println("Index column not found");
            return;
        }

        insertIndex(valueField);
    }

    public void insertIndex(ValueField[][] fields) {
        File f = new File(getIndexFilePath("Way.row_id"));
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            BPlusTreeController pc = new BPlusTreeController(rf, false);
            pc.insert_data_index(fields);
            rf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateData(ValueField[] table_info, ValueField[] newData)
            throws NullInsertException, DuplicateValueException {

        File f = new File(getFilePath());
        ArrayList<ValueField[]> data = new ArrayList<>();
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            BPlusTreeController pc = new BPlusTreeController(rf, false);
            data = pc.select_all_data(table_info);
            rf.close();
        } catch (IOException e) {
            return false;
        }
        for (ValueField newVf : newData) {
            if (!newVf.isNullValid()) {
                throw new DavisBaseExceptions.NullInsertException();
            }
        }

        for (ValueField[] existingVal : data) {
            for (ValueField newVf : newData) {
                if (!newVf.getUnique())
                    continue;
                ValueField valueField = MetaData.getMeColumnFromName(existingVal, newVf.getName());
                if (valueField.compare(newVf)) {
                    throw new DavisBaseExceptions.DuplicateValueException();
                }
            }
        }
        return true;
    }

    public boolean validateSelectFields(ValueField[] table_info, String[] columns, int skip) {
        int found = 0;
        for (ValueField info : table_info) {
            for (int i = 0; i < columns.length; i += skip) {
                if (columns[i].equalsIgnoreCase(info.getName())) {
                    found++;
                }
            }
        }
        return (columns.length / skip) == found;
    }

    public boolean exists() {
        File tableFile = new File(this.getFilePath());
        return tableFile.exists();
    }

    public boolean delete() {
        DBEngine.__metadata.deleteTableValues(this.name);
        File tableFile = new File(this.getFilePath());
        return tableFile.delete();
    }
}
