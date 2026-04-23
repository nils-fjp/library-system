/// / bookservice
//public void addBook(String title, String isbn, int year, int totalCopies,
//                    String summary, String language, int pageCount,
//                    String firstName, String lastName, int categoryId) throws SQLException {
//
//    if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be empty");
//    if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("ISBN cannot be empty");
//    if (year < 1000) throw new IllegalArgumentException("Invalid year.");
//    if (totalCopies < 1) throw new IllegalArgumentException("Total copies must be at least 1.");
//
//    // Hämta eller skapa författaren
//    Author author = authorRepository.findByName(firstName, lastName)
//            .orElseGet(() -> {
//                Author a = new Author();
//                a.setFirstName(firstName);
//                a.setLastName(lastName);
//                return a;
//            });
//
//    Category category = new Category();
//    category.setId(categoryId);
//
//    Book book = new Book();
//    book.setTitle(title);
//    book.setIsbn(isbn);
//    book.setYearPublished(year);
//    book.setTotalCopies(totalCopies);
//    book.setAvailableCopies(totalCopies);
//    book.setSummary(summary);
//    book.setLang(language);
//    book.setPageCount(pageCount);
//    book.getAuthors().add(author);
//    book.getCategories().add(category);
//
//    bookRepository.save(book);
//}
//
//// bookcontroller
//try {
//// ...all inläsning...
//int year = parseIntOrThrow("Year Published");
//int totalCopies = parseIntOrThrow("Total Copies");
//// osv.
//} catch (NumberFormatException e) {
//        ConsolePrinter.printError("Invalid number format: " + e.getMessage());
//        }
//
//// Liten hjälpmetod i controllern
//private static int parseIntOrThrow(String fieldName) {
//    ConsolePrinter.printPrompt(fieldName + ": ");
//    try {
//        return Integer.parseInt(scanner.nextLine());
//    } catch (NumberFormatException e) {
//        throw new NumberFormatException(fieldName + " must be a number.");
//    }
//}