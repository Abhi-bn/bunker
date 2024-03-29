package DavisBase.Pages;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import DavisBase.DDL.MetaData;
import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.ValueField;
import DavisBase.Util.CommonUse;
import DavisBase.Util.DavisBaseExceptions;
import DavisBase.Util.Settings;
import DavisBase.Util.DavisBaseExceptions.PageOverflow;

public class Page {
    public enum PageType {
        IndexInterior,
        TableInterior,
        IndexLeaf,
        TableLeaf,
        NotKnownYet
    };

    final static short BYTE_LEN = 8;
    final static short COL_META = 2;
    final static short HEADER_SIZE = 16;
    final boolean verbose = true;

    final static int[] supported_offset = {
            /* Interior Index B-Tree Page */2,
            /* Interior Table B-Tree Page */5,
            /* Leaf Index B-Tree Page */10,
            /* Leaf Table B-Tree Page */13 };
    RandomAccessFile _page;

    /* Header infos */
    int offset0;
    int offset1 = 0;
    int offset2;
    int offset4;
    int offset6;
    int offset0A = 0xFFFFFFFF;
    int offset0E = 0;
    int[] offset10;
    byte[] page_data;
    /* Data infos */
    int col_size;
    byte[][] all_data;

    public PageType getPageType() {
        switch (offset0) {
            case 2:
                return PageType.IndexInterior;
            case 5:
                return PageType.TableInterior;
            case 10:
                return PageType.IndexLeaf;
            case 13:
                return PageType.TableLeaf;
            default:
                System.exit(0);
        }
        return PageType.NotKnownYet;
    }

    public void set_initial_state() {
        offset2 = 0;
        offset4 = Settings.getPageSize();
        offset6 = 0;
        offset0A = 0xFFFFFFFF;
        offset10 = new int[offset2];
        all_data = new byte[0][];
        page_data = new byte[Settings.getPageSize()];
    }

    private boolean safe_to_store(byte[] data, int cols) {
        int space_occ = HEADER_SIZE + 2 * offset2;
        return offset4 - space_occ > data.length;
    }

    private byte[] make_byte_row(ValueField[] data) {
        int[] cols = new int[data.length];
        int total_size = 0;
        byte[][] store = new byte[data.length][];

        for (int i = 0; i < data.length; i++) {
            byte[] c = data[i].getByteValue();
            total_size += c.length;
            store[data[i].getOrder()] = c;
            cols[i] = total_size;
        }

        byte[] b = new byte[total_size + cols.length * 2 + 4];
        int l = 0;
        byte[] buffer = CommonUse.intToByteArr(0, 2);
        for (int j = 0; j < buffer.length; j++)
            b[l++] = buffer[j];

        buffer = CommonUse.intToByteArr(b.length - 4, 2);
        for (int j = 0; j < buffer.length; j++)
            b[l++] = buffer[j];

        for (int j = 0; j < cols.length; j++) {
            buffer = CommonUse.intToByteArr(cols[j], 2);
            for (int k = 0; k < buffer.length; k++)
                b[l++] = buffer[k];
        }

        for (int i = 0; i < store.length; i++) {
            for (int j = 0; j < store[i].length; j++) {
                b[l++] = store[i][j];
            }
        }
        return b;
    }

    private byte[] write_page_back() {
        byte[] header = makeHeader();
        for (int i = 0; i < header.length; i++) {
            page_data[i] = header[i];
        }
        for (int i = 0; i < offset2; i++) {
            if (all_data[i] == null)
                continue;
            for (int j = 0; j < all_data[i].length; j++) {
                page_data[offset10[i] + j] = all_data[i][j];
            }
        }
        return page_data;
    }

    public boolean insertData(ValueField[] data) throws PageOverflow, IOException {
        byte[] byte_row = make_byte_row(data);
        if (!safe_to_store(byte_row, data.length)) {
            throw new DavisBaseExceptions.PageOverflow();
        }

        offset2 += 1;
        int[] new_pos = new int[offset2];
        for (int i = 0; i < offset10.length; i++) {
            new_pos[i] = offset10[i];
        }
        offset4 -= byte_row.length;
        offset10 = new_pos;
        offset10[offset2 - 1] = offset4;
        byte[][] new_all_data = new byte[offset2][];
        for (int i = 0; i < all_data.length; i++) {
            new_all_data[i] = all_data[i];
        }
        all_data = new_all_data;
        all_data[offset2 - 1] = byte_row;

        page_writer();

        return true;
    }

    public void loadHeader(byte[] data, boolean init) {
        if (init) {
            set_initial_state();
            return;
        }

        byte[] __offset0 = new byte[1];
        for (int j = 0, k = 0; j < __offset0.length; j++, k++)
            __offset0[j] = data[k];
        offset0 = CommonUse.byteArrToInt(__offset0, 1);

        byte[] __offset2 = new byte[2];
        for (int j = 0, k = 2; j < __offset2.length; j++, k++)
            __offset2[j] = data[k];
        offset2 = CommonUse.byteArrToInt(__offset2, 2);

        byte[] __offset4 = new byte[2];
        for (int j = 0, k = 4; j < __offset4.length; j++, k++)
            __offset4[j] = data[k];
        offset4 = CommonUse.byteArrToInt(__offset4, 2);

        byte[] __offset6 = new byte[4];
        for (int j = 0, k = 6; j < __offset6.length; j++, k++)
            __offset6[j] = data[k];
        offset6 = CommonUse.byteArrToInt(__offset6, 4);

        byte[] __offset0A = new byte[4];
        for (int j = 0, k = 10; j < __offset0A.length; j++, k++)
            __offset0A[j] = data[k];
        offset0A = CommonUse.byteArrToInt(__offset0A, 4);
        // ignore 2 unused bytes
        int[] __offset10 = new int[offset2];
        for (int j = 0, k = 16; j < offset2; j++) {
            byte[] __each_start = new byte[2];
            for (int i = 0; i < 2; i++) {
                __each_start[i] = data[k + j * 2 + i];
            }
            __offset10[j] = CommonUse.byteArrToInt(__each_start, 2);
        }
        offset10 = __offset10;
        all_data = new byte[offset2][];
    }

    private byte[] makeHeader() {
        byte[] data = new byte[16 + 2 * offset2];
        int i = 0;
        byte[] __offset0 = CommonUse.intToByteArr(offset0, 1);
        for (int j = 0; j < __offset0.length; j++)
            data[i++] = __offset0[j];

        byte[] __offset1 = CommonUse.intToByteArr(offset1, 1);
        for (int j = 0; j < __offset1.length; j++)
            data[i++] = __offset1[j];

        byte[] __offset2 = CommonUse.intToByteArr(offset2, 2);
        for (int j = 0; j < __offset2.length; j++)
            data[i++] = __offset2[j];

        byte[] __offset4 = CommonUse.intToByteArr(offset4, 2);
        for (int j = 0; j < __offset4.length; j++)
            data[i++] = __offset4[j];

        byte[] __offset6 = CommonUse.intToByteArr(offset6, 4);
        for (int j = 0; j < __offset6.length; j++)
            data[i++] = __offset6[j];

        byte[] __offset0A = CommonUse.intToByteArr(offset0A, 4);
        for (int j = 0; j < __offset0A.length; j++)
            data[i++] = __offset0A[j];

        byte[] __offset0E = CommonUse.intToByteArr(offset0E, 2);
        for (int j = 0; j < __offset0E.length; j++)
            data[i++] = __offset0E[j];

        for (int j = 0; j < offset2; j++) {
            byte[] __offset10 = CommonUse.intToByteArr(offset10[j], 2);
            for (int k = 0; k < __offset10.length; k++)
                data[i++] = __offset10[k];
        }
        return data;
    }

    public Page(RandomAccessFile page, boolean create_new) throws IOException {
        _page = page;
        page_reader(create_new);
    }

    public ValueField[] extract_from_data(byte[] row_info, ColumnField[] column) {
        ValueField[] val = new ValueField[column.length];
        int[] pos = new int[column.length];
        for (int i = 0, j = 0; i < column.length * COL_META - 1; i += COL_META, j += 1) {
            byte[] _d = { row_info[i], row_info[i + 1] };
            pos[j] = CommonUse.byteArrToInt(_d, COL_META);
        }
        int start = COL_META * column.length;
        int l = 0;
        for (int i = 0; i < column.length; i++) {
            int data_s = pos[i] - l;
            byte[] bt = new byte[data_s];
            for (int j = l, k = 0; j < l + data_s; j++, k++) {
                bt[k] = row_info[start + j];
            }
            l += data_s;
            val[i] = new ValueField(bt, column[i]);
        }
        return val;
    }

    private int getLenOfRow(int offset) {
        byte[] s = { page_data[offset + 2], page_data[offset + 3] };
        return CommonUse.byteArrToInt(s, 2);
    }

    public byte[] get_each_row(int index, boolean with_row_buff) {
        int size = getLenOfRow(offset10[index]);
        // extend fof initial row buffer
        if (with_row_buff)
            size += 4;
        byte[] row_info = new byte[size];
        for (int j = 0; j < size; j++) {
            if (with_row_buff) {
                row_info[j] = page_data[offset10[index] + j];
            } else {
                row_info[j] = page_data[offset10[index] + 4 + j];
            }
        }
        return row_info;
    }

    public void loadAllDataInBytes() {
        for (int i = 0; i < offset2; i++) {
            all_data[i] = get_each_row(i, true);
        }
    }

    public ValueField[][] getAllData(ColumnField[] column) {
        ValueField[][] each_row = new ValueField[offset2][];
        for (int i = 0; i < offset2; i++) {
            byte[] row = get_each_row(i, false);
            each_row[i] = extract_from_data(row, column);
        }
        return each_row;
    }

    public boolean removeTheseData(ValueField[] data, ColumnField[] columns) throws IOException {
        loadAllDataInBytes();
        ValueField[][] _data = getAllData(columns);
        ArrayList<Integer> _to_data = new ArrayList<>();
        for (int i = 0; i < _data.length; i++) {
            int match = 0;
            for (int j = 0; j < data.length; j++) {
                ValueField val = MetaData.getMeColumnFromName(_data[i], data[j].getName());
                if (val.compare(data[j]))
                    match += 1;
            }
            if (match == data.length) {
                offset2 -= 1;
                _to_data.add(i);
            }
        }
        byte[][] _all_data = new byte[offset2][];
        for (int i = 0, k = 0; i < this.all_data.length; i++) {
            if (_to_data.contains(i))
                continue;
            _all_data[k++] = this.all_data[i];
        }
        this.all_data = _all_data;

        offset4 = Settings.getPageSize();
        int[] _new_offset10 = new int[offset2];
        // now update the offset positions also
        for (int j = this.all_data.length - 1; j >= 0; j--) {
            offset4 -= this.all_data[j].length;
            _new_offset10[j] = offset4;
        }
        this.offset10 = _new_offset10;
        page_writer();
        return true;
    }

    public int GetPageEndSize() {
        return Settings.getPageSize();
    }

    // only place you want to seek as every read and write need page pointer to
    // reset
    private void page_writer() throws IOException {
        long old_seek = _page.getFilePointer();
        _page.write(write_page_back());
        _page.seek(old_seek);
    }

    private void page_reader(boolean readOnly) throws IOException {
        long old_seek = _page.getFilePointer();
        page_data = new byte[Settings.getPageSize()];
        _page.read(page_data, 0, Settings.getPageSize());
        loadHeader(page_data, readOnly);
        _page.seek(old_seek);
    }

    private byte[] make_byte_row_index(ValueField[] data) {
        int[] cols = new int[data.length];
        int total_size = 0;
        byte[][] store = new byte[data.length][];

        for (int i = 0; i < data.length; i++) {
            byte[] c = data[i].getByteValue();
            total_size += c.length;
            store[data[i].getOrder()] = c;
            cols[i] = total_size;
        }

        byte[] b = new byte[total_size + cols.length * 2 + 4];
        int l = 0;
        byte[] buffer = CommonUse.intToByteArr(0, 2);
        for (int j = 0; j < buffer.length; j++)
            b[l++] = buffer[j];

        buffer = CommonUse.intToByteArr(b.length - 4, 2);
        for (int j = 0; j < buffer.length; j++)
            b[l++] = buffer[j];

        for (int j = 0; j < cols.length; j++) {
            buffer = CommonUse.intToByteArr(cols[j], 2);
            for (int k = 0; k < buffer.length; k++)
                b[l++] = buffer[k];
        }

        for (int i = 0; i < store.length; i++) {
            for (int j = 0; j < store[i].length; j++) {
                b[l++] = store[i][j];
            }
        }
        return b;
    }

    public boolean insertDataIndex(ValueField[] data) throws PageOverflow, IOException {
        byte[] byte_row = make_byte_row_index(data);
        if (!safe_to_store(byte_row, data.length)) {
            throw new DavisBaseExceptions.PageOverflow();
        }

        offset2 += 1;
        int[] new_pos = new int[offset2];
        for (int i = 0; i < offset10.length; i++) {
            new_pos[i] = offset10[i];
        }
        offset4 -= byte_row.length;
        offset10 = new_pos;
        offset10[offset2 - 1] = offset4;
        byte[][] new_all_data = new byte[offset2][];
        for (int i = 0; i < all_data.length; i++) {
            new_all_data[i] = all_data[i];
        }
        all_data = new_all_data;
        all_data[offset2 - 1] = byte_row;

        page_writer();

        return true;
    }
}
