package btl_qlptgt.util;

import java.sql.*;

public class CSDL {
    
    public static Connection getConnection() {
        final String url = "jdbc:sqlserver://localhost:1433;databaseName=QLPTGiaoThong;encrypt=true;trustServerCertificate=true;user=sa;password=123456";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Connect database success!");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Connect database failure!");
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        return null;
//final String url = "jdbc:mysql://localhost:3306/lptgiaothong";
//        final String user = "root";
//        final String password = "";
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            return DriverManager.getConnection(url,user,password);
//        }
//        catch(ClassNotFoundException e){
//            e.printStackTrace();
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return null;
    }
    

}
