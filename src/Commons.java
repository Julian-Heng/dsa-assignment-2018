import java.util.*;

/**
 *  Name:     Commons
 *  Purpose:  Provide common/generic utilities and functions
 *            to other classes
 *
 *  Author:   Julian Heng (19473701)
 **/

public class Commons
{
    // Regex String constants
    // Obtained from:
    //  https://stackoverflow.com/questions/6542996/how-to-split-csv-whose-columns-may-contain
    //  https://stackoverflow.com/a/49670696
    public static final
        String SPLIT_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    public static final
        String WHITESPACE_REGEX = "\\s+";

    /**
     *  Name:     convertTimeToString
     *  Purpose:  Convert seconds to day, hours, minutes and seconds format
     *  Imports:
     *    - totalSecs : An integer for the total number of seconds
     *  Exports:
     *    - none
     **/

    public static String convertTimeToString(int totalSecs)
    {
        String out;
        int days, hours, mins, secs;

        // Calculate days, hours, minutes and seconds
        days = (totalSecs / 3600) / 24;
        hours = (totalSecs / 3600) % 24;
        mins = (totalSecs / 60) % 60;
        secs = (totalSecs % 60) % 60;

        // Format the string
        out = String.format(
                "%d day%s, %d hour%s, %d min%s, %d sec%s",
                days,  (days > 1)  ? "s" : "",
                hours, (hours > 1) ? "s" : "",
                mins,  (mins > 1)  ? "s" : "",
                secs,  (secs > 1)  ? "s" : ""
        );

        return out;
    }

    /**
     *  Name:     printCsvTable
     *  Purpose:  Print a csv formatted string array into a table
     *  Imports:
     *    - csvArr : The csv array
     *    - header : The header of the csv table
     *  Exports:
     *    - none
     **/

    public static void printCsvTable(
        String[] csvArr,
        String[] header)
    {
        String padding, headerStr, sep, out;

        String[][] fields = new String[csvArr.length][header.length];
        String[] tempArr;
        String table;

        int[] paddingArr;

        fields = new String[csvArr.length][header.length];
        tempArr = new String[csvArr.length + 1];

        padding = "";
        headerStr = "";
        sep = "";
        out = "";

        paddingArr = new int[header.length];

        // Split csv into a 2D String array
        for (int i = 0; i < csvArr.length; i++)
        {
            fields[i] = csvArr[i].split(SPLIT_REGEX);
        }

        // Get the maximum string length for each column
        for (int i = 0; i < paddingArr.length; i++)
        {
            tempArr[0] = header[i];

            for (int j = 0; j < csvArr.length; j++)
            {
                tempArr[j + 1] = fields[j][i];
            }

            paddingArr[i] = calcMaxStringArrLength(tempArr);
        }

        // Format the header
        for (int i = 0; i < paddingArr.length - 1; i++)
        {
            padding = "%-" + (paddingArr[i] + 2) + "s";
            sep += "+" + formatPadding(padding, '-');
            padding = "%-" + paddingArr[i] + "s";
            headerStr += String.format("| " + padding + " ", header[i]);
        }

        padding = "%-" + (paddingArr[paddingArr.length - 1] + 2) + "s";
        sep += "+" + formatPadding(padding, '-') + "+";
        padding = "%-" + paddingArr[paddingArr.length - 1] + "s";
        headerStr += String.format(
                            "| " + padding + " |",
                            header[header.length - 1]
                        );

        // Format each cell in the csv table
        for (int i = 0; i < fields.length; i++)
        {
            for (int j = 0; j < fields[i].length - 1; j++)
            {
                padding = "%-" + paddingArr[j] + "s";
                out += String.format("| " + padding + " ", fields[i][j]);
            }

            padding = "%-" + paddingArr[paddingArr.length - 1] + "s";
            out += String.format(
                        "| " + padding + " |\n",
                        fields[i][fields[i].length - 1]
                    );
        }

        // Print table
        if (csvArr.length != 0)
        {
            table = String.format(
                "%s\n%s\n%s\n%s%s", sep, headerStr, sep, out, sep
            );

            System.out.println(table);
        }
    }

    /**
     *  Name:     calcMaxStringArrLength
     *  Purpose:  Find the longest string in an array of string
     *  Imports:
     *    - arr : An array of string
     *  Exports:
     *    - maxLength : An integer for the longest string
     **/

    public static int calcMaxStringArrLength(String[] arr)
    {
        int length, maxLength;
        length = 0;
        maxLength = 0;

        // For each element in the string array
        for (int i = 0; i < arr.length; i++)
        {
            length = arr[i].length();

            // Check if the current string's length is larger
            // than the current max length
            maxLength = (length > maxLength) ? length : maxLength;
        }

        return maxLength;
    }

    /**
     *  Name:     saveCsvToFile
     *  Purpose:  Wrapper function to get filename from user input
     *            and save csv array to a file
     *  Imports:
     *    - fileContents : A String array containing csv entries
     *    - defaultName  : A String for the default filename if user
     *                     did not enter anything
     *  Exports:
     *    - none
     **/

    public static void saveCsvToFile(
        String[] fileContents,
        String defaultName)
    {
        String userInput;

        // If csv file is not empty
        if (fileContents.length != 0)
        {
            // Prompt user for saving to file
            userInput = Input.string("Save report to file? [Y/n]: ");

            // If user selected yes or did not enter anything
            if ((userInput.matches("^[yY]$")) ||
                (userInput.isEmpty()))
            {
                // Get filename
                userInput = Input.string(
                    String.format(
                        "Enter filename [%s]: ", defaultName
                    )
                );

                if (userInput.isEmpty())
                {
                    userInput = defaultName;
                }

                // Write file
                FileIO.writeText(userInput, fileContents);
            }
        }
    }

    /**
     *  Name:     header
     *  Purpose:  Print a message surrounded with lines
     *  Imports:
     *    - msg : The message to print
     *  Exports:
     *    - none
     **/

    public static void header(String msg)
    {
        String line = generateLine(msg.length());
        System.out.printf("%s\n%s\n%s\n", line, msg, line);
    }

    /**
     *  Name:     formatPadding
     *  Purpose:  Wrapper function for printing whitespace padding
     *  Imports:
     *    - padding : The format string
     *  Exports:
     *    - A string containing padding
     **/

    public static String formatPadding(String padding)
    {
        return String.format(padding, " ");
    }

    /**
     *  Name:     formatPadding
     *  Purpose:  An alternative function where instead of using
     *            whitespace, use a user specified character
     *  Imports:
     *    - padding : The format string
     *    - line    : The character to replace whitespace with
     *  Exports:
     *    - A string containing padding
     **/

    public static String formatPadding(String padding, char line)
    {
        return String.format(padding, " ").replace(' ', line);
    }

    /**
     *  Name:     generateLine
     *  Purpose:  Generates a line of length size
     *  Imports:
     *    - size : An int for the size of the line
     *  Exports:
     *    - A String line of length size
     **/

    public static String generateLine(int size)
    {
        return String.format("%" + size + "s", " ").replace(' ', '=');
    }

    /**
     *  Name:     compareIntString
     *  Purpose:  Wrapper function to compare a string int with an int
     *  Imports:
     *    - num : An integer to compare with
     *    - str : A String for an integer
     *  Exports:
     *    - Boolean
     **/

    public static boolean compareIntString(int num, String str)
    {
        return Integer.toString(num).equals(str);
    }

    /**
     *  Name:     toMilliseconds
     *  Purpose:  Convert nanoseconds to milliseconds
     *  Imports:
     *    - nano : A long for the amount of nanoseconds
     *  Exports:
     *    - The formatted string
     **/

    public static String toMilliseconds(long nano)
    {
        return String.format("%1.3f", (double)nano / 1000000);
    }
}
