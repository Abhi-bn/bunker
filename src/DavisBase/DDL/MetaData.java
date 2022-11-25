package DavisBase.DDL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.DavisBaseExceptions;
import DavisBase.Util.Draw;
import DavisBase.Pages.Page;
import DavisBase.Pages.PageController;
import DavisBase.Pages.PageGenerator;
import DavisBase.TypeSupports.ColumnField;

public class MetaData extends Table {
    enum ColumnIndexes {
        _id, TABLE, COLUMN, TYPE, BYTES, UNIQUE, NULLABLE, ORDER, ACCESS, VALUE
    };

    HashMap<String, ValueField[]> tables_info = new HashMap<>();

    int total_rows = 0;

    ColumnField[] AllFields;
    ValueField[][] fields;
    String[][] meta_info;

    {
        String[][] info = {
                { "_ID", "INT", "UNIQUE" },
                { "TABLE", "TEXT", "UNIQUE" },
                { "COLUMN", "TEXT" },
                { "TYPE", "SMALLINT" },
                { "BYTES", "SMALLINT" },
                { "UNIQUE", "TINYINT" },
                { "NULLABLE", "TINYINT" },
                { "ORDER", "SMALLINT" },
                { "ACCESS", "SMALLINT" },
                { "VALUE", "INT" }
        };
        meta_info = info;

        AllFields = new ColumnField[meta_info.length];
        for (int i = 0; i < meta_info.length; i++) {
            AllFields[i] = new ColumnField(meta_info[i], i);
        }

        fields = colToValues("METADATA", AllFields, 0);
        create();
    }

    ValueField[][] colToValues(String table_name, ColumnField[] allFields, int row) {
        ValueField[][] fields = new ValueField[allFields.length][];
        for (int i = 0; i < allFields.length; i++) {
            ValueField[] info = new ValueField[meta_info.length];
            info[0] = new ValueField(row + i, meta_info[ColumnIndexes._id.ordinal()], ColumnIndexes._id.ordinal());
            info[1] = new ValueField(
                    table_name, meta_info[ColumnIndexes.TABLE.ordinal()],
                    ColumnIndexes.TABLE.ordinal());
            info[2] = new ValueField(
                    allFields[i].getName(), meta_info[ColumnIndexes.COLUMN.ordinal()],
                    ColumnIndexes.COLUMN.ordinal());
            info[3] = new ValueField(
                    allFields[i].getType(), meta_info[ColumnIndexes.TYPE.ordinal()],
                    ColumnIndexes.TYPE.ordinal());
            info[4] = new ValueField(
                    allFields[i].getBytes(), meta_info[ColumnIndexes.BYTES.ordinal()],
                    ColumnIndexes.BYTES.ordinal());
            info[5] = new ValueField(
                    allFields[i].getUnique(), meta_info[ColumnIndexes.UNIQUE.ordinal()],
                    ColumnIndexes.UNIQUE.ordinal());
            info[6] = new ValueField(
                    allFields[i].getNullable(), meta_info[ColumnIndexes.NULLABLE.ordinal()],
                    ColumnIndexes.NULLABLE.ordinal());
            info[7] = new ValueField(
                    allFields[i].getOrder(), meta_info[ColumnIndexes.ORDER.ordinal()],
                    ColumnIndexes.ORDER.ordinal());
            info[8] = new ValueField(
                    allFields[i].getAccess(), meta_info[ColumnIndexes.ACCESS.ordinal()],
                    ColumnIndexes.ACCESS.ordinal());
            info[9] = new ValueField(
                    0, meta_info[ColumnIndexes.VALUE.ordinal()],
                    ColumnIndexes.VALUE.ordinal());

            fields[i] = info;
        }
        return fields;
    }

    public MetaData(String base_path) {
        super(base_path, "MetaData");
    }

    @Override
    public boolean create(String... columns) {
        File f = new File(getFilePath());

        if (f.exists()) {
            selectRows();
            return false;
        }
        try {
            f.createNewFile();
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            Page pg = PageGenerator.generatePage(Page.PageType.TableLeaf, rf, true);
            // pg.createNewPage(rf);
            insert(fields);
            rf.close();
            selectRows();
        } catch (DavisBaseExceptions.PageOverflow e) {
        } catch (IOException e) {
        }
        return true;
    }

    public void insert(String table_name, ColumnField[] fields) {
        ValueField[][] vals = colToValues(table_name, fields, total_rows);
        insert(vals);
    }

    private ValueField get_table_name(ValueField[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i].getOrder() == ColumnIndexes.TABLE.ordinal())
                return v[i];
        }
        return null;
    }

    public void update_all_tables_info(ArrayList<ValueField[]> table_data) {
        HashMap<String, ArrayList<ValueField[]>> _tables_info = new HashMap<>();
        for (int i = 0; i < table_data.size(); i++) {
            ValueField name = get_table_name(table_data.get(i));
            if (_tables_info.get(name.getValue().toString()) == null) {
                _tables_info.put(name.getValue().toString(), new ArrayList<>());
            }
            _tables_info.get(name.getValue().toString()).add(table_data.get(i));
        }
        for (Map.Entry<String, ArrayList<ValueField[]>> values : _tables_info.entrySet()) {
            for (int i = 0; i < values.getValue().size(); i++) {
                Arrays.sort(values.getValue().get(i),
                        (ValueField arg0, ValueField arg1) -> arg0.getOrder() - arg1.getOrder());
                values.getValue().sort(new Comparator<ValueField[]>() {
                    @Override
                    public int compare(ValueField[] arg0, ValueField[] arg1) {
                        return (int) arg0[ColumnIndexes.ORDER.ordinal()].getValue()
                                - (int) arg1[ColumnIndexes.ORDER.ordinal()]
                                        .getValue();
                    }
                });
            }
        }
        for (Map.Entry<String, ArrayList<ValueField[]>> values : _tables_info.entrySet()) {
            ValueField[] table_d = new ValueField[values.getValue().size()];
            for (int i = 0; i < values.getValue().size(); i++) {
                ValueField vf = new ValueField(0, get_table_name(values.getValue().get(0)));
                vf.setName((String) values.getValue().get(i)[ColumnIndexes.COLUMN.ordinal()].getValue());
                vf.setType((int) values.getValue().get(i)[ColumnIndexes.TYPE.ordinal()].getValue());
                vf.setBytes((int) values.getValue().get(i)[ColumnIndexes.BYTES.ordinal()].getValue());
                vf.setUnique((int) values.getValue().get(i)[ColumnIndexes.UNIQUE.ordinal()].getValue() > 0);
                vf.setNullable((int) values.getValue().get(i)[ColumnIndexes.NULLABLE.ordinal()].getValue() > 0);
                vf.setOrder((int) values.getValue().get(i)[ColumnIndexes.ORDER.ordinal()].getValue());
                vf.setAccess((int) values.getValue().get(i)[ColumnIndexes.ACCESS.ordinal()].getValue());
                vf.setValue((int) values.getValue().get(i)[ColumnIndexes.VALUE.ordinal()].getValue());
                table_d[i] = vf;
            }
            tables_info.put(values.getKey(), table_d);
        }

        total_rows = _tables_info.size();
    }

    public void selectRows() {
        File f = new File(getFilePath());
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "r");
            PageController pc = new PageController(rf, false);
            ArrayList<ValueField[]> data = pc.get_me_data(AllFields);
            update_all_tables_info(data);
            Draw.drawTable(AllFields, data);
            rf.close();
        } catch (IOException e) {
        }
    }
}
