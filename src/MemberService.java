import java.sql.SQLException;
import java.util.ArrayList;

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
            throw new IllegalArgumentException("Member id must be greater than 0.");
        }
    }
}