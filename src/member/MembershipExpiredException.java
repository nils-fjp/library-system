package member;

public class MembershipExpiredException extends RuntimeException {
    public MembershipExpiredException() {
        super("Member is expired.");
    }

    public MembershipExpiredException(String message) {
        super(message);
    }
}

