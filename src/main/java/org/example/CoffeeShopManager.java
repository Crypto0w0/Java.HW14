package org.example;

import java.sql.*;
import java.util.Scanner;

public class CoffeeShopManager {
    private static final String DB_URL = "jdbc:sqlite:kavyarnya.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Scanner scanner = new Scanner(System.in);

                // 2
                addNewDrink(conn, "Latte", "Латте", 65.00);
                addNewStaff(conn, "Олена Синько", "0670001122", "вул. Хрещатик 5", "Бариста");
                addNewStaff(conn, "Андрій Петров", "0681234567", "вул. Лесі Українки 10", "Кондитер");
                addNewClient(conn, "Оксана Литвин", "1990-04-12", "0678899000", "вул. Сагайдачного 3", 10);

                // 3
                updateDrinkPrice(conn, "Latte", 70.00);
                updateConfectionerContact(conn, "Андрій Петров", "0669988776", "вул. Володимирська 15");
                updateBaristaPhone(conn, "Олена Синько", "0671231234");
                updateClientDiscount(conn, "Оксана Литвин", 15);

                // 4
                deleteDessert(conn, "Cheesecake");
                deleteWaiter(conn, "Іван Романенко");
                deleteBarista(conn, "Олена Синько");
                deleteClient(conn, "Оксана Литвин");

                // 5
                printQuery(conn, "SELECT * FROM Drinks");
                printQuery(conn, "SELECT * FROM Desserts");
                printQuery(conn, "SELECT * FROM Staff WHERE position = 'Бариста'");
                printQuery(conn, "SELECT * FROM Staff WHERE position = 'Офіціант'");

                scanner.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2
    private static void addNewDrink(Connection conn, String en, String ua, double price) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Drinks (name_en, name_ua, price) VALUES (?, ?, ?)");
        ps.setString(1, en);
        ps.setString(2, ua);
        ps.setDouble(3, price);
        ps.executeUpdate();
    }

    private static void addNewStaff(Connection conn, String name, String phone, String address, String position) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Staff (full_name, phone, address, position) VALUES (?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, phone);
        ps.setString(3, address);
        ps.setString(4, position);
        ps.executeUpdate();
    }

    private static void addNewClient(Connection conn, String name, String dob, String phone, String address, int discount) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients (full_name, birth_date, phone, address, discount) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, dob);
        ps.setString(3, phone);
        ps.setString(4, address);
        ps.setInt(5, discount);
        ps.executeUpdate();
    }

    // 3
    private static void updateDrinkPrice(Connection conn, String nameEn, double newPrice) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE Drinks SET price = ? WHERE name_en = ?");
        ps.setDouble(1, newPrice);
        ps.setString(2, nameEn);
        ps.executeUpdate();
    }

    private static void updateConfectionerContact(Connection conn, String name, String phone, String address) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE Staff SET phone = ?, address = ? WHERE full_name = ? AND position = 'Кондитер'");
        ps.setString(1, phone);
        ps.setString(2, address);
        ps.setString(3, name);
        ps.executeUpdate();
    }

    private static void updateBaristaPhone(Connection conn, String name, String phone) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE Staff SET phone = ? WHERE full_name = ? AND position = 'Бариста'");
        ps.setString(1, phone);
        ps.setString(2, name);
        ps.executeUpdate();
    }

    private static void updateClientDiscount(Connection conn, String name, int discount) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE Clients SET discount = ? WHERE full_name = ?");
        ps.setInt(1, discount);
        ps.setString(2, name);
        ps.executeUpdate();
    }

    // 4
    private static void deleteDessert(Connection conn, String nameEn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM Desserts WHERE name_en = ?");
        ps.setString(1, nameEn);
        ps.executeUpdate();
    }

    private static void deleteWaiter(Connection conn, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM Staff WHERE full_name = ? AND position = 'Офіціант'");
        ps.setString(1, name);
        ps.executeUpdate();
    }

    private static void deleteBarista(Connection conn, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM Staff WHERE full_name = ? AND position = 'Бариста'");
        ps.setString(1, name);
        ps.executeUpdate();
    }

    private static void deleteClient(Connection conn, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM Clients WHERE full_name = ?");
        ps.setString(1, name);
        ps.executeUpdate();
    }

    // 5
    private static void printQuery(Connection conn, String query) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData meta = rs.getMetaData();
        int columns = meta.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= columns; i++) {
                System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "  ");
            }
            System.out.println();
        }
    }
}