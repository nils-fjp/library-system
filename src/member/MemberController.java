package member;

import base.BaseController;
import loan.LoanService;
import loan.LoanSummaryDto;
import ui.ConsoleExceptionHandler;
import ui.ConsolePrinter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MemberController extends BaseController<Member, Integer> {

    private final Scanner scanner;
    private final MemberService service;
    private final LoanService loanService;

    public MemberController() {
        this(new Scanner(System.in), new MemberService(), new LoanService());
    }

    public MemberController(Scanner scanner, MemberService service, LoanService loanService) {
        this.scanner = scanner;
        this.service = service;
        this.loanService = loanService;
    }

    // =========================================================
    // READER MEMBER ACTIONS
    // =========================================================

    public void showCurrentMemberProfile(Member currentMember) {
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
            ConsoleExceptionHandler.print(e);
        }
    }

    public void updateOwnProfile(Member currentMember) {
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

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void changePassword(Member currentMember) {
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

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    // =========================================================
    // ADMIN MEMBER ACTIONS
    // =========================================================

    public void showAllMembersForAdmin(Member currentMember) {
        try {
            List<MemberAdminDto> members = service.getAllForAdminView(currentMember);

            if (members.isEmpty()) {
                ConsolePrinter.printError("No readers found.");
                return;
            }

            for (MemberAdminDto member : members) {
                printAdminMember(member);
            }

        } catch (SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public Optional<MemberAdminDto> showMember(Member currentMember) {
        try {
            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(service, currentMember);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return Optional.empty();
            }

            printAdminMember(optionalMember.get());
            return optionalMember;

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }

        return Optional.empty();
    }

    public void addMemberByAdmin(Member currentMember) {
        try {
            //service.validateLibrarianAccess(currentMember);

            CreateMemberDto createDto = buildCreateMemberFromInput();
            Optional<MemberAdminDto> createdDto = service.createMemberByAdmin(currentMember,createDto);

            if (createdDto.isEmpty()) {
                ConsolePrinter.printError("Member was not created.");
                return;
            }

            ConsolePrinter.printSuccess("Member created successfully.");
            printAdminMember(createdDto.get());

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void updateMemberByAdmin(Member currentMember) {
        try {
            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(service, currentMember);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            updateMemberByAdmin(currentMember, optionalMember.get());

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void updateMemberByAdmin(Member currentMember, MemberAdminDto currentDto) {
        try {
            ConsolePrinter.printSuccess("Current member data:");
            printAdminMember(currentDto);

            UpdateMemberDto updateDto = buildUpdatedMemberFromInput(currentDto);
            Optional<MemberAdminDto> updatedDto = service.updateMemberByAdmin(currentMember, updateDto);

            if (updatedDto.isEmpty()) {
                ConsolePrinter.printError("Member was not updated.");
                return;
            }

            ConsolePrinter.printSuccess("Member updated successfully.");
            printAdminMember(updatedDto.get());

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void deleteMemberByAdmin(Member currentMember) {
        try {
            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(service, currentMember);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            deleteMemberByAdmin(currentMember, optionalMember.get());

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void deleteMemberByAdmin(Member currentMember, MemberAdminDto targetMember) {

        try {
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

            boolean deleted = service.deleteMemberByAdmin(currentMember, targetMember.getId());

            if (!deleted) {
                ConsolePrinter.printError("Member was not deleted.");
                return;
            }

            ConsolePrinter.printSuccess("Member deleted successfully.");

        } catch (MemberException | IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void changeMemberPasswordByAdmin(Member currentMember) {
        try {
            Optional<MemberAdminDto> optionalMember = findMemberByKeyword(service, currentMember);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Member not found.");
                return;
            }

            changeMemberPasswordByAdmin(currentMember, optionalMember.get());

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void changeMemberPasswordByAdmin(Member currentMember, MemberAdminDto targetMember) {
        try {
            ConsolePrinter.printSuccess("Selected member:");
            printAdminMember(targetMember);

            AdminChangePasswordDto dto = buildAdminChangePasswordDto(targetMember);
            boolean changed = service.changeMemberPasswordByAdmin(currentMember, dto);

            if (!changed) {
                ConsolePrinter.printError("Password was not changed.");
                return;
            }

            ConsolePrinter.printSuccess("Password changed successfully.");

        } catch (IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    // =========================================================
    // FLOW HELPERS
    // =========================================================

    private UpdateMyProfileDto buildUpdatedMyProfileFromInput(MemberProfileDto currentDto) {
        UpdateMyProfileDto dto = new UpdateMyProfileDto();
        dto.setFirstName(promptTextOrKeepCurrent("Enter new first name", currentDto.getFirstName()));
        dto.setLastName(promptTextOrKeepCurrent("Enter new last name", currentDto.getLastName()));
        dto.setEmail(promptTextOrKeepCurrent("Enter new email", currentDto.getEmail()));
        return dto;
    }

    private ChangePasswordDto buildChangePasswordDto(Member currentMember) {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setMemberId(currentMember.getId());
        dto.setCurrentPassword(promptRequired("Enter current password"));
        dto.setNewPassword(promptRequired("Enter new password"));
        dto.setConfirmNewPassword(promptRequired("Confirm new password"));
        return dto;
    }

    private CreateMemberDto buildCreateMemberFromInput() {
        CreateMemberDto dto = new CreateMemberDto();
        dto.setFirstName(promptRequired("Enter first name"));
        dto.setLastName(promptRequired("Enter last name"));
        dto.setEmail(promptRequired("Enter email"));
        dto.setPassword(promptRequired("Enter password"));
        return dto;
    }

    private UpdateMemberDto buildUpdatedMemberFromInput(MemberAdminDto currentDto) {
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

    private AdminChangePasswordDto buildAdminChangePasswordDto(MemberAdminDto member) {
        AdminChangePasswordDto dto = new AdminChangePasswordDto();
        dto.setMemberId(member.getId());
        dto.setNewPassword(promptRequired("Enter new password"));
        dto.setConfirmNewPassword(promptRequired("Confirm new password"));
        return dto;
    }

    private Optional<MemberAdminDto> findMemberByKeyword(MemberService service, Member currentMember) throws SQLException {
        ConsolePrinter.printPrompt(MemberConsoleView.memberSearchPromptLine1());
        ConsolePrinter.printPrompt(MemberConsoleView.memberSearchPromptLine2());
        String keyword = promptRequired("Enter");

        List<MemberAdminDto> foundMembers = service.searchMembersForAdmin(currentMember, keyword);

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

    private String prompt(String label) {
        ConsolePrinter.printPromptInline(label + ": ");
        return scanner.nextLine().trim();
    }

    private String promptRequired(String label) {
        while (true) {
            String input = prompt(label);

            if (!input.isBlank()) {
                return input;
            }

            ConsolePrinter.printError("Input cannot be empty.");
        }
    }

    private int promptRequiredInt(String label) {
        while (true) {
            try {
                return Integer.parseInt(promptRequired(label));
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid number.");
            }
        }
    }

    private String promptTextOrKeepCurrent(String label, String currentValue) {
        String input = prompt(label + " " + ConsolePrinter.colorCurrentValue(currentValue));
        return input.isBlank() ? currentValue : input;
    }

    private LocalDate promptDateOrKeepCurrent(String label, LocalDate currentValue) {
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
