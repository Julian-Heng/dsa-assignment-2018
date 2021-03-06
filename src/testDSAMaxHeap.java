import java.util.*;

/**
 *  Name:     testDSAMaxHeap
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testDSAMaxHeap
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing DSAMaxHeap Class");
        testConstructor();
        testAdd();
        testRemove();
        testHeapSort();
    }

    public static void testConstructor()
    {
        DSAMaxHeap heap;

        System.out.print("Testing default constructor: ");

        try
        {
            heap = new DSAMaxHeap();

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
            heap = new DSAMaxHeap(1024);

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
        DSAMaxHeap heap;
        String toAdd;

        System.out.print("Testing add() with an empty heap: ");

        try
        {
            heap = new DSAMaxHeap(1024);
            toAdd = "A";
            heap.add(toAdd, toAdd);

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
            heap = new DSAMaxHeap(1);

            heap.add("A", "A");
            heap.add("B", "B");
            testHarnessCommons.failed("Added an item");
        }
        catch (Exception e)
        {
            testHarnessCommons.passed();
        }

        System.out.print("Testing add() with the same priority: ");

        try
        {
            heap = new DSAMaxHeap(1024);

            for (int i = 0; i < 1024; i++)
            {
                heap.add("A", "A");
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
        DSAMaxHeap heap;
        String[] pool;
        Object removedObj;
        boolean isValid;

        System.out.print("Testing remove() with an empty heap: ");

        try
        {
            heap = new DSAMaxHeap();

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
            heap = new DSAMaxHeap();

            heap.add("This is the key", "This is the object");
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

            heap = new DSAMaxHeap(26);
            pool = new String[26];

            for (char c = 'a'; c <= 'z'; c++)
            {
                pool[index] = Character.toString(c);
                index++;
            }

            for (String i : pool)
            {
                heap.add(i, i);
            }

            index = pool.length - 1;

            while (isValid && heap.getUsedSize() > 0)
            {
                isValid = heap.remove().equals(pool[index]);
                index--;
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

            heap = new DSAMaxHeap(26);
            pool = new String[26];

            for (char c = 'z'; c >= 'a'; c--)
            {
                pool[index] = Character.toString(c);
                index++;
            }

            for (String i : pool)
            {
                heap.add(i, i);
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

    public static void testHeapSort()
    {
        DSAMaxHeap heap;
        String pool[];
        int[] sizes = {
            2, 8, 32, 128, 512, 2048,
            8192, 32768, 131072, 524288,
            1000000
        };

        System.out.print(
            "Testing heapSort() with a sorted string array: "
        );

        try
        {
            int index;
            index = 0;

            heap = new DSAMaxHeap(26);
            pool = new String[26];

            for (char c = 'a'; c <= 'z'; c++)
            {
                pool[index] = Character.toString(c);
                index++;
            }

            for (String i : pool)
            {
                heap.add(i, i);
            }

            heap.heapSort();
            pool = heap.toStringArray();

            if (! validateSort(pool))
            {
                testHarnessCommons.failed("Array is not sorted");
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

        System.out.print(
            "Testing heapSort() with a reverse sorted string array: "
        );

        try
        {
            int index;
            index = 0;

            heap = new DSAMaxHeap(26);
            pool = new String[26];

            for (char c = 'z'; c >= 'a'; c--)
            {
                pool[index] = Character.toString(c);
                index++;
            }

            for (String i : pool)
            {
                heap.add(i, i);
            }

            heap.heapSort();
            pool = heap.toStringArray();

            if (! validateSort(pool))
            {
                testHarnessCommons.failed("Array is not sorted");
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

        for (int inSize : sizes)
        {
            System.out.print(
                "Testing heapSort() with a random string array [size " +
                inSize + "]: "
            );

            try
            {
                pool = generateStrings(inSize);
                heap = new DSAMaxHeap(pool.length);

                for (String inPool : pool)
                {
                    heap.add(inPool, inPool);
                }

                heap.heapSort();
                pool = heap.toStringArray();

                if (! validateSort(pool))
                {
                    testHarnessCommons.failed("Array is not sorted");
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

    public static boolean validateSort(String[] arr)
    {
        boolean isSorted = true;
        int i = 1;

        do
        {
            isSorted = (arr[i].compareTo(arr[i - 1]) > 0);
            i++;
        } while (i < arr.length && isSorted);

        return isSorted;
    }
}
