import java.io.*;
import java.nio.file.*;
public class LineCounter {
    static long unbuffered(Path p) throws IOException {
        long n=0; try(InputStream in=Files.newInputStream(p)){ int x; while((x=in.read())!=-1) if(x=='\n') n++; } return n;
    }
    static long buffered(Path p) throws IOException {
        long n=0; byte[] b=new byte[65536]; try(InputStream in=new BufferedInputStream(Files.newInputStream(p),b.length)){
            int k; while((k=in.read(b))!=-1) for(int i=0;i<k;i++) if(b[i]=='\n') n++;
        } return n;
    }
    public static void main(String[] a) throws Exception {
        Path p=Files.createTempFile("attendance",".log");
        for(int mb:new int[]{1,10,50}){
            try(OutputStream o=Files.newOutputStream(p)){
                byte[] block=("student,attendance,2026-11-18\n").getBytes();
                long target=mb*1024L*1024L;
                for(long n=0;n<target;n+=block.length) o.write(block,0,(int)Math.min(block.length,target-n));
            }
            long t=System.nanoTime(); long u=unbuffered(p); long tu=System.nanoTime()-t;
            t=System.nanoTime(); long b=buffered(p); long tb=System.nanoTime()-t;
            System.out.printf("%d MB | unbuffered=%d lines %.2f ms | buffered=%d lines %.2f ms%n",mb,u,tu/1e6,b,tb/1e6);
        }
        Files.deleteIfExists(p);
    }
}
