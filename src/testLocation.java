import java.util.*;

/**
 *  Name:     testLocation
 *  Souce:    None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testLocation
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing Location Class");
        testConstructor();
        testSetters();
    }

    public static void testConstructor()
    {
        Location loc;

        System.out.print("Testing Default Constructor: ");

        try
        {
            loc = new Location();

            if (! loc.getState().equals("TBD"))
            {
                testHarnessCommons.failed("Incorrect State");
            }
            else if (! loc.getDivision().equals("TBD"))
            {
                testHarnessCommons.failed("Incorrect Division");
            }
            else if (Math.abs(loc.getLatitude() - 0.0) > 0.000000001)
            {
                testHarnessCommons.failed("Incorrect latitude");
            }
            else if (Math.abs(loc.getLongitude() - 0.0) > 0.000000001)
            {
                testHarnessCommons.failed("Incorrect longitude");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing Alternate Constructor: ");

        try
        {
            loc = new Location("WA", "Division Name", "50.0", "100.0");

            if (! loc.getState().equals("WA"))
            {
                testHarnessCommons.failed("Incorrect State");
            }
            else if (! loc.getDivision().equals("Division Name"))
            {
                testHarnessCommons.failed("Incorrect Division");
            }
            else if (Math.abs(loc.getLatitude() - 50.0) > 0.000000001)
            {
                testHarnessCommons.failed("Incorrect latitude");
            }
            else if (Math.abs(loc.getLongitude() - 100.0) > 0.000000001)
            {
                testHarnessCommons.failed("Incorrect longitude");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testSetters()
    {
        Location loc;

        System.out.print("Testing setState(): ");

        try
        {
            loc = new Location();
            loc.setState("WA");

            if (! loc.getState().equals("WA"))
            {
                throw new IllegalArgumentException(
                    "Incorrect state"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setDivision(): ");

        try
        {
            loc = new Location();
            loc.setDivision("Division Name");

            if (! loc.getDivision().equals("Division Name"))
            {
                throw new IllegalArgumentException(
                    "Incorrect division"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setLatitude(): ");

        try
        {
            loc = new Location();
            loc.setLatitude("9.0");

            if (Math.abs(loc.getLatitude() - 9.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Incorrect latitude"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setLongitude(): ");

        try
        {
            loc = new Location();
            loc.setLongitude("9.0");

            if (Math.abs(loc.getLongitude() - 9.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Incorrect longitude"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setState() [invalid]: ");

        try
        {
            loc = new Location();
            loc.setState("");

            if (! loc.getState().equals("TBD"))
            {
                throw new IllegalArgumentException(
                    "Incorrect state"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setDivision() [invalid]: ");

        try
        {
            loc = new Location();
            loc.setDivision("");

            if (! loc.getDivision().equals("TBD"))
            {
                throw new IllegalArgumentException(
                    "Incorrect division"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setLatitude() [invalid]: ");

        try
        {
            loc = new Location();
            loc.setLatitude("not a number");

            if (Math.abs(loc.getLatitude() - 0.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Invalid location"
                );
            }

            loc.setLatitude("-100");

            if (Math.abs(loc.getLatitude() - 0.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Invalid location"
                );
            }

            loc.setLatitude("100");

            if (Math.abs(loc.getLatitude() - 0.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Invalid location"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setLongitude() [invalid]: ");

        try
        {
            loc = new Location();
            loc.setLongitude("not a number");

            if (Math.abs(loc.getLongitude() - 0.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Invalid location"
                );
            }

            loc.setLongitude("-200");

            if (Math.abs(loc.getLongitude() - 0.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Invalid location"
                );
            }

            loc.setLongitude("200");

            if (Math.abs(loc.getLongitude() - 0.0) > 0.0000001)
            {
                throw new IllegalArgumentException(
                    "Invalid location"
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
