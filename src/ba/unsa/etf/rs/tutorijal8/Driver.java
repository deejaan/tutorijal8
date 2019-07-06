package ba.unsa.etf.rs.tutorijal8;

import java.time.LocalDate;

public class Driver {
    private Integer id;
    private String Ime;
    private String Prezime;
    private String JMBG;
    private LocalDate BirthDate;
    private LocalDate EmploymentDate;

    public Driver() { }

    public Driver(String ime, String prezime, String JMBG, LocalDate birthDate, LocalDate employmentDate) {
        this.Ime = ime;
        Prezime = prezime;
        this.JMBG = JMBG;
        BirthDate = birthDate;
        EmploymentDate = employmentDate;
    }

    public Driver(int id , String ime, String prezime, String JMBG, LocalDate birthDate, LocalDate employmentDate) {
        this.id = id;
        this.Ime = ime;
        Prezime = prezime;
        this.JMBG = JMBG;
        BirthDate = birthDate;
        EmploymentDate = employmentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Ime;
    }

    public void setIme(String ime) {
        Ime = ime;
    }

    public String getPrezime() {
        return Prezime;
    }

    public void setPrezime(String prezime) {
        Prezime = prezime;
    }

    public String getJMBG() {
        return JMBG;
    }

    public void setJMBG(String JMBG) {
        this.JMBG = JMBG;
    }

    public LocalDate getBirthday() {
        return BirthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        BirthDate = birthDate;
    }

    public LocalDate getEmploymentDate() {
        return EmploymentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        EmploymentDate = employmentDate;
    }

    @Override
    public String toString() {
        return  " - (" + this.getName() + " " + this.getPrezime() + " ( " + this.getJMBG() + " ))";
    }

    public boolean equals(Driver d) {
        return (d.getJMBG().equals(this.getJMBG()));
    }
}
