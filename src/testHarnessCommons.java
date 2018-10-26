/**
 *  Name:     testHarnessCommons
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testHarnessCommons
{
    public static void header(String msg)
    {
        String line;
        line = String.format("%" + msg.length() + "s", " ").replace(' ', '=');
        System.out.printf("%s\n%s\n%s\n", line, msg, line);
    }

    public static void passed()
    {
        System.out.println("\u001B[32mPassed\u001B[0m");
    }

    public static void failed(String err)
    {
        System.out.println("\u001B[31mFailed\u001B[0m. " + err);
    }
}
