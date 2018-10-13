public class testNominee
{
    public static void run()
    {
        testHarness.header("Testing Nominee Class");
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
                testHarness.failed("State is incorrect");
            }
            else if (candidate.getIdDivision() != 0)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else if (! candidate.getNameDivision().equals("TBD"))
            {
                testHarness.failed("Division Name is incorrect");
            }
            else if (! candidate.getAbvParty().equals("TBD"))
            {
                testHarness.failed("Party Abbreviation is incorrect");
            }
            else if (! candidate.getNameParty().equals("TBD"))
            {
                testHarness.failed("Party Name is incorrect");
            }
            else if (candidate.getIdCandidate() != 0)
            {
                testHarness.failed("Candidate ID is incorrect");
            }
            else if (! candidate.getSurname().equals("TBD"))
            {
                testHarness.failed("Surname is incorrect");
            }
            else if (! candidate.getFirstName().equals("TBD"))
            {
                testHarness.failed("First Name is incorrect");
            }
            else if (candidate.getElected())
            {
                testHarness.failed("Elected is incorrect");
            }
            else if (candidate.getHistoricElected())
            {
                testHarness.failed("Historic Elected is incorrect");
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

        System.out.print("Testing Alternate Constructor [valid]: ");

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );

            if (! candidate.getState().equals("NSW"))
            {
                testHarness.failed("State is incorrect");
            }
            else if (candidate.getIdDivision() != 151)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else if (! candidate.getNameDivision().equals("Warringah"))
            {
                testHarness.failed("Division Name is incorrect");
            }
            else if (! candidate.getAbvParty().equals("LP"))
            {
                testHarness.failed("Party Abbreviation is incorrect");
            }
            else if (! candidate.getNameParty().equals("Liberal"))
            {
                testHarness.failed("Party Name is incorrect");
            }
            else if (candidate.getIdCandidate() != 28624)
            {
                testHarness.failed("Candidate ID is incorrect");
            }
            else if (! candidate.getSurname().equals("ABBOTT"))
            {
                testHarness.failed("Surname is incorrect");
            }
            else if (! candidate.getFirstName().equals("Tony"))
            {
                testHarness.failed("First Name is incorrect");
            }
            else if (! candidate.getElected())
            {
                testHarness.failed("Elected is incorrect");
            }
            else if (! candidate.getHistoricElected())
            {
                testHarness.failed("Historic Elected is incorrect");
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

        System.out.print(
            "Testing Alternate Constructor [not enough inputs]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print("Testing Alternate Constructor [all invalid]: ");

        try
        {
            candidate = new Nominee(",,,,,,,,,");
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print("Testing Alternate Constructor [state invalid]: ");

        try
        {
            candidate = new Nominee(
                ",151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [id Division invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,asdf,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            try
            {
                candidate = new Nominee(
                    "NSW,,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
                );
                testHarness.failed("Created a valid Nominee");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print(
            "Testing Alternate Constructor [division name invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,,LP,Liberal,28624,ABBOTT,Tony,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [party abv invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,,Liberal,28624,ABBOTT,Tony,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [party name invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,,28624,ABBOTT,Tony,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [candidate id invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,asdf,ABBOTT,Tony,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            try
            {
                candidate = new Nominee(
                    "NSW,151,Warringah,LP,Liberal,,ABBOTT,Tony,Y,Y"
                );
                testHarness.failed("Created a valid Nominee");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print("Testing Alternate Constructor [surname invalid]: ");

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,,Tony,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [firstname invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,,Y,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print("Testing Alternate Constructor [elected invalid]: ");

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,,Y"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            try
            {
                candidate = new Nominee(
                    "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,asdf,Y"
                );
                testHarness.failed("Created a valid Nominee");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print(
            "Testing Alternate Constructor [historic elected invalid]: "
        );

        try
        {
            candidate = new Nominee(
                "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,"
            );
            testHarness.failed("Created a valid Nominee");
        }
        catch (Exception e)
        {
            try
            {
                candidate = new Nominee(
                    "NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,asdf"
                );
                testHarness.failed("Created a valid Nominee");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }
    }
}
