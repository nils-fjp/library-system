package ui;

import member.*;

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

            String status = MemberValidator.getNormalizedStatus(currentMember);
            if ("suspended".equals(status)) {
                printer.printError("\"Your account is suspended. You can view your account and search books, \n" +
                                    "but you cannot borrow, reserve, or renew items.");
            }
            if ("LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
                LibrarianMenuController.showLibrarianMenu(currentMember);
            } else {
                ReaderMenuController.showReaderMenu(currentMember);
            }

        } catch (MemberException e) {
            printer.printError(e.getMessage());
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    public static void logout() {
        currentMember = null;
    }
}
