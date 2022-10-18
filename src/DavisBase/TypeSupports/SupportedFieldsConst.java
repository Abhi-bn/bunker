package DavisBase.TypeSupports;

import java.util.*;

public final class SupportedFieldsConst {
    public static int NULLABLE = 0;
    public static int UNIQUE = 1;

    public static String sUNIQUE = "UNIQUE";
    public static String sNULLABLE = "IS_NULLABLE";

    public static Map<String, Integer> FieldsToInt = new HashMap<>();
    public static Map<Integer, String> FieldsToString = new HashMap<>();

    static {
        FieldsToInt.put(sNULLABLE, NULLABLE);
        FieldsToInt.put(sUNIQUE, UNIQUE);

        FieldsToString.put(NULLABLE, sNULLABLE);
        FieldsToString.put(UNIQUE, sUNIQUE);
    }
}
