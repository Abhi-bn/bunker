package DavisBase.Pages;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

class TableInteriorPage extends Page {
    public TableInteriorPage(RandomAccessFile page, boolean create_new) throws IOException, EOFException {
        super(page, create_new);
        if (create_new)
            super.offset0 = Page.supported_offset[1];
    }
}
