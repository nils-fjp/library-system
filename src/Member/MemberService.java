package Member;

import Base.BaseRepository;
import Base.BaseService;

public class MemberService extends BaseService<Member, Integer> {

    private final MemberRepository memberRepository = new MemberRepository();

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
}
