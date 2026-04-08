package Member;

import Book.BookController;
import Book.BookRepository;
import Loan.LoanController;
import UI.Menu;

import java.sql.SQLException;

public class LibrarianMenuController {
        public static void showMenu(Member currentMember) throws SQLException {
            Menu menu = new Menu();
            menu.setTopTitle("Librarian Menu");
            menu.addMenuOption("Manage Books");//1
            menu.addMenuOption("Manage Loans");//2
            menu.addMenuOption("Manage Readers");//3
            menu.addMenuOption("Manage Authors");//4
            menu.addMenuOption("Manage Categories");//5

            while (menu.showMenu()) {

                switch (menu.getChoice()) {

                    //case 1 -> BookController.showManageBooksMenu();
                    case 1 -> System.out.println("BookController.showManageBooksMenu()");
                    case 2 -> LoanController.showManageLoansMenu();
                    case 3 -> MemberController.showManageMembersMenu(currentMember);
                    //case 4 -> AuthorController.showManageAuthorMenu();
                    case 4 -> System.out.println("AuthorController.showManageAuthorMenu()");
                    //case 5 -> CategoryController.showManageCategoriesMenu();
                    case 5 -> System.out.println("ategoryController.showManageCategoriesMenu()");
                    case 0 -> { return; }
                }
            }
        }
    }

