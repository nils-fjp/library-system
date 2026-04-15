package ui;

import member.LibrarianMenuController;
import member.Member;
import member.MemberService;
import member.ReaderMenuController;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;


public class AuthController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ConsolePrinter printer = new ConsolePrinter();
    private static Member currentMember;

    public static void login() {
        MemberService memberService = new MemberService();

        try {
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            Optional<Member> optionalMember = memberService.authenticate(email, password);

            if (optionalMember.isEmpty()) {
                printer.printError("Invalid email or password.");
                return;
            }

            currentMember = optionalMember.get();
            printer.printSuccess("Welcome, " + currentMember.getFirstName() + "!");
            if ("LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
                LibrarianMenuController.showLibrarianMenu(currentMember);
            } else {
                ReaderMenuController.showReaderMenu(currentMember);
            }
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

    }

    public static void logout() {
        currentMember = null;
    }
}
