package member;

import base.BaseRepository;
import base.BaseService;
import loan.LoanService;
import loan.LoanSummaryDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberService extends BaseService<Member, Integer> {

    private final MemberRepository memberRepository = new MemberRepository();
    private final LoanService loanService = new LoanService();
    private final MemberMapper memberMapper = new MemberMapper();

    // =========================================================
    // BaseService implementation
    // =========================================================

    @Override
    protected BaseRepository<Member, Integer> getRepository() {
        return memberRepository;
    }

    @Override
    protected void validateId(Integer id) {
        super.validateId(id);
        MemberValidator.validateId(id);
    }

    public Optional<MemberProfileDto> getProfileById(Integer id) throws SQLException {
        validateId(id);
        return memberRepository.getById(id).map(memberMapper::toProfileDto);
    }

    //get
    public List<MemberAdminDto> getAllForAdminView(Member currentMember) throws SQLException {
        validateLibrarianAccess(currentMember);
        List<Member> members = memberRepository.getAll();
        List<MemberAdminDto> result = new ArrayList<>();

        for (Member member : members) {
            result.add(memberMapper.toAdminDto(member));
        }

        return result;
    }
    public List<MemberAdminDto> searchMembersForAdmin(Member currentMember, String keyword) throws SQLException {
        validateLibrarianAccess(currentMember);
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty.");
        }

        List<Member> members = memberRepository.search(keyword.trim());
        List<MemberAdminDto> result = new ArrayList<>();

        for (Member member : members) {
            result.add(memberMapper.toAdminDto(member));
        }

        return result;
    }

    //update
    public Optional<MemberAdminDto> updateMemberByAdmin(Member currentMember,UpdateMemberDto dto) throws SQLException {
        validateLibrarianAccess(currentMember);
        MemberValidator.validateUpdateMemberAdminDto(dto);

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
    public Optional<MemberProfileDto> updateOwnProfile(Integer memberId, UpdateMyProfileDto dto) throws SQLException {
        MemberValidator.validateUpdateMyProfileDto(dto);
        MemberValidator.validateId(memberId);

        Optional<Member> optionalMember = memberRepository.getById(memberId);

        if (optionalMember.isEmpty()) {
            return Optional.empty();
        }

        Member member = optionalMember.get();

        memberMapper.updateMemberProfileFromDto(dto, member);
        validateEmailUniqueness(member);
        memberRepository.update(member);

        Optional<Member> updatedMember = memberRepository.getById(memberId);

        if (updatedMember.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(memberMapper.toProfileDto(updatedMember.get()));
    }
    public boolean changeOwnPassword(ChangePasswordDto dto) throws SQLException {
        MemberValidator.validateChangePasswordDto(dto);

        Optional<Member> optionalMember = memberRepository.getById(dto.getMemberId());

        if (optionalMember.isEmpty()) {
            return false;
        }

        Member member = optionalMember.get();

        if (!member.getPassword().equals(dto.getCurrentPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        memberRepository.updatePassword(member.getId(), dto.getNewPassword());

        return true;
    }
    public boolean changeMemberPasswordByAdmin(Member currentMember, AdminChangePasswordDto dto) throws SQLException {
        validateLibrarianAccess(currentMember);
        MemberValidator.validateAdminChangePasswordDto(dto);

        Optional<Member> optionalMember = memberRepository.getById(dto.getMemberId());

        if (optionalMember.isEmpty()) {
            return false;
        }

        memberRepository.updatePassword(dto.getMemberId(), dto.getNewPassword());
        return true;
    }

    //create
    public Optional<MemberAdminDto> createMemberByAdmin(Member currentMember, CreateMemberDto dto) throws SQLException{
        validateLibrarianAccess(currentMember);
        MemberValidator.validateCreateMemberDto(dto);
        Optional<Member> optionalMember = memberRepository.getByEmail(dto.getEmail());
        if (optionalMember.isPresent()){
            throw new IllegalArgumentException("Email is already in use.");
        }

        Member member = memberMapper.fromCreateDto(dto);
        memberRepository.save(member);

        return memberRepository.getByEmail(dto.getEmail())
                .map(memberMapper::toAdminDto);

    }

    //delete
    public boolean deleteMemberByAdmin(Member currentMember, Integer memberId) throws SQLException {
        validateLibrarianAccess(currentMember);
        MemberValidator.validateId(memberId);


        Optional<Member> optionalMember = memberRepository.getById(memberId);
        if (optionalMember.isEmpty()) {
            return false;
        }

        List<LoanSummaryDto> activeLoans = loanService.getActiveLoansByMember(memberId);
        if (!activeLoans.isEmpty()) {
            throw new MemberException(
                    "Member cannot be deleted because they have borrowed books that are not returned yet."
            );
        }

        memberRepository.deleteById(memberId);
        return true;
    }




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

        if (!member.getPassword().equals(password)) {
            return Optional.empty();
        }
        MemberValidator.validateLoginAccess(member);
        return Optional.of(member);

    }

//    public void validateLibrarianAccess(Member currentMember) {
//        if (currentMember == null) {
//            throw new IllegalArgumentException("User is not authorized.");
//        }
//
//        if (!"LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
//            throw new IllegalArgumentException("Access denied.");
//        }
//    }

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