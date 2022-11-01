package DavisBase.Pages;

import java.io.RandomAccessFile;

class IndexLeafPage extends Page {
    public IndexLeafPage(RandomAccessFile page, boolean create_new) {
        super(page, create_new);
        super.offset0 = Page.supported_offset[2];
    }
}
