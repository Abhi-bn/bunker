import java.util.Scanner;
import DavisBase.Util.Settings;
import DavisBase.Util.WelcomeScreen;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useDelimiter(";");

        /* Display the welcome screen */
        WelcomeScreen.splashScreen();

        /* Variable to hold user input from the prompt */
        String userCommand = "";

        while (!Settings.isExit()) {
            if (Settings.getDataBaseSelected()) {
                System.out.print(Settings.getPrompt() + Settings.getDataBaseName() + Settings.ARROW);
            } else {
                System.out.print(Settings.getPrompt());
            }
            /* Strip newlines and carriage returns */
            userCommand = scanner.next().replace("\n", " ").replace("\r", "").trim();
            userCommand = userCommand.replace("FROM", "from");
            userCommand = userCommand.replace("WHERE", "where");
            Commands.parseUserCommand(userCommand);
        }
        System.out.println("Exiting...");
        scanner.close();

    }
}
