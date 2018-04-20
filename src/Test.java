import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Test {
    static NetworkInfluence ni;

    public static void main(String[] args) throws FileNotFoundException {
        ni = new NetworkInfluence("WikiCS.txt");
        System.out.println("\nDegree: ");
        printArray(ni.mostInfluentialDegree(10));
        System.out.println("\nModular: ");
        printArray(ni.mostInfluentialModular(10));
        System.out.println("\nSubModular: ");
        printArray(ni.mostInfluentialSubModular(10));

        System.out.println(ni.influence(ni.mostInfluentialDegree(10)));
        System.out.println(ni.influence(ni.mostInfluentialModular(10)));
        System.out.println(ni.influence(ni.mostInfluentialSubModular(10)));
    }

    private static void printArray(ArrayList<String> array) {
        for (String string : array) {
            System.out.printf("%-15s %s\n", ni.influence(string), string);
        }
    }
}
