package member;

import java.time.LocalDate;

public class MemberMapper {

    //Maps Member to MemberProfileDto for reader view
    // =========================================================
    // Reader actions - 3. View my profile
    //                        --> 1. View my profile
    // =========================================================
    // 1 showCurrentMemberProfile (MemberController)
    //    -> 2 getProfileById (MemberService)
    //       -> 3 validateId (MemberService)
    //          -> 4 validateId (BaseService)
    //       -> 5 getById (MemberRepository)
    //       -> 6 toProfileDto (MemberMapper) +
    //    -> 7 printProfileMember (MemberController)
    //    -> 8 printError (ConsolePrinter)
    public MemberProfileDto toProfileDto (Member member) {
        return new MemberProfileDto(
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getMembershipDate(),
                member.getMembershipType(),
                member.getStatus()
        );
    }

    //Maps Member to MemberAdminDto for Librarian view
    public MemberAdminDto toAdminDto (Member member) {
        return new MemberAdminDto(
                member.getId(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getMembershipDate(),
                member.getMembershipType(),
                member.getStatus(),
                member.getRole()
        );
    }

    public void updateMemberFromDto(UpdateMemberDto dto, Member member) {
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setMembershipDate(dto.getMembershipDate());
        member.setMembershipType(dto.getMembershipType());
        member.setStatus(dto.getStatus());
    }

    public void updateMemberProfileFromDto(UpdateMyProfileDto dto, Member member) {
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
    }

    public Member fromCreateDto(CreateMemberDto dto) {
        Member member = new Member();
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setPassword(dto.getPassword());
        member.setMembershipDate(LocalDate.now());
        member.setMembershipType("standard");
        member.setStatus("active");
        member.setRole("READER");
        return member;
    }

}
