package book;

import author.Author;
import category.Category;

import java.util.List;

public class BookManageDTO extends BookReaderDTO {
    private int totalCopies;

    public BookManageDTO(String isbn, int id, String title, int year_published, String summary,
                         String lang, int page_count, List<Author> authors, List<Category> categories, int totalCopies) {

        // Anropar konstrukturen med alla parameterna
        super(isbn, id, title, year_published, summary, lang, page_count,
                // Konverterar direkt List klasser till List String
                authors.stream()
                        .map(author -> author.getFirst_name() + "" + author.getLast_name())
                        .toList(),
                categories.stream()
                        .map(Category::getName).toList());
        this.totalCopies = totalCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
}
