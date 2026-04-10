package Member;

public class MembershipExpiredException extends RuntimeException {
    public MembershipExpiredException() {
        super("Member is expired.");
    }

    public MembershipExpiredException(String message) {
        super(message);
    }
}



//как использовать потом.... Loan
//import Member.MemberSuspendedException;
//import Member.MembershipExpiredException;
//
//import static Member.MemberValidator.validateMember;



//private void validateMemberCanBorrow(Member member){
//    validateMember(member);
//    String status = member.getStatus();
//
//    if ("SUSPENDED".equalsIgnoreCase(status)){
//        throw new MemberSuspendedException(
//                "Your account is suspended. Please contact the librarian.");
//
//    }
//    if ("EXPIRED".equalsIgnoreCase(status)){
//        throw new MembershipExpiredException(
//                "Your membership has expired. Please renew it to borrow books."
//        );
//
//    }
//}