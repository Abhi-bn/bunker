package DavisBase.Pages;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import DavisBase.Util.DavisBaseExceptions;

public final class PageGenerator {
    public static Page generatePage(Page.PageType type, RandomAccessFile page, boolean create_new)
            throws DavisBaseExceptions.PageOverflow, IOException, EOFException {
        switch (type) {
            case IndexInterior:
                return new IndexInteriorPage(page, create_new);
            case TableInterior:
                throw new UnsupportedOperationException("Not implemented Yet");
            case IndexLeaf:
                return new IndexLeafPage(page, create_new);
            case TableLeaf:
                return new TableLeafPage(page, create_new);
            case NotKnownYet:
                return new Page(page, create_new);
        }
        return new TableLeafPage(page, create_new);
    }
}
