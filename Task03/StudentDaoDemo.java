import java.sql.*;
import java.util.*;
/** Generic DAO contract and JDBC implementation for Student. */
public class StudentDaoDemo {
    record Student(int id,String name,String email) {}
    interface Dao<T,K>{
        int insert(T x)throws SQLException;
        Optional<T> findById(K id)throws SQLException;
        List<T> findAll()throws SQLException;
        int update(T x)throws SQLException;
        int delete(K id)throws SQLException;
    }
    static class StudentDao implements Dao<Student,Integer>{
        private final Connection con;
        StudentDao(Connection con){this.con=con;}
        public int insert(Student s)throws SQLException{
            try(PreparedStatement p=con.prepareStatement("INSERT INTO student(student_id,name,email) VALUES(?,?,?)")){
                p.setInt(1,s.id());p.setString(2,s.name());p.setString(3,s.email());return p.executeUpdate();
            }
        }
        public Optional<Student> findById(Integer id)throws SQLException{
            try(PreparedStatement p=con.prepareStatement("SELECT student_id,name,email FROM student WHERE student_id=?")){
                p.setInt(1,id);try(ResultSet r=p.executeQuery()){
                    return r.next()?Optional.of(new Student(r.getInt(1),r.getString(2),r.getString(3))):Optional.empty();
                }
            }
        }
        public List<Student> findAll()throws SQLException{
            List<Student> out=new ArrayList<>();
            try(PreparedStatement p=con.prepareStatement("SELECT student_id,name,email FROM student");ResultSet r=p.executeQuery()){
                while(r.next())out.add(new Student(r.getInt(1),r.getString(2),r.getString(3)));
            }return out;
        }
        public int update(Student s)throws SQLException{
            try(PreparedStatement p=con.prepareStatement("UPDATE student SET name=?,email=? WHERE student_id=?")){
                p.setString(1,s.name());p.setString(2,s.email());p.setInt(3,s.id());return p.executeUpdate();
            }
        }
        public int delete(Integer id)throws SQLException{
            try(PreparedStatement p=con.prepareStatement("DELETE FROM student WHERE student_id=?")){
                p.setInt(1,id);return p.executeUpdate();
            }
        }
    }
    public static void main(String[] args)throws Exception{
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/assignment5");
        try(Connection c=DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""))){
            StudentDao dao=new StudentDao(c);
            Student s=new Student(1001,"Arun","arun@example.com");
            System.out.println("Inserted: "+dao.insert(s));
            System.out.println("Found: "+dao.findById(1001));
            System.out.println("All students: "+dao.findAll().size());
            System.out.println("Updated: "+dao.update(new Student(1001,"Arun Kumar","arun@example.com")));
            System.out.println("Deleted: "+dao.delete(1001));
        }
    }
}
