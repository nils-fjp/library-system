public class BookService extends BaseService {
    BookRepository bookRepository = new BookRepository();

    public Book getById() {
        Book book = bookRepository.getById();
        return book;
    }
}
