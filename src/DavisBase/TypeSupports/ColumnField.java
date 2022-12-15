package DavisBase.TypeSupports;

public class ColumnField {
    int order;
    String name;
    int type;
    int bytes;
    boolean unique;
    boolean nullable;
    int access;
    int id;

    enum SupportedTypes {
        UNIQUE, NULLABLE
    }

    public boolean getUnique() {
        return unique;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean getNullable() {
        return nullable;
    }

    public String getName() {
        return name;
    }

    public int getBytes() {
        return bytes;
    }

    public int getAccess() {
        return access;
    }

    public int getType() {
        return type;
    }

    public int getOrder() {
        return order;
    }

    public Boolean isNullValid() {
        return this.getNullable();
    }

    public ColumnField(String[] cols, int order) {
        filter_col_data(cols);
        validateColORThrow(cols);
        this.name = cols[0];
        this.type = SupportedTypesConst.TypesToInt.get(cols[1]);
        this.bytes = SupportedTypesConst.TypesToBytes.get(this.type);
        this.order = order;
        this.unique = isUnique(cols);
        this.nullable = isNullable(cols);
    }

    public ColumnField(ColumnField fd, int order) {
        this.name = fd.getName();
        this.type = fd.getType();
        this.bytes = fd.getBytes();
        this.order = order;
        this.unique = fd.unique;
        this.nullable = fd.nullable;
    }

    static void filter_col_data(String... columns) {
        // Do nothing about irregularities as validateColORThrow will take care
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columns[i].toUpperCase();
        }
    }

    static void validateColORThrow(String... info) {
        // TODO: replace with actual exception
        switch (info.length) {
            case 4:
            case 3:
                for (int i = 2; i < info.length; i++) {
                    if (!supportedFields(info[i])) {
                        System.out.println("Not a supported Field (" + info[i] + ")");
                        System.exit(1);
                    }
                }
            case 2:
                if (!SupportedTypesConst.isSupported(info[1])) {
                    System.out.println("Not a supported type (" + info[1] + ")");
                    System.exit(1);
                }
                break;
            case 1:
                System.out.println("Column (" + info[0] + ") also requires type");
                System.exit(1);
                break;
            case 0:
                System.out.println("Syntax Error");
                System.exit(1);
                break;
            default:
                System.out.println("Too Many Column Parameters");
                System.exit(1);
                break;
        }
    }

    private final static String[] supported_types = { "UNIQUE", "NULLABLE", "PRIMARY_KEY" };

    public static boolean isUnique(String[] cols) {
        for (int i = 2; i < cols.length; i++) {
            if (cols[i].toUpperCase().equals(supported_types[SupportedTypes.UNIQUE.ordinal()]))
                return true;
        }
        return false;
    }

    public static boolean isNullable(String[] cols) {
        for (int i = 2; i < cols.length; i++) {
            if (cols[i].toUpperCase().equals(supported_types[SupportedTypes.NULLABLE.ordinal()]))
                return true;
        }
        return false;
    }

    public static boolean supportedFields(String field) {
        for (int i = 0; i < supported_types.length; i++)
            if (field.equals(supported_types[i]))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "Nam:" + name
                + " Typ:" + String.valueOf(type)
                + " Byt:" + String.valueOf(bytes)
                + " UNQ:" + String.valueOf(unique)
                + " NUL:" + String.valueOf(nullable)
                + " ORD:" + String.valueOf(order)
                + " ACC:" + String.valueOf(access);
    }
}
