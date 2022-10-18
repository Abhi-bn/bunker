package DavisBase.TypeSupports;

import java.util.*;
import DavisBase.Util.CommonUse;

public class SupportedTypesConst {
    static boolean verbose = true;

    public static Map<String, Integer> TypesToInt = new HashMap<>();
    public static Map<Integer, String> TypesToString = new HashMap<>();
    public static Map<Integer, Integer> TypesToBytes = new HashMap<>();

    public static final Integer NULL = 0;
    public static final Integer TINYINT = 1;
    public static final Integer SMALLINT = 2;
    public static final Integer INT = 3;
    public static final Integer BIGINT = 4;
    public static final Integer FLOAT = 5;
    public static final Integer DOUBLE = 6;
    public static final Integer YEAR = 7;
    public static final Integer TIME = 8;
    public static final Integer DATETIME = 9;
    public static final Integer DATE = 10;
    public static final Integer TEXT = 11;

    static List<Integer> nums = Arrays.asList(TINYINT, SMALLINT, INT, BIGINT, FLOAT, DOUBLE, YEAR, TIME);
    static List<Integer> strs = Arrays.asList(DATETIME, TEXT, DATE);

    public static final String sNULL = "NULL";
    public static final String sTINYINT = "TINYINT";
    public static final String sSMALLINT = "SMALLINT";
    public static final String sINT = "INT";
    public static final String sBIGINT = "BIGINT";
    public static final String sFLOAT = "FLOAT";
    public static final String sDOUBLE = "DOUBLE";
    public static final String sYEAR = "YEAR";
    public static final String sTIME = "TIME";
    public static final String sDATETIME = "DATETIME";
    public static final String sDATE = "DATE";
    public static final String sTEXT = "TEXT";

    static {
        TypesToString.put(NULL, sNULL);
        TypesToString.put(TINYINT, sTINYINT);
        TypesToString.put(SMALLINT, sSMALLINT);
        TypesToString.put(INT, sINT);
        TypesToString.put(BIGINT, sBIGINT);
        TypesToString.put(FLOAT, sFLOAT);
        TypesToString.put(DOUBLE, sDOUBLE);
        TypesToString.put(YEAR, sYEAR);
        TypesToString.put(TIME, sTIME);
        TypesToString.put(DATETIME, sDATETIME);
        TypesToString.put(DATE, sDATE);
        TypesToString.put(TEXT, sTEXT);

        TypesToInt.put(sNULL, NULL);
        TypesToInt.put(sTINYINT, TINYINT);
        TypesToInt.put(sSMALLINT, SMALLINT);
        TypesToInt.put(sINT, INT);
        TypesToInt.put(sBIGINT, BIGINT);
        TypesToInt.put(sFLOAT, FLOAT);
        TypesToInt.put(sDOUBLE, DOUBLE);
        TypesToInt.put(sYEAR, YEAR);
        TypesToInt.put(sTIME, TIME);
        TypesToInt.put(sDATETIME, DATETIME);
        TypesToInt.put(sDATE, DATE);
        TypesToInt.put(sTEXT, TEXT);

        TypesToBytes.put(NULL, 0);
        TypesToBytes.put(TINYINT, 1);
        TypesToBytes.put(SMALLINT, 2);
        TypesToBytes.put(INT, 4);
        TypesToBytes.put(BIGINT, 8);
        TypesToBytes.put(FLOAT, 4);
        TypesToBytes.put(DOUBLE, 8);
        TypesToBytes.put(YEAR, 1);
        TypesToBytes.put(TIME, 4);
        TypesToBytes.put(DATETIME, 8);
        TypesToBytes.put(DATE, 8);
        TypesToBytes.put(TEXT, -1);
    }

    public static String getTypeStrBytes(String type) {
        CommonUse.Print(verbose, "Type:" + TypesToInt.get(type) + " Bytes:" + TypesToBytes.get(TypesToInt.get(type)));
        return String.valueOf(TypesToBytes.get(TypesToInt.get(type)));
    }

    public static boolean validateType(int type, String value) {
        // if (nums.contains(type)) {

        // }
        return true;
    }

    public static boolean validateType(String type, String value) {
        if (!isSupported(type))
            return false;

        return true;
    }

    public static boolean isSupported(String type) {
        CommonUse.Print(verbose, TypesToInt.containsKey(type.toUpperCase()));
        return TypesToInt.containsKey(type);
    }

    public static boolean isSupported(Integer type) {
        return TypesToString.containsKey(type);
    }
}
