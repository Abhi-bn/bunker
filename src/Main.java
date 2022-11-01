import DavisBase.DBEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("This is Bunker");
        DBEngine db = new DBEngine();
        db.connect("student");
        // System.out.println(db.createDB("student"));
        db.describe();
        // System.out.println(db.createTable("Way", "Name Text"));
        // Page p = new Page("info");
        // print the ByteBuffer
        // System.out.println(Main._ByteArrToInt(Main._IntArrToByte(255, 1)));
    }
}
