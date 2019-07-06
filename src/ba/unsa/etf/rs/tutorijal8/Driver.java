package ba.unsa.etf.rs.tutorijal8;

import java.sql.Date;
import java.time.LocalDate;

public class Driver {
    private Integer id = null;
    private String name;
    private String surname;
    private String jmb;
    private LocalDate birthday;
    private LocalDate hireDate;

    public Driver() {
    }

    public Driver(String name, String surname, String jmb, LocalDate birthday, LocalDate hireDate) {
        this.name = name;
        this.surname = surname;
        this.jmb = jmb;
        this.birthday = birthday;
        this.hireDate = hireDate;
    }

    public Driver(Integer idDriver, String name, String surname, String jmb, LocalDate birthDate, LocalDate hireDate) {
        this.id = idDriver;
        this.name = name;
        this.surname = surname;
        this.jmb = jmb;
        this.birthday = birthDate;
        this.hireDate = hireDate;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getJmb() {
        return jmb;
    }

    public void setJmb(String jmb) {
        this.jmb = jmb;
    }
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  " - (" + this.getName() + " " + this.getSurname() + " ( " + this.getJmb() + " ))";
    }

    public boolean equals(Driver d) {
        return (d.getJmb().equals(this.getJmb()));
    }
}