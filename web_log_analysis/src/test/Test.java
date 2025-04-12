import models.utils.ip2Location;

public class Test {
    public static void main(String[] args) {
        try {
            System.out.println("");
            ip2Location x = new ip2Location();
            long startTime = System.nanoTime();
            for(int i = 0; i < 50000; i++)
            {
                x.parse("217.168.17.5");
            }
            long stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
