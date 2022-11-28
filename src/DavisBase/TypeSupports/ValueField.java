package DavisBase.TypeSupports;

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
        if (this.getValue() instanceof String) {
            if (this.getValue().equals(vf.getValue()))
                return true;
        } else if (this.getValue() instanceof Integer) {
            return (Integer) this.getValue() == (Integer) vf.getValue();
        }
        return false;
    }

    private Object getMeObject(Object field) {
        if (field instanceof byte[]) {
            return ConvertObject((byte[]) field);
        }

        if (this.getType() == 11) {
            return field;
        } else if (SupportedTypesConst.isIntTypes(this.getType())) {
            if (field instanceof Short)
                return field;
            if (field instanceof Integer)
                return field;
            if (field instanceof Boolean)
                return field;
            return (Integer) Integer.parseInt((String) field);
        } else if (getType() == 5) {
            if (field instanceof Integer)
                return field;
            if (field instanceof Float)
                return field;
            return Float.valueOf((String) field);
        } else if (getType() == 6) {
            if (field instanceof Integer)
                return field;
            if (field instanceof Double)
                return field;
            return Double.valueOf((String) field);
        } else {
            throw new UnsupportedOperationException("Not implemented Yet");
        }
    }

    private Object ConvertObject(byte[] field) {
        if (getType() == 11) {
            return new String(field);
        } else if (SupportedTypesConst.isIntTypes(getType())) {
            if (this.getType() == 1 || this.getType() == 2)
                return (short) CommonUse.byteArrToInt(field, getBytes());
            return CommonUse.byteArrToInt(field, getBytes());
        } else if (getType() == 5) {
            return CommonUse.byteArrayToFloat(field);
        } else if (getType() == 6) {
            return CommonUse.byteArrayToDouble(field);
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
        if (getType() == 11) {
            return String.valueOf(getValue()).getBytes();
        } else if (SupportedTypesConst.isIntTypes(getType())) {
            if (getValue() instanceof Boolean)
                return CommonUse.intToByteArr((Boolean) getValue() ? 1 : 0, getBytes());
            if (getValue() instanceof Short)
                return CommonUse.intToByteArr((Short) getValue(), getBytes());
            return CommonUse.intToByteArr((Integer) getValue(), getBytes());
        } else if (getType() == 5) {
            return CommonUse.floatToByteArray((float) getValue(), getBytes());
        } else if (getType() == 6) {
            return CommonUse.doubleToByteArray((double) getValue(), getBytes());
        } else {
            throw new UnsupportedOperationException("Not implemented Yet");
        }
    }
}
