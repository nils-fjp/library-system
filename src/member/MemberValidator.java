package member;

import java.time.LocalDate;

public final class MemberValidator {

    private MemberValidator() {
    }
    public static void validateId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Member id is required.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Member id must be greater than 0.");
        }
    }

    public static void validateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email format is invalid.");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public static void validateName(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }

    public static void validateMembershipDate(LocalDate membershipDate) {
        if (membershipDate == null) {
            throw new IllegalArgumentException("Membership date cannot be null.");
        }
    }

    public static void validateMemberForUpdate(Member member) {
        validateMember(member);
        validateName(member.getFirstName(), "First name");
        validateName(member.getLastName(), "Last name");
        validateEmail(member.getEmail());
        validateMembershipDate(member.getMembershipDate());
        validateMembershipType(member.getMembershipType());
        validateStatus(member.getStatus());
    }

    public static void validateUpdateMemberAdminDto(UpdateMemberDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Update data cannot be null.");
        }
        validateId(dto.getId());
        validateName(dto.getFirstName(), "First name");
        validateName(dto.getLastName(), "Last name");
        validateEmail(dto.getEmail());
        validateMembershipDate(dto.getMembershipDate());
        validateMembershipType(dto.getMembershipType());
        validateStatus(dto.getStatus());
    }

    public static void validateUpdateMyProfileDto(UpdateMyProfileDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Update data cannot be null.");
        }

        validateName(dto.getFirstName(), "First name");
        validateName(dto.getLastName(), "Last name");
        validateEmail(dto.getEmail());
    }

    public static void validateCreateMemberDto(CreateMemberDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Member data is required.");
        }

        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }

        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public static void validateChangePasswordDto(ChangePasswordDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Password change data cannot be null.");
        }

        validateId(dto.getMemberId());
        validatePassword(dto.getCurrentPassword());
        validatePassword(dto.getNewPassword());
        validatePassword(dto.getConfirmNewPassword());

        if (dto.getNewPassword().equals(dto.getCurrentPassword())) {
            throw new IllegalArgumentException("New password must be different from current password.");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password confirmation does not match.");
        }
    }

    public static void validateAdminChangePasswordDto(AdminChangePasswordDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Password change data cannot be null.");
        }

        validateId(dto.getMemberId());
        validatePassword(dto.getNewPassword());
        validatePassword(dto.getConfirmNewPassword());

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Password confirmation does not match.");
        }
    }

    public static void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }

        String normalized = status.trim().toLowerCase();

        if (!normalized.equals("active")
                && !normalized.equals("suspended")
                && !normalized.equals("expired")) {
            throw new IllegalArgumentException("Invalid status. Allowed: active, suspended, expired.");
        }
    }

    public static void validateMembershipType(String membershipType) {
        if (membershipType == null || membershipType.isBlank()) {
            throw new IllegalArgumentException("Membership type cannot be empty.");
        }

        String normalized = membershipType.trim().toLowerCase();

        if (!normalized.equals("standard")
                && !normalized.equals("premium")) {
            throw new IllegalArgumentException("Invalid membership type. Allowed: standard, premium.");
        }
    }

    public static void validateLoginAccess(Member member) {
        validateMember(member);

        String status = member.getStatus();

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Member status is missing.");
        }

        if ("expired".equalsIgnoreCase(status.trim())) {
            throw new MembershipExpiredException("Your membership has expired. Login is not allowed.");
        }
    }

    public static String getNormalizedStatus(Member member) {
        validateMember(member);

        String status = member.getStatus();

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Member status is missing.");
        }

        return status.trim().toLowerCase();
    }
}