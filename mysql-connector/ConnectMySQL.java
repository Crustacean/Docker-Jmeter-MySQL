import java.sql.*;
import java.io.FileReader;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConnectMySQL {
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://172.19.0.7:3306/jmeter";

   static final String USER = "root";
   static final String PASS = "admin";

   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");

      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      stmt = conn.createStatement();
      if (conn != null) {
        System.out.println("Connected to the database jmeter");
      }
	  
     String loadQuery = "LOAD DATA INFILE '/tmp/jmeter.csv' INTO TABLE loadTest FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 ROWS;";
     System.out.println(loadQuery);
     stmt.execute(loadQuery);

      /*String sql;
      sql = "SELECT timestamp,elapsed,label,responseCode,responseMessage,threadName,dataType,success,failureMessage,bytes,sentBytes,grpThreads,allThreads,URL,Latency,IdleTime,Connect FROM loadTest";
      ResultSet rs = stmt.executeQuery(sql);

      while(rs.next()){
         long id  = rs.getLong("timestamp");
         String elapsed = rs.getString("elapsed");
         String label = rs.getString("label");
         String responseCode = rs.getString("responseCode");
         String responseMessage = rs.getString("responseMessage");
         String threadName = rs.getString("threadName");
         String dataType = rs.getString("dataType");
         String success = rs.getString("success");
         String failureMessage = rs.getString("failureMessage");
         String bytes = rs.getString("bytes");
         String sentBytes = rs.getString("sentBytes");
         String grpThreads = rs.getString("grpThreads");
         String allThreads = rs.getString("allThreads");
         String URL = rs.getString("URL");
         String Latency = rs.getString("Latency");
         String IdleTime = rs.getString("IdleTime");
         String Connect = rs.getString("Connect");

         System.out.println(id);
         System.out.println(elapsed);
         System.out.println(label);
         System.out.println(responseCode);
         System.out.println(responseMessage);
         System.out.println(threadName);
         System.out.println(dataType);
         System.out.println(success);
         System.out.println(failureMessage);
         System.out.println(bytes);
         System.out.println(sentBytes);
         System.out.println(grpThreads);
         System.out.println(allThreads);
         System.out.println(URL);
         System.out.println(Latency);
         System.out.println(IdleTime);
         System.out.println(Connect);
      }
      rs.close();*/
      stmt.close();
      conn.close();
   }catch(SQLException se){
      System.out.println("An error occurred.");
      se.printStackTrace();
   }catch(Exception e){
      e.printStackTrace();
   }finally{
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
   }
 }
}