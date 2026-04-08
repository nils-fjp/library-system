package Book;

public class Book {
    private String title;
    private String isbn;
    private int yearPublished;
    private int availableCopies;
    private int totalCopies;
    private String summary;
    private String lang;
    private int pageCount;
    private String author;

    public Book() {
    }

    public Book(String title, String isbn, int yearPublished, int availableCopies, int totalCopies, String summary, String lang, int pageCount, String author) {
        this.title = title;
        this.isbn = isbn;
        this.yearPublished = yearPublished;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.summary = summary;
        this.lang = lang;
        this.pageCount = pageCount;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getIsbn() {
//        return isbn;
//    }

//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }

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

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
