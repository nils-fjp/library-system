package member;

import base.BaseController;
import ui.ANSI;
import ui.ConsolePrinter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.LinkedHashMap;


public class MemberController extends BaseController<Member, Integer> {

    // =========================================================
    // UI dependencies
    // =========================================================

    private static final Scanner scanner = new Scanner(System.in);
    static final ConsolePrinter printer = new ConsolePrinter();

    // =========================================================
    // READER MEMBER ACTIONS
    // =========================================================

    // 1 showCurrentMemberProfile (MemberController) +
    //    -> 2 getProfileById (MemberService)
    //       -> 3 validateId (MemberService)
    //          -> 4 validateId (BaseService)
    //       -> 5 getById (MemberRepository)
    //       -> 6 toProfileDto (MemberMapper)
    //    -> 7 printProfileMember (MemberController)
    //    -> 8 printError (ConsolePrinter)

    // 1. View my profile
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

    // 2. Update profile info
    public static void updateOwnProfile(Member currentMember){
        MemberService service = new MemberService();
        try{
            if (currentMember == null) {
                printer.printError("No authorized user.");
                return;
            }

            Optional<MemberProfileDto> optionalMember = service.getProfileById(currentMember.getId());
            if (optionalMember.isEmpty()) {
                printer.printError("Member not found.");
                return;
            }
            MemberProfileDto currentDto = optionalMember.get();
            printer.printSuccess("Your current data:");
            printProfileMember(currentDto);

            UpdateMyProfileDto updateDto = buildUpdatedMyProfileFromInput(currentMember, currentDto);
            //MemberValidator.validateUpdateMyProfileDto(updateDto);

            Optional<MemberProfileDto> updatedMember = service.updateOwnProfile(updateDto);
            if (updatedMember.isEmpty()) {
                printer.printError("Profile was not updated.");
                return;
            }

            printer.printSuccess("Profile updated successfully.");
            printProfileMember(updatedMember.get());

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }

    }

    private static UpdateMyProfileDto buildUpdatedMyProfileFromInput(Member currentMember, MemberProfileDto currentDto) {
        UpdateMyProfileDto dto = new UpdateMyProfileDto();
        dto.setId(currentMember.getId());
        dto.setFirstName(readUpdatedString("Enter new first name", currentDto.getFirstName()));
        dto.setLastName(readUpdatedString("Enter new last name", currentDto.getLastName()));
        dto.setEmail(readUpdatedString("Enter new email", currentDto.getEmail()));
        return dto;
    }

    // 3. Change password
    public static void changePassword(Member currentMember) {
        MemberService service = new MemberService();

        try {
            if (currentMember == null) {
                printer.printError("No authorized user.");
                return;
            }

            ChangePasswordDto dto = buildChangePasswordDto(currentMember);

            boolean changed = service.changeOwnPassword(dto);

            if (!changed) {
                printer.printError("Password was not changed.");
                return;
            }

            printer.printSuccess("Password changed successfully.");

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }
    private static ChangePasswordDto buildChangePasswordDto(Member currentMember) {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setMemberId(currentMember.getId());
        dto.setCurrentPassword(readRequiredInput("Enter current password"));
        dto.setNewPassword(readRequiredInput("Enter new password"));
        dto.setConfirmNewPassword(readRequiredInput("Confirm new password"));
        return dto;
    }


    // =========================================================
    // ADMIN MEMBER ACTIONS
    // =========================================================

    /**
     * Loads and displays all members in admin view.
     * Uses MemberService.getAllForAdminView().
     * Prints every member through printAdminMember().
     */
    // 1. View All Readers
    public static void showAllMembersForAdmin(Member currentMember) {
        MemberService service = new MemberService();

        try {
            service.validateLibrarianAccess(currentMember);
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
    //2. Search Readers
    public static void showMemberByEmail(Member currentMember) {
        MemberService service = new MemberService();

        try {
            service.validateLibrarianAccess(currentMember);
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

    // 3. Add Reader
    public static void addMemberByAdmin(Member currentMember){
        MemberService service = new MemberService();

        try {
            service.validateLibrarianAccess(currentMember);

            CreateMemberDto createDto = buildCreateMemberFromInput();
            Optional<MemberAdminDto> createdDto = service.createMemberByAdmin(createDto);

            if (createdDto.isEmpty()) {
                printer.printError("Member was not created.");
                return;
            }
            printer.printSuccess("Member created successfully.");
            printAdminMember(createdDto.get());

        } catch (IllegalArgumentException e){
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static CreateMemberDto buildCreateMemberFromInput() {
        CreateMemberDto dto = new CreateMemberDto();

        dto.setFirstName(readRequiredInput("Enter first name"));
        dto.setLastName(readRequiredInput("Enter last name"));
        dto.setEmail(readRequiredInput("Enter email"));
        dto.setPassword(readRequiredInput("Enter password"));
        return dto;
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
    //  4. Update Reader
    public static void updateMemberByAdmin(Member currentMember) {
        MemberService service = new MemberService();


        try {
            service.validateLibrarianAccess(currentMember);
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

    // 5. Change password
    public static void changeMemberPasswordByAdmin(Member currentMember) {
        MemberService service = new MemberService();

        try {
            service.validateLibrarianAccess(currentMember);

            Optional<MemberAdminDto> optionalMember = findMemberByEmail(service);

            if (optionalMember.isEmpty()) {
                printer.printError("Member not found.");
                return;
            }

            MemberAdminDto targetMember = optionalMember.get();

            printer.printSuccess("Selected member:");
            printAdminMember(targetMember);

            AdminChangePasswordDto dto = buildAdminChangePasswordDto(targetMember);

            boolean changed = service.changeMemberPasswordByAdmin(currentMember, dto);

            if (!changed) {
                printer.printError("Password was not changed.");
                return;
            }

            printer.printSuccess("Password changed successfully.");

        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static AdminChangePasswordDto buildAdminChangePasswordDto(MemberAdminDto member) {
        AdminChangePasswordDto dto = new AdminChangePasswordDto();
        dto.setMemberId(member.getId());
        dto.setNewPassword(readRequiredInput("Enter new password"));
        dto.setConfirmNewPassword(readRequiredInput("Confirm new password"));
        return dto;
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

        public static void printProfileMember(MemberProfileDto member) {
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("First name", member.getFirstName());
            fields.put("Last name", member.getLastName());
            fields.put("Email", member.getEmail());
            fields.put("Membership date", member.getMembershipDate());
            fields.put("Membership type", member.getMembershipType());
            fields.put("Status", formatStatus(member.getStatus()));

            ConsolePrinter.printFields("My Profile", fields);
        }

        public static void printAdminMember(MemberAdminDto member) {
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("Member Id", member.getId());
            fields.put("First name", member.getFirstName());
            fields.put("Last name", member.getLastName());
            fields.put("Email", member.getEmail());
            fields.put("Membership date", member.getMembershipDate());
            fields.put("Membership type", member.getMembershipType());
            fields.put("Status", formatStatus(member.getStatus()));
            fields.put("Member Role", member.getRole());

            ConsolePrinter.printFields("Member Info", fields);
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
