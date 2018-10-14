public class HousePreference
{
    private String prefNameDivision;
    private int prefIdDivision;
    private DSALinkedList<Nominee> listNominee;

    public static final String DEFAULT = "TBD";

    public HousePreference()
    {
        prefNameDivision = DEFAULT;
        prefIdDivision = 0;
        listNominee = new DSALinkedList<Nominee>();
    }

    public HousePreference(String inNameDivision, String inIdDivision)
    {
        setPrefNameDivision(inNameDivision);
        setPrefIdDivision(inIdDivision);
        listNominee = new DSALinkedList<Nominee>();
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
