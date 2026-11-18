import java.io.*; import java.nio.file.*; import java.sql.*; import java.util.*;
/** Reads CSV pass rows, validates them, batch-loads valid records, then reports JDBC metadata. */
public class MigrateAndInspect {
    public static void main(String[] args) throws Exception {
        String file=args.length>0?args[0]:"../Task06/passes.csv";
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/assignment5");
        try(Connection c=DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""))){
            List<String[]> valid=new ArrayList<>(); int lineNo=0,failed=0;
            try(BufferedReader r=Files.newBufferedReader(Path.of(file))){
                String line; while((line=r.readLine())!=null){lineNo++; String[] f=line.split(",",-1);
                    try { if(f.length!=7) throw new IllegalArgumentException(); Integer.parseInt(f[3]); Double.parseDouble(f[6]); java.time.LocalDate.parse(f[5]); valid.add(f); }
                    catch(Exception ex){failed++;System.out.println("Rejected line "+lineNo+": "+line);}
                }
            }
            System.out.println("Valid rows: "+valid.size()+", rejected rows: "+failed);
            System.out.println("Rows inserted: "+PassLoader.loadAll(c,valid));
            DatabaseMetaData md=c.getMetaData();
            System.out.println("Database: "+md.getDatabaseProductName()+" "+md.getDatabaseProductVersion());
            System.out.println("JDBC driver: "+md.getDriverName()+" "+md.getDriverVersion());
            try(ResultSet rs=md.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})){
                System.out.print("Tables: "); while(rs.next()) System.out.print(rs.getString("TABLE_NAME")+" "); System.out.println();
            }
        }
    }
}
