package ui;

import member.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class AuthController {
    private static final Scanner scanner = new Scanner(System.in);
    public static void login(){
        MemberService memberService = new MemberService();

        try {
            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            Optional<Member> optionalMember = memberService.authenticate(email, password);

            if (optionalMember.isEmpty()) {
                System.out.println("Invalid email or password.");
                return;
            }

            Member currentMember = optionalMember.get();
            System.out.println("Welcome, " + currentMember.getFirstName() + "!");
            if ("LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
                //MemberController.showLibrarianMenu(currentMember);
                LibrarianMenuController.showLibrarianMenu(currentMember);
            } else {
                //MemberController.showReaderMenu(currentMember);
                ReaderMenuController.showReaderMenu(currentMember);
            }
        }catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}

