package com.colourful.spider.webmegicspider;

import java.sql.*;

public class MySQLDBUtil {
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wang_yi_news?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8 ", "root", "tounima.0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static void close(Connection conn, PreparedStatement ps, ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
