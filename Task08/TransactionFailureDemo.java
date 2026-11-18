import java.sql.*;
/** Demonstrates rollback after a deliberate invalid amount in the middle of a transaction. */
public class TransactionFailureDemo {
    public static void main(String[] args)throws Exception{
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/assignment5");
        try(Connection c=DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""))){
            try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");}
            c.setAutoCommit(false);
            try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass(pass_id,reg_no,name,route_no,boarding_stop,valid_until,amount) VALUES (?,?,?,?,?,?,?)")){
                for(int i=0;i<10;i++){
                    p.setString(1,"FAIL"+i);p.setString(2,"192300001");p.setString(3,"Test");
                    p.setInt(4,1);p.setString(5,"Stop");p.setDate(6,Date.valueOf("2027-03-31"));
                    p.setBigDecimal(7,i==5?new java.math.BigDecimal("-1"):new java.math.BigDecimal("100"));
                    p.executeUpdate();
                }
                c.commit();
            }catch(SQLException e){c.rollback();System.out.println("Forced failure: "+e.getMessage());}
            try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM bus_pass")){
                r.next();System.out.println("Rows remaining after rollback: "+r.getInt(1));
            }finally{c.setAutoCommit(true);}
        }
    }
}
