// Usage
public class Main {
    public static void main(String[] args) {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        logger1.log("This is a log message.");
        logger2.log("This is another log message.");

        System.out.println("Are logger1 and logger2 the same instance? " + (logger1 == logger2));
    }
}