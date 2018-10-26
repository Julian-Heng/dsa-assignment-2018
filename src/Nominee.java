/**
 *  Name:     Nominee
 *  Purpose:  Provide a class that contains a candidate or nominee details.
 *            Based on the house candidates file, it also stores the number
 *            of votes as well
 *
 *  Author:   Julian Heng (19473701)
 **/

public class Nominee
{
    private String state,
                   nameDivision,
                   abvParty,
                   nameParty,
                   surname,
                   firstname;
    private int idDivision, idCandidate, numVotes;
    private boolean elected, historicElected;

    public static final String DEFAULT = "TBD";

    // Default Constructor
    public Nominee()
    {
        state = DEFAULT;
        idDivision = 0;
        nameDivision = DEFAULT;
        abvParty = DEFAULT;
        nameParty = DEFAULT;
        idCandidate = 0;
        surname = DEFAULT;
        firstname = DEFAULT;
        elected = false;
        historicElected = false;
        numVotes = 0;
    }

    // Alternate Constructor
    public Nominee(String inEntry)
    {
        String[] splitString;

        if (! validateString(inEntry))
        {
            throw new IllegalArgumentException(
                "Nominee is invalid"
            );
        }
        else
        {
            // Expecting the format:
            //   StateAb, DivisionID, DivisionNm, PartyAb, PartyNm
            //   CandidateID, Surname, GivenNm, Elected, HistoricElected

            splitString = inEntry.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            //splitString = inEntry.split(",");

            if (splitString.length != 10)
            {
                throw new IllegalArgumentException(
                    "Not enough data for Nominee"
                );
            }
            else
            {
                this.setState(splitString[0]);
                this.setIdDivision(splitString[1]);
                this.setNameDivision(splitString[2]);
                this.setAbvParty(splitString[3]);
                this.setNameParty(splitString[4]);
                this.setIdCandidate(splitString[5]);
                this.setSurname(splitString[6]);
                this.setFirstName(splitString[7]);
                this.setElected(splitString[8]);
                this.setHistoricElected(splitString[9]);
                this.setNumVotes("0");
            }
        }
    }

    // Setters
    public void setState(String input)
    {
        state = (! validateString(input)) ? DEFAULT : input;
    }

    public void setIdDivision(String input)
    {
        idDivision = (! validateInt(input)) ? 0 : Integer.parseInt(input);
    }

    public void setNameDivision(String input)
    {
        nameDivision = (! validateString(input)) ? DEFAULT : input;
    }

    public void setAbvParty(String input)
    {
        abvParty = (! validateString(input)) ? DEFAULT : input;
    }

    public void setNameParty(String input)
    {
        nameParty = (! validateString(input)) ? DEFAULT : input;
    }

    public void setIdCandidate(String input)
    {
        idCandidate = (! validateInt(input)) ? 0 : Integer.parseInt(input);
    }

    public void setSurname(String input)
    {
        surname = (! validateString(input)) ? DEFAULT : input;
    }

    public void setFirstName(String input)
    {
        firstname = (! validateString(input)) ? DEFAULT : input;
    }

    public void setElected(String input)
    {
        if (! validateString(input))
        {
            elected = false;
        }
        else
        {
            if (input.equals("y") || input.equals("Y"))
            {
                elected = true;
            }
            else if (input.equals("n") || input.equals("N"))
            {
                elected = false;
            }
            else
            {
                throw new IllegalArgumentException(
                    "Historic Elected is invalid"
                );
            }
        }
    }

    public void setHistoricElected(String input)
    {
        if (! validateString(input))
        {
            historicElected = false;
        }
        else
        {
            if (input.equals("y") || input.equals("Y"))
            {
                historicElected = true;
            }
            else if (input.equals("n") || input.equals("N"))
            {
                historicElected = false;
            }
            else
            {
                throw new IllegalArgumentException(
                    "Historic Elected is invalid"
                );
            }
        }
    }

    public void setNumVotes(String input)
    {
        if (! validateString(input))
        {
            throw new IllegalArgumentException(
                "Number of Votes is invalid"
            );
        }
        else
        {
            numVotes = Integer.parseInt(input);
        }
    }

    public void addNumVotes(String strVotes)
    {
        if (! validateInt(strVotes))
        {
            throw new IllegalArgumentException(
                "Number of additional Votes is invalid"
            );
        }
        else
        {
            numVotes += Integer.parseInt(strVotes);
        }
    }

    // Getters
    public String  getState()           { return state; }
    public int     getIdDivision()      { return idDivision; }
    public String  getNameDivision()    { return nameDivision; }
    public String  getAbvParty()        { return abvParty; }
    public String  getNameParty()       { return nameParty; }
    public int     getIdCandidate()     { return idCandidate; }
    public String  getSurname()         { return surname; }
    public String  getFirstName()       { return firstname; }
    public boolean getElected()         { return elected; }
    public boolean getHistoricElected() { return historicElected; }
    public int     getNumVotes()        { return numVotes; }

    public String toString()
    {
        return  String.format(
                    "%s,%d,%s,%s,%s,%d,%s,%s,%s,%s",
                    state, idDivision, nameDivision, abvParty,
                    nameParty, idCandidate, surname, firstname,
                    (elected ? "Y" : "N"), (historicElected ? "Y" : "N")
                );
    }

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
}
