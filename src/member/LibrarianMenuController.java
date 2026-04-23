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
        Menu menu = new Menu();
        menu.setTopTitle("Main Menu » Librarian Menu");
        menu.setMainTitle("Librarian Menu");
        menu.setMenuInfo(ANSI.ITALIC + ANSI.BRIGHT_GREEN + "Welcome, " + currentMember.getFirstName() + "!" + ANSI.NO_ITALIC + ANSI.DEFAULT_FG);
        menu.addMenuOption("Manage Books");
        menu.addMenuOption("Manage Loans");
        menu.addMenuOption("Manage Readers");
        menu.addMenuOption("Manage Authors");
        menu.addMenuOption("Manage Categories");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> showManageBooksMenu(currentMember);
                case 2 -> showManageLoansMenu(currentMember);
                case 3 -> showManageReadersMenu(currentMember);
                case 4 -> showManageAuthorsMenu(currentMember);
                case 5 -> showManageCategoriesMenu();
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
        manageLoansMenu.setMenuInfo(ANSI.ITALIC + "Manage active loans and returns." + ANSI.NO_ITALIC);
        manageLoansMenu.setExitOption("Back to Librarian Menu");
        manageLoansMenu.addMenuOption("View All Active Loans");
        manageLoansMenu.addMenuOption("Add Loan");
        manageLoansMenu.addMenuOption("Register return");

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
        menu.setTopTitle("Manage Readers");
        menu.addMenuOption("View readers");
        menu.addMenuOption("Add reader");
        menu.addMenuOption("Modify reader");
        menu.addMenuOption("Delete reader");
        menu.addMenuOption("Change reader password");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> showViewReadersSubMenu(currentMember);
                case 2 -> MemberController.addMemberByAdmin(currentMember);
                case 3 -> MemberController.updateMemberByAdmin(currentMember);
                case 4 -> MemberController.deleteMemberByAdmin(currentMember);
                case 5 -> MemberController.changeMemberPasswordByAdmin(currentMember);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void showViewReadersSubMenu(Member currentMember) throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("View Readers");
        menu.addMenuOption("View all readers");
        menu.addMenuOption("Search reader by email");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> MemberController.showAllMembersForAdmin(currentMember);
                case 2 -> MemberController.showMemberByEmail(currentMember);
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
        menu.addMenuOption("Delete author");
        menu.addMenuOption("Find author");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                //case 1 -> System.out.println("AuthorController.showAllAuthors()");
                case 1 -> AuthorController.showAllAuthors();
                case 2 -> AuthorController.addAuthor(currentMember);
                case 3 -> AuthorController.updateAuthor(currentMember);
                case 4 -> AuthorController.deleteAuthor(currentMember);
                case 5 -> AuthorController.findAuthor();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    //5. Manage Categories
    private static void showManageCategoriesMenu() throws SQLException {
        Menu menu = new Menu();
        menu.setTopTitle("Manage Categories");
        menu.addMenuOption("View categories");
        menu.addMenuOption("Add category");
        menu.addMenuOption("Modify category");
        menu.addMenuOption("Delete category");

        while (menu.showMenu()) {
            switch (menu.getChoice()) {
                case 1 -> System.out.println("CategoryController.showAllCategories()");
                // case 1 -> CategoryController.showAllCategories();
                case 2 -> System.out.println("CategoryController.addCategory()");
                // case 2 -> CategoryController.addCategory();
                case 3 -> System.out.println("CategoryController.updateCategory()");
                // case 3 -> CategoryController.updateCategory();
                case 4 -> System.out.println("CategoryController.deleteCategory()");
                // case 4 -> CategoryController.deleteCategory();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
