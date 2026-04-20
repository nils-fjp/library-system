package member;

import java.time.LocalDate;

public class MemberProfileDto {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate membershipDate;
    private String membershipType;
    private String status;


    public MemberProfileDto() {
    }

    public MemberProfileDto(String firstName, String lastName, String email,
                            LocalDate membershipDate, String membershipType, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.membershipDate = membershipDate;
        this.membershipType = membershipType;
        this.status = status;
    }

//    public Integer getId() {
//        return id;
//    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public String getStatus() {
        return status;
    }

//    public String getRole() {
//        return role;
//    }

}
