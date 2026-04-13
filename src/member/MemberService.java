package member;

import base.BaseRepository;
import base.BaseService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberService extends BaseService<Member, Integer> {

    private final MemberRepository memberRepository = new MemberRepository();
    private final MemberMapper memberMapper = new MemberMapper();

    // =========================================================
    // BaseService implementation
    // =========================================================

    @Override
    protected BaseRepository<Member, Integer> getRepository() {
        return memberRepository;
    }

    //       -> 3 validateId (MemberService) +
    @Override
    protected void validateId(Integer id) {
        super.validateId(id);

        if (id <= 0) {
            throw new IllegalArgumentException("Member id must be greater than 0.");
        }
    }

    // =========================================================
    // Basic member lookup
    // =========================================================

    public Optional<Member> getByEmail(String email) throws SQLException {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        return memberRepository.getByEmail(email);
    }



    // =========================================================
    // Reader actions - 3. View my profile
    //                        --> 1. View my profile
    // =========================================================
    // 1 showCurrentMemberProfile (MemberController)
    //    -> 2 getProfileById (MemberService) +
    //       -> 3 validateId (MemberService) +
    //          -> 4 validateId (BaseService)
    //       -> 5 getById (MemberRepository)
    //       -> 6 toProfileDto (MemberMapper)
    //    -> 7 printProfileMember (MemberController)
    //    -> 8 printError (ConsolePrinter)
    public Optional<MemberProfileDto> getProfileById(Integer id) throws SQLException {
        validateId(id);
        return memberRepository.getById(id).map(memberMapper::toProfileDto);
    }



    //get
    public Optional<MemberAdminDto> getByEmailForViewForAdmin(String email) throws SQLException {
        MemberValidator.validateEmail(email);
        return memberRepository.getByEmail(email)
                .map(memberMapper::toAdminDto);
    }
    public List<MemberAdminDto> getAllForAdminView() throws SQLException {
        List<Member> members = memberRepository.getAll();
        List<MemberAdminDto> result = new ArrayList<>();

        for (Member member : members) {
            result.add(memberMapper.toAdminDto(member));
        }

        return result;
    }

    //update
    public Optional<MemberAdminDto> updateMemberByAdmin(UpdateMemberDto dto) throws SQLException {
        MemberValidator.validateUpdateMemberDto(dto);

        Optional<Member> optionalMember = memberRepository.getById(dto.getId());

        if (optionalMember.isEmpty()) {
            return Optional.empty();
        }

        Member member = optionalMember.get();

        memberMapper.updateMemberFromDto(dto, member);
        validateEmailUniqueness(member);

        memberRepository.update(member);

        return memberRepository.getById(member.getId())
                .map(memberMapper::toAdminDto);
    }

    //create

    //delete






    // =========================================================
    // Authentication and access control
    // =========================================================

    public Optional<Member> authenticate(String email, String password) throws SQLException {
        MemberValidator.validateEmail(email);
        MemberValidator.validatePassword(password);

        Optional<Member> optionalMember = memberRepository.getByEmail(email);

        if (optionalMember.isEmpty()) {
            return Optional.empty();
        }

        Member member = optionalMember.get();

        if (member.getPassword().equals(password)) {
            return Optional.of(member);
        }

        return Optional.empty();
    }

    public void validateLibrarianAccess(Member currentMember) {
        if (currentMember == null) {
            throw new IllegalArgumentException("User is not authorized.");
        }

        if (!"LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
            throw new IllegalArgumentException("Access denied.");
        }
    }

    // =========================================================
    // Validation helpers
    // =========================================================

    private void validateEmailUniqueness(Member member) throws SQLException {
        Optional<Member> existing = memberRepository.getByEmail(member.getEmail());

        if (existing.isPresent() && !existing.get().getId().equals(member.getId())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
    }
}
