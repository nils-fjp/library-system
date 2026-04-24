package author;

import base.BaseRepository;
import base.BaseService;
import member.Member;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AuthorService extends BaseService<Author, Integer> {

    private final AuthorRepository authorRepository = new AuthorRepository();

    @Override
    protected BaseRepository<Author, Integer> getRepository() {
        return authorRepository;
    }

    @Override
    protected void validateId(Integer id) {
        super.validateId(id);
        AuthorValidator.validateId(id);
    }

    public Optional<Author> createAuthor(Member currentMember, Author author) throws SQLException {
        validateLibrarianAccess(currentMember);
        AuthorValidator.validateAuthor(author);

        authorRepository.save(author);
        return authorRepository.getById(author.getId());
    }

    public Optional<Author> getAuthorById(Integer id) throws SQLException {
        validateId(id);
        return authorRepository.getById(id);
    }

    public Optional<Author> updateAuthor(Member currentMember, Author author) throws SQLException {
        validateLibrarianAccess(currentMember);
        AuthorValidator.validateAuthor(author);
        validateId(author.getId());

        Optional<Author> optionalAuthor = authorRepository.getById(author.getId());

        if (optionalAuthor.isEmpty()) {
            return Optional.empty();
        }

        authorRepository.update(author);
        return authorRepository.getById(author.getId());
    }

    public List<Author> getAllAuthors() throws SQLException {
        return authorRepository.getAll();
    }

    public boolean deleteAuthor(Member currentMember, Integer authorId) throws SQLException {
        validateLibrarianAccess(currentMember);
        validateId(authorId);

        Optional<Author> optionalAuthor = authorRepository.getById(authorId);

        if (optionalAuthor.isEmpty()) {
            return false;
        }

        authorRepository.deleteById(authorId);
        return true;
    }

    public List<Author> searchAuthors(String keyword) throws SQLException {
        AuthorValidator.validateSearchKeyword(keyword);
        return authorRepository.search(keyword.trim());
    }
}