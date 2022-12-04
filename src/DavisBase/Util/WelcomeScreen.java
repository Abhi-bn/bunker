package DavisBase.Util;

public class WelcomeScreen {

    /**
     * Display the splash screen
     */
    public static void splashScreen() {
        System.out.println(printSeparator("─", 80));
        System.out.println("Welcome to Bunker"); // Display the
                                                 // string
        System.out.println("Safe and Protected Storage Space");
        System.out.println("BunkerLite Version " + Settings.getVersion());
        System.out.println(Settings.getCopyright());
        System.out.println("Type \"help;\" to display supported commands.");
        System.out.println(printSeparator("─", 80));
    }

    public static String printSeparator(String s, int len) {
        String bar = "";
        for (int i = 0; i < len; i++) {
            bar += s;
        }
        return bar;
    }

}
