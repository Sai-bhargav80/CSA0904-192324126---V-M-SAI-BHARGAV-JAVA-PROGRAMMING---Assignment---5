import java.sql.*;
/** Compares Statement, PreparedStatement row-by-row, and batched PreparedStatement. */
public class JdbcComparison {
    static final String VALUES="('ID','REG','Name',1,'Stop','2027-03-31',100.0)";
    static String insert(int i){return "INSERT INTO bus_pass(pass_id,reg_no,name,route_no,boarding_stop,valid_until,amount) VALUES ('P"+i+"','192300001','Student',1,'Stop','2027-03-31',100.0)";}
    static void clear(Connection c)throws SQLException{try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");}}
    static long statement(Connection c,int n)throws SQLException{
        clear(c); long t=System.nanoTime();
        try(Statement s=c.createStatement()){for(int i=0;i<n;i++)s.executeUpdate(insert(i));}
        return System.nanoTime()-t;
    }
    static long prepared(Connection c,int n)throws SQLException{
        clear(c); long t=System.nanoTime();
        try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass(pass_id,reg_no,name,route_no,boarding_stop,valid_until,amount) VALUES (?,?,?,?,?,?,?)")){
            for(int i=0;i<n;i++){bind(p,i);p.executeUpdate();}
        } return System.nanoTime()-t;
    }
    static long batch(Connection c,int n)throws SQLException{
        clear(c); boolean old=c.getAutoCommit(); c.setAutoCommit(false); long t=System.nanoTime();
        try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass(pass_id,reg_no,name,route_no,boarding_stop,valid_until,amount) VALUES (?,?,?,?,?,?,?)")){
            for(int i=0;i<n;i++){bind(p,i);p.addBatch();if((i+1)%1000==0)p.executeBatch();}
            p.executeBatch();c.commit();
        }catch(SQLException e){c.rollback();throw e;}finally{c.setAutoCommit(old);}
        return System.nanoTime()-t;
    }
    static void bind(PreparedStatement p,int i)throws SQLException{
        p.setString(1,"P"+i);p.setString(2,"192300001");p.setString(3,"Student");
        p.setInt(4,1);p.setString(5,"Stop");p.setDate(6,Date.valueOf("2027-03-31"));p.setDouble(7,100);
    }
    public static void main(String[] args)throws Exception{
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/assignment5");
        try(Connection c=DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""))){
            for(int n:new int[]{1000,10000,100000}){
                System.out.printf("%d rows | Statement %.2f ms | Prepared %.2f ms | Batch %.2f ms%n",n,
                    statement(c,n)/1e6,prepared(c,n)/1e6,batch(c,n)/1e6);
            }
        }
    }
}
