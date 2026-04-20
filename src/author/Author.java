package author;

import java.time.LocalDate;

public class Author {
    private Integer id;
    private String first_name;
    private String last_name;
    private String nationality;
    private LocalDate birth_date;

    public Author() {
    }

    public Author(Integer id, String first_name, String last_name, String nationality, LocalDate birth_date) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.nationality = nationality;
        this.birth_date = birth_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }
}