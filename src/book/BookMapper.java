package book;

// Innehåller antalet metoder som styrs av
// hur många konvertering man behöver i sin user stories

import category.Category;

import java.util.List;

// Översättare mellan Book(entity) och DTO
public class BookMapper {

    // För låntagaren
    public BookDetailDTO toViewDTO(Book book) {

        // Konverterar direkt List klasser till List String
        List<String> authorNames = book.getAuthors().stream()
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .toList();

        List<String> categoryNames = book.getCategories().stream()
                .map(Category::getName).toList();

        return new BookDetailDTO(
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
        // Anropar toViewDTO för att undvika dubplicerad kod
        BookDetailDTO readerDTO = toViewDTO(book);

        return new BookManageDTO(
                readerDTO.getId(),
                readerDTO.getTitle(),
                readerDTO.getIsbn(),
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

    // När bibliotekarien skapar/redigerar
    public Book toEntity(BookCreateDTO dto) {

        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setYearPublished(dto.getYearPublished());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getTotalCopies());
        book.setSummary(dto.getSummary());
        book.setLang(dto.getLanguage());
        book.setPageCount(dto.getPageCount());
        // author och categories sätts av BookService
        return book;
    }

}
