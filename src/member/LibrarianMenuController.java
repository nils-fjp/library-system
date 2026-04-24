package member;

import author.AuthorController;
import book.BookController;
import loan.LoanController;
import ui.ANSI;
import ui.AuthController;
import ui.Menu;

import java.sql.SQLException;
import java.util.Optional;

public class LibrarianMenuController {

    private static final AuthController AUTH_CONTROLLER = new AuthController();
    private static final MemberController MEMBER_CONTROLLER = new MemberController();
    private static final AuthorController AUTHOR_CONTROLLER = new AuthorController();

    public static void showLibrarianMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Main Menu » Librarian Menu");
        menu.setMainTitle("Librarian Menu");
        menu.setMenuInfo(ANSI.ITALIC + ANSI.BRIGHT_GREEN + "Welcome, " + currentMember.getFirstName() + "!" + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
        menu.setExitOption("Logout"); /////
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
                    AUTH_CONTROLLER.logout();
                    return;
                }
            }
        }
    }

    //1. Manage Books
    private static void showManageBooksMenu(Member currentMember) throws SQLException {
        Menu manageBooksMenu = new Menu();
        manageBooksMenu.setTopTitle("Librarian Menu » Manage Books");
        manageBooksMenu.setMainTitle("Manage Books");
        manageBooksMenu.setMenuInfo(ANSI.ITALIC + "Manage books." + ANSI.NO_ITALIC);
        manageBooksMenu.setExitOption("Back to Librarian Menu");
        manageBooksMenu.addMenuOption("View Books");
        manageBooksMenu.addMenuOption("Search Books");
        manageBooksMenu.addMenuOption("Add Book");
        manageBooksMenu.addMenuOption("Update Book");
        manageBooksMenu.addMenuOption("Delete Book");

        while (manageBooksMenu.showMenu()) {
            switch (manageBooksMenu.getChoice()) {
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
        manageLoansMenu.addMenuOption("Delete Loan");

        while (manageLoansMenu.showMenu()) {
            switch (manageLoansMenu.getChoice()) {
                case 1 -> LoanController.showAllActiveLoans();
                case 2 -> manageLoansMenu.setTemporaryPrePrompt(LoanController.createLoan(currentMember));
                case 3 -> LoanController.showUpdateLoanMenu();
//                case 4 -> LoanController.deleteLoan(currentMember);
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
                case 1 -> MEMBER_CONTROLLER.showAllMembersForAdmin(currentMember);
                case 2 ->  {
                    Optional<MemberAdminDto> optionalReader = MEMBER_CONTROLLER.showMember(currentMember);
                    if (optionalReader.isPresent()) {
                        showReaderActionsMenu(currentMember, optionalReader.get());
                    }
                }
                case 3 -> MEMBER_CONTROLLER.addMemberByAdmin(currentMember);
                case 4 -> MEMBER_CONTROLLER.updateMemberByAdmin(currentMember);
                case 5 -> MEMBER_CONTROLLER.deleteMemberByAdmin(currentMember);
                case 6 -> MEMBER_CONTROLLER.changeMemberPasswordByAdmin(currentMember);
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
                case 1 -> MEMBER_CONTROLLER.updateMemberByAdmin(currentMember, reader);
                case 2 -> {
                    MEMBER_CONTROLLER.deleteMemberByAdmin(currentMember, reader);
                    return;
                }
                case 3 -> MEMBER_CONTROLLER.changeMemberPasswordByAdmin(currentMember, reader);
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
                case 1 -> AUTHOR_CONTROLLER.showAllAuthors(currentMember);
                case 2 -> AUTHOR_CONTROLLER.addAuthor(currentMember);
                case 3 -> AUTHOR_CONTROLLER.updateAuthor(currentMember);
                //case 4 -> AUTHOR_CONTROLLER.deleteAuthor(currentMember);
                case 4 -> AUTHOR_CONTROLLER.showAuthor(currentMember);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
