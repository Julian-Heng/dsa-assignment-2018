import java.util.*;

public class testDSAHashTable
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing DSAHashTable class");
        testConstructor();
        testPut();
        testGet();
        testRemove();
    }

    public static void testConstructor()
    {
        DSAHashTable hash;
        int sizes[] = {1, 2, 8, 32, 256, 1024, 8192};

        for (int i : sizes)
        {
            System.out.print(
                "Testing Alternate Constructor with size " + i + ": "
            );

            try
            {
                hash = new DSAHashTable(i);
                passed();
            }
            catch (Exception e)
            {
                failed(e.getMessage());
            }
        }
    }

    public static void testPut()
    {
        DSAHashTable hash;
        String str;
        int sizes[] = {2, 8, 32, 256, 1024, 8192};
        int intArr[];

        System.out.print("Testing put() with the alphabet: ");

        try
        {
            hash = new DSAHashTable(100);

            for (char i = 'a'; i <= 'z'; i++)
            {
                hash.put(Character.toString(i), Integer.valueOf(i));
            }

            if (hash.getUsedCount() == 26)
            {
                passed();
            }
            else
            {
                failed("Incorrect number of slots filled");
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing put() with string permutation size 4: ");

        try
        {
            hash = new DSAHashTable(8 * (6 * 6 * 6 * 6));

            for (char i = 'a'; i <= 'f'; i++)
            {
                for (char j = 'f'; j >= 'a'; j--)
                {
                    for (char k = 'a'; k <= 'f'; k++)
                    {
                        for (char l = 'f'; l >= 'a'; l--)
                        {
                            str = i + "" + j + "" + k + "" + l;
                            hash.put(str, str);
                        }
                    }
                }
            }

            if (hash.getUsedCount() == (6 * 6 * 6 * 6))
            {
                passed();
            }
            else
            {
                failed("Incorrect number of slots filled");
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing put() with ids 10000 to 10100: ");

        try
        {
            hash = new DSAHashTable(1000);

            for (int i = 10000; i < 10100; i++)
            {
                hash.put(Integer.toString(i), Integer.valueOf(i));
            }

            if (hash.getUsedCount() == 100)
            {
                passed();
            }
            else
            {
                failed("Incorrect number of slots filled");
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing put() with ids 10000 to 10100 shuffed: ");

        try
        {
            hash = new DSAHashTable(1000);
            intArr = new int[100];

            for (int i = 10000; i < 10100; i++)
            {
                intArr[i - 10000] = i;
            }

            shuffle(intArr, intArr.length);

            for (int i = 0; i < intArr.length; i++)
            {
                hash.put(Integer.toString(i), Integer.valueOf(i));
            }

            if (hash.getUsedCount() == 100)
            {
                passed();
            }
            else
            {
                failed("Incorrect number of slots filled");
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print(
            "Testing put() with ids 10000 to 11000 incrementing by 100: "
        );

        try
        {
            hash = new DSAHashTable(20);

            for (int i = 10000; i < 11000; i += 100)
            {
                hash.put(Integer.toString(i), Integer.valueOf(i));
            }

            if (hash.getUsedCount() == 10)
            {
                passed();
            }
            else
            {
                failed("Incorrect number of slots filled");
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing put() with a set of integers: ");

        try
        {
            hash = new DSAHashTable(11);

            hash.put(Integer.toString(15), Integer.valueOf(15));
            hash.put(Integer.toString(46), Integer.valueOf(46));
            hash.put(Integer.toString(27), Integer.valueOf(27));
            hash.put(Integer.toString(13), Integer.valueOf(13));
            hash.put(Integer.toString(37), Integer.valueOf(37));
            hash.put(Integer.toString(24), Integer.valueOf(24));

            if (hash.getUsedCount() == 6)
            {
                passed();
            }
            else
            {
                failed("Incorrect number of slots filled");
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        for (int size : sizes)
        {
            System.out.print(
                "Testing put() with " + size + " indentical keys: "
            );

            try
            {
                hash = new DSAHashTable(size);

                for (int i = 0; i < size; i++)
                {
                    hash.put(Integer.toString(1), Integer.valueOf(1));
                }

                failed("Successfully added a key");
            }
            catch (Exception e)
            {
                passed();
            }
        }
    }

    public static void testGet()
    {
        DSAHashTable hash;
        Integer intObj[];
        String str;
        int sizes[] = {2, 8, 32, 256, 1024, 8192};
        int intArr[];
        int tempInt;
        Integer tempIntObj;

        System.out.print("Testing get() with 1 entry: ");

        try
        {
            hash = new DSAHashTable(1);
            intObj = new Integer[1];

            intObj[0] = Integer.valueOf(1);

            hash.put(intObj[0].toString(), intObj[0]);
            if (hash.get(intObj[0].toString()) == intObj[0])
            {
                passed();
            }
            else
            {
                failed("Return value doesn't match");
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        for (int size : sizes)
        {
            System.out.print("Testing get() with " + size + " entries: ");

            try
            {
                hash = new DSAHashTable(size);
                intObj = new Integer[size];

                for (int i = 0; i < size; i++)
                {
                    intObj[i] = Integer.valueOf(i);
                }

                for (int i = 0; i < size; i++)
                {
                    //System.out.println(hash.getSize());
                    hash.put(intObj[i].toString(), intObj[i]);
                }

                for (int i = 0; i < size; i++)
                {
                    if (hash.get(intObj[i].toString()) != intObj[i])
                    {
                        throw new IllegalArgumentException(
                            "Return value doesn't match"
                        );
                    }
                }

                passed();
            }
            catch (Exception e)
            {
                failed(e.getMessage());
            }
        }

        System.out.print("Testing get() with the alphabet: ");

        try
        {
            hash = new DSAHashTable(100);

            for (char i = 'a'; i <= 'z'; i++)
            {
                hash.put(Character.toString(i), Character.valueOf(i));
            }

            for (char i = 'a'; i <= 'z'; i++)
            {
                if (hash.get(Character.toString(i)) != Character.valueOf(i))
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
            }

            passed();
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing get() with string permutation size 4: ");

        try
        {
            hash = new DSAHashTable(6 * 6 * 6 * 6);

            for (char i = 'a'; i <= 'f'; i++)
            {
                for (char j = 'a'; j <= 'f'; j++)
                {
                    for (char k = 'a'; k <= 'f'; k++)
                    {
                        for (char l = 'a'; l <= 'f'; l++)
                        {
                            str = i + "" + j + "" + k + "" + l;
                            hash.put(str, str);
                        }
                    }
                }
            }

            for (char i = 'a'; i <= 'f'; i++)
            {
                for (char j = 'a'; j <= 'f'; j++)
                {
                    for (char k = 'a'; k <= 'f'; k++)
                    {
                        for (char l = 'a'; l <= 'f'; l++)
                        {
                            str = i + "" + j + "" + k + "" + l;
                            if (! hash.get(str).equals(str))
                            {
                                throw new IllegalArgumentException(
                                    "Return value doesn't match"
                                );
                            }
                        }
                    }
                }
            }

            passed();
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing get() with ids 10000 to 10100: ");

        try
        {
            hash = new DSAHashTable(100);

            for (int i = 10000; i < 10100; i++)
            {
                hash.put(Integer.toString(i), Integer.valueOf(i));
            }

            for (int i = 10000; i < 10100; i++)
            {
                tempIntObj = (Integer)hash.get(Integer.toString(i));
                if (tempIntObj.intValue() != i)
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
            }

            passed();
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing get() with ids 10000 to 10100 shuffed: ");

        try
        {
            hash = new DSAHashTable(1000);
            intArr = new int[100];

            for (int i = 10000; i < 10100; i++)
            {
                intArr[i - 10000] = i;
            }

            shuffle(intArr, intArr.length);

            for (int i = 0; i < intArr.length; i++)
            {
                hash.put(
                    Integer.toString(intArr[i]), Integer.valueOf(intArr[i])
                );
            }

            for (int i = 0; i < intArr.length; i++)
            {
                tempIntObj = (Integer)hash.get(Integer.toString(intArr[i]));

                if (tempIntObj.intValue() != intArr[i])
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
            }

            passed();
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print(
            "Testing get() with ids 10000 to 11000 incrementing by 100: "
        );

        try
        {
            hash = new DSAHashTable(1000);

            for (int i = 10000; i < 11000; i += 100)
            {
                hash.put(Integer.toString(i), Integer.valueOf(i));
            }

            for (int i = 10000; i < 11000; i += 100)
            {
                tempIntObj = (Integer)hash.get(Integer.toString(i));
                if (tempIntObj.intValue() != i)
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
            }

            passed();
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing get() with a set of integers: ");

        try
        {
            hash = new DSAHashTable(11);
            int arr[] = {15, 46, 27, 13, 37, 24};

            hash.put(Integer.toString(15), Integer.valueOf(15));
            hash.put(Integer.toString(46), Integer.valueOf(46));
            hash.put(Integer.toString(27), Integer.valueOf(27));
            hash.put(Integer.toString(13), Integer.valueOf(13));
            hash.put(Integer.toString(37), Integer.valueOf(37));
            hash.put(Integer.toString(24), Integer.valueOf(24));

            for (int i : arr)
            {
                if (hash.get(Integer.toString(i)) != Integer.valueOf(i))
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
            }

            passed();
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing get() with an empty table: ");

        try
        {
            hash = new DSAHashTable(10);
            hash.get("asdf");

            failed("Found something in the empty table");
        }
        catch (Exception e)
        {
            passed();
        }

        System.out.print("Testing get() with a non existant key: ");

        try
        {
            hash = new DSAHashTable(10);

            for (int i = 0; i < 10; i++)
            {
                hash.put(Integer.toString(i), Integer.valueOf(i));
            }

            hash.get("12");
            failed("Found a non existant key in table");
        }
        catch (Exception e)
        {
            passed();
        }
    }

    public static void testRemove()
    {
        DSAHashTable hash;
        String str;

        Integer intObj[];
        Integer tempIntObj;

        Character charObj[];
        Character tempCharObj;

        String stringObj[];
        String tempStringObj;

        int sizes[] = {2, 8, 32, 256, 1024, 8192};
        int intArr[];
        int tempInt;

        System.out.print("Testing remove() with 1 entry: ");

        try
        {
            hash = new DSAHashTable(1);
            intObj = new Integer[2];

            intObj[0] = Integer.valueOf(1);
            hash.put(intObj[0].toString(), intObj[0]);
            intObj[1] = (Integer)hash.remove(intObj[0].toString());

            if (intObj[1] != intObj[0])
            {
                failed("Return value doesn't match");
            }
            else if (hash.getCount() != 0)
            {
                failed("Count is incorrect");
            }
            else if (! hash.isEmpty())
            {
                failed("Table is not empty");
            }
            else
            {
                passed();
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        for (int size : sizes)
        {
            System.out.print("Testing remove() with " + size + " entries: ");
            try
            {
                hash = new DSAHashTable(size);
                intObj = new Integer[size];

                for (int i = 0; i < size; i++)
                {
                    intObj[i] = Integer.valueOf(i);
                }

                for (int i = 0; i < size; i++)
                {
                    //System.out.println(hash.getSize());
                    hash.put(intObj[i].toString(), intObj[i]);
                }

                for (int i = 0; i < size; i++)
                {
                    tempIntObj = (Integer)hash.remove(intObj[i].toString());

                    if (tempIntObj != intObj[i])
                    {
                        throw new IllegalArgumentException(
                            "Return value doesn't match, " + i
                        );
                    }
                    else if (hash.getCount() != (size - (i + 1)))
                    {
                        throw new IllegalArgumentException(
                            "Count is incorrect"
                        );
                    }
                }

                if (! hash.isEmpty())
                {
                    throw new IllegalArgumentException(
                        "Table is not empty"
                    );
                }
                else
                {
                    passed();
                }
            }
            catch (Exception e)
            {
                failed(e.getMessage());
            }
        }

        System.out.print("Testing remove() with the alphabet: ");

        try
        {
            hash = new DSAHashTable(100);
            charObj = new Character[26];
            tempInt = 0;

            for (char i = 'a'; i <= 'z'; i++)
            {
                charObj[tempInt] = Character.valueOf(i);
                hash.put(charObj[tempInt].toString(), charObj[tempInt]);
                tempInt++;
            }

            tempInt = 0;

            for (char i = 'a'; i <= 'z'; i++)
            {
                tempCharObj = (Character)hash.remove(Character.toString(i));

                if (tempCharObj != charObj[tempInt])
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
                else if (hash.getCount() != (charObj.length - (tempInt + 1)))
                {
                    throw new IllegalArgumentException(
                        "Count is incorrect"
                    );
                }

                tempInt++;
            }

            if (! hash.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Table is not empty"
                );
            }
            else
            {
                passed();
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing remove() with string permutation size 4: ");

        try
        {
            hash = new DSAHashTable(6 * 6 * 6 * 6);
            stringObj = new String[6 * 6 * 6 * 6];
            tempInt = 0;

            for (char i = 'a'; i <= 'f'; i++)
            {
                for (char j = 'a'; j <= 'f'; j++)
                {
                    for (char k = 'a'; k <= 'f'; k++)
                    {
                        for (char l = 'a'; l <= 'f'; l++)
                        {
                            str = i + "" + j + "" + k + "" + l;
                            stringObj[tempInt] = str;
                            hash.put(stringObj[tempInt], stringObj[tempInt]);
                            tempInt++;
                        }
                    }
                }
            }

            tempInt = 0;

            for (char i = 'a'; i <= 'f'; i++)
            {
                for (char j = 'a'; j <= 'f'; j++)
                {
                    for (char k = 'a'; k <= 'f'; k++)
                    {
                        for (char l = 'a'; l <= 'f'; l++)
                        {
                            str = i + "" + j + "" + k + "" + l;

                            tempStringObj = (String)hash.remove(str);

                            if (tempStringObj != stringObj[tempInt])
                            {
                                throw new IllegalArgumentException(
                                    "Return value doesn't match"
                                );
                            }
                            else if (
                                hash.getCount() != (
                                    stringObj.length - (tempInt + 1)
                                ))
                            {
                                throw new IllegalArgumentException(
                                    "Count is incorrect"
                                );
                            }

                            tempInt++;
                        }
                    }
                }
            }

            if (! hash.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Table is not empty"
                );
            }
            else
            {
                passed();
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing remove() with ids 10000 to 10100: ");

        try
        {
            hash = new DSAHashTable(100);
            intObj = new Integer[100];
            tempInt = 0;

            for (int i = 10000; i < 10100; i++)
            {
                intObj[tempInt] = Integer.valueOf(i);
                hash.put(intObj[tempInt].toString(), intObj[tempInt]);
                tempInt++;
            }

            tempInt = 0;

            for (int i = 10000; i < 10100; i++)
            {
                tempIntObj = (Integer)hash.remove(Integer.toString(i));
                if (tempIntObj != intObj[tempInt])
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
                else if (hash.getCount() != (intObj.length - (tempInt + 1)))
                {
                    throw new IllegalArgumentException(
                        "Count is incorrect"
                    );
                }

                tempInt++;
            }

            if (! hash.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Table is not empty"
                );
            }
            else
            {
                passed();
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing remove() with ids 10000 to 10100 shuffed: ");

        try
        {
            hash = new DSAHashTable(1000);
            intArr = new int[100];
            intObj = new Integer[100];

            for (int i = 10000; i < 10100; i++)
            {
                intArr[i - 10000] = i;
            }

            shuffle(intArr, intArr.length);

            for (int i = 0; i < intArr.length; i++)
            {
                intObj[i] = Integer.valueOf(intArr[i]);

                hash.put(
                    intObj[i].toString(), intObj[i]
                );
            }

            for (int i = 0; i < intObj.length; i++)
            {
                tempIntObj = (Integer)hash.remove(intObj[i].toString());

                if (tempIntObj != intObj[i])
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
                else if (hash.getCount() != (intObj.length - (i + 1)))
                {
                    throw new IllegalArgumentException(
                        "Count is incorrect"
                    );
                }
            }

            if (! hash.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Table is not empty"
                );
            }
            else
            {
                passed();
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print(
            "Testing remove() with ids 10000 to 11000 incrementing by 100: "
        );

        try
        {
            hash = new DSAHashTable(20);
            intObj = new Integer[10];
            tempInt = 0;

            for (int i = 10000; i < 11000; i += 100)
            {
                intObj[tempInt] = Integer.valueOf(i);
                hash.put(intObj[tempInt].toString(), intObj[tempInt]);
                tempInt++;
            }

            tempInt = 0;

            for (int i = 10000; i < 11000; i += 100)
            {
                tempIntObj = (Integer)hash.remove(intObj[tempInt].toString());

                if (tempIntObj != intObj[tempInt])
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
                else if (hash.getCount() != (intObj.length - (tempInt + 1)))
                {
                    throw new IllegalArgumentException(
                        "Count is incorrect"
                    );
                }

                tempInt++;
            }

            if (! hash.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Table is not empty"
                );
            }
            else
            {
                passed();
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing remove() with a set of integers: ");

        try
        {
            hash = new DSAHashTable(11);
            intObj = new Integer[11];
            int arr[] = {15, 46, 27, 13, 37, 24};

            tempInt = 0;

            for (int i : arr)
            {
                intObj[tempInt] = Integer.valueOf(i);
                hash.put(intObj[tempInt].toString(), intObj[tempInt]);
                tempInt++;
            }

            tempInt = 0;

            for (int i : arr)
            {
                tempIntObj = (Integer)hash.remove(Integer.toString(i));

                if (tempIntObj != intObj[tempInt])
                {
                    throw new IllegalArgumentException(
                        "Return value doesn't match"
                    );
                }
                else if (hash.getCount() != (arr.length - (tempInt + 1)))
                {
                    throw new IllegalArgumentException(
                        "Count is incorrect"
                    );
                }

                tempInt++;
            }

            if (! hash.isEmpty())
            {
                throw new IllegalArgumentException(
                    "Table is not empty"
                );
            }
            else
            {
                passed();
            }
        }
        catch (Exception e)
        {
            failed(e.getMessage());
        }

        System.out.print("Testing remove() with an empty table: ");

        try
        {
            hash = new DSAHashTable(10);
            hash.remove("asdf");

            failed("Found something in the empty table");
        }
        catch (Exception e)
        {
            passed();
        }

        System.out.print("Testing remove() with a non existant key: ");

        try
        {
            hash = new DSAHashTable(10);

            for (int i = 0; i < 10; i++)
            {
                hash.put(Integer.toString(i), Integer.valueOf(i));
            }

            hash.remove("12");
            failed("Found a non existant key in table");
        }
        catch (Exception e)
        {
            passed();
        }
    }

    public static void shuffle(int[] arr, int range)
    {
        Random rand = new Random();
        int index, temp;

        for (int i = 0; i < range; i++)
        {
            index = rand.nextInt(i + 1);
            temp = arr[index];
            arr[index] = arr[i];
            arr[i] = temp;
        }
    }

    public static void header(String msg)
    {
        String line = "";
        String stripMsg = msg.replaceAll("\u001B\\[;\\d]*m", "");

        for (int i = 0; i < stripMsg.length(); i++)
        {
            line += "=";
        }

        System.out.println(line + "\n" + msg + "\n" + line);
    }

    public static void passed()
    {
        System.out.println("\u001B[32mPassed\u001B[0m");
    }

    public static void failed(String err)
    {
        System.out.println("\u001B[31mFailed\u001B[0m. " + err);
    }
}
