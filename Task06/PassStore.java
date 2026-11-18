import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** CSV pass store used for B1. Fields: id,regNo,name,route,stop,validUntil,amount. */
public class PassStore {
    private final Path file;
    public PassStore(Path file) { this.file = file; }
    public void add(String id,String reg,String name,int route,String stop,String valid,double amount) throws IOException {
        if (id.isBlank() || reg.isBlank() || route < 0 || amount < 0) throw new IllegalArgumentException("Invalid pass");
        String[] fields={id,reg,name,String.valueOf(route),stop,valid,String.valueOf(amount)};
        // This assignment's test data avoids commas; quote/double quotes for CSV compatibility.
        StringJoiner line=new StringJoiner(",");
        for(String f:fields) line.add("\""+f.replace("\"","\"\"")+"\"");
        try(BufferedWriter w=Files.newBufferedWriter(file,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.APPEND)){
            w.write(line.toString()); w.newLine();
        }
    }
    public String[] find(String id) throws IOException {
        if(!Files.exists(file)) return null;
        try(BufferedReader r=Files.newBufferedReader(file,StandardCharsets.UTF_8)){
            String line;
            while((line=r.readLine())!=null){
                String[] f=parse(line);
                if(f==null || f.length!=7) continue;
                try { Integer.parseInt(f[3]); Double.parseDouble(f[6]); }
                catch(NumberFormatException ex){ continue; }
                if(f[0].equals(id)) return f;
            }
        }
        return null;
    }
    public List<String[]> listByRoute(int route) throws IOException {
        List<String[]> found=new ArrayList<>();
        if(!Files.exists(file)) return found;
        try(BufferedReader r=Files.newBufferedReader(file,StandardCharsets.UTF_8)){
            String line;
            while((line=r.readLine())!=null){
                String[] f=parse(line);
                if(f==null || f.length!=7) continue;
                try { if(Integer.parseInt(f[3])==route && Double.parseDouble(f[6])>=0) found.add(f); }
                catch(NumberFormatException ignored) {}
            }
        }
        return found;
    }
    private static String[] parse(String line) {
        List<String> out=new ArrayList<>(); StringBuilder b=new StringBuilder(); boolean quoted=false;
        for(int i=0;i<line.length();i++){
            char c=line.charAt(i);
            if(c=='"'){ if(quoted && i+1<line.length() && line.charAt(i+1)=='"'){b.append('"');i++;} else quoted=!quoted; }
            else if(c==',' && !quoted){out.add(b.toString());b.setLength(0);} else b.append(c);
        }
        if(quoted) return null; out.add(b.toString()); return out.toArray(String[]::new);
    }
    public static void main(String[] args) throws Exception {
        Path p=Files.createTempFile("bus-passes",".csv"); PassStore s=new PassStore(p);
        s.add("P1001","192311001","Arun",12,"Central","2027-03-31",4500);
        Files.writeString(p,"P9999,DAMAGED\nP8888,R,Name,2,Stop,2027-01-01,not-a-number\n",StandardOpenOption.APPEND);
        s.add("P1003","192311003","Divya",7,"Library","2027-03-31",3800);
        System.out.println("Find P1003: "+Arrays.toString(s.find("P1003")));
        System.out.println("Find missing: "+s.find("P404"));
        System.out.println("Route 12 count: "+s.listByRoute(12).size());
        System.out.println("Damaged rows skipped: "+(s.find("P1003")!=null));
        Files.deleteIfExists(p);
    }
}
