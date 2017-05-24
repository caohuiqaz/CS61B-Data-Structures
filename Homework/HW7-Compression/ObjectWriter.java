import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

public class ObjectWriter {
    private FileOutputStream fos;
    private ObjectOutputStream os;

    public ObjectWriter(String filename) {
        try {
            fos = new FileOutputStream(filename, false);
            os = new ObjectOutputStream(fos);
        } catch (java.io.IOException e) {
            System.out.println("Error creating ObjectWriter: ");
            e.printStackTrace();            
        }
    }

    public void writeObject(Serializable s) {
        try {
            os.writeObject(s);
        } catch (java.io.IOException e) {
            System.out.println("Error writing object: ");
            e.printStackTrace();
        }
    } 
} 
