package member;

import base.BaseRepository;
import base.BaseService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberService extends BaseService<Member, Integer> {

    private final MemberRepository memberRepository = new MemberRepository();

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

        if (id <= 0) {
            throw new IllegalArgumentException("Member.Member id must be greater than 0.");
        }

    }

    // =========================================================
    // DTO mappers (перенести в класс MemberMapper)
    // =========================================================

    //Maps Member to MemberProfileDto for reader view
    private MemberProfileDto toProfileDto(Member member) {
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
    private MemberAdminDto toAdminDto(Member member) {
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
    // Reader/profile view methods DTO
    // =========================================================

    public Optional<MemberProfileDto> getProfileById(Integer id) throws SQLException {
        validateId(id);
        return memberRepository.getById(id).map(this::toProfileDto);
    }

    // =========================================================
    // Librarian/admin view methods DTO
    // =========================================================

    public Optional<MemberAdminDto> getByEmailForViewForAdmin(String email) throws SQLException {
        validateEmail(email);
        Optional<Member> optionalMember = memberRepository.getByEmail(email);
        if (optionalMember.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toAdminDto(optionalMember.get()));
    }
    // Этот вместо того что выше
    //    public Optional<MemberAdminDto> getByEmailForViewForAdmin(String email) throws SQLException {
    //        validateEmail(email);
    //        return memberRepository.getByEmail(email).map(this::toAdminDto);
    //    }

    public List<MemberAdminDto> getAllForAdminView() throws SQLException {
        List<Member> members = memberRepository.getAll();
        List<MemberAdminDto> result = new ArrayList<>();

        for (Member member : members) {
            result.add(toAdminDto(member));
        }
        return result;
    }

    public Optional<MemberAdminDto> updateMemberByAdmin(Member member) throws SQLException {
        validateMemberForUpdate(member);
        validateEmailUniqueness(member);
        memberRepository.update(member);
        return memberRepository.getById(member.getId()).map(this::toAdminDto);
    }

    // =========================================================
    // Authentication and access control
    // =========================================================

    public Optional<Member> authenticate(String email, String password) throws SQLException {

        validateEmail(email);
        validatePassword(password);

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
    // Validation helpers (подумать про класс MemberSuspendedExaption extends RuntimeException)
    // =========================================================

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
    }

    private void validateName(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }

    private void validateMembershipDate(LocalDate membershipDate) {
        if (membershipDate == null) {
            throw new IllegalArgumentException("Membership date cannot be null.");
        }
    }

    private void validateMembershipType(String membershipType) {
        if (membershipType == null || membershipType.isBlank()) {
            throw new IllegalArgumentException("Membership type cannot be empty.");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }
    }

    // улучшить (добавить проверку введения символов)
    private void validateEmailUniqueness(Member member) throws SQLException {
        Optional<Member> existing = memberRepository.getByEmail(member.getEmail());

        if (existing.isPresent() && !existing.get().getId().equals(member.getId())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
    }

    private void validateMemberForUpdate(Member member) {
        validateMember(member);
        validateId(member.getId());
        validateName(member.getFirstName(), "First name");
        validateName(member.getLastName(), "Last name");
        validateEmail(member.getEmail());
        validateMembershipDate(member.getMembershipDate());
        validateMembershipType(member.getMembershipType());
        validateStatus(member.getStatus());
    }

}
