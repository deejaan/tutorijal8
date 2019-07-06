package ba.unsa.etf.rs.tutorijal8;

public class Bus {
    private Integer id = null;
    private String maker;
    private String series;
    private int numberOfSeats;
    private int numberOfDrivers;
    private Driver FirstDriver = null;
    private Driver SecondDriver = null;

    public void setFirstDriver(Driver firstDriver) {
        FirstDriver = firstDriver;
    }

    public void setSecondDriver(Driver secondDriver) {
        SecondDriver = secondDriver;
    }

    public Bus(){ }

    public Bus(String maker, String series, int numberOfSeats) {
        this.maker = maker;
        this.series = series;
        this.numberOfSeats = numberOfSeats;
    }

    public Bus(int Id, String maker, String series, int numberOfSeats) {
        id = Id;
        this.maker = maker;
        this.series = series;
        this.numberOfSeats = numberOfSeats;

    }
    public Bus(int Id, String maker, String series, int numberOfSeats, Driver firstDriver, Driver secondDriver) {
        id = Id;
        this.maker = maker;
        this.series = series;
        this.numberOfSeats = numberOfSeats;
        FirstDriver = firstDriver;
        SecondDriver = secondDriver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getSeatNumber() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfDrivers() {
        return numberOfDrivers;
    }

    public void setNumberOfDrivers(int numberOfDrivers) {
        this.numberOfDrivers = numberOfDrivers;
    }

    public Driver getDriverOne() {
        return FirstDriver;
    }

    public Driver getDriverTwo() {
        return SecondDriver;
    }

    @Override
    public String toString () {
        String s = "";
        s += this.maker + " " + this.series + " ( seats: " + this.getSeatNumber() + " )";
        if (FirstDriver != null) {
            s += FirstDriver.toString();
        }
        if (SecondDriver != null) {
            s += SecondDriver.toString();
        }
        return s;
    }
}