import java.sql.*; import java.util.*;
public class BatchBenchmark {
    public static void main(String[] args) throws Exception {
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/assignment5");
        try(Connection c=DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""))){
            for(int size:new int[]{1000,10000,100000}){
                List<String[]> rows=new ArrayList<>();
                for(int i=0;i<size;i++) rows.add(new String[]{"P"+size+"_"+i,"192300001","Test", "1","Main","2027-03-31","100.0"});
                try(Statement st=c.createStatement()){st.executeUpdate("DELETE FROM bus_pass");}
                long t=System.nanoTime(); int count=PassLoader.loadAll(c,rows);
                System.out.printf("Batch transaction: %d rows in %.2f ms%n",count,(System.nanoTime()-t)/1e6);
            }
        }
    }
}
