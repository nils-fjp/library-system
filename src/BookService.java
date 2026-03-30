public class BookService extends BaseService<Book, Integer> {

    private final BookRepository bookRepository = new BookRepository();

    @Override
    protected BaseRepository<Book, Integer> getRepository() {
        return bookRepository;
    }
}