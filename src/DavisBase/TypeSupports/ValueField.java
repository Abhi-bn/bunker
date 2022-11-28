package DavisBase.TypeSupports;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import DavisBase.Util.CommonUse;

public class ValueField extends ColumnField {

    Object value;

    public ValueField(Object value, String[] cols, int order) {
        super(cols, order);
        this.value = getMeObject(value);
    }

    public ValueField(Object value, ColumnField fd) {
        super(fd, fd.getOrder());
        this.value = getMeObject(value);
    }

    public Object getValue() {
        if (type == SupportedTypesConst.DATE) {

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                return formatter.parse(formatter.format((Date) value));
            } catch (Exception e) {
            }
        }
        return value;
    }

    @Override
    public Boolean isNullValid() {
        if (super.isNullValid())
            return true;
        // if (this.getValue())
        if (this.getValue() instanceof String) {
            return ((String) this.getValue()).length() != 0;
        }
        return true;
    }

    public void setValue(Object value) {
        this.value = getMeObject(value);
    }

    public boolean compare(ValueField vf) {
        // if (this.getValue() instanceof String) {
        // if (this.getValue().equals(vf.getValue()))
        // return true;
        // } else if (this.getValue() instanceof Integer) {
        return this.getValue().equals(vf.getValue());
        // return false;
    }

    private Object getMeObject(Object field) {
        if (field instanceof byte[]) {
            return ConvertObject((byte[]) field);
        }

        // If its not string it means its of the write type
        if (!(field instanceof String)) {
            return field;
        }

        if (this.getType() == SupportedTypesConst.TEXT) {
            return field;
        } else if (SupportedTypesConst.isIntTypes(this.getType())) {
            return (Integer) Integer.parseInt((String) field);
        } else if (getType() == SupportedTypesConst.FLOAT) {
            return Float.valueOf((String) field);
        } else if (getType() == SupportedTypesConst.DOUBLE) {
            return Double.valueOf((String) field);
        } else if (getType() == SupportedTypesConst.YEAR) {
            return Integer.parseInt((String) field);
        } else if (getType() == SupportedTypesConst.DATE) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date = df.parse((String) field);
                return date.getTime();
            } catch (Exception e) {
            }
            return 0;
        } else if (getType() == SupportedTypesConst.DATETIME) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
                Date date = df.parse((String) field);
                return date.getTime();
            } catch (Exception e) {
            }
            return 0;
        } else if (getType() == SupportedTypesConst.TIME) {
            String data = (String) field;
            String[] d = data.split(":");
            return new Time(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
        } else {
            throw new UnsupportedOperationException("Not implemented Yet");
        }
    }

    private Object ConvertObject(byte[] field) {
        if (getType() == SupportedTypesConst.TEXT) {
            return new String(field);
        } else if (SupportedTypesConst.isIntTypes(getType())) {
            if (this.getType() == 1 || this.getType() == 2)
                return (short) CommonUse.byteArrToInt(field, getBytes());
            return CommonUse.byteArrToInt(field, getBytes());
        } else if (getType() == SupportedTypesConst.FLOAT) {
            return CommonUse.byteArrayToFloat(field);
        } else if (getType() == SupportedTypesConst.DOUBLE) {
            return CommonUse.byteArrayToDouble(field);
        } else if (getType() == SupportedTypesConst.YEAR) {
            return 1900 + CommonUse.byteArrToInt(field, getBytes());
        } else if (getType() == SupportedTypesConst.DATE) {
            long epoc = CommonUse.byteArrToLong(field, getBytes());
            return new Date(epoc);
        } else if (getType() == SupportedTypesConst.DATETIME) {
            long epoc = CommonUse.byteArrToLong(field, getBytes());
            return new Date(epoc);
        } else if (getType() == SupportedTypesConst.TIME) {
            int val = CommonUse.byteArrToInt(field, getBytes());
            int inter = (val / 60);
            return new Time(inter / 60, inter % 60, val % 60);
        } else {
            throw new UnsupportedOperationException("Not implemented Yet");
        }
    }

    public static ValueField[] ConvertToValue(ColumnField[] columns) {
        ValueField[] dum = new ValueField[columns.length];
        for (int i = 0; i < columns.length; i++) {
            dum[i] = new ValueField(0, columns[i]);
        }
        return dum;
    }

    public byte[] getByteValue() {
        if (getType() == SupportedTypesConst.TEXT) {
            return String.valueOf(getValue()).getBytes();
        } else if (SupportedTypesConst.isIntTypes(getType())) {
            if (getValue() instanceof Boolean)
                return CommonUse.intToByteArr((Boolean) getValue() ? 1 : 0, getBytes());
            if (getValue() instanceof Short)
                return CommonUse.intToByteArr((Short) getValue(), getBytes());
            return CommonUse.intToByteArr((Integer) getValue(), getBytes());
        } else if (getType() == SupportedTypesConst.FLOAT) {
            return CommonUse.floatToByteArray((float) getValue(), getBytes());
        } else if (getType() == SupportedTypesConst.DOUBLE) {
            return CommonUse.doubleToByteArray((double) getValue(), getBytes());
        } else if (getType() == SupportedTypesConst.YEAR) {
            return CommonUse.intToByteArr(((int) getValue()) - 1900, getBytes());
        } else if (getType() == SupportedTypesConst.DATE) {
            return CommonUse.longToByteArr((long) getValue(), bytes);
        } else if (getType() == SupportedTypesConst.DATETIME) {
            return CommonUse.longToByteArr((long) getValue(), bytes);
        } else if (getType() == SupportedTypesConst.TIME) {
            Time t = (Time) getValue();
            int val = t.getHours() * 60 * 60;
            val += t.getMinutes() * 60;
            val += t.getSeconds();
            return CommonUse.intToByteArr(val, getBytes());
        } else {
            throw new UnsupportedOperationException("Not implemented Yet");
        }
    }
}
