import java.io.*;
import java.util.*;

public class test {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader(args[0]);
        //BufferedReader bufferedReader = new BufferedReader(fileReader);
        Scanner scanner = new Scanner(fileReader);
        String a = scanner.next();
        System.out.println(a);
    }
}
