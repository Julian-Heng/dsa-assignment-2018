public class VoteStats
{
    String nameDivision, state;
    int idDivision;
    String abvParty, nameParty;
    int votesFor, votesAgainst, votesTotal;
    double margin;

    public static final String DEFAULT = "TBD";

    public VoteStats()
    {
        nameDivision = DEFAULT;
        idDivision = 0;
        state = DEFAULT;
        abvParty = DEFAULT;
        nameParty = DEFAULT;
        votesFor = 0;
        votesAgainst = 0;
        votesTotal = 0;
        margin = 0.0;
    }

    public VoteStats(
        String inNameDivision,
        String inIdDivision,
        String inState,
        String inAbvParty,
        String inNameParty,
        int inFor,
        int inAgainst,
        int inTotal)
    {
        this.setNameDivision(inNameDivision);
        this.setIdDivision(inIdDivision);
        this.setState(inState);
        this.setAbvParty(inAbvParty);
        this.setNameParty(inNameParty);
        this.setVotesFor(inFor);
        this.setVotesAgainst(inAgainst);
        this.setVotesTotal(inTotal);
        this.calculateMargin();
    }

    public void setNameDivision(String inNameDivision)
    {
        if (! validateString(inNameDivision))
        {
            nameDivision = DEFAULT;
        }
        else
        {
            nameDivision = inNameDivision;
        }
    }

    public void setIdDivision(String inIdDivision)
    {
        if (! validateInt(inIdDivision))
        {
            idDivision = 0;
        }
        else
        {
            idDivision = Integer.parseInt(inIdDivision);
        }
    }

    public void setState(String inState)
    {
        if (! validateString(inState))
        {
            state = DEFAULT;
        }
        else
        {
            state = inState;
        }
    }

    public void setAbvParty(String inAbvParty)
    {
        if (! validateString(inAbvParty))
        {
            abvParty = DEFAULT;
        }
        else
        {
            abvParty = inAbvParty;
        }
    }

    public void setNameParty(String inNameParty)
    {
        if (! validateString(inNameParty))
        {
            nameParty = DEFAULT;
        }
        else
        {
            nameParty = inNameParty;
        }
    }

    public void setVotesFor(int inFor)
    {
        if (inFor < 0)
        {
            throw new IllegalArgumentException(
                "Votes For is negative"
            );
        }
        else
        {
            votesFor = inFor;
        }
    }

    public void setVotesAgainst(int inAgainst)
    {
        if (inAgainst < 0)
        {
            throw new IllegalArgumentException(
                "Votes Against is negative"
            );
        }
        else
        {
            votesAgainst = inAgainst;
        }
    }

    public void setVotesTotal(int inTotal)
    {
        if (inTotal < 0)
        {
            throw new IllegalArgumentException(
                "Votes Total is negative"
            );
        }
        else
        {
            votesTotal = inTotal;
        }
    }

    public String getNameDivision() { return nameDivision; }
    public int getIdDivision() { return idDivision; }
    public String getState() { return state; }
    public String getAbvParty() { return abvParty; }
    public String getNameParty() { return nameParty; }
    public int getVotesFor() { return votesFor; }
    public int getVotesAgainst() { return votesAgainst; }
    public int getVotesTotal() { return votesTotal; }
    public double getMargin() { return margin; }
    public double getPercent()
    {
        double percent;

        percent = 0.0;

        if (votesTotal != 0)
        {
            percent = ((double)votesFor / (double)votesTotal) * 100;
        }

        return percent;
    }

    public String toString()
    {
        return idDivision + "," + nameDivision + "," +
               state + "," +
               abvParty + "," + nameParty + "," +
               votesFor + "," + votesAgainst + "," +
               votesTotal + "," +
               String.format("%3.2f", getPercent()) + "%," +
               String.format("%2.4f", margin);
    }

    private void calculateMargin()
    {
        margin = this.getPercent() - 50.0;
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
