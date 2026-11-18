import java.sql.*;
/** Requires MySQL Connector/J and the assignment database configured by environment variables. */
public class UnsafeVsSafeLogin {
    public static void main(String[] args) throws Exception {
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/assignment5");
        String user=System.getenv().getOrDefault("DB_USER","root");
        String pass=System.getenv().getOrDefault("DB_PASSWORD","");
        try(Connection c=DriverManager.getConnection(url,user,pass)){
            String id=args.length>0?args[0]:"' OR '1'='1";
            String pwd=args.length>1?args[1]:"wrong";
            String unsafe="SELECT * FROM users WHERE id='"+id+"' AND pwd='"+pwd+"'";
            try(Statement st=c.createStatement(); ResultSet rs=st.executeQuery(unsafe)){
                System.out.println("Unsafe query accepted row? "+rs.next());
            }
            String sql="SELECT * FROM users WHERE id=? AND pwd=?";
            try(PreparedStatement ps=c.prepareStatement(sql)){
                ps.setString(1,id); ps.setString(2,pwd);
                try(ResultSet rs=ps.executeQuery()){System.out.println("PreparedStatement accepted row? "+rs.next());}
            }
        }
    }
}
