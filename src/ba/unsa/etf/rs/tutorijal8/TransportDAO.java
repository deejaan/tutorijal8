package ba.unsa.etf.rs.tutorijal8;

import org.sqlite.JDBC;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class TransportDAO {
    private Connection conn;
    //ST se odnosi na upite
    private static PreparedStatement addDriverST;
    private static PreparedStatement getDriverST;
    private static PreparedStatement deleteDriverST;
    private static PreparedStatement addDriver;
    private static PreparedStatement idDriver;
    private static PreparedStatement deleteDriver;
    private static PreparedStatement addBusST;
    private static PreparedStatement getBusST;
    private static PreparedStatement deleteBusST;
    private static PreparedStatement idBus;
    private static PreparedStatement deleteBus;
    private static PreparedStatement addDriverBus;
    private static PreparedStatement getDriverBus;
    private static PreparedStatement deleteDriverBus;
    private static TransportDAO instance;
    private Driver driver;

    static {
        try {
            DriverManager.registerDriver(new JDBC());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TransportDAO getInstance() {
        if(instance == null) instance = new TransportDAO();
        return instance;
    }

    private TransportDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:Baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            deleteBusST = conn.prepareStatement("DELETE FROM Bus WHERE bus_id=?");
            deleteDriverST = conn.prepareStatement("DELETE FROM Driver WHERE driver_id=?");
            addDriver = conn.prepareStatement("INSERT INTO Driver VALUES (?,?,?,?,?,?)");
            getDriverST = conn.prepareStatement("SELECT * FROM Driver;");
            getBusST = conn.prepareStatement("SELECT * FROM Bus");
            addBusST = conn.prepareStatement("INSERT INTO Bus VALUES(?,?,?,?,?)");
            idBus = conn.prepareStatement("SELECT MAX(bus_id)+1 FROM Bus");
            idDriver = conn.prepareStatement("SELECT MAX(driver_id)+1 FROM Driver");
            deleteBus = conn.prepareStatement("DELETE FROM Bus");
            deleteDriver = conn.prepareStatement("DELETE FROM Driver");
            deleteDriverBus = conn.prepareStatement("DELETE FROM DriverBus");
            getDriverBus = conn.prepareStatement("SELECT DISTINCT d.driver_id, d.name, d.surname, d.JMBG, d.birthday_date, d.employment_date" + " FROM DriverBus db , Driver d WHERE db.driverId = d.driver_id AND db.busId=?");
            addDriverBus = conn.prepareStatement("INSERT INTO DriverBus VALUES (?,?,null)");
        } catch (SQLException e) {
            regenerisiBazu();
            e.printStackTrace();
        }
    }

    public void regenerisiBazu() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("Baza.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()){
                sqlUpit += ulaz.nextLine();
                if (sqlUpit.charAt(sqlUpit.length() - 1 ) == ';' ){
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ulaz.close();
    }

    public ArrayList<Driver> getDrivers() {
        ArrayList<Driver> drivers = new ArrayList<Driver>();
        ResultSet result = null;
        try {
            result = getDriverST.executeQuery();
            Driver driver;
            while (  ( driver = dajVozaceUpit(result) ) != null )
                drivers.add(driver);
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
    private Driver dajVozaceUpit(ResultSet result) {
        Driver driver = null;
        try {
            if (result.next() ){
                int id = result.getInt("driver_id");
                String name = result.getString("name");
                String surname = result.getString("surname");
                String JMBG = result.getString("JMBG");
                LocalDate birthday = convertToLocalDateViaSqlDate(result.getDate("birthday_date"));
                LocalDate employment = convertToLocalDateViaSqlDate(result.getDate("employment_date"));
                driver=new Driver(name, surname, JMBG, birthday, employment);
                driver.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    public ArrayList<Bus> getBusses() {
        ArrayList<Bus> buses = new ArrayList<>();
        try {
            ResultSet result = getBusST.executeQuery();
            while(result.next()) {
                Integer id = result.getInt(1);
                String maker = result.getString(2);
                String series = result.getString(3);
                int numberOfSeats = result.getInt(4);
                getDriverBus.setInt(1, id);

                ResultSet ResultatDrugi = getDriverBus.executeQuery();
                Driver driver;
                ArrayList<Driver> drivers = new ArrayList<Driver>();
                while (ResultatDrugi.next()) {
                    Integer id_drivera = ResultatDrugi.getInt(1);
                    String name = ResultatDrugi.getString(2);
                    String surname = ResultatDrugi.getString(3);
                    String JMBG = ResultatDrugi.getString(4);
                    Date birthday = ResultatDrugi.getDate(5);
                    Date employment = ResultatDrugi.getDate(6);
                    drivers.add(new Driver(id_drivera, name, surname, JMBG, birthday.toLocalDate(), employment.toLocalDate()));
                }
                if (drivers.size() == 1) {
                    buses.add(new Bus(id, maker, series, numberOfSeats, drivers.get(0), null));
                }
                else if (drivers.size() == 2) {
                    buses.add(new Bus(id, maker, series, numberOfSeats, drivers.get(0), drivers.get(1)));
                }
                else {
                    buses.add(new Bus(id, maker, series, numberOfSeats, null, null));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buses;
    }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public void addDriver(Driver driver){
        try {
            ResultSet rs = idDriver.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            addDriver.setInt(1, id);
            addDriver.setString(2, driver.getName());
            addDriver.setString(3, driver.getPrezime());
            addDriver.setString(4 , driver.getJMBG());
            addDriver.setDate(5 , convertToDateViaSqlDate(driver.getBirthday()));
            addDriver.setDate(6 , convertToDateViaSqlDate(driver.getEmploymentDate()));
            addDriver.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Taj vozač već postoji!");
        }
    }
    public void addBus(Bus bus) {
        try {
            ResultSet rs = idBus.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            addBusST.setInt(1, id);
            addBusST.setString(2, bus.getMaker());
            addBusST.setString(3, bus.getSeries());
            addBusST.setInt(4, bus.getSeatNumber());
            addBusST.setInt(5,bus.getNumberOfDrivers());
            addBusST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBus(Bus bus) {
        try {
            deleteBusST.setInt(1, bus.getId());
            deleteBusST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDriver(Driver driver) {
        try {
            deleteDriverST.setInt(1, driver.getId());
            deleteDriverST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetDatabase() {
        try {
            deleteDriverBus.executeUpdate();
            deleteBus.executeUpdate();
            deleteDriver.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodijeliVozacuAutobus(Driver driver, Bus bus, int which) {
        try {
            getDriverBus.setInt(1, bus.getId());
            getDriverBus.setInt(2, driver.getId());
            getDriverBus.executeUpdate();
            if(which == 1){
                bus.setFirstDriver(driver);
            }
            if (which == 2){
                bus.setSecondDriver(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}