import ui.ANSI;
import ui.Menu;

import java.sql.SQLException;

public class Main {

//    private static final java.util.Scanner scanner = new java.util.Scanner(System.in);
//    private static final ui.ConsolePrinter printer = new ui.ConsolePrinter();
//    private static member.Member currentMember;

//    public static void login() {
//        member.MemberService memberService = new member.MemberService();
//
//        try {
//            System.out.print("Enter email: ");
//            String email = scanner.nextLine().trim();
//
//            System.out.print("Enter password: ");
//            String password = scanner.nextLine().trim();
//
//            java.util.Optional<member.Member> optionalMember = memberService.authenticate(email, password);
//
//            if (optionalMember.isEmpty()) {
//                printer.printError("Invalid email or password.");
//                return;
//            }
//
//            currentMember = optionalMember.get();
//            printer.printSuccess("Welcome, " + currentMember.getFirstName() + "!");
//
//            String status = member.MemberValidator.getNormalizedStatus(currentMember);
//            if ("suspended".equals(status)) {
//                printer.printError("Your account is suspended. You can view your account and search books, but you cannot borrow, reserve, or renew items.");
//            }
//            if ("LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
//                member.LibrarianMenuController.showLibrarianMenu(currentMember);
//            } else {
//                member.ReaderMenuController.showReaderMenu(currentMember);
//            }
//
//        } catch (member.MembershipExpiredException e) {
//            printer.printError(e.getMessage());
//        } catch (IllegalArgumentException e) {
//            printer.printError(e.getMessage());
//        } catch (java.sql.SQLException e) {
//            printer.printError("Database error: " + e.getMessage());
//        }
//    }
//
//    public static void logout() {
//        currentMember = null;
//    }

    static void main(String[] args) throws SQLException {
        Menu mainMenu = new Menu();

        ui.ConsolePrinter.printHeaderCenter("Welcome to the Library System");
        mainMenu.setTopTitle("Main Menu");
        mainMenu.setMainTitle("Library System");
        mainMenu.setMenuInfo(ANSI.ITALIC +
                "Select which menu to open" +
                ANSI.NO_ITALIC);
        mainMenu.setExitOption("Exit");
        mainMenu.addMenuOption("Search Books");
        mainMenu.addMenuOption("Log in");
        mainMenu.setPrePrompt("Type a number and press enter...");
        mainMenu.setPromptLine("Enter: ");

        while (mainMenu.showMenu()) {
            switch (mainMenu.getChoice()) {
                case 1 -> book.BookController.searchForBooks();
                case 2 -> ui.AuthController.login();
                default -> System.out.println("Invalid input");
            }
        }

        System.out.println(ANSI.CLEAR_SCREEN);
        System.out.println("Exiting application...");
    }
}

//          LIBRARIAN:      emma.hill88@email.com
//                          QaMzTpLs
//          READER Expired:
//                          ava.white25@email.com
//                          NyUiOpAs
//          READER Active:
//                          abigail.thomas4@email.com
//                          PlMnBcXa
//          READER loan history:
//                          harper.thomas36@email.com
//                          RkVnYeHu
//
