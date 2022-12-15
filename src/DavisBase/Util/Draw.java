package DavisBase.Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import DavisBase.TypeSupports.ColumnField;
import DavisBase.TypeSupports.SupportedTypesConst;
import DavisBase.TypeSupports.ValueField;

public class Draw {

    private static int format_length(ColumnField[] column, ArrayList<ValueField[]> values, int[] special,
            int[] format) {
        for (int i = 0; i < column.length; i++) {
            int l = String.format("%s │ ", column[i].getName()).length();
            format[i] = Math.max(format[i], l);
            int k = String.format("%" + l + "s │ ", column[i].getName()).length();
            special[i] = Math.max(special[i], k);
        }
        for (int j = 0; j < values.size(); j++) {
            for (int i = 0; i < values.get(j).length; i++) {
                int l = String.format("%s │ ", values.get(j)[i].getValue()).length();
                format[i] = Math.max(format[i], l);
                int k = String.format("%" + l + "s │ ", values.get(j)[i].getValue()).length();
                special[i] = Math.max(special[i], k);
            }
        }
        int total_length = 0;
        for (int i = 0; i < format.length; i++) {
            total_length += special[i];
            special[i] = total_length - 1;
        }
        return total_length;
    }

    public static void drawTable(String name, ColumnField[] column, ArrayList<ValueField[]> values) {
        int[] format = new int[column.length];
        int[] special = new int[column.length];
        int count = 0;
        for (int i = 0; i < column.length; i++) {
            if (column[i].getAccess() == 1) {
                column[i].setName(name + "(P_KEY)");
                count += 1;
            }
        }

        if (count == 0) {
            for (int i = 0; i < column.length; i++) {
                if (column[i].getName().equals("_ID"))
                    column[i].setName(name + "(D_P_KEY)");
            }
        }

        int total_length = format_length(column, values, special, format);

        System.out.println(name);

        // Draw Header
        draw_line(special, total_length, 0);
        draw_each_column(format, column);
        draw_line(special, total_length, 1);
        // Draw the Data
        for (int i = 0; i < values.size(); i++)
            draw_each_field(format, values.get(i), true);
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
        System.out.println();
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
        System.out.println();
    }

    private static String center(Object text, int len) {
        String out = String.format("%" + len + "s%s%" + len + "s", "", text, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }
}
