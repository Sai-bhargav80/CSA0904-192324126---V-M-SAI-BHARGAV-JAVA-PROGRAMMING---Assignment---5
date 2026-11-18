import java.io.*;
import java.nio.file.*;
/** Counts LF bytes using one-byte reads and a 64 KiB buffered block strategy. */
public class BufferedReadDemo {
    static long countByteAtATime(Path p)throws IOException{
        long lines=0; try(InputStream in=Files.newInputStream(p)){int x;while((x=in.read())!=-1)if(x=='\n')lines++;}return lines;
    }
    static long countBuffered(Path p)throws IOException{
        long lines=0; byte[] buf=new byte[65536];
        try(InputStream in=new BufferedInputStream(Files.newInputStream(p),buf.length)){
            int n;while((n=in.read(buf))!=-1)for(int i=0;i<n;i++)if(buf[i]=='\n')lines++;
        }return lines;
    }
    public static void main(String[] args)throws Exception{
        Path p=Files.createTempFile("attendance",".txt");
        Files.writeString(p,"A\\nB\\nC\\nD\\n");
        long t=System.nanoTime(), a=countByteAtATime(p), ta=System.nanoTime()-t;
        t=System.nanoTime();long b=countBuffered(p),tb=System.nanoTime()-t;
        System.out.printf("Unbuffered: %d lines, %.3f ms%n",a,ta/1e6);
        System.out.printf("Buffered: %d lines, %.3f ms%n",b,tb/1e6);
        Files.deleteIfExists(p);
    }
}
