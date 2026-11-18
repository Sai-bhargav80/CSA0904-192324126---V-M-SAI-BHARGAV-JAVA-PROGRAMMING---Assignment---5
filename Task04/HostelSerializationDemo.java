import java.io.*;
import java.util.*;
/** Demonstrates serializing a Hostel -> Room -> Resident object graph safely. */
public class HostelSerializationDemo {
    static class Resident implements Serializable{
        private static final long serialVersionUID=1L;
        String name,regNo;
        transient String password; // never persist plaintext credentials
        Resident(String n,String r,String p){name=n;regNo=r;password=p;}
    }
    static class Room implements Serializable{
        private static final long serialVersionUID=1L;
        int roomNo; List<Resident> residents=new ArrayList<>();
        Room(int n){roomNo=n;}
    }
    static class Hostel implements Serializable{
        private static final long serialVersionUID=1L;
        List<Room> rooms=new ArrayList<>();
        transient Object liveConnection; // stand-in for non-serializable JDBC Connection
    }
    public static void main(String[] args)throws Exception{
        Hostel h=new Hostel();Room r=new Room(101);
        r.residents.add(new Resident("Meena","192311002","do-not-save"));h.rooms.add(r);
        h.liveConnection=new Object();
        File f=File.createTempFile("hostel",".ser");
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f))){out.writeObject(h);}
        Hostel restored;
        try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(f))){restored=(Hostel)in.readObject();}
        Resident x=restored.rooms.get(0).residents.get(0);
        System.out.println("Resident: "+x.name+", "+x.regNo);
        System.out.println("Password after load: "+x.password);
        System.out.println("Live connection after load: "+restored.liveConnection);
        f.delete();
    }
}
