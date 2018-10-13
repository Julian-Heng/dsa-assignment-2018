public class HousePreference
{
    private Nominee candidate;
    private String namePolingPlace;
    private int idPollingPlace, ballotPos, ordinaryVotes;
    private double swing;

    public static final String DEFAULT = "TBD";

    public HousePreference()
    {
        candidate = new Nominee();
        idPollingPlace = 0;
        namePolingPlace = DEFAULT;
        ballotPos = 0;
        ordinaryVotes = 0;
        swing = 0.0;
    }

    public HousePreference(String inEntry)
    {
        String[] splitString;
        String[] nomineeEntry = new String[10];
        String nomineeLine;

        int indexForNominee[] = {0, 1, 2, 11, 12, 5, 6, 7, 9, 10};

        if (! validateString(inEntry))
        {
            throw new IllegalArgumentException(
                "House Preference is invalid"
            );
        }
        else
        {
            splitString = inEntry.split(",");

            if (splitString.length != 15)
            {
                throw new IllegalArgumentException(
                    "Not enough data for House Preference"
                );
            }
            else
            {
                nomineeLine = "";

                for (int i = 0; i < indexForNominee.length - 1; i++)
                {
                    nomineeLine += splitString[indexForNominee[i]] + ",";
                }
                nomineeLine += splitString[
                    indexForNominee[
                        indexForNominee.length - 1
                    ]
                ];

                candidate = new Nominee(nomineeLine);
                this.setIdPollingPlace(splitString[3]);
                this.setNamePollingPlace(splitString[4]);
                this.setBallotPosition(splitString[8]);
                this.setOrdinaryVotes(splitString[13]);
                this.setSwing(splitString[14]);
            }
        }
    }

    public void setIdPollingPlace(String str)
    {
        if (! validateInt(str))
        {
            throw new IllegalArgumentException(
                "Polling Place ID is invalid"
            );
        }
        else
        {
            idPollingPlace = Integer.parseInt(str);
        }
    }

    public void setNamePollingPlace(String str)
    {
        if (! validateString(str))
        {
            throw new IllegalArgumentException(
                "Polling Place Name is invalid"
            );
        }
        else
        {
            namePolingPlace = str;
        }
    }

    public void setBallotPosition(String str)
    {
        if (! validateInt(str))
        {
            throw new IllegalArgumentException(
                "Ballot Position is invalid"
            );
        }
        else
        {
            ballotPos = Integer.parseInt(str);
        }
    }

    public void setOrdinaryVotes(String str)
    {
        if (! validateInt(str))
        {
            throw new IllegalArgumentException(
                "Ordinary Votes is invalid"
            );
        }
        else
        {
            ordinaryVotes = Integer.parseInt(str);
        }
    }

    public void setSwing(String str)
    {
        if (! validateDouble(str))
        {
            throw new IllegalArgumentException(
                "Swing is invalid"
            );
        }
        else
        {
            swing = Double.parseDouble(str);
        }
    }

    public String  getState()            { return candidate.getState(); }
    public int     getIdDivision()       { return candidate.getIdDivision(); }
    public String  getNameDivision()     { return candidate.getNameDivision(); }
    public int     getIdPollingPlace()   { return idPollingPlace; }
    public String  getNamePollingPlace() { return namePolingPlace; }
    public int     getIdCandidate()      { return candidate.getIdCandidate(); }
    public String  getSurname()          { return candidate.getSurname(); }
    public String  getFirstName()        { return candidate.getFirstName(); }
    public int     getBallotPosition()   { return ballotPos; }
    public boolean getElected()          { return candidate.getElected(); }
    public boolean getHistoricElected()
    {
        return candidate.getHistoricElected();
    }
    public String getAbvParty()      { return candidate.getAbvParty(); }
    public String getNameParty()     { return candidate.getNameParty(); }
    public int    getOrdinaryVotes() { return ordinaryVotes; }
    public double getSwing()         { return swing; }

    private boolean validateString(String str)
    {
        return (str != null && ! (str.isEmpty() || str.equals("")));
    }

    private boolean validateInt(String str)
    {
        boolean isValid;
        int intValue;

        try
        {
            intValue = Integer.parseInt(str);
            isValid = true;
        }
        catch (NumberFormatException e)
        {
            isValid = false;
        }

        return isValid;
    }

    private boolean validateDouble(String str)
    {
        boolean isValid;
        double doubleValue;

        try
        {
            doubleValue = Double.parseDouble(str);
            isValid = true;
        }
        catch (NumberFormatException e)
        {
            isValid = false;
        }

        return isValid;
    }
}
