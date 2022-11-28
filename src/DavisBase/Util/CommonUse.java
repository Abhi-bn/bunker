package DavisBase.Util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

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

}
