import java.util.*;

/**
 *  Name:     DSAMaxHeap
 *  Source:   Practical 7
 *
 *  Modification:
 *      Use strings as priority instead of integers
 *
 *  Author:       Julian Heng (19473701)
 **/

public class DSAMaxHeap
{
    private class DSAMaxHeapEntry
    {
        private String priority;
        private Object value;

        public DSAMaxHeapEntry(String inPriority, Object inValue)
        {
            priority = inPriority;
            value = inValue;
        }

        public String getPriority() { return priority; }
        public Object getValue() { return value; }
    }

    private DSAMaxHeapEntry[] m_heap;
    private int m_count;

    public DSAMaxHeap()
    {
        m_heap = new DSAMaxHeap.DSAMaxHeapEntry[100];
        m_count = 0;
    }

    public DSAMaxHeap(int maxSize)
    {
        m_heap = new DSAMaxHeap.DSAMaxHeapEntry[maxSize];
        m_count = 0;
    }

    public void add(String priority, Object value)
    {
        if (m_count == m_heap.length)
        {
            throw new IllegalArgumentException("Heap is full");
        }
        else
        {
            DSAMaxHeapEntry toAdd = new DSAMaxHeapEntry(priority, value);
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

    public void heapSort()
    {
        for (int i = m_count - 1; i > 0; i--)
        {
            swap(0, i);
            recurseTrickleDown(0, i);
        }
    }

    public DSALinkedList<Object> toObjList()
    {
        DSALinkedList<Object> list = new DSALinkedList<Object>();

        for (int i = 0; i < m_count; i++)
        {
            list.insertLast(m_heap[i].getValue());
        }

        return list;
    }

    public String[] toStringArray()
    {
        String[] newArr = new String[m_count];

        for (int i = 0; i < m_count; i++)
        {
            newArr[i] = m_heap[i].getPriority();
        }

        return newArr;
    }

    private void trickleUp(int index)
    {
        recurseTrickleUp(index);
    }

    private void recurseTrickleUp(int index)
    {
        DSAMaxHeapEntry temp;
        int parentIndex;

        parentIndex = (index - 1) / 2;

        if ((index > 0) &&
            (m_heap[index].getPriority().compareTo(
                m_heap[parentIndex].getPriority()) > 0))
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
                (m_heap[leftChild].getPriority().compareTo(
                    m_heap[rightChild].getPriority()) < 0))
            {
                largeIndex = rightChild;
            }

            if (m_heap[largeIndex].getPriority().compareTo(
                    m_heap[index].getPriority()) > 0)
            {
                swap(largeIndex, index);
                recurseTrickleDown(largeIndex, numItems);
            }
        }
    }

    private void swap(int index1, int index2)
    {
        DSAMaxHeapEntry temp;

        temp = m_heap[index1];
        m_heap[index1] = m_heap[index2];
        m_heap[index2] = temp;
    }

    public int getSize() { return m_heap.length; }
    public int getUsedSize() { return m_count; }
}
