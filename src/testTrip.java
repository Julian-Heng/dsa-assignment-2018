/**
 *  Name:     testTrip
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testTrip
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing Trip Class");
        testConstructor();
        testSetters();
    }

    public static void testConstructor()
    {
        Trip trip;

        System.out.print("Testing Alternate Constructor: ");

        try
        {
            trip = new Trip("Car", 100, 100);

            if (! trip.getTransportType().equals("Car"))
            {
                throw new IllegalArgumentException(
                    "Incorrect transport type"
                );
            }
            else if (trip.getDistance() != 100)
            {
                throw new IllegalArgumentException(
                    "Incorrect distance"
                );
            }
            else if (trip.getDuration() != 100)
            {
                throw new IllegalArgumentException(
                    "Incorrect duration"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testSetters()
    {
        Trip trip;

        System.out.print("Testing setTransportType(): ");

        try
        {
            trip = new Trip("Car", 100, 100);

            trip.setTransportType("Plane");

            if (! trip.getTransportType().equals("Plane"))
            {
                throw new IllegalArgumentException(
                    "Incorrect transport type"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setDistance(): ");

        try
        {
            trip = new Trip("Car", 100, 100);

            trip.setDistance(500);

            if (trip.getDistance() != 500)
            {
                throw new IllegalArgumentException(
                    "Incorrect distance"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing setDuration(): ");

        try
        {
            trip = new Trip("Car", 100, 100);

            trip.setDuration(500);

            if (trip.getDuration() != 500)
            {
                throw new IllegalArgumentException(
                    "Incorrect duration"
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
