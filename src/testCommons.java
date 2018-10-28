import java.util.*;

/**
 *  Name:     testCommons
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testCommons
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        String expected;
        String[] inputArr;
        testHarnessCommons.header("Testing Commons class");

        System.out.print("Testing convertTimeToString(): ");

        try
        {
            expected = "0 day, 0 hour, 0 min, 0 sec";
            if (! Commons.convertTimeToString(0).equals(expected))
            {
                throw new IllegalArgumentException(
                    "Incorrect time string"
                );
            }

            expected = "0 day, 0 hour, 1 min, 0 sec";

            if (! Commons.convertTimeToString(60).equals(expected))
            {
                throw new IllegalArgumentException(
                    "Incorrect time string"
                );
            }

            expected = "0 day, 1 hour, 0 min, 0 sec";

            if (! Commons.convertTimeToString(60 * 60).equals(expected))
            {
                throw new IllegalArgumentException(
                    "Incorrect time string"
                );
            }

            expected = "1 day, 0 hour, 0 min, 0 sec";

            if (! Commons.convertTimeToString(60 * 60 * 24).equals(expected))
            {
                throw new IllegalArgumentException(
                    "Incorrect time string"
                );
            }

            expected = "0 day, 0 hour, 2 mins, 0 sec";

            if (! Commons.convertTimeToString(60 * 2).equals(expected))
            {
                throw new IllegalArgumentException(
                    "Incorrect time string"
                );
            }

            expected = "0 day, 2 hours, 0 min, 0 sec";

            if (! Commons.convertTimeToString(2 * 60 * 60).equals(expected))
            {
                throw new IllegalArgumentException(
                    "Incorrect time string"
                );
            }

            expected = "2 days, 0 hour, 0 min, 0 sec";

            if (! Commons.convertTimeToString(2 * 60 * 60 * 24).equals(expected))
            {
                throw new IllegalArgumentException(
                    "Incorrect time string"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing calcMaxStringArrLength(): ");

        try
        {
            inputArr = new String[5];

            for (int i = 1; i <= 5; i++)
            {
                inputArr[i - 1] = String.format("%" + i + "s", " ").replace(' ', '=');
            }

            if (Commons.calcMaxStringArrLength(inputArr) != 5)
            {
                throw new IllegalArgumentException(
                    "Incorrect maximum length"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
            e.printStackTrace();
        }

        System.out.print("Testing formatPadding(): ");

        try
        {
            if (! Commons.formatPadding("%1s").equals(" "))
            {
                throw new IllegalArgumentException(
                    "Incorrect padding"
                );
            }

            if (! Commons.formatPadding("%10s").equals("          "))
            {
                throw new IllegalArgumentException(
                    "Incorrect padding"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
           testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing formatPadding(): ");

        try
        {
            if (! Commons.formatPadding("%1s", '#').equals("#"))
            {
                throw new IllegalArgumentException(
                    "Incorrect padding"
                );
            }

            if (! Commons.formatPadding("%10s", '#').equals("##########"))
            {
                throw new IllegalArgumentException(
                    "Incorrect padding"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
           testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing generateLine(): ");

        try
        {
            for (int i = 1; i <= 1024; i++)
            {
                if (Commons.generateLine(i).length() != i)
                {
                    throw new IllegalArgumentException(
                        "Incorrect line generated"
                    );
                }
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing compareIntString(): ");

        try
        {
            for (int i = 1; i <= 1024; i++)
            {
                if (! Commons.compareIntString(i, Integer.toString(i)))
                {
                    throw new IllegalArgumentException(
                        "Incorrect int comparison"
                    );
                }
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing toMilliseconds(): ");

        try
        {
            if (! Commons.toMilliseconds(1000000).equals("1.000"))
            {
                throw new IllegalArgumentException(
                    "Incorrect miliseconds"
                );
            }

            if (! Commons.toMilliseconds(100000).equals("0.100"))
            {
                throw new IllegalArgumentException(
                    "Incorrect miliseconds"
                );
            }

            if (! Commons.toMilliseconds(10000).equals("0.010"))
            {
                throw new IllegalArgumentException(
                    "Incorrect miliseconds"
                );
            }

            if (! Commons.toMilliseconds(1000).equals("0.001"))
            {
                throw new IllegalArgumentException(
                    "Incorrect miliseconds"
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
