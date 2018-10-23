public class HousePreference
{
    private String prefNameDivision;
    private int prefIdDivision;
    private DSALinkedList<Nominee> listNominee;
    private int numInformalVotes, numTotalVotes;

    public static final String DEFAULT = "TBD";

    public HousePreference()
    {
        prefNameDivision = DEFAULT;
        prefIdDivision = 0;
        listNominee = new DSALinkedList<Nominee>();
        numInformalVotes = 0;
        numTotalVotes = 0;
    }

    public HousePreference(String inNameDivision, String inIdDivision)
    {
        setPrefNameDivision(inNameDivision);
        setPrefIdDivision(inIdDivision);
        listNominee = new DSALinkedList<Nominee>();
        numInformalVotes = 0;
        numTotalVotes = 0;
    }

    public void setPrefNameDivision(String inNameDivision)
    {
        if (! validateString(inNameDivision))
        {
            throw new IllegalArgumentException(
                "Preference Division Name is invalid"
            );
        }
        else
        {
            prefNameDivision = inNameDivision;
        }
    }

    public void setPrefIdDivision(String inIdDivision)
    {
        if (! validateInt(inIdDivision))
        {
            throw new IllegalArgumentException(
                "Preference Division ID is invalid"
            );
        }
        else
        {
            prefIdDivision = Integer.parseInt(inIdDivision);
        }
    }

    public void setNumInformalVotes(String inNumInformalVotes)
    {
        if (! validateInt(inNumInformalVotes))
        {
            throw new IllegalArgumentException(
                "Number of Informal Votes invalid"
            );
        }
        else
        {
            numInformalVotes = Integer.parseInt(inNumInformalVotes);
        }
    }

    public void setNumTotalVotes(String inNumTotalVotes)
    {
        if (! validateInt(inNumTotalVotes))
        {
            throw new IllegalArgumentException(
                "Number of Total Votes invalid"
            );
        }
        else
        {
            numTotalVotes = Integer.parseInt(inNumTotalVotes);
        }
    }

    public void addNumInformalVotes(String strVotes)
    {
        if (! validateInt(strVotes))
        {
            throw new IllegalArgumentException(
                "Number of additional Votes invalid"
            );
        }
        else
        {
            numInformalVotes += Integer.parseInt(strVotes);
        }
    }

    public void addNomineeToList(Nominee inNominee)
    {
        if (! validateObject(inNominee))
        {
            throw new IllegalArgumentException(
                "Nominee is invalid"
            );
        }
        else
        {
            listNominee.insertLast(inNominee);
        }
    }

    public void updateTotalVotes()
    {
        numTotalVotes = numInformalVotes;

        for (Nominee i : listNominee)
        {
            numTotalVotes += i.getNumVotes();
        }
    }

    public String getPrefNameDivision() { return prefNameDivision; }
    public int getPrefIdDivision() { return prefIdDivision; }
    public DSALinkedList<Nominee> getListNominee() { return listNominee; }
    public int getNumInformalVotes() { return numInformalVotes; }
    public int getNumTotalVotes() { return numTotalVotes; }

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

    private boolean validateObject(Object inObj)
    {
        return (inObj != null);
    }
}
