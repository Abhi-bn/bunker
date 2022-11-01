package DavisBase.Util;

import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.ValueField;

public class Draw {
    public static void drawTable(ColumnField[] column, ValueField[][] values) {
        int[] format = new int[column.length];
        int[] special = new int[column.length];
        int total_length = 0;
        for (int j = 0; j < column.length; j++) {
            if (column[j].getBytes() == -1) {
                format[j] = 20;
            } else {
                format[j] = column[j].getName().length() + 1;
            }
            int l = String.format("%" + String.valueOf(
                    format[j]) + "s │ ",
                    column[j].getName()).length();
            total_length += l;
            special[j] = total_length - 1;
        }
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                draw_line(special, total_length, 0);
                draw_each_field(format, values[i], false);
                System.out.println();
                draw_line(special, total_length, 1);
            }
            draw_each_field(format, values[i], true);
            System.out.println();
        }
        draw_line(special, total_length, 2);
    }

    private static void draw_line(int[] special, int total, int type) {
        String[] top = { "─", "┌", "┬", "┐" };
        String[] mid = { "─", "├", "┼", "┤" };
        String[] bottom = { "─", "└", "┴", "┘" };

        String[] select = new String[0];
        switch (type) {
            case 0:
                select = top;
                break;
            case 1:
                select = mid;
                break;
            case 2:
                select = bottom;
                break;
            default:
                break;
        }
        for (int i = 0, k = 0; i < total; i++) {
            if (i == 0) {
                System.out.print(select[1]);
            } else if (i == total - 1) {
                System.out.print(select[3]);
            } else if (i == special[k]) {
                System.out.print(select[2]);
                k++;
            } else {
                System.out.print(select[0]);
            }
        }
        System.out.println();
    }

    private static void draw_each_field(int[] format, ValueField[] vf, boolean value) {
        for (int j = 0; j < vf.length; j++) {
            if (j == 0)
                System.out.print('│');
            String center = "";
            if (value) {
                center = center(vf[j].getValue(), format[j]);
            } else {
                center = center(vf[j].getName(), format[j]);
            }

            System.out.format("%" + String.valueOf(
                    format[j]) + "s │ ", center);
        }
    }

    private static String center(Object text, int len) {
        String out = String.format("%" + len + "s%s%" + len + "s", "", text, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }
}
