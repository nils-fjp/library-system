package Book;

import Base.BaseController;
import UI.Menu;

public class BookController extends BaseController {


    public static void showMenu() {

        Menu bookMenu = new Menu();

        bookMenu.setTopTitle("Book.Book UI.Menu");
        // bookMenu.setMidTitle("Subtitle");
        // bookMenu.setMenuInfo("UI.Menu Info");
        bookMenu.setExitOption("Back to UI.Menu");
        bookMenu.addMenuOption("Search all books");
        bookMenu.addMenuOption("Add new books");
        bookMenu.addMenuOption("Search by categories");
        bookMenu.setPrePrompt("Type a number and press enter...");
        bookMenu.setPromptLine("Enter: ");

        while (bookMenu.showMenu()) {
            switch (bookMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1: {
                    System.out.println("WIP!");
                    break;
                }
                case 2: {
                    System.out.println("WIP");
                    break;
                }
                case 3: {
                    System.out.println("wip");
                    break;
                }
                default: {
                    bookMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }
}
