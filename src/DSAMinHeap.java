import java.util.*;

/**
 *  Name:     DSAMinHeap
 *  Source:   Practical 7
 *
 *  Modification:
 *      Convert from a max heap to a minimum heap
 *
 *  Author:       Julian Heng (19473701)
 **/

public class DSAMinHeap
{
    private class DSAMinHeapEntry
    {
        private int priority;
        private Object value;

        public DSAMinHeapEntry(int inPriority, Object inValue)
        {
            priority = inPriority;
            value = inValue;
        }

        public int getPriority() { return priority; }
        public Object getValue() { return value; }
    }

    private DSAMinHeapEntry[] m_heap;
    private int m_count;

    public DSAMinHeap()
    {
        m_heap = new DSAMinHeap.DSAMinHeapEntry[100];
        m_count = 0;
    }

    public DSAMinHeap(int maxSize)
    {
        m_heap = new DSAMinHeap.DSAMinHeapEntry[maxSize];
        m_count = 0;
    }

    public DSAMinHeap(int[] arr)
    {
        m_heap = new DSAMinHeap.DSAMinHeapEntry[arr.length];
        m_count = 0;
        this.heapify(arr);
    }

    public void add(int priority, Object value)
    {
        if (m_count == m_heap.length)
        {
            throw new IllegalArgumentException("Heap is full");
        }
        else
        {
            DSAMinHeapEntry toAdd = new DSAMinHeapEntry(priority, value);
            m_heap[m_count] = toAdd;
            trickleUp(m_count);
            m_count++;
        }
    }

    public Object remove()
    {
        Object removedObj;

        removedObj = null;

        if (m_count == 0)
        {
            throw new IllegalArgumentException("Heap is empty");
        }
        else
        {
            m_count--;
            swap(0, m_count);
            trickleDown(0);
            removedObj = m_heap[m_count].getValue();
            m_heap[m_count] = null;
        }

        return removedObj;
    }

    private void heapify(int[] arr)
    {
        for (int inArr : arr)
        {
            this.add(inArr, Integer.valueOf(inArr));
        }

        for (int i = ((m_count / 2) - 1); i >= 0; i--)
        {
            trickleDown(i);
        }
    }

    private void trickleUp(int index)
    {
        recurseTrickleUp(index);
    }

    private void recurseTrickleUp(int index)
    {
        DSAMinHeapEntry temp;
        int parentIndex;

        parentIndex = (index - 1) / 2;

        if ((index > 0) &&
            (m_heap[index].getPriority() <
                m_heap[parentIndex].getPriority()))
        {
            temp = m_heap[parentIndex];
            m_heap[parentIndex] = m_heap[index];
            m_heap[index] = temp;
            recurseTrickleUp(parentIndex);
        }
    }

    private void trickleDown(int index)
    {
        recurseTrickleDown(index, m_count);
    }

    private void recurseTrickleDown(int index, int numItems)
    {
        int leftChild, rightChild, parent, largeIndex;

        leftChild = (index * 2) + 1;
        rightChild = (index * 2) + 2;
        parent = (index - 1) / 2;

        if (leftChild < numItems)
        {
            largeIndex = leftChild;
            if ((rightChild < numItems) &&
                (m_heap[leftChild].getPriority() >
                    m_heap[rightChild].getPriority()))
            {
                largeIndex = rightChild;
            }

            if (m_heap[largeIndex].getPriority() <
                    m_heap[index].getPriority())
            {
                swap(largeIndex, index);
                recurseTrickleDown(largeIndex, numItems);
            }
        }
    }

    private void swap(int index1, int index2)
    {
        DSAMinHeapEntry temp;

        temp = m_heap[index1];
        m_heap[index1] = m_heap[index2];
        m_heap[index2] = temp;
    }

    public boolean isEmpty()
    {
        return m_count == 0;
    }

    public int getSize() { return m_heap.length; }
    public int getUsedSize() { return m_count; }
}
