public class testVoteStats
{
    public static void run()
    {
        testHarness.header("Testing VoteStats class");
        testConstructor();
        testSetters();
    }

    public static void testConstructor()
    {
        VoteStats votes;

        System.out.print("Testing Default Constructor: ");

        try
        {
            votes = new VoteStats();

            if (! votes.getNameDivision().equals("TBD"))
            {
                testHarness.failed("Division Name is incorrect");
            }
            else if (votes.getIdDivision() != 0)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else if (! votes.getState().equals("TBD"))
            {
                testHarness.failed("State is incorrect");
            }
            else if (! votes.getAbvParty().equals("TBD"))
            {
                testHarness.failed("Party Abbreviation is incorrect");
            }
            else if (! votes.getNameParty().equals("TBD"))
            {
                testHarness.failed("Party Name is incorrect");
            }
            else if (votes.getVotesFor() != 0)
            {
                testHarness.failed("Votes For is incorrect");
            }
            else if (votes.getVotesAgainst() != 0)
            {
                testHarness.failed("Votes Against is incorrect");
            }
            else if (votes.getVotesTotal() != 0)
            {
                testHarness.failed("Votes Total is incorrect");
            }
            else if (votes.getMargin() != 0.0)
            {
                testHarness.failed("Margin is incorrect");
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
            votes = new VoteStats("a", "1", "a", "a", "a", 1, 2, 3);

            if (! votes.getNameDivision().equals("a"))
            {
                testHarness.failed("Division Name is incorrect");
            }
            else if (votes.getIdDivision() != 1)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else if (! votes.getState().equals("a"))
            {
                testHarness.failed("State is incorrect");
            }
            else if (! votes.getAbvParty().equals("a"))
            {
                testHarness.failed("Party Abbreviation is incorrect");
            }
            else if (! votes.getNameParty().equals("a"))
            {
                testHarness.failed("Party Name is incorrect");
            }
            else if (votes.getVotesFor() != 1)
            {
                testHarness.failed("Votes For is incorrect");
            }
            else if (votes.getVotesAgainst() != 2)
            {
                testHarness.failed("Votes Against is incorrect");
            }
            else if (votes.getVotesTotal() != 3)
            {
                testHarness.failed("Votes Total is incorrect");
            }
            else if (Math.abs(votes.getMargin() - -16.666666666) > 0.0000001)
            {
                testHarness.failed("Margin is incorrect");
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
        VoteStats votes;

        System.out.print("Testing setNameDivision(): ");
        try
        {
            votes = new VoteStats();

            votes.setNameDivision("asdf");

            if (! votes.getNameDivision().equals("asdf"))
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

        System.out.print("Testing setIdDivision(): ");

        try
        {
            votes = new VoteStats();

            votes.setIdDivision("4");

            if (votes.getIdDivision() != 4)
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

        System.out.print("Testing setState(): ");

        try
        {
            votes = new VoteStats();

            votes.setState("A");

            if (! votes.getState().equals("A"))
            {
                testHarness.failed("State is set incorrectly");
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

        System.out.print("Testing setAbvParty(): ");

        try
        {
            votes = new VoteStats();

            votes.setAbvParty("A");

            if (! votes.getAbvParty().equals("A"))
            {
                testHarness.failed("Party Abbreviation is set incorrectly");
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

        System.out.print("Testing setNameParty(): ");

        try
        {
            votes = new VoteStats();

            votes.setNameParty("A");

            if (! votes.getNameParty().equals("A"))
            {
                testHarness.failed("Party Name is set incorrectly");
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

        System.out.print("Testing setVotesFor(): ");

        try
        {
            votes = new VoteStats();

            votes.setVotesFor(1);

            if (votes.getVotesFor() != 1)
            {
                testHarness.failed("Votes For is set incorrectly");
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

        System.out.print("Testing setVotesAgainst(): ");

        try
        {
            votes = new VoteStats();

            votes.setVotesAgainst(1);

            if (votes.getVotesAgainst() != 1)
            {
                testHarness.failed("Votes Against is set incorrectly");
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

        System.out.print("Testing setVotesTotal(): ");

        try
        {
            votes = new VoteStats();

            votes.setVotesTotal(1);

            if (votes.getVotesTotal() != 1)
            {
                testHarness.failed("Votes Total is set incorrectly");
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
