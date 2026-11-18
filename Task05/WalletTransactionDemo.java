import java.sql.*;
/** JDBC transfer using one transaction and a parameterized login check. */
public class WalletTransactionDemo {
    static void transfer(Connection c,int from,int to,double amount)throws SQLException{
        if(amount<=0)throw new IllegalArgumentException("Amount must be positive");
        boolean old=c.getAutoCommit();c.setAutoCommit(false);
        try{
            int debited;
            try(PreparedStatement p=c.prepareStatement("UPDATE wallet SET balance=balance-? WHERE wallet_id=? AND balance>=?")){
                p.setBigDecimal(1,java.math.BigDecimal.valueOf(amount));p.setInt(2,from);
                p.setBigDecimal(3,java.math.BigDecimal.valueOf(amount));debited=p.executeUpdate();
            }
            if(debited!=1)throw new SQLException("Insufficient funds or source wallet missing");
            try(PreparedStatement p=c.prepareStatement("UPDATE wallet SET balance=balance+? WHERE wallet_id=?")){
                p.setBigDecimal(1,java.math.BigDecimal.valueOf(amount));p.setInt(2,to);
                if(p.executeUpdate()!=1)throw new SQLException("Destination wallet missing");
            }
            c.commit();
        }catch(SQLException|RuntimeException e){c.rollback();throw e;}
        finally{c.setAutoCommit(old);}
    }
    static boolean login(Connection c,String id,String password)throws SQLException{
        try(PreparedStatement p=c.prepareStatement("SELECT 1 FROM users WHERE id=? AND pwd=?")){
            p.setString(1,id);p.setString(2,password);
            try(ResultSet r=p.executeQuery()){return r.next();}
        }
    }
    public static void main(String[] args)throws Exception{
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/assignment5");
        try(Connection c=DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""))){
            transfer(c,1,2,500.0);
            System.out.println("Transfer completed: 500.00");
            System.out.println("Login check: "+login(c,"student","correct-password"));
        }
    }
}
