package Member;

public class MemberSuspendedException extends RuntimeException {

    public MemberSuspendedException() {
        super("Member is suspended.");
    }

    public MemberSuspendedException(String message) {
        super(message);
    }
}