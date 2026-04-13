package member;

import java.time.LocalDate;

public final class MemberValidator {

    private MemberValidator() {
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

    public static void validateMembershipType(String membershipType) {
        if (membershipType == null || membershipType.isBlank()) {
            throw new IllegalArgumentException("Membership type cannot be empty.");
        }
    }

    public static void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty.");
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

    public static void validateUpdateMemberDto(UpdateMemberDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Update data cannot be null.");
        }

        if (dto.getId() == null) {
            throw new IllegalArgumentException("Member id is required.");
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

        if (dto.getMembershipDate() == null) {
            throw new IllegalArgumentException("Membership date is required.");
        }

        if (dto.getMembershipType() == null || dto.getMembershipType().isBlank()) {
            throw new IllegalArgumentException("Membership type cannot be empty.");
        }

        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }
    }
}