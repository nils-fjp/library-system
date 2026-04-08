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
        mainMenu.addMenuOption("Log in");
        mainMenu.addMenuOption("MemberController.showMenu()");
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
                case 1 -> book.BookController.showMenu();
                case 2 -> loan.LoanController.showMenu();
                case 3 -> login();
                case 4 -> MemberController.showMenu(); // for testing
                default -> System.out.println("Invalid input");
            }
        }
    }
    public static void login(){
        MemberService memberService = new MemberService();

        try {
            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            Optional<Member> optionalMember = memberService.authenticate(email, password);

            if (optionalMember.isEmpty()) {
                System.out.println("Invalid email or password.");
                return;
            }

//          LIBRARIAN emma.hill88@email.com
//                    QaMzTpLs
//            READER ava.white25@email.com
//                   NyUiOpAs

            Member currentMember = optionalMember.get();
            System.out.println("Welcome, " + currentMember.getFirstName() + "!");
            if ("LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
                MemberController.showLibrarianMenu(currentMember);
            } else {
                MemberController.showReaderMenu(currentMember);
            }
        }catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
