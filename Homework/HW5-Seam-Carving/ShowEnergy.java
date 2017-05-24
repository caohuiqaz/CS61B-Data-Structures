
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class ShowEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

    }

}
