public class testHousePreference
{
    public static void run()
    {
        testHarness.header("Testing House Preference Class");
        testConstructor();
    }

    public static void testConstructor()
    {
        HousePreference pref;
        System.out.print("Testing Default Constructor: ");

        try
        {
            pref = new HousePreference();

            if (! pref.getState().equals("TBD"))
            {
                testHarness.failed("State is incorrect");
            }
            else if (pref.getIdDivision() != 0)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else if (! pref.getNameDivision().equals("TBD"))
            {
                testHarness.failed("Division Name is incorrect");
            }
            else if (pref.getIdPollingPlace() != 0)
            {
                testHarness.failed("Polling Place ID is incorrect");
            }
            else if (! pref.getNamePollingPlace().equals("TBD"))
            {
                testHarness.failed("Polling Place Name is incorrect");
            }
            else if (pref.getIdCandidate() != 0)
            {
                testHarness.failed("Candidate ID is incorrect");
            }
            else if (! pref.getSurname().equals("TBD"))
            {
                testHarness.failed("Surname is incorrect");
            }
            else if (! pref.getFirstName().equals("TBD"))
            {
                testHarness.failed("First Name is incorrect");
            }
            else if (pref.getBallotPosition() != 0)
            {
                testHarness.failed("Ballot Position is incorrect");
            }
            else if (pref.getElected())
            {
                testHarness.failed("Elected is incorrect");
            }
            else if (pref.getHistoricElected())
            {
                testHarness.failed("Historic Elected is incorrect");
            }
            else if (! pref.getAbvParty().equals("TBD"))
            {
                testHarness.failed("Party Abbreviation is incorrect");
            }
            else if (! pref.getNameParty().equals("TBD"))
            {
                testHarness.failed("Party Name is incorrect");
            }
            else if (pref.getOrdinaryVotes() != 0)
            {
                testHarness.failed("Ordinary Votes is incorrect");
            }
            else if (Math.abs(0.0 - pref.getSwing()) > 0.0000001)
            {
                testHarness.failed("Swing is incorrect");
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
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );

            if (! pref.getState().equals("WA"))
            {
                testHarness.failed("State is incorrect");
            }
            else if (pref.getIdDivision() != 235)
            {
                testHarness.failed("Division ID is incorrect");
            }
            else if (! pref.getNameDivision().equals("Brand"))
            {
                testHarness.failed("Division Name is incorrect");
            }
            else if (pref.getIdPollingPlace() != 7472)
            {
                testHarness.failed("Polling Place ID is incorrect");
            }
            else if (! pref.getNamePollingPlace().equals("Baldivis"))
            {
                testHarness.failed("Polling Place Name is incorrect");
            }
            else if (pref.getIdCandidate() != 28420)
            {
                testHarness.failed("Candidate ID is incorrect");
            }
            else if (! pref.getSurname().equals("SCOTT"))
            {
                testHarness.failed("Surname is incorrect");
            }
            else if (! pref.getFirstName().equals("Philip"))
            {
                testHarness.failed("First Name is incorrect");
            }
            else if (pref.getBallotPosition() != 1)
            {
                testHarness.failed("Ballot Position is incorrect");
            }
            else if (pref.getElected())
            {
                testHarness.failed("Elected is incorrect");
            }
            else if (pref.getHistoricElected())
            {
                testHarness.failed("Historic Elected is incorrect");
            }
            else if (! pref.getAbvParty().equals("RUA"))
            {
                testHarness.failed("Party Abbreviation is incorrect");
            }
            else if (! pref.getNameParty().equals("Rise Up Australia Party"))
            {
                testHarness.failed("Party Name is incorrect");
            }
            else if (pref.getOrdinaryVotes() != 176)
            {
                testHarness.failed("Ordinary Votes is incorrect");
            }
            else if (Math.abs(4.28 - pref.getSwing()) > 0.0000001)
            {
                testHarness.failed("Swing is incorrect");
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
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N," +
                "RUA,Rise Up Australia Party,176"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [all invalid]: "
        );

        try
        {
            pref = new HousePreference(
                ",,,,,,,,,,,,,,,,,,"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [state invalid]: "
        );

        try
        {
            pref = new HousePreference(
                ",235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
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
            pref = new HousePreference(
                "WA,asdf,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N," +
                "RUA,Rise Up Australia Party,176"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,asdf,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N," +
                    "RUA,Rise Up Australia Party,176"
                );
                testHarness.failed("Created a valid House Preference");
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
            pref = new HousePreference(
                "WA,235,,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [polling place id invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,asdf,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,235,Brand,,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                    "Rise Up Australia Party,176,4.28"
                );
                testHarness.failed("Created a valid House Preference");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print(
            "Testing Alternate Constructor [polling place invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,7472,,28420,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
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
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,asdf,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,235,Brand,7472,Baldivis,,SCOTT,Philip,1,N,N,RUA," +
                    "Rise Up Australia Party,176,4.28"
                );
                testHarness.failed("Created a valid House Preference");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print(
            "Testing Alternate Constructor [surname invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
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
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,,1,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [ballot position invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,a,N,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,,N,N,RUA," +
                    "Rise Up Australia Party,176,4.28"
                );
                testHarness.failed("Created a valid House Preference");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print(
            "Testing Alternate Constructor [elected invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,,N,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,a,N,RUA," +
                    "Rise Up Australia Party,176,4.28"
                );
                testHarness.failed("Created a valid House Preference");
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
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,,RUA," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,a,RUA," +
                    "Rise Up Australia Party,176,4.28"
                );
                testHarness.failed("Created a valid House Preference");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print(
            "Testing Alternate Constructor [party abv invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,," +
                "Rise Up Australia Party,176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
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
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                ",176,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            testHarness.passed();
        }

        System.out.print(
            "Testing Alternate Constructor [ordinary votes invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,,4.28"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                    "Rise Up Australia Party,asdf,4.28"
                );
                testHarness.failed("Created a valid House Preference");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }

        System.out.print(
            "Testing Alternate Constructor [swing invalid]: "
        );

        try
        {
            pref = new HousePreference(
                "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                "Rise Up Australia Party,176,"
            );
            testHarness.failed("Created a valid House Preference");
        }
        catch (Exception e)
        {
            try
            {
                pref = new HousePreference(
                    "WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
                    "Rise Up Australia Party,176,asdf"
                );
                testHarness.failed("Created a valid House Preference");
            }
            catch (Exception ex)
            {
                testHarness.passed();
            }
        }
    }
}
