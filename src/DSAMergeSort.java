/**
 *  Name:     DSAMergeSort
 *  Source:   Practical 2
 *
 *  Modification:
 *      Seperate merge sort into it's own file
 *
 *  Author:       Julian Heng (19473701)
 **/

public class DSAMergeSort
{
    public static void mergeSort(int[] A)
    {
        recurseMergeSort(A, 0, A.length - 1);
    }

    public static void recurseMergeSort(
        int[] A,
        int leftIndex,
        int rightIndex)
    {
        int midIndex;
        if (leftIndex < rightIndex)
        {
            midIndex = (leftIndex + rightIndex) / 2;
            recurseMergeSort(A, leftIndex, midIndex);
            recurseMergeSort(A, midIndex + 1, rightIndex);
            merge(A, leftIndex, midIndex, rightIndex);
        }
    }

    public static void merge(
        int[] A,
        int leftIndex,
        int midIndex,
        int rightIndex)
    {
        int[] tempArr = new int[rightIndex - leftIndex + 1];
        int i = leftIndex;
        int j = midIndex + 1;
        int k = 0;

        while ((i <= midIndex) && (j <= rightIndex))
        {
            if (A[i] <= A[j])
            {
                tempArr[k] = A[i];
                i++;
            }
            else
            {
                tempArr[k] = A[j];
                j++;
            }

            k++;
        }

        for (int ii = i; ii <= midIndex; ii++)
        {
            tempArr[k] = A[ii];
            k++;
        }

        for (int jj = j; jj <= rightIndex; jj++)
        {
            tempArr[k] = A[jj];
            k++;
        }

        for (int kk = leftIndex; kk <= rightIndex; kk++)
        {
            A[kk] = tempArr[kk - leftIndex];
        }
    }
}
