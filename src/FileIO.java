import java.io.*;
import io.*;

/**
 *  Name:     FileIO
 *  Source:   OOPD Assignment 2018 Sem 1
 *
 *  Modifications:
 *      Convert so that it would read to a linked list instead of an array
 *
 *  Author:   Julian Heng (19473701)
 **/

public class FileIO
{
    public static DSALinkedList<String> readText(String inFilename)
    {
        FileInputStream inFileStream = null;
        InputStreamReader rdr;
        BufferedReader buffRdr;

        String line;
        DSALinkedList<String> fileContents;
        int numLines = 0;

        if (! validateString(inFilename))
        {
            throw new IllegalArgumentException(
                err("Invalid filename")
            );
        }
        else
        {
            fileContents = new DSALinkedList<String>();

            try
            {
                inFileStream = new FileInputStream(inFilename);
                rdr = new InputStreamReader(inFileStream);
                buffRdr = new BufferedReader(rdr);

                line = buffRdr.readLine();

                while (line != null)
                {
                    fileContents.insertLast(line);
                    line = buffRdr.readLine();
                }

                inFileStream.close();
            }
            catch (FileNotFoundException e)
            {
                throw new IllegalArgumentException(
                    err(inFilename + " does not exist")
                );
            }
            catch (IOException e)
            {
                if (inFileStream != null)
                {
                    try
                    {
                        inFileStream.close();
                    }
                    catch (IOException ex)
                    {

                    }
                }
            }
        }
        return fileContents;
    }

    public static void writeText(String inFilename, String[] inFileContents)
    {
        FileOutputStream outFileStream = null;
        PrintWriter pw;

        if (! validateString(inFilename))
        {
            throw new IllegalArgumentException(
                err("Filename is invalid")
            );
        }
        else
        {
            try
            {
                outFileStream = new FileOutputStream(inFilename);
                pw = new PrintWriter(outFileStream);

                for (String line : inFileContents)
                {
                    pw.println(line);
                }

                pw.close();
            }
            catch (IOException e)
            {
                if (outFileStream != null)
                {
                    try
                    {
                        outFileStream.close();
                    }
                    catch (IOException ex)
                    {

                    }
                }
            }
        }
    }

    private static boolean validateString(String inString)
    {
        boolean isValid = false;
        isValid = ((inString != null) && (! inString.equals("")));
        return isValid;
    }

    private static String err(String msg)
    {
        return "\u001B[31mFileIO Error\u001B[0m: " + msg;
    }
}
