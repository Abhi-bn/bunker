package DavisBase.Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.SupportedTypesConst;
import DavisBase.TypeSupports.ValueField;

public class Draw {
    public static void drawTable(ColumnField[] column, ArrayList<ValueField[]> values) {
        int[] format = new int[column.length];
        int[] special = new int[column.length];
        int total_length = 0;

        for (int j = 0; j < column.length; j++) {
            if (column[j].getBytes() == 0) {
                format[j] = 20;
            } else if (column[j].getType() == 9) {
                format[j] = 40;
            } else if (column[j].getType() > 7) {
                format[j] = 20;
            } else if (column[j].getType() > 5) {
                format[j] = 10;
            } else {
                format[j] = column[j].getName().length() + 1;
            }
            int l = String.format("%" + String.valueOf(
                    format[j]) + "s │ ",
                    column[j].getName()).length();
            total_length += l;
            special[j] = total_length - 1;
        }
        // Draw Header
        draw_line(special, total_length, 0);
        draw_each_column(format, column);
        System.out.println();
        draw_line(special, total_length, 1);

        // Draw the Data
        for (int i = 0; i < values.size(); i++) {
            draw_each_field(format, values.get(i), true);
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

    private static void draw_each_column(int[] format, ColumnField[] vf) {
        for (int j = 0; j < vf.length; j++) {
            if (j == 0)
                System.out.print('│');
            String center = "";
            center = center(vf[j].getName(), format[j]);

            System.out.format("%" + String.valueOf(
                    format[j]) + "s │ ", center);
        }
    }

    private static void draw_each_field(int[] format, ValueField[] vf, boolean value) {
        for (int j = 0; j < vf.length; j++) {
            if (j == 0)
                System.out.print('│');
            String center = "";
            if (value) {
                if (vf[j].getType() == SupportedTypesConst.DATE) {
                    LocalDate date = LocalDate.ofInstant(((Date) vf[j].getValue()).toInstant(), ZoneId.systemDefault());
                    center = center(date, format[j]);
                } else {
                    center = center(vf[j].getValue(), format[j]);
                }
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
