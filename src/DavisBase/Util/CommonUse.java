package DavisBase.Util;

import java.io.File;
import java.io.RandomAccessFile;

public class CommonUse {
    private static void newFile(String name, int size) {
        File file = new File(name);
        RandomAccessFile rFile;
        try {
            rFile = new RandomAccessFile(file, "rw");
            rFile.setLength(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createNewFile(String name) {
        newFile(name, 512);
    }

    public static String generatePath(String... name) {
        assert name.length != 0;
        String str = name[0];
        for (int i = 1; i < name.length; i++) {
            str += "/" + name[i];
        }
        return str;
    }

    public static void Print(boolean verbose, Object msg) {
        if (!verbose)
            return;
        StackTraceElement dyn = new Exception().getStackTrace()[1];
        System.out.println(dyn.getClassName().replace(".", "/") + ":" + dyn.getLineNumber() + " (" + dyn.getMethodName()
                + ") > " + msg);
    }
}
