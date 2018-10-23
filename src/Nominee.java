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

    public void setState(String input)
    {
        if (! validateString(input))
        {
            state = DEFAULT;
        }
        else
        {
            state = input;
        }
    }

    public void setIdDivision(String input)
    {
        if (! validateInt(input))
        {
            idDivision = 0;
        }
        else
        {
            idDivision = Integer.parseInt(input);
        }
    }

    public void setNameDivision(String input)
    {
        if (! validateString(input))
        {
            nameDivision = DEFAULT;
        }
        else
        {
            nameDivision = input;
        }
    }

    public void setAbvParty(String input)
    {
        if (! validateString(input))
        {
            abvParty = DEFAULT;
        }
        else
        {
            abvParty = input;
        }
    }

    public void setNameParty(String input)
    {
        if (! validateString(input))
        {
            nameParty = DEFAULT;
        }
        else
        {
            nameParty = input;
        }
    }

    public void setIdCandidate(String input)
    {
        if (! validateInt(input))
        {
            idCandidate = 0;
        }
        else
        {
            idCandidate = Integer.parseInt(input);
        }
    }

    public void setSurname(String input)
    {
        if (! validateString(input))
        {
            surname = DEFAULT;
        }
        else
        {
            surname = input;
        }
    }

    public void setFirstName(String input)
    {
        if (! validateString(input))
        {
            firstname = DEFAULT;
        }
        else
        {
            firstname = input;
        }
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
        return state + "," + idDivision + "," + nameDivision + "," +
               abvParty + "," + nameParty + "," + idCandidate + "," +
               surname + "," + firstname + "," + 
               (elected ? "Y" : "N") + "," +
               (historicElected ? "Y" : "N");
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
