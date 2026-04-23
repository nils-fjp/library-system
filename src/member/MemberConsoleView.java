package member;

import loan.LoanSummaryDto;
import ui.ANSI;

import java.util.LinkedHashMap;

final class MemberConsoleView {
    private MemberConsoleView() {
    }

    static LinkedHashMap<String, Object> buildProfileFields(MemberProfileDto member) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("First name", member.getFirstName());
        fields.put("Last name", member.getLastName());
        fields.put("Email", member.getEmail());
        fields.put("Membership date", member.getMembershipDate());
        fields.put("Membership type", member.getMembershipType());
        fields.put("Status", formatStatus(member.getStatus()));
        return fields;
    }

    static LinkedHashMap<String, Object> buildAdminMemberFields(MemberAdminDto member) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("Member Id", member.getId());
        fields.put("First name", member.getFirstName());
        fields.put("Last name", member.getLastName());
        fields.put("Email", member.getEmail());
        fields.put("Membership date", member.getMembershipDate());
        fields.put("Membership type", member.getMembershipType());
        fields.put("Status", formatStatus(member.getStatus()));
        fields.put("Member Role", member.getRole());
        return fields;
    }

    static LinkedHashMap<String, Object> buildActiveLoanFields(LoanSummaryDto loan) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("Loan id", loan.id());
        fields.put("Book", loan.bookTitle());
        fields.put("Loan date", loan.loanDate());
        fields.put("Due date", loan.dueDate());
        return fields;
    }

    static String memberSearchPromptLine1() {
        return "Search member by id, name, email,";
    }

    static String memberSearchPromptLine2() {
        return "type, status, role, or date.";
    }

    static String activeLoansDeleteLine1() {
        return "Member cannot be deleted.";
    }

    static String activeLoansDeleteLine2() {
        return "Active loans found for this member.";
    }

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