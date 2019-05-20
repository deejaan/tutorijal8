package ba.unsa.etf.rs.tutorijal8;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransportDAO {
    private static TransportDAO instance;
    private Connection conn;
    private PreparedStatement psAddDriver, psGetDrivers, psDeleteAllFromBusses, psDeleteAllFromDrivers, psDeleteDriver;

    private TransportDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            TestDatabase();
            InitStatements();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void TestDatabase() {
        try {
            Statement s = conn.createStatement();
            s.execute("select * from busses");
            System.out.println("Database exists...");
        } catch (SQLException e) {
            InitializeDatabase();
        }
    }

    private void InitStatements() throws SQLException {
        psAddDriver = conn.prepareStatement("insert into drivers values (null, ?, ?, ?, ?, ?)");
        psGetDrivers = conn.prepareStatement("select * from drivers");
        psDeleteAllFromBusses = conn.prepareStatement("delete from busses");
        psDeleteAllFromDrivers = conn.prepareStatement("delete from drivers");
        psDeleteDriver = conn.prepareStatement("delete from drivers where id=?");
    }

    private void InitializeDatabase() {
        try {
            Scanner sc = new Scanner(new FileReader("database_init.sql"));
            while (sc.hasNext()) {
                String nextStat = sc.next();
                if (!nextStat.trim().isEmpty()) {
                    Statement s = conn.createStatement();
                    s.execute(nextStat);
                }
            }
            System.out.println("Database initialization complete...");
        } catch (FileNotFoundException | SQLException e) {
            System.out.println("Error initializing database!");
            e.printStackTrace();
        }
    }

    public static TransportDAO getInstance() {
        if (instance == null) {
            instance = new TransportDAO();
        }
        return instance;
    }

    public ArrayList<Bus> getBusses() {
    }

    public List<Object> getDrivers() {
    }

    public void dodijeliVozacuAutobus(Driver driver, Bus bus, int which) {
    }

    public void deleteBus(Bus bus) {
    }

    public void deleteDriver(Driver driver) {
    }

    public void addBus(Bus bus) {
    }

    public void addDriver(Driver driver) {
    }
}
