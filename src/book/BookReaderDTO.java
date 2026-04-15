package book;

import java.util.List;

public class BookReaderDTO {
    private int id;
    private String title;
    private String isbn;
    private int yearPublished;
    private String summary;
    private String lang;
    private int pageCount;
    private List<String> authorNames;
    private List<String> categoryNames;


    public BookReaderDTO(String isbn, int id, String title, int year_published, String summary,
                         String lang, int pageCount, List<String> authorNames, List<String> categoryNames) {
        this.isbn = isbn;
        this.id = id;
        this.title = title;
        this.yearPublished = year_published;
        this.summary = summary;
        this.lang = lang;
        this.pageCount = pageCount;
        this.authorNames = authorNames;
        this.categoryNames = categoryNames;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getAuthorName() {
        return authorNames;
    }

    public void setAuthorName(List<String> authorName) {
        this.authorNames = authorName;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }
}
