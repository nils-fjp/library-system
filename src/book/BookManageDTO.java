// Data Transfer Object
package book;

import java.util.List;

public class BookManageDTO extends BookReaderDTO {
    private int totalCopies;

    public BookManageDTO(int id, String isbn, String title, int year_published, int availableCopies, String summary,
                         String lang, int page_count, List<String> authors, List<String> categories, int totalCopies) {

        // Anropar konstrukturen med alla parameterna
        super(id, isbn, title, year_published, availableCopies, summary, lang, page_count,
                authors,
                categories);
        this.totalCopies = totalCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
}
