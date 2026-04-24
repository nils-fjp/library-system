package member;

import author.AuthorController;
import book.BookController;
import loan.LoanController;
import ui.ANSI;
import ui.AuthController;
import ui.Menu;
import java.util.Optional;

import java.sql.SQLException;

public class LibrarianMenuController {

    public static void showLibrarianMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Main Menu » Librarian Menu");
        menu.setMainTitle("Librarian Menu");
        menu.setMenuInfo(ANSI.ITALIC + ANSI.BRIGHT_GREEN + "Welcome, " + currentMember.getFirstName() + "!" + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
        menu.addMenuOption("Manage Books");
        menu.addMenuOption("Manage Loans");
        menu.addMenuOption("Manage Readers");
        menu.addMenuOption("Manage Authors");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> showManageBooksMenu(currentMember);
                case 2 -> showManageLoansMenu(currentMember);
                case 3 -> showManageReadersMenu(currentMember);
                case 4 -> showManageAuthorsMenu(currentMember);
                case 0 -> {
                    AuthController.logout();
                    return;
                }
            }
        }
    }

    //1. Manage Books
    private static void showManageBooksMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Librarian Menu » Manage Books");
        menu.addMenuOption("View Books");
        menu.addMenuOption("Search");
        menu.addMenuOption("Add Book");
        menu.addMenuOption("Update Book");
        menu.addMenuOption("Delete Book");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> BookController.showAllBooksForAdmin(currentMember);
                case 2 -> BookController.AdminSearchForBooks(currentMember);
                case 3 -> BookController.addBookForAdmin(currentMember);
                case 4 -> BookController.updateBookForAdmin(currentMember);
                case 5 -> BookController.deleteBookForAdmin(currentMember);
            }
        }
    }

    //2. Manage Loans
    public static void showManageLoansMenu(Member currentMember) {
        Menu manageLoansMenu = new Menu();
        manageLoansMenu.setTopTitle("Librarian Menu » Manage Loans");
        manageLoansMenu.setMainTitle("Manage Loans");
        manageLoansMenu.setMenuInfo(ANSI.ITALIC + "Manage active loans." + ANSI.NO_ITALIC);
        manageLoansMenu.setExitOption("Back to Librarian Menu");
        manageLoansMenu.addMenuOption("View All Active Loans");
        manageLoansMenu.addMenuOption("Add Loan");
        manageLoansMenu.addMenuOption("Update Loan");

        while (manageLoansMenu.showMenu()) {
            switch (manageLoansMenu.getChoice()) {
                case 1 -> LoanController.showAllActiveLoans(currentMember);
                case 2 -> LoanController.createLoan(currentMember);
                case 3 -> LoanController.registerReturnedLoan(currentMember);
            }
        }
    }

    //3. Manage Readers
    private static void showManageReadersMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Librarian Menu » Manage Readers");
        menu.setMainTitle("Manage Readers");
        menu.setMenuInfo(ANSI.ITALIC + "Manage readers." + ANSI.NO_ITALIC);
        menu.setExitOption("Back to Librarian Menu");

        menu.addMenuOption("View All Readers");
        menu.addMenuOption("Search Reader");
        menu.addMenuOption("Add Reader");
        menu.addMenuOption("Update Reader");
        menu.addMenuOption("Delete Reader");
        menu.addMenuOption("Change Reader Password");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> MemberController.showAllMembersForAdmin(currentMember);
                case 2 ->  {
                    Optional<MemberAdminDto> optionalReader = MemberController.showMember(currentMember);
                    if (optionalReader.isPresent()) {
                        showReaderActionsMenu(currentMember, optionalReader.get());
                    }
                }
                case 3 -> MemberController.addMemberByAdmin(currentMember);
                case 4 -> MemberController.updateMemberByAdmin(currentMember);
                case 5 -> MemberController.deleteMemberByAdmin(currentMember);
                case 6 -> MemberController.changeMemberPasswordByAdmin(currentMember);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
    private static void showReaderActionsMenu(Member currentMember, MemberAdminDto reader) throws SQLException {

        Menu menu = new Menu();
        menu.setMenuInfo(
                ANSI.ITALIC +
                        "Selected reader: " + reader.getFirstName() + " " + reader.getLastName() +
                        ANSI.NO_ITALIC
        );
        menu.setExitOption("Back to Manage Readers");

        menu.addMenuOption("Update Reader");
        menu.addMenuOption("Delete Reader");
        menu.addMenuOption("Change Reader Password");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> MemberController.updateMemberByAdmin(currentMember, reader);
                case 2 -> {
                    MemberController.deleteMemberByAdmin(currentMember, reader);
                    return;
                }
                case 3 -> MemberController.changeMemberPasswordByAdmin(currentMember, reader);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    //4. Manage Authors
    private static void showManageAuthorsMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Manage Authors");
        menu.addMenuOption("View authors");
        menu.addMenuOption("Add author");
        menu.addMenuOption("Modify author");
        //menu.addMenuOption("Delete author");
        menu.addMenuOption("Find author");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> AuthorController.showAllAuthors(currentMember);
                case 2 -> AuthorController.addAuthor(currentMember);
                case 3 -> AuthorController.updateAuthor(currentMember);
                //case 4 -> AuthorController.deleteAuthor(currentMember);
                case 4 -> AuthorController.showAuthor(currentMember);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
