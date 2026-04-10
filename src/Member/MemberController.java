package Member;

import Base.BaseController;
import UI.ANSI;
import UI.ConsolePrinter;
import UI.Menu;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MemberController extends BaseController<Member, Integer> {

    // =========================================================
    // UI dependencies
    // =========================================================

    private static final Scanner scanner = new Scanner(System.in);
    private static final ConsolePrinter printer = new ConsolePrinter();



    // =========================================================
    // Reader actions - 3. My profile
    //                   --> 1. View my profile
    //                       2. Change password
    // =========================================================
    /**
     * Shows profile information for the currently authorized member.
     * Uses MemberService to load profile data by member id.
     * Displays result through printProfileMember().
     */
    // 1 showCurrentMemberProfile (MemberController) +
    //    -> 2 getProfileById (MemberService)
    //       -> 3 validateId (MemberService)
    //          -> 4 validateId (BaseService)
    //       -> 5 getById (MemberRepository)
    //       -> 6 toProfileDto (MemberMapper)
    //    -> 7 printProfileMember (MemberController)
    //    -> 8 printError (ConsolePrinter)
    public static void showCurrentMemberProfile(Member currentMember) {
        MemberService service = new MemberService();

        try {
            if (currentMember == null) {
                printer.printError("No authorized user.");
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



    // =========================================================
    // Reader actions - 3. My profile
    //                       1. View my profile
    //                   --> 2. Change password
    // =========================================================
    public static void changePass(Member currentMember){

    }



    // =========================================================
    // Librarian menu actions
    // =========================================================

    /**
     * Shows librarian menu for member management.
     * Uses MemberService to validate librarian access.
     * Routes user choice to admin actions like search, update, and list.
     */
    public static void showManageMembersMenu(Member currentMember) {
        MemberService service = new MemberService();

        try {
            service.validateLibrarianAccess(currentMember);

            Menu menu = new Menu();
            menu.setTopTitle("Manage Readers");
            menu.setExitOption("Back");
            menu.addMenuOption("View all readers");
            menu.addMenuOption("Search reader by email");
            menu.addMenuOption("Add reader");
            menu.addMenuOption("Modify reader");
            menu.addMenuOption("Delete reader");
            menu.setPrePrompt("Type a number and press enter...");
            menu.setPromptLine("Enter: ");

            while (menu.showMenu()) {
                switch (menu.getChoice()) {
                    case 0 -> {
                        return;
                    }
                    case 1 -> showAllMembersForAdmin();
                    case 2 -> showMemberByEmail();
                    case 3 -> printer.printError("WIP: Add reader");
                    case 4 -> updateMemberByAdmin();
                    case 5 -> printer.printError("WIP: Delete reader");
                    default -> menu.setMenuInfo("Invalid input!");
                }
            }

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        }
    }

    // =========================================================
    // Librarian/admin member actions
    // =========================================================

    /**
     * Loads and displays all members in admin view.
     * Uses MemberService.getAllForAdminView().
     * Prints every member through printAdminMember().
     */
    public static void showAllMembersForAdmin() {
        MemberService service = new MemberService();

        try {
            List<MemberAdminDto> members = service.getAllForAdminView();

            if (members.isEmpty()) {
                printer.printError("No readers found.");
                return;
            }

            for (MemberAdminDto member : members) {
                printAdminMember(member);
            }

        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    /**
     * Finds one member by email and displays it in admin view.
     * Uses findMemberByEmail() to read input and MemberService to search member data.
     * Prints result through printAdminMember().
     */
    public static void showMemberByEmail() {
        MemberService service = new MemberService();

        try {
            Optional<MemberAdminDto> optionalMember = findMemberByEmail(service);

            if (optionalMember.isEmpty()) {
                printer.printError("Member not found.");
                return;
            }

            printAdminMember(optionalMember.get());

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    /**
     * Updates member data in librarian/admin flow.
     * Uses findMemberByEmail() to locate member, buildUpdatedMemberFromInput() to collect changes,
     * and MemberService.updateMemberByAdmin() to save updated data.
     */
//1. найти участника
//2. показать текущие данные
//3. собрать новые данные
//4. вызвать service.updateMemberByAdmin(dto)
//5. показать результат
//Это и есть controller flow.

    public static void updateMemberByAdmin() {
        MemberService service = new MemberService();

        try {
            Optional<MemberAdminDto> optionalMember = findMemberByEmail(service);

            if (optionalMember.isEmpty()) {
                printer.printError("Member not found.");
                return;
            }

            MemberAdminDto currentDto = optionalMember.get();

            printer.printSuccess("Current member data:");
            printAdminMember(currentDto);

            UpdateMemberDto updateDto = buildUpdatedMemberFromInput(currentDto);
            Optional<MemberAdminDto> updatedDto = service.updateMemberByAdmin(updateDto);

            if (updatedDto.isEmpty()) {
                printer.printError("Member was not updated.");
                return;
            }

            printer.printSuccess("Member updated successfully.");
            printAdminMember(updatedDto.get());

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    // =========================================================
    // Input helper methods
    // =========================================================

    /**
     * Reads member email from console and requests member data from MemberService.
     * Returns admin DTO if member exists.
     */
    private static Optional<MemberAdminDto> findMemberByEmail(MemberService service) throws SQLException {
        String email = readRequiredInput("Enter member email");
        return service.getByEmailForViewForAdmin(email);
    }

    /**
     * Builds UpdateMemberDto from console input.
     * Keeps old values when user enters empty input.
     * Used in admin member update flow.
     */
    private static UpdateMemberDto buildUpdatedMemberFromInput(MemberAdminDto currentDto) {
        UpdateMemberDto dto = new UpdateMemberDto();
        dto.setId(currentDto.getId());

        dto.setFirstName(readUpdatedString("Enter new first name", currentDto.getFirstName()));
        dto.setLastName(readUpdatedString("Enter new last name", currentDto.getLastName()));
        dto.setEmail(readUpdatedString("Enter new email", currentDto.getEmail()));
        dto.setMembershipDate(readUpdatedDate("Enter new membership date", currentDto.getMembershipDate()));
        dto.setMembershipType(readUpdatedString("Enter new membership type", currentDto.getMembershipType()));
        dto.setStatus(readUpdatedString("Enter new status", currentDto.getStatus()));

        return dto;
    }

    /**
     * Reads required text input from console.
     * Repeats until user enters non-empty value.
     */
    private static String readRequiredInput(String label) {
        while (true) {
            System.out.print("\t" + label + ": ");
            String input = scanner.nextLine().trim();

            if (!input.isBlank()) {
                return input;
            }

            printer.printError("Input cannot be empty.");
        }
    }

    /**
     * Reads editable string value from console.
     * Returns current value if input is empty.
     */
    private static String readUpdatedString(String label, String currentValue) {
        System.out.print("\t" + label + " [" + currentValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isBlank() ? currentValue : input;
    }

    /**
     * Reads editable LocalDate value from console.
     * Returns current value if input is empty.
     * Repeats until user enters date in yyyy-mm-dd format.
     */
    private static LocalDate readUpdatedDate(String label, LocalDate currentValue) {
        while (true) {
            System.out.print("\t" + label + " [" + currentValue + "] (yyyy-mm-dd): ");
            String input = scanner.nextLine().trim();

            if (input.isBlank()) {
                return currentValue;
            }

            try {
                return LocalDate.parse(input);
            } catch (java.time.format.DateTimeParseException e) {
                printer.printError("Invalid date format. Use yyyy-mm-dd.");
            }
        }
    }

    // =========================================================
    // Output helper methods
    // =========================================================

    /**
     * Prints fields shared by profile and admin member views.
     * Used by printProfileMember() and printAdminMember().
     */
    private static void printCommonMemberInfo(
            String firstName,
            String lastName,
            String email,
            LocalDate membershipDate,
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

    /**
     * Prints current member profile information.
     * Uses printCommonMemberInfo() for shared fields.
     */
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

    /**
     * Prints member information in admin view.
     * Uses printCommonMemberInfo() and adds admin-only fields like id and role.
     */
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

    // =========================================================
    // Formatting helpers
    // =========================================================

    /**
     * Formats member status with ANSI colors for console output.
     * Used in profile and admin member printing.
     */
    private static String formatStatus(String status) {
        if (status == null) {
            return ANSI.BRIGHT_BLACK + "[UNKNOWN]" + ANSI.DEFAULT_FG;
        }

        return switch (status.toLowerCase()) {
            case "active" -> ANSI.BRIGHT_GREEN + ANSI.BOLD + "[ACTIVE]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            case "suspended" -> ANSI.BRIGHT_YELLOW + ANSI.BOLD + "[SUSPENDED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            case "expired" -> ANSI.BRIGHT_RED + ANSI.BOLD + "[EXPIRED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            default -> ANSI.BRIGHT_BLACK + "[" + status.toUpperCase() + "]" + ANSI.DEFAULT_FG;
        };
    }
}

//package Member;
//
//import Base.BaseController;
//import UI.ANSI;
//import UI.ConsolePrinter;
//
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.Scanner;
//
//public class MemberController extends BaseController<Member, Integer> {
//
//    private static final Scanner scanner = new Scanner(System.in);
//    private static final ConsolePrinter printer = new ConsolePrinter();
//
//    // =========================================================
//    // Reader actions
//    // =========================================================
//
//    public static void showCurrentMemberProfile(Member currentMember) {
//        MemberService service = new MemberService();
//
//        try {
//            if (currentMember == null) {
//                printer.printError("No authorized user.");
//                return;
//            }
//
//            Optional<MemberProfileDto> optionalMember = service.getProfileById(currentMember.getId());
//
//            if (optionalMember.isPresent()) {
//                printProfileMember(optionalMember.get());
//            } else {
//                printer.printError("Profile not found.");
//            }
//
//        } catch (SQLException e) {
//            printer.printError("Database error: " + e.getMessage());
//        }
//    }
//
//    public static void changePassword(Member currentMember) {
//        printer.printError("WIP: MemberController.changePassword(currentMember)");
//    }
//
//    // =========================================================
//    // Librarian/admin member actions
//    // =========================================================
//
//    public static void showAllMembersForAdmin() {
//        MemberService service = new MemberService();
//
//        try {
//            List<MemberAdminDto> members = service.getAllForAdminView();
//
//            if (members.isEmpty()) {
//                printer.printError("No readers found.");
//                return;
//            }
//
//            for (MemberAdminDto member : members) {
//                printAdminMember(member);
//            }
//
//        } catch (SQLException e) {
//            printer.printError("Database error: " + e.getMessage());
//        }
//    }
//
//    public static void showMemberByEmail() {
//        MemberService service = new MemberService();
//
//        try {
//            Optional<MemberAdminDto> optionalMember = findMemberByEmail(service);
//
//            if (optionalMember.isEmpty()) {
//                printer.printError("Member not found.");
//                return;
//            }
//
//            printAdminMember(optionalMember.get());
//
//        } catch (IllegalArgumentException e) {
//            printer.printError(e.getMessage());
//        } catch (SQLException e) {
//            printer.printError("Database error: " + e.getMessage());
//        }
//    }
//
//    public static void addMemberByAdmin() {
//        printer.printError("WIP: MemberController.addMemberByAdmin()");
//    }
//
//    public static void updateMemberByAdmin() {
//        MemberService service = new MemberService();
//
//        try {
//            Optional<MemberAdminDto> optionalMember = findMemberByEmail(service);
//
//            if (optionalMember.isEmpty()) {
//                printer.printError("Member not found.");
//                return;
//            }
//
//            MemberAdminDto currentDto = optionalMember.get();
//
//            printer.printSuccess("Current member data:");
//            printAdminMember(currentDto);
//
//            UpdateMemberDto updateDto = buildUpdatedMemberFromInput(currentDto);
//            Optional<MemberAdminDto> updatedDto = service.updateMemberByAdmin(updateDto);
//
//            if (updatedDto.isEmpty()) {
//                printer.printError("Member was not updated.");
//                return;
//            }
//
//            printer.printSuccess("Member updated successfully.");
//            printAdminMember(updatedDto.get());
//
//        } catch (IllegalArgumentException e) {
//            printer.printError(e.getMessage());
//        } catch (SQLException e) {
//            printer.printError("Database error: " + e.getMessage());
//        }
//    }
//
//    public static void deleteMemberByAdmin() {
//        printer.printError("WIP: MemberController.deleteMemberByAdmin()");
//    }
//
//    // =========================================================
//    // Input helper methods
//    // =========================================================
//
//    private static Optional<MemberAdminDto> findMemberByEmail(MemberService service) throws SQLException {
//        String email = readRequiredInput("Enter member email");
//        return service.getByEmailForViewForAdmin(email);
//    }
//
//    private static UpdateMemberDto buildUpdatedMemberFromInput(MemberAdminDto currentDto) {
//        UpdateMemberDto dto = new UpdateMemberDto();
//        dto.setId(currentDto.getId());
//
//        dto.setFirstName(readUpdatedString("Enter new first name", currentDto.getFirstName()));
//        dto.setLastName(readUpdatedString("Enter new last name", currentDto.getLastName()));
//        dto.setEmail(readUpdatedString("Enter new email", currentDto.getEmail()));
//        dto.setMembershipDate(readUpdatedDate("Enter new membership date", currentDto.getMembershipDate()));
//        dto.setMembershipType(readUpdatedString("Enter new membership type", currentDto.getMembershipType()));
//        dto.setStatus(readUpdatedString("Enter new status", currentDto.getStatus()));
//
//        return dto;
//    }
//
//    private static String readRequiredInput(String label) {
//        while (true) {
//            System.out.print("\t" + label + ": ");
//            String input = scanner.nextLine().trim();
//
//            if (!input.isBlank()) {
//                return input;
//            }
//
//            printer.printError("Input cannot be empty.");
//        }
//    }
//
//    private static String readUpdatedString(String label, String currentValue) {
//        System.out.print("\t" + label + " [" + currentValue + "]: ");
//        String input = scanner.nextLine().trim();
//        return input.isBlank() ? currentValue : input;
//    }
//
//    private static LocalDate readUpdatedDate(String label, LocalDate currentValue) {
//        while (true) {
//            System.out.print("\t" + label + " [" + currentValue + "] (yyyy-mm-dd): ");
//            String input = scanner.nextLine().trim();
//
//            if (input.isBlank()) {
//                return currentValue;
//            }
//
//            try {
//                return LocalDate.parse(input);
//            } catch (java.time.format.DateTimeParseException e) {
//                printer.printError("Invalid date format. Use yyyy-mm-dd.");
//            }
//        }
//    }
//
//    // =========================================================
//    // Output helper methods
//    // =========================================================
//
//    private static void printCommonMemberInfo(
//            String firstName,
//            String lastName,
//            String email,
//            LocalDate membershipDate,
//            String membershipType,
//            String status
//    ) {
//        printer.printField("First name", firstName);
//        printer.printField("Last name", lastName);
//        printer.printField("Email", email);
//        printer.printField("Membership date", membershipDate);
//        printer.printField("Membership type", membershipType);
//        printer.printField("Status", formatStatus(status));
//    }
//
//    private static void printProfileMember(MemberProfileDto member) {
//        printer.printHeader("My Profile");
//
//        printCommonMemberInfo(
//                member.getFirstName(),
//                member.getLastName(),
//                member.getEmail(),
//                member.getMembershipDate(),
//                member.getMembershipType(),
//                member.getStatus()
//        );
//
//        printer.printFooter();
//    }
//
//    private static void printAdminMember(MemberAdminDto member) {
//        printer.printHeader("Member Info");
//        printer.printField("Member Id", member.getId());
//
//        printCommonMemberInfo(
//                member.getFirstName(),
//                member.getLastName(),
//                member.getEmail(),
//                member.getMembershipDate(),
//                member.getMembershipType(),
//                member.getStatus()
//        );
//
//        printer.printField("Member Role", member.getRole());
//        printer.printFooter();
//    }
//
//    // =========================================================
//    // Formatting helpers
//    // =========================================================
//
//    private static String formatStatus(String status) {
//        if (status == null) {
//            return ANSI.BRIGHT_BLACK + "[UNKNOWN]" + ANSI.DEFAULT_FG;
//        }
//
//        return switch (status.toLowerCase()) {
//            case "active" -> ANSI.BRIGHT_GREEN + ANSI.BOLD + "[ACTIVE]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
//            case "suspended" -> ANSI.BRIGHT_YELLOW + ANSI.BOLD + "[SUSPENDED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
//            case "expired" -> ANSI.BRIGHT_RED + ANSI.BOLD + "[EXPIRED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
//            default -> ANSI.BRIGHT_BLACK + "[" + status.toUpperCase() + "]" + ANSI.DEFAULT_FG;
//        };
//    }
//}