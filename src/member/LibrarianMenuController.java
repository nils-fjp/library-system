package member;

import author.AuthorController;
import book.BookController;
import loan.LoanController;
import ui.ANSI;
import ui.AuthController;
import ui.Menu;

import java.sql.SQLException;

public class LibrarianMenuController {

    // endast test kod meny - ta bort senare
    public static void showLibrarianMenu(Member currentMember) throws SQLException {
        Menu librarianMenu = new Menu();
        librarianMenu.setTopTitle("Main Menu » Librarian Menu");
        librarianMenu.setMainTitle("Librarian Menu");
        librarianMenu.setMenuInfo(ANSI.ITALIC + ANSI.BRIGHT_GREEN + "Welcome, " + currentMember.getFirstName() + "!" + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
        librarianMenu.setExitOption("Log out");
        librarianMenu.addMenuOption("Manage Books");
        librarianMenu.addMenuOption("Manage Loans");
        librarianMenu.addMenuOption("Manage Readers");
        librarianMenu.addMenuOption("Manage Authors");

        while (librarianMenu.showMenu()) {
            switch (librarianMenu.getChoice()) {
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
//                case 3 -> BookController.addBookForAdmin(currentMember);
//                case 4 -> BookController.updateBookForAdmin(currentMember);
//                case 5 -> BookController.deleteBookForAdmin(currentMember);
            }
        }
    }

//    private static void showManageBooksMenu() throws SQLException {
//        Menu menu = new Menu();
//        menu.setTopTitle("Manage Books");
//        menu.addMenuOption("View Books");
//        menu.addMenuOption("Add book");
//        menu.addMenuOption("Modify book");
//        menu.addMenuOption("Delete book");
//
//        while (menu.showMenu()) {
//            switch (menu.getChoice()) {
//                case 1 -> showViewBooksSubMenu();
//                case 2 -> System.out.println("BookController.addBook()");
//                // case 2 -> BookController.addBook();
//                case 3 -> System.out.println("BookController.updateBook()");
//                // case 3 -> BookController.updateBook();
//                case 4 -> System.out.println("BookController.deleteBook()");
//                // case 4 -> BookController.deleteBook();
//                case 0 -> {
//                    return;
//                }
//                default -> System.out.println("Invalid option.");
//            }
//        }
//    }
//
//    private static void showViewBooksSubMenu() throws SQLException {
//        Menu menu = new Menu();
//        menu.setTopTitle("View Books");
//        menu.addMenuOption("View all books");
//        menu.addMenuOption("Search Books");
//        menu.addMenuOption("Search Authors");
//
//        while (menu.showMenu()) {
//            switch (menu.getChoice()) {
//                case 1 -> System.out.println("BookController.showAllBooksForAdmin()");
//                // case 1 -> BookController.showAllBooksForAdmin();
//                case 2 -> System.out.println("BookController.searchBooksForAdmin()");
//                // case 2 -> BookController.searchBooksForAdmin();
//                case 3 -> System.out.println("BookController.searchAuthorsForAdmin()");
//                // case 3 -> BookController.searchAuthorsForAdmin();
//                case 0 -> {
//                    return;
//                }
//                default -> System.out.println("Invalid option.");
//            }
//        }
//    }

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
        Menu manageReadersMenu = new Menu();
        manageReadersMenu.setTopTitle("Librarian Menu » Manage Readers");
        manageReadersMenu.setMainTitle("Manage Readers");
        manageReadersMenu.setMenuInfo(ANSI.ITALIC + "Manage readers." + ANSI.NO_ITALIC);
        manageReadersMenu.addMenuOption("View readers");
        manageReadersMenu.addMenuOption("Add reader");
        manageReadersMenu.addMenuOption("Modify reader");
        manageReadersMenu.addMenuOption("Delete reader");
        manageReadersMenu.addMenuOption("Change reader password");

        while (manageReadersMenu.showMenu()) {
            switch (manageReadersMenu.getChoice()) {
                case 1 -> showViewReadersSubMenu(currentMember);
                case 2 -> MemberController.addMemberByAdmin(currentMember);
                case 3 -> MemberController.updateMemberByAdmin(currentMember);
                case 4 -> MemberController.deleteMemberByAdmin(currentMember);
                case 5 -> MemberController.changeMemberPasswordByAdmin(currentMember);
            }
        }
    }

    private static void showViewReadersSubMenu(Member currentMember) throws SQLException {
        Menu viewReadersSubMenu = new Menu();
        viewReadersSubMenu.setTopTitle("Librarian Menu » View Readers");
        viewReadersSubMenu.setMainTitle("View Readers");
        viewReadersSubMenu.setMenuInfo(ANSI.ITALIC + "View readers." + ANSI.NO_ITALIC);
        viewReadersSubMenu.addMenuOption("View all readers");
        viewReadersSubMenu.addMenuOption("Search reader by email");

        while (viewReadersSubMenu.showMenu()) {
            switch (viewReadersSubMenu.getChoice()) {
                case 1 -> MemberController.showAllMembersForAdmin(currentMember);
                case 2 -> MemberController.showMemberByEmail(currentMember);
            }
        }
    }

    //4. Manage Authors
    private static void showManageAuthorsMenu(Member currentMember) throws SQLException {
        Menu manageAuthorsMenu = new Menu();
        manageAuthorsMenu.setTopTitle("Librarian Menu » Manage Authors");
        manageAuthorsMenu.setMainTitle("Manage Authors");
        manageAuthorsMenu.setMenuInfo(ANSI.ITALIC + "Manage authors." + ANSI.NO_ITALIC);
        manageAuthorsMenu.addMenuOption("View authors");
        manageAuthorsMenu.addMenuOption("Add author");
        manageAuthorsMenu.addMenuOption("Modify author");
        manageAuthorsMenu.addMenuOption("Delete author");
        manageAuthorsMenu.addMenuOption("Find author");

        while (manageAuthorsMenu.showMenu()) {
            switch (manageAuthorsMenu.getChoice()) {
                //case 1 -> System.out.println("AuthorController.showAllAuthors()");
                case 1 -> AuthorController.showAllAuthors();
                case 2 -> AuthorController.addAuthor(currentMember);
                case 3 -> AuthorController.updateAuthor(currentMember);
                case 4 -> AuthorController.deleteAuthor(currentMember);
                case 5 -> AuthorController.findAuthor();
            }
        }
    }

    //5. Manage Categories
    private static void showManageCategoriesMenu() throws SQLException {
        Menu manageCategoriesMenu = new Menu();
        manageCategoriesMenu.setTopTitle("Librarian Menu » Manage Categories");
        manageCategoriesMenu.setMainTitle("Manage Categories");
        manageCategoriesMenu.setMenuInfo(ANSI.ITALIC + "Manage categories." + ANSI.NO_ITALIC);
        manageCategoriesMenu.setExitOption("Back to Librarian Menu");
        manageCategoriesMenu.addMenuOption("View categories");
        manageCategoriesMenu.addMenuOption("Add category");
        manageCategoriesMenu.addMenuOption("Modify category");
        manageCategoriesMenu.addMenuOption("Delete category");

        while (manageCategoriesMenu.showMenu()) {
            switch (manageCategoriesMenu.getChoice()) {
                case 1 -> System.out.println("CategoryController.showAllCategories()");
                // case 1 -> CategoryController.showAllCategories();
                case 2 -> System.out.println("CategoryController.addCategory()");
                // case 2 -> CategoryController.addCategory();
                case 3 -> System.out.println("CategoryController.updateCategory()");
                // case 3 -> CategoryController.updateCategory();
                case 4 -> System.out.println("CategoryController.deleteCategory()");
                // case 4 -> CategoryController.deleteCategory();
            }
        }
    }
}
