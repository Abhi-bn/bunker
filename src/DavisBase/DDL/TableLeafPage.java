package DavisBase.DDL;

import java.io.RandomAccessFile;

public class TableLeafPage extends Page {
    public TableLeafPage(RandomAccessFile page, boolean create_new) {
        super(page, create_new);
        super.offset0 = Page.supported_offset[3];
    }
}
