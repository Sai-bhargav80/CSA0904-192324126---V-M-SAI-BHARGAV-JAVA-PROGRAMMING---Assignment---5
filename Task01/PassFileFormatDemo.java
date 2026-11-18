import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Demonstrates the assignment's delimited-text and DataOutputStream record layouts. */
public class PassFileFormatDemo {
    record Pass(String id,String regNo,String name,int route,String stop,String validUntil,double amount) {}
    static String csv(String s){ return "\"" + s.replace("\"","\"\"") + "\""; }
    static void writeText(Path p, Pass x)throws IOException{
        String line=String.join(",",csv(x.id()),csv(x.regNo()),csv(x.name()),
            Integer.toString(x.route()),csv(x.stop()),csv(x.validUntil()),Double.toString(x.amount()));
        Files.writeString(p,line+System.lineSeparator(),StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,StandardOpenOption.APPEND);
    }
    static void writeBinary(Path p,Pass x)throws IOException{
        try(DataOutputStream out=new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(p)))){
            out.writeUTF(x.id()); out.writeUTF(x.regNo()); out.writeUTF(x.name());
            out.writeInt(x.route()); out.writeUTF(x.stop()); out.writeUTF(x.validUntil()); out.writeDouble(x.amount());
        }
    }
    static Pass readBinary(Path p)throws IOException{
        try(DataInputStream in=new DataInputStream(new BufferedInputStream(Files.newInputStream(p)))){
            return new Pass(in.readUTF(),in.readUTF(),in.readUTF(),in.readInt(),in.readUTF(),in.readUTF(),in.readDouble());
        }
    }
    public static void main(String[] args)throws Exception{
        Pass p=new Pass("P1001","192311001","Arun, K",12,"Central \"A\"","2027-03-31",4500);
        Path text=Files.createTempFile("pass",".csv"), bin=Files.createTempFile("pass",".bin");
        writeText(text,p); writeBinary(bin,p);
        System.out.println("Text record: "+Files.readString(text).trim());
        System.out.println("Binary record decoded: "+readBinary(bin));
        Files.deleteIfExists(text); Files.deleteIfExists(bin);
    }
}
