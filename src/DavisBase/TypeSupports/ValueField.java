package DavisBase.TypeSupports;

import DavisBase.Pages.Page;

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
            if (field instanceof Integer)
                return field;
            if (field instanceof Boolean)
                return field;
            return (Integer) Integer.parseInt((String) field);
        } else {
            throw new UnsupportedOperationException("Not implemented Yet");
        }
    }

    private Object ConvertObject(byte[] field) {
        if (getType() == 11) {
            return new String(field);
        } else if (SupportedTypesConst.isIntTypes(getType())) {
            return Page.byteArrToInt(field, getBytes());
        } else {
            throw new UnsupportedOperationException("Not implemented Yet");
        }
    }
}
