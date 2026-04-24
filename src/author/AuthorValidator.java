package author;

public final class AuthorValidator {

    private AuthorValidator() {
    }

    public static void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid author id.");
        }
    }

    public static void validateAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null.");
        }

        validateName(author.getFirstName(), "First name cannot be empty.");
        validateName(author.getLastName(), "Last name cannot be empty.");
    }

    public static void validateSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty.");
        }
    }

    private static void validateName(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}