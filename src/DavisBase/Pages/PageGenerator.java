package DavisBase.Pages;

import java.io.RandomAccessFile;

public final class PageGenerator {
    public static Page generatePage(Page.PageType type, RandomAccessFile page, boolean create_new) {
        switch (type) {
            case IndexInterior:
                throw new UnsupportedOperationException("Not implemented Yet");
            case TableInterior:
                throw new UnsupportedOperationException("Not implemented Yet");
            case IndexLeaf:
                return new IndexLeafPage(page, create_new);
            case TableLeaf:
                return new TableLeafPage(page, create_new);
        }
        return new TableLeafPage(page, create_new);
    }
}
