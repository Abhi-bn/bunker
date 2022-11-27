import DavisBase.DBEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("This is Bunker");
        DBEngine db = new DBEngine();
        db.connect("student");
        // System.out.println(db.createDB("student"));
        // db.describe();
        System.out
                .println(db.createTable("NewExptTable", "First Text", "Address Text", "phonenumber TEXT", "count int"));
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
        db.select("NewExptTable");
        db.describe();
    }
}
