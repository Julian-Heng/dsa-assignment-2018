/**
 *  Name:     testHousePreference
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testHousePreference
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing HousePreference class");
        testConstructor();
        testSetters();
        testAddNominee();
    }

    public static void testConstructor()
    {
        HousePreference pref;
        System.out.print("Testing Default Constructor: ");

        try
        {
            pref = new HousePreference();

            if (! pref.getPrefNameDivision().equals("TBD"))
            {
                testHarnessCommons.failed("Division Name is incorrect");
            }
            else if (pref.getPrefIdDivision() != 0)
            {
                testHarnessCommons.failed("Division ID is incorrect");
            }
            else if (pref.getNumInformalVotes() != 0)
            {
                testHarnessCommons.failed("Informal votes count is incorrect");
            }
            else if (pref.getNumTotalVotes() != 0)
            {
                testHarnessCommons.failed("Total votes count is incorrect");
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
            pref = new HousePreference("name", "100");

            if (! pref.getPrefNameDivision().equals("name"))
            {
                testHarnessCommons.failed("Division Name is incorrect");
            }
            else if (pref.getPrefIdDivision() != 100)
            {
                testHarnessCommons.failed("Division ID is incorrect");
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
        HousePreference pref;
        System.out.print("Testing setPrefNameDivision(): ");

        try
        {
            pref = new HousePreference("name", "100");
            pref.setPrefNameDivision("new");

            if (! pref.getPrefNameDivision().equals("new"))
            {
                testHarnessCommons.failed("Division Name is set incorrectly");
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

        System.out.print("Testing setPrefIdDivision(): ");

        try
        {
            pref = new HousePreference("name", "100");
            pref.setPrefIdDivision("200");

            if (pref.getPrefIdDivision() != 200)
            {
                testHarnessCommons.failed("Division ID is set incorrectly");
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

        System.out.print("Testing setNumInformalVotes(): ");

        try
        {
            pref = new HousePreference("name", "100");
            pref.setNumInformalVotes("200");

            if (pref.getNumInformalVotes() != 200)
            {
                testHarnessCommons.failed("Informal Votes is set incorrectly");
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

        System.out.print("Testing setNumTotalVotes(): ");

        try
        {
            pref = new HousePreference("name", "100");
            pref.setNumTotalVotes("200");

            if (pref.getNumTotalVotes() != 200)
            {
                testHarnessCommons.failed("Total Votes is set incorrectly");
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

    public static void testAddNominee()
    {
        HousePreference pref;
        Nominee nominee;

        System.out.print("Testing addNomineeToList() with 1 Nominee: ");

        try
        {
            pref = new HousePreference("name", "100");
            nominee = new Nominee();
            nominee.setNumVotes("200");
            pref.addNomineeToList(nominee);
            pref.updateTotalVotes();

            if (pref.getNumTotalVotes() != 200)
            {
                testHarnessCommons.failed("Total votes count is incorrect");
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

        System.out.print("Testing addNomineeToList() with 3 Nominees: ");

        try
        {
            pref = new HousePreference("name", "100");
            nominee = new Nominee();
            nominee.setNumVotes("200");
            pref.addNomineeToList(nominee);

            nominee = new Nominee();
            nominee.setNumVotes("300");
            pref.addNomineeToList(nominee);

            pref.updateTotalVotes();

            if (pref.getNumTotalVotes() != 500)
            {
                testHarnessCommons.failed("Total votes count is incorrect");
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
}
