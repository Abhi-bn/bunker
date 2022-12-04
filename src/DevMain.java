import DavisBase.DBEngine;

public class DevMain {
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

        // db.select("NewExptTable", "");
        // System.out.println(db.delete("NewExptTable", "First", "Abhinava"));
        // db.select("NewExptTable", "");
        // db.select("NewExptTable", "");
        // String[] values = { "First", "Hari" };
        // String[] condition = { "_ID", "5" };
        // db.updateInfo("NewExptTable", values, condition);

        System.out
                .println(db.createTable("Harsha", "First Text Unique", "phonenumber TEXT",
                        "count int Unique"));
        db.insertInto("Harsha", "Harsha Priya", "9739354195", "30");
        // db.select("UniqCheck");
        // db.describe();
        // db.insertInto("UniqCheck", "", "9739354195", "31");

        // System.out
        // .println(
        // db.createTable("DoubleCheck", "First Text", "phonenumber TEXT", "count
        // Double"));
        // db.insertInto("DoubleCheck", "Abhinava", "9739354195", "5.1231");
        // System.out
        // .println(
        // db.createTable("yearCheck", "First Text", "phonenumber TEXT", "year
        // YEar"));
        // // System.out
        // // .println(
        // // db.createTable("DoubleCheck", "First Text", "phonenumber TEXT", "year
        // double"));

        // String[] values = { "year", "2.12", "First", "Hari" };
        // String[] condition = { "_ID", "2" };
        // db.updateInfo("DateCheck", values, condition);
        // // db.insertInto("DateTimeCheck", "Abhinava", "9739354195", "Jun 13 2003
        // // 23:11:52.454 UTC");
        // // db.insertInto("DoubleCheck", "Abhinava", "9739354195", "22.54");
        // System.out.println(db.delete("TimeCheck", "First", "Abhinava"));
        // db.select("TextTypeCheck");
        // db.describe();
        // db.insertInto("yearCheck", "Hari", "9739354195", "1996");
        String[] where = {};
        String[] a = {};
        db.select("DateCheck", where, a);
        db.describe("");
    }
}
