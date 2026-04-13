package book;

import author.Author;

import java.util.List;

public class Book {
    private int id;
    private String title;
    private int yearPublished;
    private int availableCopies;
    private String summary;
    private String lang;
    private int pageCount;
    private List<Author> author;
    private int isbn;
    private int total_copies;

    public Book() {

    }

    public Book(Integer id, String title, int yearPublished, int availableCopies, String summary, String lang, int pageCount, List<Author> author, int isbn, String total_copies) {
        this.id = id;
        this.title = title;
        this.yearPublished = yearPublished;
        this.availableCopies = availableCopies;
        this.summary = summary;
        this.lang = lang;
        this.pageCount = pageCount;
        this.author = author;
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_copies() {
        return total_copies;
    }

    public void setTotal_copies(int total_copies) {
        this.total_copies = total_copies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
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

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
}
