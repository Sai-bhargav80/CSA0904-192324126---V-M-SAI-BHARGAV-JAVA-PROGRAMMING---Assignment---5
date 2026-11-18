import java.nio.file.*; import java.util.*;
public class PassGrowthBenchmark {
    public static void main(String[] args) throws Exception {
        for(int n:new int[]{1000,10000,50000,100000}){
            Path p=Files.createTempFile("growth",".csv"); PassStore s=new PassStore(p);
            for(int i=0;i<n;i++) s.add("P"+i,"192300001","Student"+i, i%20,"Stop","2027-03-31",100);
            long t=System.nanoTime(); String[] found=s.find("P"+(n-1)); long elapsed=System.nanoTime()-t;
            System.out.printf("%d records | found=%s | %.3f ms%n",n,found!=null,elapsed/1e6);
            Files.deleteIfExists(p);
        }
    }
}
