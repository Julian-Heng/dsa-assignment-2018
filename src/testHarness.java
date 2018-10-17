public class testHarness
{
    public static void main(String[] args)
    {
        testNominee.run();
        testHousePreference.run();
        testVoteStats.run();
        testDSAGraph.run();
        testDSAHeap.run();
    }

    public static void header(String msg)
    {
        String line = "";

        for (int i = 0; i < msg.length(); i++)
        {
            line += "=";
        }

        System.out.println(line + "\n" + msg + "\n" + line);
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
