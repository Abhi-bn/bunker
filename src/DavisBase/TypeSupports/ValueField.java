package DavisBase.TypeSupports;

public class ValueField extends ColumnField {

    Object value;

    public ValueField(Object value, String[] cols, int order) {
        super(cols, order);
        this.value = value;
    }

    public ValueField(Object value, ColumnField fd) {
        super(fd, fd.getOrder());
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean compare(ValueField vf) {
        if (this.getValue() instanceof String) {
            if (this.getValue().equals(vf.getValue()))
                return true;
        } else {
            if (this.getValue() == vf.getValue())
                return true;
        }
        return false;
    }
}
