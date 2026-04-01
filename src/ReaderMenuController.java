public class ReaderMenuController {
    public static void showMenu() {
        Menu menu = new Menu();

        menu.setTopTitle("Reader Menu");
        menu.setMidTitle("Reader");
        menu.setMenuInfo("Choose section");
        menu.setExitOption("Back");
        menu.addMenuOption("Library"); //BookRepository
        menu.addMenuOption("My Loans"); //LoanRepository
        menu.addMenuOption("My Profile +"); //MemberRepository
        menu.setPrePrompt("Type a number and press enter...");
        menu.setPromptLine("Enter: ");

        while (true) {
            menu.showMenu();

            switch (menu.getChoice()) {
                case 0 -> { return; }
                //case 1 -> BookController.showReaderMenu();
                //case 2 -> LoanController.showMenu();
                case 3 -> MemberController.showReaderMenu();
                default -> System.out.println("Invalid input");
            }
        }
    }
}
