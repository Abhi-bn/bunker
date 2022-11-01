package DavisBase.Util;

public class Log {
    public static void DEBUG(boolean verbose, Object msg) {
        if (!verbose)
            return;
        StackTraceElement dyn = new Exception().getStackTrace()[1];
        System.out.println(dyn.getClassName().replace(".", "/") + ":" + dyn.getLineNumber() + " (" + dyn.getMethodName()
                + ") > " + msg);
    }
}
