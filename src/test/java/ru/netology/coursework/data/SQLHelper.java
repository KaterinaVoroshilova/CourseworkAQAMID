package ru.netology.coursework.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {

    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    private static Connection getConnPostgresql() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass");
    }

    @SneakyThrows
    public static String getStatusPay() {
        var codeSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var status = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return status;
    }

    @SneakyThrows
    public static String getStatusCredit() {
        var codeSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var status = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return status;
    }

    @SneakyThrows
    public static String getIDPay() {
        var codeSQL = "SELECT id FROM payment_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var id = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return id;
    }

    @SneakyThrows
    public static String getIDCredit() {
        var codeSQL = "SELECT id FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var id = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return id;
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var conn = getConn();
        runner.execute(conn, "DELETE FROM payment_entity");
        runner.execute(conn, "DELETE FROM credit_request_entity");
        runner.execute(conn, "DELETE FROM order_entity");
    }
}
