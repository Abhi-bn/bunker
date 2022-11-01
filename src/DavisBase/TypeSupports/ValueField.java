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
}
