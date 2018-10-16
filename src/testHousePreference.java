public class testHousePreference
{
    public static void run()
    {
        testHarness.header("Testing HousePreference class");
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
                testHarness.failed("Division Name is incorrect");
            }
            else if (pref.getPrefIdDivision() != 0)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else if (pref.getNumInformalVotes() != 0)
            {
                testHarness.failed("Informal votes count is incorrect");
            }
            else if (pref.getNumTotalVotes() != 0)
            {
                testHarness.failed("Total votes count is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
        }

        System.out.print("Testing Alternate Constructor: ");

        try
        {
            pref = new HousePreference("name", "100");

            if (! pref.getPrefNameDivision().equals("name"))
            {
                testHarness.failed("Division Name is incorrect");
            }
            else if (pref.getPrefIdDivision() != 100)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
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
                testHarness.failed("Division Name is set incorrectly");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
        }

        System.out.print("Testing setPrefIdDivision(): ");

        try
        {
            pref = new HousePreference("name", "100");
            pref.setPrefIdDivision("200");

            if (pref.getPrefIdDivision() != 200)
            {
                testHarness.failed("Division ID is set incorrectly");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
        }

        System.out.print("Testing setNumInformalVotes(): ");

        try
        {
            pref = new HousePreference("name", "100");
            pref.setNumInformalVotes("200");

            if (pref.getNumInformalVotes() != 200)
            {
                testHarness.failed("Informal Votes is set incorrectly");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
        }

        System.out.print("Testing setNumTotalVotes(): ");

        try
        {
            pref = new HousePreference("name", "100");
            pref.setNumTotalVotes("200");

            if (pref.getNumTotalVotes() != 200)
            {
                testHarness.failed("Total Votes is set incorrectly");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
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
                testHarness.failed("Total votes count is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
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
                testHarness.failed("Total votes count is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
        }
    }
}
