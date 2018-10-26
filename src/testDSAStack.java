/**
 *  Name:     testDSAStack
 *  Source:   Practical 3
 *
 *  Modifications:
 *      Rewrite test harness from scratch
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testDSAStack
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing DSAStack Class");
        testConstructor();
        testPushPopTop();
    }

    public static void testConstructor()
    {
        DSAStack<Integer> stack;

        System.out.print("Testing Default Constructor: ");

        try
        {
            stack = new DSAStack<Integer>();

            if (stack.getCount() != 0 || ! stack.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Incorrect count"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testPushPopTop()
    {
        DSAStack<Integer> stack = null;

        System.out.print("Testing push(): ");

        try
        {
            stack = new DSAStack<Integer>();

            stack.push(Integer.valueOf(1));
            stack.push(Integer.valueOf(2));
            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing top(): ");

        try
        {
            if (stack.top().intValue() != 2)
            {
                throw new IllegalArgumentException(
                    "Invalid value from stack"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing pop(): ");

        try
        {
            if (stack.pop().intValue() != 2 ||
                stack.pop().intValue() != 1)
            {
                throw new IllegalArgumentException(
                    "Invalid value from stack"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }
}
