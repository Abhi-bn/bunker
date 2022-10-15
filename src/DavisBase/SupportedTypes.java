package DavisBase;

public class SupportedTypes {
    int type;
    String name;
    TypeField tf;

    public SupportedTypes(String... cols) {
        filter_col_data(cols);
        validateColORThrow(cols);
    }

    void filter_col_data(String... columns) {
        // Do nothing about irregularities as validateColORThrow will take care
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columns[i].toUpperCase();
        }
    }

    void validateColORThrow(String... info) {
        switch (info.length) {
            case 4:
            case 3:

            case 2:
            case 1:
            case 0:
                break;

            default:
                break;
        }
        // TODO: replace with actual exception
        if (info.length == 0) {
            System.out.println("Syntax Error");
            System.exit(1);
        } else if (info.length == 1) {
            System.out.println("Column (" + info[0] + ") also requires type");
            System.exit(1);
        } else if (info.length == 2) {
            if (!SupportedTypesConst.isSupported(info[1])) {
                System.out.println("Not a supported type (" + info[1] + ")");
                System.exit(1);
            }
        }
    }

}
