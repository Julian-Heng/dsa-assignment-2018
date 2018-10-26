import java.util.*;

/**
 *  Name:     testDSAMergeSort
 *  Source:   None
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testDSAMergeSort
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        int[] arr;

        testHarnessCommons.header("Testing DSAMergeSort class");
        System.out.print("Testing mergeSort() with a sorted array: ");

        try
        {
            for (int i = 2; i < 65536; i *= 2)
            {
                arr = new int[i];
                for (int j = 0; j < i; j++)
                {
                    arr[j] = j;
                }

                DSAMergeSort.mergeSort(arr);

                if (! validateSort(arr))
                {
                    throw new IllegalArgumentException(
                        "Sort failed"
                    );
                }
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing mergeSort() with a reverse array: ");

        try
        {
            for (int i = 2; i < 65536; i *= 2)
            {
                arr = new int[i];
                for (int j = 0; j < i; j++)
                {
                    arr[j] = i - j;
                }

                DSAMergeSort.mergeSort(arr);

                if (! validateSort(arr))
                {
                    throw new IllegalArgumentException(
                        "Sort failed"
                    );
                }
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print("Testing mergeSort() with a shuffled array: ");

        try
        {
            for (int i = 2; i < 65536; i *= 2)
            {
                arr = new int[i];
                for (int j = 0; j < i; j++)
                {
                    arr[j] = j;
                }

                shuffle(arr, arr.length);
                DSAMergeSort.mergeSort(arr);

                if (! validateSort(arr))
                {
                    throw new IllegalArgumentException(
                        "Sort failed"
                    );
                }
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        System.out.print(
            "Testing mergeSort() with a partially shuffled array: "
        );

        try
        {
            for (int i = 2; i < 65536; i *= 2)
            {
                arr = new int[i];
                for (int j = 0; j < i; j++)
                {
                    arr[j] = j;
                }

                shuffle(arr, arr.length / (arr.length < 4 ? 2 : 4));
                DSAMergeSort.mergeSort(arr);

                if (! validateSort(arr))
                {
                    throw new IllegalArgumentException(
                        "Sort failed"
                    );
                }
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static boolean validateSort(int[] arr)
    {
        boolean isSorted = true;
        int i = 1;

        do
        {
            isSorted = (arr[i] > arr[i - 1]);
            i++;
        } while (i < arr.length && isSorted);

        return isSorted;
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
}
