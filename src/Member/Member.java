package Member;

import java.time.LocalDate;

public class Member {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate membershipDate;
    private String membershipType;
    private String status;

    public Member() {
    }

    public Member(int id, String firstName, String lastName, String email,
                  LocalDate membershipDate, String membershipType, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.membershipDate = membershipDate;
        this.membershipType = membershipType;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public LocalDate getMembershipDate() {return membershipDate;}

    public void setMembershipDate(LocalDate membershipDate) {this.membershipDate = membershipDate;}

    public String getMembershipType() {return membershipType;}

    public void setMembershipType(String membershipType) {this.membershipType = membershipType;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

//    @Override
//    public String toString() {
    //        return "Member.Member{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", email='" + email + '\'' +
//                ", membershipDate=" + membershipDate +
//                ", membershipType='" + membershipType + '\'' +
//                ", status='" + status + '\'' +
//                '}';
//    }
}
