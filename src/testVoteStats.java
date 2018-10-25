public class testVoteStats
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing VoteStats class");
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
                testHarnessCommons.failed("Division Name is incorrect");
            }
            else if (votes.getIdDivision() != 0)
            {
                testHarnessCommons.failed("Division ID is incorrect");
            }
            else if (! votes.getState().equals("TBD"))
            {
                testHarnessCommons.failed("State is incorrect");
            }
            else if (! votes.getAbvParty().equals("TBD"))
            {
                testHarnessCommons.failed("Party Abbreviation is incorrect");
            }
            else if (! votes.getNameParty().equals("TBD"))
            {
                testHarnessCommons.failed("Party Name is incorrect");
            }
            else if (votes.getVotesFor() != 0)
            {
                testHarnessCommons.failed("Votes For is incorrect");
            }
            else if (votes.getVotesAgainst() != 0)
            {
                testHarnessCommons.failed("Votes Against is incorrect");
            }
            else if (votes.getVotesTotal() != 0)
            {
                testHarnessCommons.failed("Votes Total is incorrect");
            }
            else if (votes.getMargin() != 0.0)
            {
                testHarnessCommons.failed("Margin is incorrect");
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
            votes = new VoteStats("a", "1", "a", "a", "a", 1, 2, 3);

            if (! votes.getNameDivision().equals("a"))
            {
                testHarnessCommons.failed("Division Name is incorrect");
            }
            else if (votes.getIdDivision() != 1)
            {
                testHarnessCommons.failed("Division ID is incorrect");
            }
            else if (! votes.getState().equals("a"))
            {
                testHarnessCommons.failed("State is incorrect");
            }
            else if (! votes.getAbvParty().equals("a"))
            {
                testHarnessCommons.failed("Party Abbreviation is incorrect");
            }
            else if (! votes.getNameParty().equals("a"))
            {
                testHarnessCommons.failed("Party Name is incorrect");
            }
            else if (votes.getVotesFor() != 1)
            {
                testHarnessCommons.failed("Votes For is incorrect");
            }
            else if (votes.getVotesAgainst() != 2)
            {
                testHarnessCommons.failed("Votes Against is incorrect");
            }
            else if (votes.getVotesTotal() != 3)
            {
                testHarnessCommons.failed("Votes Total is incorrect");
            }
            else if (Math.abs(votes.getMargin() - -16.666666666) > 0.0000001)
            {
                testHarnessCommons.failed("Margin is incorrect");
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
        VoteStats votes;

        System.out.print("Testing setNameDivision(): ");
        try
        {
            votes = new VoteStats();

            votes.setNameDivision("asdf");

            if (! votes.getNameDivision().equals("asdf"))
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

        System.out.print("Testing setIdDivision(): ");

        try
        {
            votes = new VoteStats();

            votes.setIdDivision("4");

            if (votes.getIdDivision() != 4)
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

        System.out.print("Testing setState(): ");

        try
        {
            votes = new VoteStats();

            votes.setState("A");

            if (! votes.getState().equals("A"))
            {
                testHarnessCommons.failed("State is set incorrectly");
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

        System.out.print("Testing setAbvParty(): ");

        try
        {
            votes = new VoteStats();

            votes.setAbvParty("A");

            if (! votes.getAbvParty().equals("A"))
            {
                testHarnessCommons.failed("Party Abbreviation is set incorrectly");
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

        System.out.print("Testing setNameParty(): ");

        try
        {
            votes = new VoteStats();

            votes.setNameParty("A");

            if (! votes.getNameParty().equals("A"))
            {
                testHarnessCommons.failed("Party Name is set incorrectly");
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

        System.out.print("Testing setVotesFor(): ");

        try
        {
            votes = new VoteStats();

            votes.setVotesFor(1);

            if (votes.getVotesFor() != 1)
            {
                testHarnessCommons.failed("Votes For is set incorrectly");
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

        System.out.print("Testing setVotesAgainst(): ");

        try
        {
            votes = new VoteStats();

            votes.setVotesAgainst(1);

            if (votes.getVotesAgainst() != 1)
            {
                testHarnessCommons.failed("Votes Against is set incorrectly");
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

        System.out.print("Testing setVotesTotal(): ");

        try
        {
            votes = new VoteStats();

            votes.setVotesTotal(1);

            if (votes.getVotesTotal() != 1)
            {
                testHarnessCommons.failed("Votes Total is set incorrectly");
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
