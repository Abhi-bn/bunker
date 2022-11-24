package DavisBase.Pages;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

class TableLeafPage extends Page {
    public TableLeafPage(RandomAccessFile page, boolean create_new) throws IOException, EOFException {
        super(page, create_new);
        super.offset0 = Page.supported_offset[3];
    }
}
