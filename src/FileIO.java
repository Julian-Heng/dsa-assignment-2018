import java.io.*;
import io.*;

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

    public static String[] readTextToArray(String inFilename)
    {
        FileInputStream inFileStream = null;
        InputStreamReader rdr;
        BufferedReader buffRdr;

        String line;
        String[] fileContents;
        int numLines = 0;

        if (! validateString(inFilename))
        {
            throw new IllegalArgumentException(
                err("Invalid filename")
            );
        }
        else
        {
            numLines = calcFileLines(inFilename);
            fileContents = new String[numLines];

            try
            {
                inFileStream = new FileInputStream(inFilename);
                rdr = new InputStreamReader(inFileStream);
                buffRdr = new BufferedReader(rdr);

                for (int i = 0; i < numLines; i++)
                {
                    fileContents[i] = buffRdr.readLine();
                }
                inFileStream.close();
            }
            catch (FileNotFoundException e)
            {
                throw new IllegalArgumentException(
                    err("File does not exist")
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

    public static Object readObject(String inFilename)
    {
        FileInputStream inFileStream = null;
        ObjectInputStream inObjStream;

        Object inObj = null;

        if (! validateString(inFilename) || ! validateFile(inFilename))
        {
            throw new IllegalArgumentException(
                err("Invalid filename")
            );
        }
        else
        {
            try
            {
                inFileStream = new FileInputStream(inFilename);
                inObjStream = new ObjectInputStream(inFileStream);
                inObj = inObjStream.readObject();
                inObjStream.close();
            }
            catch (ClassNotFoundException e)
            {
                System.out.println(
                    err("Class is not found" + e.getMessage())
                );
            }
            catch (FileNotFoundException e)
            {
                throw new IllegalArgumentException(
                    err("File does not exist")
                );
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(
                    err("Unable to load object from file")
                );
            }
        }
        return inObj;
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

    public static void writeObject(String inFilename, Object inObj)
    {
        FileOutputStream outFileStream = null;
        ObjectOutputStream outObjStream;

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
                outObjStream = new ObjectOutputStream(outFileStream);
                outObjStream.writeObject(inObj);
                outObjStream.close();
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(
                    err("Unable to save object to file, " + e.getMessage())
                );
            }
        }
    }

    public static int calcFileLines(String inFilename)
    {
        FileInputStream inFileStream = null;
        InputStreamReader rdr;
        BufferedReader buffRdr;

        int numLines = 0;
        try
        {
            inFileStream = new FileInputStream(inFilename);
            rdr = new InputStreamReader(inFileStream);
            buffRdr = new BufferedReader(rdr);

            while (buffRdr.readLine() != null)
            {
                numLines++;
            }
            inFileStream.close();
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
        return numLines;
    }

    private static boolean validateString(String inString)
    {
        boolean isValid = false;
        isValid = ((inString != null) && (! inString.equals("")));
        return isValid;
    }

    private static boolean validateFile(String inFilename)
    {
        boolean isValid = false;
        isValid = (calcFileLines(inFilename) != 0);
        return isValid;
    }

    private static String err(String msg)
    {
        return "\u001B[31mFileIO Error\u001B[0m: " + msg;
    }
}
