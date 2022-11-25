package DavisBase.Pages;

import java.io.IOException;
import java.io.RandomAccessFile;

public class IndexInteriorPage extends Page {

    public IndexInteriorPage(RandomAccessFile page, boolean create_new) throws IOException {
        super(page, create_new);
        super.offset0 = Page.supported_offset[2];
    }

    @Override
    public byte[]

}
