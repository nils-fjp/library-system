import java.sql.SQLException;
import java.util.Optional;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        // skapa meny som visar information och läser input
        Menu mainMenu = new Menu();

        // Välj vad det ska stå i menytexten med följande metoder:
        mainMenu.setTopTitle("Main Menu");
        mainMenu.setMidTitle("Subtitle");
        mainMenu.setMenuInfo("Menu Info");
        mainMenu.setExitOption("Exit");
        mainMenu.addMenuOption("Show Book Menu");
        mainMenu.addMenuOption("Show Loan Menu");
        mainMenu.addMenuOption("Show Member Menu");
        mainMenu.setPrePrompt("Type a number and press enter...");
        mainMenu.setPromptLine("Enter: ");


        // Visa menyn genom showMenu() som returnerar en boolean.
        while (mainMenu.showMenu()) {
            // Läs in valet med en getter i meny-klassen.
            // Menyn läser bara input. Logiken måste hanteras med en switch.
            switch (mainMenu.getChoice()) {
                case 0: {
                    break; // Avslutar while-loopen (lämnar menyn)
                }
                case 1: {
                    BookController.showMenu(); // Kan vara en annan meny
                    break;
                }
                case 2: {
                    LoanController.showMenu();
                    break;
                }
                case 3: {
                    MemberController.showMenu();
                    break;
                }
                default:
                    System.out.println("Invalid Input");
            }
        }
        System.out.println(ANSI.CLEAR_SCREEN);
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
