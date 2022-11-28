package DavisBase.Util;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.annotation.Retention;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

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

    public static int byteArrToInt(byte[] b, int bytesSize) {
        int val = 0;
        for (int i = 0; i < bytesSize; i++)
            val += ((b[bytesSize - i - 1] & 0xff) << (i * 8));
        return val;
    }

    public static byte[] intToByteArr(int integer, int bytesSize) {
        byte[] bytes = new byte[bytesSize];
        for (int i = 0; i < bytesSize; i++)
            bytes[bytesSize - i - 1] = (byte) (integer >>> (i * 8));
        return bytes;
    }

    public static byte[] doubleToByteArray(double value, int bytesSize) {
        byte[] bytes = new byte[bytesSize];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double byteArrayToDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public static byte[] floatToByteArray(float value, int bytesSize) {
        byte[] bytes = new byte[bytesSize];
        ByteBuffer.wrap(bytes).putFloat(value);
        return bytes;
    }

    public static float byteArrayToFloat(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getFloat();
    }

    public static String[] createQueryString(String command, int wordsToRemove) {
        for (int i = 0; i < wordsToRemove; i++) {
            String[] arr = command.split(" ", 2);
            command = arr[1];
        }
        String createColumnString[] = command.split(",");
        for (int i = 0; i < createColumnString.length; i++) {
            createColumnString[i] = createColumnString[i].trim();
        }
        return createColumnString;
    }

    public static String removeBegning(String command, int wordsToRemove) {
        for (int i = 0; i < wordsToRemove; i++) {
            String[] arr = command.split(" ", 2);
            command = arr[1];
        }
        return command;
    }

    public static String removeEnd(String command, int wordsToRemove) {
        for (int i = 0; i < wordsToRemove; i++) {
            int index = command.lastIndexOf(" ");
            command = command.substring(0, index);
        }
        return command;
    }

    public static String[] splitGenerator(String command, String splitBy) {
        String splitRes[] = command.split(splitBy);
        for (int i = 0; i < splitRes.length; i++) {
            splitRes[i] = splitRes[i].trim();
        }
        return splitRes;
    }

    public static Map<String, String[]> updateQueryPrep(String command) {
        Map<String, String[]> result = new HashMap<>();
        command = CommonUse.removeBegning(command, 3);
        String updateValues = CommonUse.splitGenerator(command, "where")[0];
        String updateCondition = CommonUse.splitGenerator(command, "where")[1];
        String[] updateValuesArray = CommonUse.splitGenerator(updateValues, ",");
        String[] updateConditionssArray = CommonUse.splitGenerator(updateCondition, ",");
        String[] where = new String[updateConditionssArray.length * 2];
        String[] values = new String[updateValuesArray.length * 2];
        int i = 0;
        for (String string : updateConditionssArray) {
            String[] temp = string.split("=");
            for (String string2 : temp) {
                where[i] = string2.strip();
                i++;
            }
        }
        i = 0;
        for (String string : updateValuesArray) {
            String[] temp = string.split("=");
            for (String string2 : temp) {
                values[i] = string2.strip();
                i++;
            }
        }
        result.put("where", where);
        result.put("values", values);
        return result;
    }

    public static String[] whereClauseSelect(String command) {
        command = CommonUse.removeBegning(command, 3);
        String whereCondition = CommonUse.splitGenerator(command, "where")[1];
        String[] whereConditionssArray = CommonUse.splitGenerator(whereCondition, ",");
        String[] whereColumns = new String[whereConditionssArray.length * 2];
        int i = 0;
        for (String string : whereConditionssArray) {
            String[] temp = string.split("=");
            for (String string2 : temp) {
                whereColumns[i] = string2.strip();
                i++;
            }
        }
        return whereColumns;
    }

    public static String[] deletePrep(String command) {
        command = CommonUse.removeBegning(command, 3);
        String whereCondition = CommonUse.splitGenerator(command, "where")[1];
        String[] whereConditionssArray = CommonUse.splitGenerator(whereCondition, ",");
        String[] whereColumns = new String[whereConditionssArray.length * 2];
        int i = 0;
        for (String string : whereConditionssArray) {
            String[] temp = string.split("=");
            for (String string2 : temp) {
                whereColumns[i] = string2.strip();
                i++;
            }
        }
        return whereColumns;
    }
}
