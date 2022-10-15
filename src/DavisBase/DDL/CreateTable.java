package DavisBase.DDL;

import java.util.*;

import DavisBase.DBEngine;
import DavisBase.SupportedTypesConst;
import DavisBase.Util.CommonUse;

public class CreateTable {
    static boolean verbose = true;
    ArrayList columns = new ArrayList<>();
    Map<String, Map<String, String>> meta_data = new HashMap<>();
    String name;

    public CreateTable(String name, String... columns) {
        this.name = name;
        generateMeta(columns);
    }

    Map<String, String> make_info(String... info) {
        assert info.length == 2;
        Map<String, String> data = new HashMap<>();
        data.put("n", info[0]);
        data.put("t", info[1]);
        data.put("b", SupportedTypesConst.getTypeStrBytes(info[1]));
        return data;
    }

    void generateMeta(String... columns) {
        for (int i = 0; i < columns.length; i++) {
            String[] eachCol = columns[i].split(" ");
            // if still not exception by :validateColORThrow go ahead
            meta_data.put(eachCol[0], make_info(eachCol));
            CommonUse.Print(verbose, meta_data.toString());
        }
    }

    public boolean create() {
        CommonUse.createNewFile(CommonUse.generatePath(DBEngine.DBPath, name + ".tbl"));
        return true;
    }
}
