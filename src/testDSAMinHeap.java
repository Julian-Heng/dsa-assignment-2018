import java.util.*;

/**
 *  Name:     testDSAMinHeap
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testDSAMinHeap
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing DSAMinHeap Class");
        testConstructor();
        testAdd();
        testRemove();
    }

    public static void testConstructor()
    {
        DSAMinHeap heap;

        System.out.print("Testing default constructor: ");

        try
        {
            heap = new DSAMinHeap();

            if (heap.getSize() != 100)
            {
                testHarnessCommons.failed("Incorrect heap max size");
            }
            else if (heap.getUsedSize() != 0)
            {
                testHarnessCommons.failed("Incorrect heap used size");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing alternate constructor: ");

        try
        {
            heap = new DSAMinHeap(1024);

            if (heap.getSize() != 1024)
            {
                testHarnessCommons.failed("Incorrect heap max size");
            }
            else if (heap.getUsedSize() != 0)
            {
                testHarnessCommons.failed("Incorrect heap used size");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testAdd()
    {
        DSAMinHeap heap;
        String toAdd;

        System.out.print("Testing add() with an empty heap: ");

        try
        {
            heap = new DSAMinHeap(1024);
            toAdd = "A";
            heap.add(1, toAdd);

            if (heap.getSize() != 1024)
            {
                testHarnessCommons.failed("Incorrect heap max size");
            }
            else if (heap.getUsedSize() != 1)
            {
                testHarnessCommons.failed("Incorrect heap used size");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing add() with a full heap: ");

        try
        {
            heap = new DSAMinHeap(1);

            heap.add(1, "A");
            heap.add(2, "B");
            testHarnessCommons.failed("Added an item");
        }
        catch (Exception e)
        {
            testHarnessCommons.passed();
        }

        System.out.print("Testing add() with the same priority: ");

        try
        {
            heap = new DSAMinHeap(1024);

            for (int i = 0; i < 1024; i++)
            {
                heap.add(i, "A");
            }

            if (heap.getSize() != 1024)
            {
                testHarnessCommons.failed("Incorrect heap max size");
            }
            else if (heap.getUsedSize() != 1024)
            {
                testHarnessCommons.failed("Incorrect heap used size");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testRemove()
    {
        DSAMinHeap heap;
        String[] pool;
        Object removedObj;
        boolean isValid;

        System.out.print("Testing remove() with an empty heap: ");

        try
        {
            heap = new DSAMinHeap();

            removedObj = heap.remove();
            testHarnessCommons.failed("Successfully removed an object");
        }
        catch (Exception e)
        {
            testHarnessCommons.passed();
        }

        System.out.print("Testing remove() with a single item: ");

        try
        {
            heap = new DSAMinHeap();

            heap.add(1, "This is the object");
            removedObj = heap.remove();

            if (! removedObj.toString().equals("This is the object"))
            {
                testHarnessCommons.failed("Invalid return value");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing remove() with alphabet: ");

        try
        {
            int index;
            index = 0;

            isValid = true;

            heap = new DSAMinHeap(26);
            pool = new String[26];

            for (char c = 'a'; c <= 'z'; c++)
            {
                pool[index] = Character.toString(c);
                index++;
            }

            for (String i : pool)
            {
                heap.add(index++, i);
            }

            index = 0;

            while (isValid && heap.getUsedSize() > 0)
            {
                isValid = heap.remove().equals(pool[index]);
                index++;
            }

            if (! isValid)
            {
                testHarnessCommons.failed("Invalid return value");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing remove() with reverse alphabet: ");

        try
        {
            int index;
            index = 0;

            isValid = true;

            heap = new DSAMinHeap(26);
            pool = new String[26];

            for (char c = 'z'; c >= 'a'; c--)
            {
                pool[index] = Character.toString(c);
                index++;
            }

            index = 0;

            for (String i : pool)
            {
                heap.add(index++, i);
            }

            index = 0;

            while (isValid && heap.getUsedSize() > 0)
            {
                isValid = heap.remove().equals(pool[index]);
                index++;
            }

            if (! isValid)
            {
                testHarnessCommons.failed("Invalid return value");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static String[] generateStrings(int size)
    {
        Random rand = new Random();
        int index;
        char temp;

        char alpha[] = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'y', 'v', 'w', 'x',
            'y', 'z'
        };

        char arr[][] = new char[size][alpha.length];
        String returnArr[] = new String[size];

        for (int i = 0; i < returnArr.length; i++)
        {
            returnArr[i] = "";
        }

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < alpha.length; j++)
            {
                arr[i][j] = alpha[j];
            }

            for (int j = 0; j < alpha.length; j++)
            {
                index = rand.nextInt(j + 1);
                temp = arr[i][index];
                arr[i][index] = arr[i][j];
                arr[i][j] = temp;
            }

            for (char c : arr[i])
            {
                returnArr[i] += Character.toString(c);
            }
        }

        return returnArr;
    }
}
