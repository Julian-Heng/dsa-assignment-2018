/**
 *  Name:     Location
 *  Purpose:  Provide a class that contains information for storing
 *            the attributes for a location. Used as the object stored
 *            in the verticies of the graph
 *
 *  Author:   Julian Heng (19473701)
 **/

public class Location
{
    private String state, division;
    private double latitude, longitude;

    public static final String DEFAULT = "TBD";

    // Default Constructor
    public Location()
    {
        state = DEFAULT;
        division = DEFAULT;
        latitude = 0.0;
        longitude = 0.0;
    }

    // Alternate Constructor
    public Location(
        String inState,
        String inDivision,
        String inLat,
        String inLong)
    {
        this.setState(inState);
        this.setDivision(inDivision);
        this.setLatitude(inLat);
        this.setLongitude(inLong);
    }

    // Setters
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

    public void setDivision(String inDivision)
    {
        if (! validateString(inDivision))
        {
            division = DEFAULT;
        }
        else
        {
            division = inDivision;
        }
    }

    public void setLatitude(String inLat)
    {
        if (! (validateDouble(inLat) && checkBound(inLat, -90, 90)))
        {
            latitude = 0.0;
        }
        else
        {
            latitude = Double.parseDouble(inLat);
        }
    }

    public void setLongitude(String inLong)
    {
        if (! (validateDouble(inLong) && checkBound(inLong, -180, 180)))
        {
            longitude = 0.0;
        }
        else
        {
            longitude = Double.parseDouble(inLong);
        }
    }

    // Getters
    public String getState()     { return state; }
    public String getDivision()  { return division; }
    public double getLatitude()  { return latitude; }
    public double getLongitude() { return longitude; }

    private boolean checkBound(String num, double low, double up)
    {
        double val = Double.parseDouble(num);
        return ((val > low || doubleCompare(val, low)) &&
                (val < up  || doubleCompare(val, up)));
    }

    private boolean doubleCompare(double num1, double num2)
    {
        return (Math.abs(num1 - num2) < 0.000000001);
    }

    private boolean validateString(String str)
    {
        return (str != null && ! (str.isEmpty() || str.equals("")));
    }

    private boolean validateDouble(String num)
    {
        boolean isValid;
        double val;

        try
        {
            val = Double.parseDouble(num);
            isValid = true;
        }
        catch (NumberFormatException e)
        {
            isValid = false;
        }

        return isValid;
    }
}
