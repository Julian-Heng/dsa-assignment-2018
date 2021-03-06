/**
 *  Name:     VoteStats
 *  Purpose:  Provide a class that contains information for storing
 *            the number of votes one particular party has against
 *            other parties within a division
 *
 *  Author:   Julian Heng (19473701)
 **/

public class VoteStats
{
    String nameDivision, state;
    int idDivision;
    String abvParty, nameParty;
    int votesFor, votesAgainst, votesTotal;
    double margin;

    public static final String DEFAULT = "TBD";

    // Default Constructor
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

    // Alternate Constructor
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

    // Setters
    public void setNameDivision(String inNameDivision)
    {
        nameDivision = (! validateString(inNameDivision)) ?
                            DEFAULT : inNameDivision;
    }

    public void setIdDivision(String inIdDivision)
    {
        idDivision = (! validateInt(inIdDivision)) ?
                            0 : Integer.parseInt(inIdDivision);
    }

    public void setState(String inState)
    {
        state = (! validateString(inState)) ? DEFAULT : inState;
    }

    public void setAbvParty(String inAbvParty)
    {
        abvParty = (! validateString(inAbvParty)) ? DEFAULT : inAbvParty;
    }

    public void setNameParty(String inNameParty)
    {
        nameParty = (! validateString(inNameParty)) ? DEFAULT : inNameParty;
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

    // Getters
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

        if (votesTotal != 0)
        {
            percent = ((double)votesFor / (double)votesTotal) * 100;
        }
        else
        {
            throw new IllegalArgumentException(
                "Votes Total is 0, division by 0"
            );
        }

        return percent;
    }

    public String toString()
    {
        return  String.format(
                    "%d,%s,%s,%s,%s,%d,%d,%d,%3.2f%%,%2.4f",
                    idDivision, nameDivision, state, abvParty,
                    nameParty, votesFor, votesAgainst, votesTotal,
                    getPercent(), margin
                );
    }

    // Mutators
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
