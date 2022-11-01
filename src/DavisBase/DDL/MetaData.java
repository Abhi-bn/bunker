package DavisBase.DDL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.Draw;
import DavisBase.TypeSupports.ColumnField;

public class MetaData extends Table {
    enum ColumnIndexes {
        _id, TABLE, COLUMN, TYPE, BYTES, UNIQUE, NULLABLE, ORDER, ACCESS
    };

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
                { "ACCESS", "SMALLINT" }
        };
        meta_info = info;

        AllFields = new ColumnField[meta_info.length];
        for (int i = 0; i < meta_info.length; i++) {
            AllFields[i] = new ColumnField(meta_info[i], i);
        }

        fields = colToValues("METADATA", AllFields);
        create();
    }

    ValueField[][] colToValues(String table_name, ColumnField[] allFields) {
        ValueField[][] fields = new ValueField[allFields.length][];
        for (int i = 0; i < allFields.length; i++) {
            ValueField[] info = new ValueField[meta_info.length];
            info[0] = new ValueField(i, meta_info[ColumnIndexes._id.ordinal()], ColumnIndexes._id.ordinal());
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
        if (f.exists())
            return false;
        try {
            f.createNewFile();
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            TableLeafPage tlp = new TableLeafPage(rf, true);
            tlp.createNewPage(rf);
            insert(fields);
            rf.close();
        } catch (IOException e) {

        }
        return true;
    }

    // @Override
    // public void insert(ValueField[][] fields) {
    // throw new UnsupportedOperationException("Do not use this method for MetaData
    // Table");
    // }

    public void insert(String table_name, ColumnField[] fields) {
        ValueField[][] vals = colToValues(table_name, fields);
        insert(vals);
    }

    public void selectRows() {
        File f = new File(getFilePath());
        try {
            RandomAccessFile rf = new RandomAccessFile(f, "rw");
            TableLeafPage tlp = new TableLeafPage(rf, false);
            Draw.drawTable(AllFields, tlp.getAllData(AllFields));
            rf.close();
        } catch (IOException e) {
        }
    }
}
