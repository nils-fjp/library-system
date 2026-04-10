
//import book.BookController;
//import Member.LibrarianMenuController;
//import Member.Member;
//import Member.MemberService;
//import Member.ReaderMenuController;
//import UI.ANSI;
//import UI.AuthController;
//import UI.Menu;
import member.Member;
import member.MemberController;
import member.MemberService;
import ui.ANSI;
import ui.Menu;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        Menu mainMenu = new Menu();

        mainMenu.setTopTitle("Main Menu");
        mainMenu.setMidTitle("Test Menu");
        mainMenu.setMenuInfo("Select which menu to open");
        mainMenu.setExitOption("Exit program");
        //mainMenu.addMenuOption("ReaderMenuController.showMenu()");
        // mainMenu.addMenuOption("LibrarianMenuController.showMenu()");
        mainMenu.addMenuOption("BookController.showMenu()");
        // mainMenu.addMenuOption("MemberController.showMenu()");
        mainMenu.addMenuOption("LoanController.showMenu()");
        mainMenu.addMenuOption("Search Books");
        mainMenu.addMenuOption("Log in");
        mainMenu.setPrePrompt("Type a number and press enter...");
        mainMenu.setPromptLine("Enter: ");

        while (mainMenu.showMenu()) {
            switch (mainMenu.getChoice()) {
                case 0 -> {
                    System.out.println(ANSI.CLEAR_SCREEN);
                    System.out.println("Exiting application...");
                    return;
                }
                //case 1 -> ReaderMenuController.showMenu();
                // case 2 -> LibrarianMenuController.showMenu(currentMember);
                case 1 -> Book.BookController.showMenu();
                //case 4 -> MemberController.showMenu();
                case 2 -> Loan.LoanController.showMenu();
                case 3 -> System.out.println(("Book Menu\n" +
                        "1. View Books -> BookController.showAllBooks()\n" +
                        "2. Search Books -> BookController.searchBooks()\n" +
                        "3. Search Authors -> BookController.searchAuthors()\n" +
                        "0. Back"));
                case 4 -> AuthController.login();
                default -> System.out.println("Invalid input");
            }
        }
    }
}



//          LIBRARIAN emma.hill88@email.com
//                    QaMzTpLs
//            READER ava.white25@email.com
//                   NyUiOpAs