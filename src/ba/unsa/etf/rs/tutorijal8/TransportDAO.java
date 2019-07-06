package ba.unsa.etf.rs.tutorijal8;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransportDAO {



    private static TransportDAO instance = null;
    private Connection conn;
    private PreparedStatement addDriverStatement, latestDriverId,
            deleteDriverStatement, deleteBusStatement, addBusStatement, latestBusId, getBusesStatement,
            getDodjelaVozaci, getDriversStatement, deleteDodjelaBus, deleteDodjelaDriver, truncateBuses,
            truncateDrivers, truncateDodjela, resetAutoIncrementDodjela, dodijeliVozacuAutobusStatement,
            resetAutoIncrementDrivers, resetAutoIncrementBuses;



    public static TransportDAO getInstance () {
        if (instance == null) {
            instance = new TransportDAO();
        }
        return instance;
    }

    public TransportDAO() {
        //TODO
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:proba.db");
            Class.forName("org.sqlite.JDBC");
            //DriverManager.registerDriver(new JDBC());
            latestDriverId = conn.prepareStatement("SELECT max(id) + 1 FROM drivers");
            latestBusId = conn.prepareStatement("SELECT max(id) + 1 FROM buses");
            addDriverStatement = conn.prepareStatement("INSERT INTO drivers(id, name, surname, jmb, birth, hire_date)" +
                    " VALUES(?,?,?,?,?,?)");
            addBusStatement = conn.prepareStatement("INSERT INTO buses(id, proizvodjac, serija, broj_sjedista)" +
                    " VALUES(?, ?, ?, ?)");


            getBusesStatement = conn.prepareStatement("SELECT id, proizvodjac, serija, broj_sjedista" +
                    " FROM buses");
            getDodjelaVozaci = conn.prepareStatement("SELECT DISTINCT dr.id, dr.name, dr.surname, dr.jmb, dr.birth, dr.hire_date" +
                    " FROM dodjela d INNER JOIN drivers dr ON (d.driver_id = dr.id) WHERE d.bus_id=?");
            getDriversStatement = conn.prepareStatement("SELECT id, name, surname, jmb, birth, hire_date" +
                    " FROM drivers");

            deleteDodjelaBus = conn.prepareStatement("DELETE FROM dodjela WHERE bus_id = ?");
            deleteDodjelaDriver = conn.prepareStatement("DELETE FROM dodjela WHERE driver_id = ?");
            deleteDriverStatement = conn.prepareStatement("DELETE FROM Drivers WHERE id = ?");
            deleteBusStatement = conn.prepareStatement("DELETE FROM buses WHERE id = ?");
            truncateBuses = conn.prepareStatement("DELETE FROM buses WHERE 1=1;");
            truncateDrivers = conn.prepareStatement("DELETE FROM drivers WHERE 1=1;");
            truncateDodjela = conn.prepareStatement("DELETE FROM dodjela WHERE 1=1;");

            resetAutoIncrementDodjela = conn.prepareStatement("DELETE FROM SQLITE_SEQUENCE WHERE name='dodjela'");
            resetAutoIncrementBuses = conn.prepareStatement("DELETE FROM SQLITE_SEQUENCE WHERE name='buses'");
            resetAutoIncrementDrivers = conn.prepareStatement("DELETE FROM SQLITE_SEQUENCE WHERE name='drivers'");
            //todo
            dodijeliVozacuAutobusStatement = conn.prepareStatement("INSERT OR REPLACE INTO dodjela(bus_id, driver_id)" +
                    " VALUES (?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Nije pronadjen driver za konekciju na bazu");
            e.printStackTrace();
        }

    }

    public static void removeInsance() {
        if (instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }


    public ArrayList<Bus> getBusses() {
        ArrayList<Bus> buses = new ArrayList<>();
        try {
            ResultSet result = getBusesStatement.executeQuery();
            while(result.next()) {
                Integer id = result.getInt(1);
                String maker = result.getString(2);
                String series = result.getString(3);
                int seatNumber = result.getInt(4);
                getDodjelaVozaci.setInt(1, id);
                //uzimam samo prva 2 vozaca jer bus svakako ne bi smio imati vise iako nema
                //neki constraint u bazi da to zabrani sto bi mogao uz pomoc funkcije
                //count i triggera pri umetanju podataka

                ResultSet result2 = getDodjelaVozaci.executeQuery();
                Driver v1;
                ArrayList<Driver> drivers = new ArrayList<Driver>();
                while (result2.next()) {
                    Integer idDriver = result2.getInt(1);
                    String name = result2.getString(2);
                    String surname = result2.getString(3);
                    String jmb = result2.getString(4);
                    Date birthDate = result2.getDate(5);
                    Date hireDate = result2.getDate(5);
                    drivers.add(new Driver(idDriver, name, surname, jmb, birthDate.toLocalDate(), hireDate.toLocalDate()));
                }
                if (drivers.size() == 1) {
                    buses.add(new Bus(id, maker, series, seatNumber, drivers.get(0), null));
                }
                else if (drivers.size() == 2) {
                    buses.add(new Bus(id, maker, series, seatNumber, drivers.get(0), drivers.get(1)));
                }
                else {
                    buses.add(new Bus(id, maker, series, seatNumber, null, null));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buses;

    }

    //TODO zamjena za ponavljanje koda
/*
    private Driver getOneDriver(ResultSet result) {
        try {
            Integer idDriver = result.getInt(1);
            String name = result.getString(2);
            String surname = result.getString(3);
            String jmb = result.getString(4);
            Date birthDate = result.getDate(5);
            Date hireDate = result.getDate(5);
            return new Driver(idDriver, name, surname, jmb, birthDate.toLocalDate(), hireDate.toLocalDate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Driver();
    }*/




    public ArrayList<Driver> getDrivers() {
        ArrayList<Driver> drivers = new ArrayList<>();
        try {
            ResultSet result = getDriversStatement.executeQuery();
            while (result.next()) {
                Integer idDriver = result.getInt(1);
                String name = result.getString(2);
                String surname = result.getString(3);
                String jmb = result.getString(4);
                Date birthDate = result.getDate(5);
                Date hireDate = result.getDate(5);
                drivers.add(new Driver(idDriver, name, surname, jmb, birthDate.toLocalDate(), hireDate.toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }


    public void addDriver(String name, String surname, int jmb, LocalDate dateOfBirth, LocalDate hireDate) {
        try {

            ResultSet result = latestDriverId.executeQuery();
            result.next();
            Integer id = result.getInt(1);
            if (id == null) {
                id = 1;
            }
            addDriverStatement.setInt(1, id);
            addDriverStatement.setString(2, name);
            addDriverStatement.setString(3, surname);
            addDriverStatement.setInt(4, jmb);
            addDriverStatement.setDate(5, Date.valueOf(dateOfBirth));
            addDriverStatement.setDate(6, Date.valueOf(hireDate));
            addDriverStatement.executeUpdate();

            /*
            SimpleDateFormat textFormat = new SimpleDateFormat("yyyy-MM-dd");
            String paramDateAsString = "2007-12-25";
            Date myDate = null;
            myDate = textFormat.parse(paramDateAsString);
             */

        } catch (SQLException e) {
            //e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }


    public void addBus(Bus bus) {
        try {
            ResultSet result = latestBusId.executeQuery();
            result.next();
            Integer id = result.getInt(1);
            if (id == null) {
                id = 1;
            }
            addBusStatement.setInt(1, id);
            addBusStatement.setString(2, bus.getMaker());
            addBusStatement.setString(3, bus.getSeries());
            addBusStatement.setInt(4, bus.getSeatNumber());
            addBusStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addDriver(Driver driver) {
        try {
            ResultSet result = latestDriverId.executeQuery();
            result.next();
            Integer id = result.getInt(1);
            if (id == null) {
                id = 0;
            }
            addDriverStatement.setInt(1, id);
            addDriverStatement.setString(2, driver.getName());
            addDriverStatement.setString(3, driver.getSurname());
            addDriverStatement.setString(4, driver.getJmb());
            addDriverStatement.setDate(5, Date.valueOf(driver.getBirthday()));
            addDriverStatement.setDate(6, Date.valueOf(driver.getHireDate()));
            addDriverStatement.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new IllegalArgumentException("Taj vozač već postoji!");
        }
    }


    //todo code duplicates
    public void deleteDriver(Driver driver) {
        try {
            deleteDodjelaDriver.setInt(1, driver.getId());
            deleteDodjelaDriver.executeUpdate();
            deleteDriverStatement.setInt(1, driver.getId());
            deleteDriverStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteBus(Bus bus) {
        try {
            deleteDodjelaBus.setInt(1, bus.getId());
            deleteDodjelaBus.executeUpdate();
            deleteBusStatement.setInt(1, bus.getId());
            deleteBusStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dodijeliVozacuAutobus(Driver driver, Bus bus, int which) {
        try {
            dodijeliVozacuAutobusStatement.setInt(1, bus.getId());
            dodijeliVozacuAutobusStatement.setInt(2, driver.getId());
            dodijeliVozacuAutobusStatement.executeUpdate();
            if (which == 1) {
                bus.setDriverOne(driver);
            } else {
                bus.setDriverTwo(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void resetDatabase() {
        try {
            truncateDodjela.executeUpdate();
            truncateDrivers.executeUpdate();
            truncateBuses.executeUpdate();
            resetAutoIncrementDodjela.executeUpdate();
            resetAutoIncrementDrivers.executeUpdate();
            resetAutoIncrementBuses.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}