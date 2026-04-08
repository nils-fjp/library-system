package member;

import base.BaseController;
import book.BookController;
import loan.LoanController;
import ui.ANSI;
import ui.ConsolePrinter;
import ui.Menu;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MemberController extends BaseController<Member, Integer> {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ConsolePrinter printer = new ConsolePrinter();

//    public static void showMenu() throws SQLException {
//        Menu menu = new Menu();
//        menu.setTopTitle("Your Profile");
//        menu.addMenuOption("Change your data");
//        menu.addMenuOption("Manage Loans");
//
//        while (menu.showMenu()) {
//
//            switch (menu.getChoice()) {
//
//                //case 1 -> BookController.showManageBooksMenu();
//                case 1 -> System.out.println("BookController.showManageBooksMenu()");
//                case 2 -> LoanController.showManageLoansMenu();
//                //case 3 -> MemberController.showManageMembersMenu();
//                case 3 -> System.out.println("MemberController.showManageMembersMenu()");
//                //case 4 -> AuthorController.showManageAuthorMenu();
//                case 4 -> System.out.println("AuthorController.showManageAuthorMenu()");
//                //case 5 -> CategoryController.showManageCategoriesMenu();
//                case 5 -> System.out.println("ategoryController.showManageCategoriesMenu()");
//                case 0 -> { return; }
//            }
//        }
//    }
public static void showReaderMenu(Member currentMember) throws SQLException {
    Menu menu = new Menu();

    menu.setTopTitle("Reader Menu");
    menu.setMidTitle("Reader");
    menu.setMenuInfo("Choose section");
    menu.setExitOption("Back");
    menu.addMenuOption("Books");
    menu.addMenuOption("My Loans");
    menu.addMenuOption("My Profile");
    menu.setPrePrompt("Type a number and press enter...");
    menu.setPromptLine("Enter: ");

    while (menu.showMenu()) {

        switch (menu.getChoice()) {
            case 1 -> BookController.showMenu();
            case 2 -> LoanController.showMenu();
            case 3 -> MemberController.showCurrentMemberProfile(currentMember);
            case 0 -> {
                return;
            }
            default -> System.out.println("Invalid input");
        }
    }
}

    public static void showLibrarianMenu(Member currentMember) throws SQLException {
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
                case 5 -> System.out.println("CategoryController.showManageCategoriesMenu()");
                case 0 -> {
                    return;
                }
            }
        }
    }
    public static void showCurrentMemberProfile(Member currentMember) {
        MemberService service = new MemberService();

        try {
            if (currentMember == null) {
                printer.printError("No authorized user. ");
                return;
            }
            Optional<MemberProfileDto> optionalMember = service.getProfileById(currentMember.getId());

            if (optionalMember.isPresent()) {
                printProfileMember(optionalMember.get());
            } else {
                printer.printError("Profile not found.");
            }

        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    public static void showManageMembersMenu(Member currentMember) {
        MemberService service = new MemberService();
        try{
            service.validateLibrarianAccess(currentMember);
            Menu menu = new Menu();

            menu.setTopTitle("Manage Readers");
            menu.setExitOption("Back");
            menu.addMenuOption("View all readers +");//1
            menu.addMenuOption("Search reader by email +");//2
            menu.addMenuOption("Add reader");//3
            menu.addMenuOption("Modify reader +");//4
            menu.addMenuOption("Delete reader");//5
            menu.setPrePrompt("Type a number and press enter...");
            menu.setPromptLine("Enter: ");

            while (menu.showMenu()) {
                switch (menu.getChoice()) {
                    case 0 -> {
                        return;
                    }
                    case 1 -> showAllMembersForAdmin();
                    case 2 -> showMemberByEmail();
                    //case 3 -> addMember();
                    case 4 -> updateMemberByAdmin();
                    case 5 -> System.out.println("WIP: Delete reader");
                    default -> menu.setMenuInfo("Invalid input!");
                }
            }
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        }




    }

    public static void showAllMembersForAdmin() {
        MemberService service = new MemberService();

        try {
            List<MemberAdminDto> members = service.getAllForAdminView();
            for (MemberAdminDto member : members) {
                printAdminMember(member);
            }
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    public static void showMemberByEmail() {
        MemberService service = new MemberService();

        try {
            System.out.print("\tEnter member email: ");
            String email = scanner.nextLine();

            //Optional<MemberProfileDto> optionalMember = service.getByEmailForView(email);
            Optional<MemberAdminDto> optionalMember = service.getByEmailForViewForAdmin(email);

            if (optionalMember.isPresent()) {
                printAdminMember(optionalMember.get());
            } else {
                printer.printError("Member not found.");
            }

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

//    public static void showMemberById() {
//        MemberService service = new MemberService();
//
//        try {
//            System.out.print("\tEnter member id: ");
//            int id = Integer.parseInt(scanner.nextLine());
//
//            Optional<Member> optionalMember = service.getById(id);
//
//            if (optionalMember.isPresent()) {
//                printAdminMember(optionalMember.get());
//            } else {
//                printer.printError("Member not found.");
//            }
//
//        } catch (NumberFormatException e) {
//            printer.printError("Invalid input. Enter a number.");
//        } catch (IllegalArgumentException e) {
//            printer.printError(e.getMessage());
//        } catch (SQLException e) {
//            printer.printError("Database error: " + e.getMessage());
//        }
//    }

    public static void updateMemberByAdmin() {
        MemberService service = new MemberService();

        try {
            System.out.print("\tEnter member email: ");
            String email = scanner.nextLine();

            Optional<MemberAdminDto> optionalMember = service.getByEmailForViewForAdmin(email);

            if (optionalMember.isEmpty()) {
                printer.printError("Member not found.");
                return;
            }

            MemberAdminDto currentDto = optionalMember.get();

            printer.printSuccess("Current member data:");
            printAdminMember(currentDto);

            Member member = new Member();
            member.setId(currentDto.getId());

            System.out.print("\tEnter new first name [" + currentDto.getFirstName() + "]: ");
            String firstName = scanner.nextLine();
            member.setFirstName(firstName.isBlank() ? currentDto.getFirstName() : firstName);

            System.out.print("\tEnter new last name [" + currentDto.getLastName() + "]: ");
            String lastName = scanner.nextLine();
            member.setLastName(lastName.isBlank() ? currentDto.getLastName() : lastName);

            System.out.print("\tEnter new email [" + currentDto.getEmail() + "]: ");
            String newEmail = scanner.nextLine();
            member.setEmail(newEmail.isBlank() ? currentDto.getEmail() : newEmail);

            System.out.print("\tEnter new membership date [" + currentDto.getMembershipDate() + "] (yyyy-mm-dd): ");
            String membershipDate = scanner.nextLine();
            member.setMembershipDate(
                    membershipDate.isBlank()
                            ? currentDto.getMembershipDate()
                            : LocalDate.parse(membershipDate)
            );

            System.out.print("\tEnter new membership type [" + currentDto.getMembershipType() + "]: ");
            String membershipType = scanner.nextLine();
            member.setMembershipType(
                    membershipType.isBlank()
                            ? currentDto.getMembershipType()
                            : membershipType
            );

            System.out.print("\tEnter new status [" + currentDto.getStatus() + "]: ");
            String status = scanner.nextLine();
            member.setStatus(
                    status.isBlank()
                            ? currentDto.getStatus()
                            : status
            );

            Optional<MemberAdminDto> updatedMember = service.updateMemberByAdmin(member);

            if (updatedMember.isPresent()) {
                printer.printSuccess("Member updated successfully.");
                printAdminMember(updatedMember.get());
            } else {
                printer.printError("Member was not updated.");
            }

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        } catch (Exception e) {
            printer.printError("Input error: " + e.getMessage());
        }
    }

//    private static void printProfileMember(MemberProfileDto member) {
//        printer.printHeader("Member Info");
//        printer.printField("First name", member.getFirstName());
//        printer.printField("Last name", member.getLastName());
//        printer.printField("Email", member.getEmail());
//        printer.printField("Membership date", member.getMembershipDate());
//        printer.printField("Membership type", member.getMembershipType());
//        printer.printField("Status", formatStatus(member.getStatus()));
//        printer.printFooter();
//    }
//
//    private static void printAdminMember(MemberAdminDto member) {
//        printer.printHeader("Member Info");
//        printer.printField("Member Id", member.getId());
//        printer.printField("First name", member.getFirstName());
//        printer.printField("Last name", member.getLastName());
//        printer.printField("Email", member.getEmail());
//        printer.printField("Membership date", member.getMembershipDate());
//        printer.printField("Membership type", member.getMembershipType());
//        printer.printField("Status", formatStatus(member.getStatus()));
//        printer.printField("Member Role", member.getRole());
//        printer.printFooter();
//    }

    private static void printCommonMemberInfo(
            String firstName,
            String lastName,
            String email,
            Object membershipDate,
            String membershipType,
            String status
    ) {
        printer.printField("First name", firstName);
        printer.printField("Last name", lastName);
        printer.printField("Email", email);
        printer.printField("Membership date", membershipDate);
        printer.printField("Membership type", membershipType);
        printer.printField("Status", formatStatus(status));
    }

    private static void printProfileMember(MemberProfileDto member) {
        printer.printHeader("My Profile");

        printCommonMemberInfo(
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getMembershipDate(),
                member.getMembershipType(),
                member.getStatus()
        );

        printer.printFooter();
    }

    private static void printAdminMember(MemberAdminDto member) {
        printer.printHeader("Member Info");
        printer.printField("Member Id", member.getId());

        printCommonMemberInfo(
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getMembershipDate(),
                member.getMembershipType(),
                member.getStatus()
        );

        printer.printField("Member Role", member.getRole());
        printer.printFooter();
    }

    private static String formatStatus(String status) {
        if (status == null) {
            return ANSI.BRIGHT_BLACK + "[UNKNOWN]" + ANSI.DEFAULT_FG;
        }

        return switch (status.toLowerCase()) {
            case "active" -> ANSI.BRIGHT_GREEN + ANSI.BOLD + "[ACTIVE]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            case "suspended" -> ANSI.BRIGHT_YELLOW + ANSI.BOLD + "[SUSPENDED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            case "expired" -> ANSI.BRIGHT_RED + ANSI.BOLD + "[EXPIRED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            default -> status;
        };
    }
}
