


    public class LibrarianMenuController {
        public static void showMenu() {
            Menu menu = new Menu();
            menu.setTopTitle("Librarian Menu");
            menu.addMenuOption("Dashboard");
            menu.addMenuOption("Books");
            menu.addMenuOption("Borrowers");
            menu.addMenuOption("Loans");
            menu.addMenuOption("Authors");
            menu.addMenuOption("Categories");
            menu.addMenuOption("Statistics");

            while (true) {
                menu.showMenu();

                switch (menu.getChoice()) {
                    case 0 -> { return; }
                    //case 1 -> DashboardController.showDashboard();
                    //case 2 -> BookController.showLibrarianMenu();
                    case 3 -> MemberController.showLibrarianMenu();
                    //case 4 -> LoanController.showAdminLoanMenu();
                    //case 5 -> AuthorController.showMenu();
                   // case 6 -> CategoryController.showMenu();
                   // case 7 -> StatisticsController.showMenu();
                }
            }
        }
    }

