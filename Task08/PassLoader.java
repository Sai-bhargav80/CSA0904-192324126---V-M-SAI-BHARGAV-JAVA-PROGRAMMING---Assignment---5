import java.sql.*;
import java.util.List;
/** Transactional batch loader. Requires a MySQL JDBC connection supplied by caller. */
public class PassLoader {
    public static int loadAll(Connection con, List<String[]> passes) throws SQLException {
        String sql="INSERT INTO bus_pass(pass_id,reg_no,name,route_no,boarding_stop,valid_until,amount) VALUES (?,?,?,?,?,?,?)";
        boolean old=con.getAutoCommit(); con.setAutoCommit(false);
        try(PreparedStatement ps=con.prepareStatement(sql)){
            int n=0;
            for(String[] p:passes){
                if(p.length!=7) throw new SQLException("Expected seven fields");
                ps.setString(1,p[0]); ps.setString(2,p[1]); ps.setString(3,p[2]);
                ps.setInt(4,Integer.parseInt(p[3])); ps.setString(5,p[4]);
                ps.setDate(6,Date.valueOf(p[5])); ps.setDouble(7,Double.parseDouble(p[6]));
                ps.addBatch(); n++;
                if(n%1000==0) ps.executeBatch();
            }
            ps.executeBatch(); con.commit(); return n;
        } catch(SQLException|RuntimeException e){
            con.rollback(); throw e;
        } finally { con.setAutoCommit(old); }
    }
}
