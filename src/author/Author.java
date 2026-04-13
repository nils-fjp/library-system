package author;

public class Author {
    private String first_name;
    private String last_name;
    private String nationality;
    private String birth_date;

    public Author() {

    }

    public Author(String first_name, String last_name, String nationality, String birth_date) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.nationality = nationality;
        this.birth_date = birth_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}
