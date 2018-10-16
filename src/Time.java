public class Time
{
    int hours, minutes, seconds;

    public Time(String timeStr)
    {
        String[] split;

        if (! validateString(timeStr))
        {
            throw new IllegalArgumentException(
                "Input time is invalid"
            );
        }
        else
        {
            split = timeStr.split(":");
            if (split.length != 3)
            {
                throw new IllegalArgumentException(
                    "Input time is invalid"
                );
            }
            else
            {
                this.setHours(split[0]);
                this.setMinutes(split[1]);
                this.setSeconds(split[2]);
            }
        }
    }

    public void setHours(String inHours)
    {
        int val;
        if (! validateInt(inHours))
        {
            throw new IllegalArgumentException(
                "Input hours is invalid"
            );
        }
        else
        {
            val = Integer.parseInt(inHours);

            if (val < 0)
            {
                throw new IllegalArgumentException(
                    "Input hours is invalid"
                );
            }
            else
            {
                hours = val;
            }
        }
    }

    public void setMinutes(String inMinutes)
    {
        if (! (validateInt(inMinutes) && checkBound(inMinutes, 0, 60)))
        {
            throw new IllegalArgumentException(
                "Input minutes is invalid"
            );
        }
        else
        {
            minutes = Integer.parseInt(inMinutes);
        }
    }

    public void setSeconds(String inSeconds)
    {
        if (! (validateInt(inSeconds) && checkBound(inSeconds, 0, 60)))
        {
            throw new IllegalArgumentException(
                "Input seconds is invalid"
            );
        }
        else
        {
            seconds = Integer.parseInt(inSeconds);
        }
    }

    public int getHours()   { return hours; }
    public int getMinutes() { return minutes; }
    public int getSeconds() { return seconds; }

    private boolean validateString(String str)
    {
        return (str != null && ! (str.isEmpty() || str.equals("")));
    }

    private boolean validateInt(String num)
    {
        boolean isValid;
        int val;

        try
        {
            val = Integer.parseInt(num);
            isValid = true;
        }
        catch (NumberFormatException e)
        {
            isValid = false;
        }

        return isValid;
    }

    private boolean checkBound(String num, int low, int up)
    {
        int val;
        val = Integer.parseInt(num);

        return ((val >= low) && (up >= val));
    }
}
