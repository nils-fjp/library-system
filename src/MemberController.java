//import java.sql.SQLException;
//import java.util.Optional;
//import java.util.Scanner;
//
//public class MemberController extends BaseController <Member, Integer>{
//
//    private static final Scanner scanner = new Scanner(System.in);
//    public static void showMenu() {
//
//        Menu memberMenu = new Menu();
//
//        memberMenu.setTopTitle("Member Menu");
//        // bookMenu.setMidTitle("Subtitle");
//        // bookMenu.setMenuInfo("Menu Info");
//        memberMenu.setExitOption("Back to Menu");
//        memberMenu.addMenuOption("Search all members");
//        memberMenu.addMenuOption("Add new members");
//        //memberMenu.addMenuOption("Search by categories");
//        memberMenu.addMenuOption("Search member by id");
//        memberMenu.setPrePrompt("Type a number and press enter...");
//        memberMenu.setPromptLine("Enter: ");
//
//        while (memberMenu.showMenu()) {
//            switch (memberMenu.getChoice()) {
//                case 0: {
//                    break;
//                }
//                case 1: {
//                    System.out.println("WIP!");
//                    break;
//                }
//                case 2: {
//                    System.out.println("WIP");
//                    break;
//                }
//                case 3: {
//                    showMemberById();
//                    break;
//                }
//                default: {
//                    memberMenu.setMenuInfo("Invalid Input!");
//                }
//            }
//        }
//    }
//
//    public static void showMemberById() {
//        MemberService service = new MemberService();
//
//        try {
//            System.out.print("Enter member id: ");
//            int id = Integer.parseInt(scanner.nextLine());
//
//            Optional<Member> optionalMember = service.getById(id);
//
//            if (optionalMember.isPresent()) {
//                printMember(optionalMember.get());
//            } else {
//                System.out.println("\t" + ANSI.RED + "Member not found." + ANSI.DEFAULT_FG);
//            }
//
//        } catch (NumberFormatException e) {
//            System.out.println("\t" + ANSI.RED + "Invalid input. Enter a number." + ANSI.DEFAULT_FG);
//        } catch (IllegalArgumentException e) {
//            System.out.println("\t" + ANSI.RED + e.getMessage() + ANSI.DEFAULT_FG);
//        } catch (SQLException e) {
//            System.out.println("\t" + ANSI.RED + "Database error: " + e.getMessage() + ANSI.DEFAULT_FG);
//        }
//    }
//
//    private static void printMember(Member member) {
//        System.out.println();
//
//        System.out.println("\t" + ANSI.CYAN + ANSI.BOLD + "Member Info" + ANSI.NO_BOLD + ANSI.DEFAULT_FG);
//        System.out.println("\t" + ANSI.BRIGHT_BLACK + "────────────────────────────────────────" + ANSI.DEFAULT_FG);
//
//        System.out.println("\t" + ANSI.YELLOW + "ID: " + ANSI.DEFAULT_FG + member.getId());
//        System.out.println("\t" + ANSI.YELLOW + "First name: " + ANSI.DEFAULT_FG + member.getFirstName());
//        System.out.println("\t" + ANSI.YELLOW + "Last name: " + ANSI.DEFAULT_FG + member.getLastName());
//        System.out.println("\t" + ANSI.YELLOW + "Email: " + ANSI.DEFAULT_FG + member.getEmail());
//        System.out.println("\t" + ANSI.YELLOW + "Membership date: " + ANSI.DEFAULT_FG + member.getMembershipDate());
//        System.out.println("\t" + ANSI.YELLOW + "Membership type: " + ANSI.DEFAULT_FG + member.getMembershipType());
//        System.out.println("\t" + ANSI.YELLOW + "Status: " + ANSI.DEFAULT_FG + formatStatus(member.getStatus()));
//
//        System.out.println("\t" + ANSI.BRIGHT_BLACK + "────────────────────────────────────────" + ANSI.DEFAULT_FG);
//        System.out.println();
//    }
//
//    private static String formatStatus(String status) {
//        if (status == null) {
//            return ANSI.BRIGHT_BLACK + "[UNKNOWN]" + ANSI.DEFAULT_FG;
//        }
//
//        return switch (status.toLowerCase()) {
//            case "active" -> ANSI.BRIGHT_GREEN + ANSI.BOLD + "[ACTIVE]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
//            case "suspended" -> ANSI.BRIGHT_YELLOW + ANSI.BOLD + "[SUSPENDED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
//            case "expired" -> ANSI.BRIGHT_RED + ANSI.BOLD + "[EXPIRED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
//            default -> status;
//        };
//    }
//}

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class MemberController extends BaseController<Member, Integer> {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ConsolePrinter printer = new ConsolePrinter();

    public static void showLibrarianMenu() {

        Menu memberMenu = new Menu();

        memberMenu.setTopTitle("Librarians Menu");
        memberMenu.setExitOption("Back");
        memberMenu.addMenuOption("Search all members");
        memberMenu.addMenuOption("Add new members");
        memberMenu.addMenuOption("Search member by id");
        memberMenu.setPrePrompt("Type a number and press enter...");
        memberMenu.setPromptLine("Enter: ");

        while (memberMenu.showMenu()) {
            switch (memberMenu.getChoice()) {
                case 0: {
                    break;
                }
                case 1: {
                    System.out.println("WIP");
                    break;
                }
                case 2: {
                    System.out.println("WIP");
                    break;
                }
                case 3: {
                   showMemberById();
                    break;
                }
                default: {
                    memberMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    public static void showReaderMenu(){
        Menu memberMenu = new Menu();

        memberMenu.setTopTitle("My Profile");
        memberMenu.setTopTitle("My Reader");
        //memberMenu.setMenuInfo("Show all active loans");
        memberMenu.setExitOption("Back");
        //memberMenu.addMenuOption("View profile");
        memberMenu.addMenuOption("Show all active loans");
        memberMenu.setPrePrompt("Type a number and press enter...");
        memberMenu.setPromptLine("Enter: ");

        while (true){
            memberMenu.showMenu();
            switch (memberMenu.getChoice()){
                case 0: {
                    return;
                }
                case 1: {
                    System.out.println("WIP: Information about your loans");
                    break;
                }
                case 2: {
                    System.out.println("WIP: All active loans");
                    break;
                }
                default: {
                    memberMenu.setMenuInfo("Invalid Input!");
                }
            }
        }
    }

    public static void showMemberById() {
        MemberService service = new MemberService();

        try {
            System.out.print("\tEnter member id: ");
            int id = Integer.parseInt(scanner.nextLine());

            Optional<Member> optionalMember = service.getById(id);

            if (optionalMember.isPresent()) {
                printMember(optionalMember.get());
            } else {
                printer.printError("Member not found.");
            }

        } catch (NumberFormatException e) {
            printer.printError("Invalid input. Enter a number.");
        } catch (IllegalArgumentException e) {
            printer.printError(e.getMessage());
        } catch (SQLException e) {
            printer.printError("Database error: " + e.getMessage());
        }
    }

    private static void printMember(Member member) {
        printer.printHeader("Member Info");
        printer.printField("ID", member.getId());
        printer.printField("First name", member.getFirstName());
        printer.printField("Last name", member.getLastName());
        printer.printField("Email", member.getEmail());
        printer.printField("Membership date", member.getMembershipDate());
        printer.printField("Membership type", member.getMembershipType());
        printer.printField("Status", formatStatus(member.getStatus()));
        printer.printFooter();
    }

    private static String formatStatus(String status) {
        if (status == null) {
            return ANSI.BRIGHT_BLACK + "[UNKNOWN]" + ANSI.DEFAULT_FG;
        }

        return switch (status.toLowerCase()) {
            case "active" -> ANSI.BRIGHT_GREEN + ANSI.BOLD + "[ACTIVE]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            case "suspended" -> ANSI.BRIGHT_YELLOW + ANSI.BOLD + "[SUSPENDED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            case "expired" -> ANSI.BRIGHT_RED + ANSI.BOLD + "[EXPIRED]" + ANSI.NO_BOLD + ANSI.DEFAULT_FG;
            default -> status;
        };
    }
}