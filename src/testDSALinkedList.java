import java.util.Iterator;

/**
 *  Name:     testDSALinkedList
 *  Source:   Practical 4
 *
 *  Modifications:
 *      Rewrite test harness from scratch
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testDSALinkedList
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing DSALinkedList Class");
        testConstructor();
        testMethods();
    }

    public static void testConstructor()
    {
        DSALinkedList<Integer> list;

        System.out.print("Testing Default Constructor: ");

        try
        {
            list = new DSALinkedList<Integer>();
            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testMethods()
    {
        DSALinkedList<Integer> list = new DSALinkedList<Integer>();
        Iterator<Integer> iter;
        int count;

        System.out.print("Testing insertFirst(): ");

        try
        {
            list.insertFirst(Integer.valueOf(1));
            list.insertFirst(Integer.valueOf(2));
            list.insertFirst(Integer.valueOf(3));
            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing insertLast(): ");

        try
        {
            list.insertLast(Integer.valueOf(1));
            list.insertLast(Integer.valueOf(2));
            list.insertLast(Integer.valueOf(3));
            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing peekFirst(): ");

        try
        {
            if (list.peekFirst().intValue() != 3)
            {
                throw new IllegalArgumentException(
                    "Invalid value"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing peekLast(): ");

        try
        {
            if (list.peekLast().intValue() != 3)
            {
                throw new IllegalArgumentException(
                    "Invalid value"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing removeFirst(): ");

        try
        {
            if (list.removeFirst().intValue() != 3 ||
                list.removeFirst().intValue() != 2 ||
                list.removeFirst().intValue() != 1)
            {
                throw new IllegalArgumentException(
                    "Invalid value"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing removeLast(): ");

        try
        {
            if (list.removeLast().intValue() != 3 ||
                list.removeLast().intValue() != 2 ||
                list.removeLast().intValue() != 1)
            {
                throw new IllegalArgumentException(
                    "Invalid value"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing isEmpty(): ");

        try
        {
            if (! list.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Not empty"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing iterator(): ");

        try
        {
            list = new DSALinkedList<Integer>();

            for (int i = 0; i < 8192; i++)
            {
                list.insertLast(Integer.valueOf(i));
            }

            iter = list.iterator();
            count = 0;

            while (iter.hasNext())
            {
                if (iter.next().intValue() != count++)
                {
                    throw new IllegalArgumentException(
                        "Invalid value"
                    );
                }
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }
}
