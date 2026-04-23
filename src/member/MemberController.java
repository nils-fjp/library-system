package member;

import base.BaseController;
import loan.LoanService;
import loan.LoanSummaryDto;
import ui.ConsolePrinter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MemberController extends BaseController<Member, Integer> {

    private static final Scanner scanner = new Scanner(System.in);

    // =========================================================
    // READER MEMBER ACTIONS
    // =========================================================

    public static void showCurrentMemberProfile(Member currentMember) {
        MemberService service = new MemberService();

        try {
            if (currentMember == null) {
                ConsolePrinter.printError("No authorized user.");
                return;
            }

            Optional<MemberProfileDto> optionalMember = service.getProfileById(currentMember.getId());

            if (optionalMember.isPresent()) {
                printProfileMember(optionalMember.get());
            } else {
                ConsolePrinter.printError("Profile not found.");
            }

        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void updateOwnProfile(Member currentMember) {
        MemberService service = new MemberService();

        try {
            if (currentMember == null) {
                ConsolePrinter.printError("No authorized user.");
                return;
            }

            Optional<MemberProfileDto> optionalMember = service.getProfileById(currentMember.getId());
            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            MemberProfileDto currentDto = optionalMember.get();
            ConsolePrinter.printSuccess("Your current data:");
            printProfileMember(currentDto);

            UpdateMyProfileDto updateDto = buildUpdatedMyProfileFromInput(currentDto);
            Optional<MemberProfileDto> updatedMember = service.updateOwnProfile(currentMember.getId(), updateDto);

            if (updatedMember.isEmpty()) {
                ConsolePrinter.printError("Profile was not updated.");
                return;
            }

            ConsolePrinter.printSuccess("Profile updated successfully.");
            printProfileMember(updatedMember.get());

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void changePassword(Member currentMember) {
        MemberService service = new MemberService();

        try {
            if (currentMember == null) {
                ConsolePrinter.printError("No authorized user.");
                return;
            }

            ChangePasswordDto dto = buildChangePasswordDto(currentMember);
            boolean changed = service.changeOwnPassword(dto);

            if (!changed) {
                ConsolePrinter.printError("Password was not changed.");
                return;
            }

            ConsolePrinter.printSuccess("Password changed successfully.");

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    // =========================================================
    // ADMIN MEMBER ACTIONS
    // =========================================================

    public static void showAllMembersForAdmin(Member currentMember) {
        MemberService service = new MemberService();

        try {
            //service.validateLibrarianAccess(currentMember);
            List<MemberAdminDto> members = service.getAllForAdminView();

            if (members.isEmpty()) {
                ConsolePrinter.printError("No readers found.");
                return;
            }

            for (MemberAdminDto member : members) {
                printAdminMember(member);
            }

        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void showMember(Member currentMember) {
        MemberService service = new MemberService();

        try {
            //service.validateLibrarianAccess(currentMember);
            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(service);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            printAdminMember(optionalMember.get());

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void addMemberByAdmin(Member currentMember) {
        MemberService service = new MemberService();

        try {
            //service.validateLibrarianAccess(currentMember);

            CreateMemberDto createDto = buildCreateMemberFromInput();
            Optional<MemberAdminDto> createdDto = service.createMemberByAdmin(createDto);

            if (createdDto.isEmpty()) {
                ConsolePrinter.printError("Member was not created.");
                return;
            }

            ConsolePrinter.printSuccess("Member created successfully.");
            printAdminMember(createdDto.get());

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void updateMemberByAdmin(Member currentMember) {
        MemberService service = new MemberService();

        try {
            //service.validateLibrarianAccess(currentMember);
            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(service);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            MemberAdminDto currentDto = optionalMember.get();

            ConsolePrinter.printSuccess("Current member data:");
            printAdminMember(currentDto);

            UpdateMemberDto updateDto = buildUpdatedMemberFromInput(currentDto);
            Optional<MemberAdminDto> updatedDto = service.updateMemberByAdmin(updateDto);

            if (updatedDto.isEmpty()) {
                ConsolePrinter.printError("Member was not updated.");
                return;
            }

            ConsolePrinter.printSuccess("Member updated successfully.");
            printAdminMember(updatedDto.get());

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void deleteMemberByAdmin(Member currentMember) {
        MemberService memberService = new MemberService();
        LoanService loanService = new LoanService();

        try {
            //memberService.validateLibrarianAccess(currentMember);

            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(memberService);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            MemberAdminDto targetMember = optionalMember.get();

            ConsolePrinter.printSuccess("Selected member:");
            printAdminMember(targetMember);

            List<LoanSummaryDto> activeLoans = loanService.getActiveLoansByMember(targetMember.getId());

            if (!activeLoans.isEmpty()) {
                ConsolePrinter.printError(MemberConsoleView.activeLoansDeleteLine1());
                ConsolePrinter.printPrompt(MemberConsoleView.activeLoansDeleteLine2());
                printMemberActiveLoans(activeLoans);
                return;
            }

            String confirm = promptRequired("Type DELETE to confirm member deletion");

            if (!"DELETE".equals(confirm)) {
                ConsolePrinter.printError("Deletion cancelled.");
                return;
            }

            boolean deleted = memberService.deleteMemberByAdmin(targetMember.getId());

            if (!deleted) {
                ConsolePrinter.printError("Member was not deleted.");
                return;
            }

            ConsolePrinter.printSuccess("Member deleted successfully.");

        } catch (MemberException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    public static void changeMemberPasswordByAdmin(Member currentMember) {
        MemberService service = new MemberService();

        try {
            //service.validateLibrarianAccess(currentMember);
            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(service);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            MemberAdminDto targetMember = optionalMember.get();

            ConsolePrinter.printSuccess("Selected member:");
            printAdminMember(targetMember);

            AdminChangePasswordDto dto = buildAdminChangePasswordDto(targetMember);
            boolean changed = service.changeMemberPasswordByAdmin(currentMember, dto);

            if (!changed) {
                ConsolePrinter.printError("Password was not changed.");
                return;
            }

            ConsolePrinter.printSuccess("Password changed successfully.");

        } catch (IllegalArgumentException e) {
            ConsolePrinter.printError(e.getMessage());
        } catch (SQLException e) {
            ConsolePrinter.printError("Database error: " + e.getMessage());
        }
    }

    // =========================================================
    // FLOW HELPERS
    // =========================================================

    private static UpdateMyProfileDto buildUpdatedMyProfileFromInput(MemberProfileDto currentDto) {
        UpdateMyProfileDto dto = new UpdateMyProfileDto();
        dto.setFirstName(promptTextOrKeepCurrent("Enter new first name", currentDto.getFirstName()));
        dto.setLastName(promptTextOrKeepCurrent("Enter new last name", currentDto.getLastName()));
        dto.setEmail(promptTextOrKeepCurrent("Enter new email", currentDto.getEmail()));
        return dto;
    }

    private static ChangePasswordDto buildChangePasswordDto(Member currentMember) {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setMemberId(currentMember.getId());
        dto.setCurrentPassword(promptRequired("Enter current password"));
        dto.setNewPassword(promptRequired("Enter new password"));
        dto.setConfirmNewPassword(promptRequired("Confirm new password"));
        return dto;
    }

    private static CreateMemberDto buildCreateMemberFromInput() {
        CreateMemberDto dto = new CreateMemberDto();
        dto.setFirstName(promptRequired("Enter first name"));
        dto.setLastName(promptRequired("Enter last name"));
        dto.setEmail(promptRequired("Enter email"));
        dto.setPassword(promptRequired("Enter password"));
        return dto;
    }

    private static UpdateMemberDto buildUpdatedMemberFromInput(MemberAdminDto currentDto) {
        UpdateMemberDto dto = new UpdateMemberDto();
        dto.setId(currentDto.getId());
        dto.setFirstName(promptTextOrKeepCurrent("Enter new first name", currentDto.getFirstName()));
        dto.setLastName(promptTextOrKeepCurrent("Enter new last name", currentDto.getLastName()));
        dto.setEmail(promptTextOrKeepCurrent("Enter new email", currentDto.getEmail()));
        dto.setMembershipDate(promptDateOrKeepCurrent("Enter new membership date", currentDto.getMembershipDate()));
        dto.setMembershipType(promptTextOrKeepCurrent(
                "Enter new membership type " + ConsolePrinter.colorOptions("standard/premium"),
                currentDto.getMembershipType()
        ));
        dto.setStatus(promptTextOrKeepCurrent(
                "Enter new status " + ConsolePrinter.colorOptions("active/suspended/expired"),
                currentDto.getStatus()
        ));
        return dto;
    }

    private static AdminChangePasswordDto buildAdminChangePasswordDto(MemberAdminDto member) {
        AdminChangePasswordDto dto = new AdminChangePasswordDto();
        dto.setMemberId(member.getId());
        dto.setNewPassword(promptRequired("Enter new password"));
        dto.setConfirmNewPassword(promptRequired("Confirm new password"));
        return dto;
    }

    private static Optional<MemberAdminDto> findMemberByKeyword(MemberService service) throws SQLException {
        ConsolePrinter.printPrompt(MemberConsoleView.memberSearchPromptLine1());
        ConsolePrinter.printPrompt(MemberConsoleView.memberSearchPromptLine2());
        String keyword = promptRequired("Enter");

        List<MemberAdminDto> foundMembers = service.searchMembersForAdmin(keyword);

        if (foundMembers.isEmpty()) {
            return Optional.empty();
        }

        if (foundMembers.size() == 1) {
            return Optional.of(foundMembers.get(0));
        }

        ConsolePrinter.printSuccess("Found members:");
        for (MemberAdminDto member : foundMembers) {
            printAdminMember(member);
        }

        Integer selectedId = promptRequiredInt("Enter member id");
        for (MemberAdminDto member : foundMembers) {
            if (member.getId().equals(selectedId)) {
                return Optional.of(member);
            }
        }

        return Optional.empty();
    }

    // =========================================================
    // INPUT HELPERS
    // =========================================================

    private static String prompt(String label) {
        ConsolePrinter.printPromptInline(label + ": ");
        return scanner.nextLine().trim();
    }

    private static String promptRequired(String label) {
        while (true) {
            String input = prompt(label);

            if (!input.isBlank()) {
                return input;
            }

            ConsolePrinter.printError("Input cannot be empty.");
        }
    }

    private static int promptRequiredInt(String label) {
        while (true) {
            try {
                return Integer.parseInt(promptRequired(label));
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid number.");
            }
        }
    }

    private static String promptTextOrKeepCurrent(String label, String currentValue) {
        String input = prompt(label + " " + ConsolePrinter.colorCurrentValue(currentValue));
        return input.isBlank() ? currentValue : input;
    }

    private static LocalDate promptDateOrKeepCurrent(String label, LocalDate currentValue) {
        while (true) {
            String input = prompt(label + " " + ConsolePrinter.colorHint("yyyy-mm-dd") + " " + ConsolePrinter.colorCurrentValue(currentValue));

            if (input.isBlank()) {
                return currentValue;
            }

            try {
                return LocalDate.parse(input);
            } catch (java.time.format.DateTimeParseException e) {
                ConsolePrinter.printError("Invalid date format. Use yyyy-mm-dd.");
            }
        }
    }

    // =========================================================
    // OUTPUT HELPERS
    // =========================================================

    public static void printProfileMember(MemberProfileDto member) {
        ConsolePrinter.printFields("My Profile", MemberConsoleView.buildProfileFields(member));
    }

    public static void printAdminMember(MemberAdminDto member) {
        ConsolePrinter.printFields("Member Info", MemberConsoleView.buildAdminMemberFields(member));
    }

    private static void printMemberActiveLoans(List<LoanSummaryDto> activeLoans) {
        for (LoanSummaryDto loan : activeLoans) {
            ConsolePrinter.printFields("Active Loan", MemberConsoleView.buildActiveLoanFields(loan));
        }
    }
}