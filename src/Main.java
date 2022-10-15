import DavisBase.DBEngine;
import DavisBase.DDL.*;;

public class Main {
    public static void main(String[] args) {
        System.out.println("This is Bunker");
        DBEngine db = new DBEngine();
        db.createDB(null);

        CreateTable r = new CreateTable("class", "std TEXT", "asds int");
    }
}
