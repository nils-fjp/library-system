package member;


public class MemberMapper {

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
        return member;
    }

}
