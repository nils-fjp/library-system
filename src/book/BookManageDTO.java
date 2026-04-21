// Data Transfer Object
package book;

import java.util.List;

public class BookManageDTO extends BookDetailDTO {
    private int totalCopies;

    public BookManageDTO(
            int id, String title, String isbn, int yearPublished, int availableCopies, String summary,
            String lang, int pageCount, List<String> authorNames, List<String> categoryNames, int totalCopies
    ) {

        // Anropar konstrukturen med alla parameterna
        super(
                id, title, isbn, yearPublished, availableCopies, summary, lang, pageCount,
                authorNames,
                categoryNames
        );
        this.totalCopies = totalCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
}
