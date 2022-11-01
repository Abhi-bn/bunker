package DavisBase.Util;

import java.io.File;
import java.io.RandomAccessFile;

public class CommonUse {
    public static RandomAccessFile createNewFile(String name, byte[] col_data) {
        File file = new File(name);
        try {
            return new RandomAccessFile(file, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generatePath(String... name) {
        assert name.length != 0;
        String str = name[0];
        for (int i = 1; i < name.length; i++) {
            str += "/" + name[i];
        }
        return str;
    }
}
