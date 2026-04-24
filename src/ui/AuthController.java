package ui;

import member.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;


public class AuthController {
    private final Scanner scanner;
    private final MemberService memberService;
    private static Member currentMember;

    public AuthController() {
        this(new Scanner(System.in), new MemberService());
    }

    public AuthController(Scanner scanner, MemberService memberService) {
        this.scanner = scanner;
        this.memberService = memberService;
    }

    public void login() {
        try {
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            Optional<Member> optionalMember = memberService.authenticate(email, password);

            if (optionalMember.isEmpty()) {
                ConsolePrinter.printError("Invalid email or password.");
                return;
            }

            currentMember = optionalMember.get();
            ConsolePrinter.printSuccess("Welcome, " + currentMember.getFirstName() + "!");

            String status = MemberValidator.getNormalizedStatus(currentMember);
            if ("suspended".equals(status)) {
                ConsolePrinter.printError("\"Your account is suspended. You can view your account and search books, \n" +
                                    "but you cannot borrow, reserve, or renew items.");
            }
            if ("LIBRARIAN".equalsIgnoreCase(currentMember.getRole())) {
                LibrarianMenuController.showLibrarianMenu(currentMember);
            } else {
                ReaderMenuController.showReaderMenu(currentMember);
            }

        } catch (MemberException | IllegalArgumentException | SQLException e) {
            ConsoleExceptionHandler.print(e);
        }
    }

    public void logout() {
        currentMember = null;
    }
}
