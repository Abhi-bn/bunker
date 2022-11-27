import DavisBase.DBEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("This is Bunker");
        DBEngine db = new DBEngine();
        db.createDB("student");
        db.connect("student");
        // System.out.println(db.createDB("student"));
        // db.describe();
        // System.out
        // .println(db.createTable("NewExptTable", "First Text", "Address Text",
        // "phonenumber TEXT", "count int"));
        // db.insertInto("NewExptTable", "Abhinava", "800 W", "9739354195", "30");
        // db.insertInto("NewExptTable", "Abhinava", "800 W", "9739354195", "15");
        // db.insertInto("NewExptTable", "Hari", "1230 East", "9739356979", "10");
        // db.insertInto("NewExptTable", "Hari", "1230 East", "9739356979");

        // // db.select("NewExptTable", "");
        // System.out.println(db.delete("NewExptTable", "First", "Abhinava"));
        // db.select("NewExptTable", "");
        // db.select("NewExptTable", "");
        // String[] values = { "First", "Hari" };
        // String[] condition = { "_ID", "5" };
        // db.updateInfo("NewExptTable", values, condition);

        // System.out
        // .println(db.createTable("UniqCheck", "First Text Unique", "phonenumber TEXT",
        // "count int Unique"));
        // // db.insertInto("UniqCheck", "Abhinava", "9739354195", "30");
        // db.select("UniqCheck");
        // db.describe();
        // db.insertInto("UniqCheck", "", "9739354195", "31");

        // System.out
        // .println(
        // db.createTable("DoubleCheck", "First Text", "phonenumber TEXT", "count
        // Double"));
        // db.insertInto("DoubleCheck", "Abhinava", "9739354195", "5.1231");
        db.describe();
        System.out
                .println(
                        db.createTable("FloatCheck", "First Text", "phonenumber TEXT", "count Float"));
        db.insertInto("FloatCheck", "Abhinava", "9739354195", "5.1231");
        // db.select("TextTypeCheck");
        // db.describe();
        // db.insertInto("TextTypeCheck", "Hari", "9739354195", "31");

        db.select("FloatCheck", "_ID", "count", "First");
        // db.describe();
    }
}
