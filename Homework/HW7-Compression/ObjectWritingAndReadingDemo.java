import java.util.ArrayList;
import java.util.TreeSet;

public class ObjectWritingAndReadingDemo {

    public static void main(String[] args) {
        ArrayList<Integer> integerList = new ArrayList<Integer>();
        TreeSet<String> stringSet = new TreeSet<String>();

        integerList.add(5);
        integerList.add(10);
        integerList.add(20);

        stringSet.add("five");
        stringSet.add("ten");
        stringSet.add("twenty");

        ObjectWriter ow = new ObjectWriter("somefile.bla");
        ow.writeObject(integerList);
        ow.writeObject(stringSet);

        ObjectReader or = new ObjectReader("somefile.bla");
        Object x = or.readObject();
        Object y = or.readObject();


        ArrayList<Integer> xAsList = (ArrayList<Integer>) x;
        TreeSet<String> yAsSet = (TreeSet<String>) y;

        System.out.println(xAsList);
        System.out.println(yAsSet);
    }
} 
