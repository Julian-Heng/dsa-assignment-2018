/**
 *  Name:     testNominee
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testNominee
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing Nominee Class");
        testConstructor();
    }

    public static void testConstructor()
    {
        Nominee candidate;
        System.out.print("Testing Default Constructor: ");

        try
        {
            candidate = new Nominee();

            if (! candidate.getState().equals("TBD"))
            {
                testHarnessCommons.failed("State is incorrect");
            }
            else if (candidate.getIdDivision() != 0)
            {
                testHarnessCommons.failed("Division ID is incorrect");
            }
            else if (! candidate.getNameDivision().equals("TBD"))
            {
                testHarnessCommons.failed("Division Name is incorrect");
            }
            else if (! candidate.getAbvParty().equals("TBD"))
            {
                testHarnessCommons.failed("Party Abbreviation is incorrect");
            }
            else if (! candidate.getNameParty().equals("TBD"))
            {
                testHarnessCommons.failed("Party Name is incorrect");
            }
            else if (candidate.getIdCandidate() != 0)
            {
                testHarnessCommons.failed("Candidate ID is incorrect");
            }
            else if (! candidate.getSurname().equals("TBD"))
            {
                testHarnessCommons.failed("Surname is incorrect");
            }
            else if (! candidate.getFirstName().equals("TBD"))
            {
                testHarnessCommons.failed("First Name is incorrect");
            }
            else if (candidate.getElected())
            {
                testHarnessCommons.failed("Elected is incorrect");
            }
            else if (candidate.getHistoricElected())
            {
                testHarnessCommons.failed("Historic Elected is incorrect");
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

        System.out.print("Testing Alternate Constructor [valid]: ");

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );

            if (! candidate.getState().equals("NSW"))
            {
                testHarnessCommons.failed("State is incorrect");
            }
            else if (candidate.getIdDivision() != 151)
            {
                testHarnessCommons.failed("Division ID is incorrect");
            }
            else if (! candidate.getNameDivision().equals("Warringah"))
            {
                testHarnessCommons.failed("Division Name is incorrect");
            }
            else if (! candidate.getAbvParty().equals("LP"))
            {
                testHarnessCommons.failed("Party Abbreviation is incorrect");
            }
            else if (! candidate.getNameParty().equals("Liberal"))
            {
                testHarnessCommons.failed("Party Name is incorrect");
            }
            else if (candidate.getIdCandidate() != 28624)
            {
                testHarnessCommons.failed("Candidate ID is incorrect");
            }
            else if (! candidate.getSurname().equals("ABBOTT"))
            {
                testHarnessCommons.failed("Surname is incorrect");
            }
            else if (! candidate.getFirstName().equals("Tony"))
            {
                testHarnessCommons.failed("First Name is incorrect");
            }
            else if (! candidate.getElected())
            {
                testHarnessCommons.failed("Elected is incorrect");
            }
            else if (! candidate.getHistoricElected())
            {
                testHarnessCommons.failed("Historic Elected is incorrect");
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

        System.out.print(
            "Testing Alternate Constructor [not enough inputs]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y"
            );
            testHarnessCommons.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarnessCommons.passed();
        }

        System.out.print("Testing Alternate Constructor [all invalid]: ");

        try
        {
            candidate = new Nominee(",,,,,,,,,");
            testHarnessCommons.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarnessCommons.passed();
        }

        System.out.print("Testing Alternate Constructor [state invalid]: ");

        try
        {
            candidate = new Nominee(
                ",151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );

            if (! candidate.getState().equals("TBD"))
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

        System.out.print(
            "Testing Alternate Constructor [id Division invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,asdf,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );

            if (candidate.getIdDivision() != 0)
            {
                throw new IllegalArgumentException(
                    "Division ID is set incorrectly"
                );
            }

            candidate = new Nominee(
                "NSW,,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );

            if (candidate.getIdDivision() != 0)
            {
                throw new IllegalArgumentException(
                    "Division ID is set incorrectly"
                );
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

        System.out.print(
            "Testing Alternate Constructor [division name invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );

            if (! candidate.getNameDivision().equals("TBD"))
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

        System.out.print(
            "Testing Alternate Constructor [party abv invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,,Liberal,28624,ABBOTT,Tony,Y,Y"
            );

            if (! candidate.getAbvParty().equals("TBD"))
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

        System.out.print(
            "Testing Alternate Constructor [party name invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,,28624,ABBOTT,Tony,Y,Y"
            );

            if (! candidate.getNameParty().equals("TBD"))
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

        System.out.print(
            "Testing Alternate Constructor [candidate id invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,asdf,ABBOTT,Tony,Y,Y"
            );

            if (candidate.getIdCandidate() != 0)
            {
                throw new IllegalArgumentException(
                    "Candidate ID is set incorrectly"
                );
            }

            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,,ABBOTT,Tony,Y,Y"
            );

            if (candidate.getIdCandidate() != 0)
            {
                testHarnessCommons.failed("Candidate ID is set incorrectly");
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

        System.out.print("Testing Alternate Constructor [surname invalid]: ");

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,,Tony,Y,Y"
            );

            if (! candidate.getSurname().equals("TBD"))
            {
                testHarnessCommons.failed("Surname is set incorrectly");
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

        System.out.print(
            "Testing Alternate Constructor [firstname invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,,Y,Y"
            );

            if (! candidate.getFirstName().equals("TBD"))
            {
                testHarnessCommons.failed("First Name is set incorrectly");
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

        System.out.print("Testing Alternate Constructor [elected invalid]: ");

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,,Y"
            );

            if (candidate.getElected())
            {
                throw new IllegalArgumentException(
                    "Elected is set incorrectly"
                );
            }

            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,asdf,Y"
            );

            testHarnessCommons.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarnessCommons.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [historic elected invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,"
            );

            if (candidate.getElected())
            {
                throw new IllegalArgumentException(
                    "Historic Elected is set incorrectly"
                );
            }

            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,asdf"
            );

            testHarnessCommons.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarnessCommons.passed();
        }
    }
}
