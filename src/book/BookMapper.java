package book;

// Innehåller antalet metoder som styrs av
// hur många konvertering man behöver i sin user stories

import category.Category;

import java.util.List;

// Översättare mellan Book(entity) och DTO
public class BookMapper {

    // För låntagaren
    public BookReaderDTO toReaderDTO(Book book) {

        // Konverterar direkt List klasser till List String
        List<String> authorNames = book.getAuthors().stream()
                .map(author -> author.getFirst_name() + " " + author.getLast_name())
                .toList();

        List<String> categoryNames = book.getCategories().stream()
                .map(Category::getName).toList();

        return new BookReaderDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getYearPublished(),
                book.getAvailableCopies(),
                book.getSummary(),
                book.getLang(),
                book.getPageCount(),
                authorNames,
                categoryNames
        );
    }

    // För bibliotekarien
    public BookManageDTO toManageDTO(Book book) {
        // Anropar toReaderDTO för att undvika dubplicerad kod
        BookReaderDTO readerDTO = toReaderDTO(book);

        return new BookManageDTO(
                readerDTO.getId(),
                readerDTO.getIsbn(),
                readerDTO.getTitle(),
                readerDTO.getYearPublished(),
                readerDTO.getAvailableCopies(),
                readerDTO.getSummary(),
                readerDTO.getLang(),
                readerDTO.getPageCount(),
                readerDTO.getAuthorNames(),
                readerDTO.getCategoryNames(),
                book.getTotalCopies()
        );
    }

    // När bibliotekarien skapr/redigerar
    public Book toEntity(BookManageDTO dto) {
        Book book = new Book();

        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setYearPublished(dto.getYearPublished());
        book.setAvailableCopies(dto.getAvailableCopies());
        book.setSummary(dto.getSummary());
        book.setLang(dto.getLang());
        book.setPageCount(dto.getPageCount());
        book.setTotalCopies(dto.getTotalCopies());
        // author och categories sätts av BookService
        return book;
    }

}
