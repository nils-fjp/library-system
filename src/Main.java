//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // Skapa en ny meny för att visa information
        Menu mainMenu = new Menu();

        // Välj vad det ska stå överst i menyn
        mainMenu.setTitle("Main Menu");
        mainMenu.setSubtitle("Subtitle");
        mainMenu.setMenuInfo("Menu Info");
        mainMenu.setExitOption("Exit");
        mainMenu.addMenuOption("Option 1");
        mainMenu.addMenuOption("Option 2");
        mainMenu.addMenuOption("Option 3");
        mainMenu.addMenuOption("Option 4");
        mainMenu.setPrePrompt("Type a number and press enter...");
        mainMenu.setPromptLine("Enter: ");
        while (mainMenu.showMenu()) {

            switch (mainMenu.getChoice()) {
                case 0: {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                case 1: {
                    System.out.println("Option 1 not implemented yet");
                    break;
                }
                case 2: {
                    System.out.println("Cption 2 implemented yet");
                    break;
                }
                case 3: {
                    System.out.println("Option 3 not implemented yet");
                    break;
                }
                default:
                    System.out.println("Invalid Input");
            }
        }
    }
}
