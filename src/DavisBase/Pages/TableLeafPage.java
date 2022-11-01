package DavisBase.Pages;

import java.io.RandomAccessFile;

class TableLeafPage extends Page {
    public TableLeafPage(RandomAccessFile page, boolean create_new) {
        super(page, create_new);
        super.offset0 = Page.supported_offset[3];
    }
}
