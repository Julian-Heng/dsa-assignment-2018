import java.util.*;

public class DSAMinHeap
{
    private class DSAMinHeapEntry
    {
        private String priority;
        private Object value;

        public DSAMinHeapEntry(String inPriority, Object inValue)
        {
            priority = inPriority;
            value = inValue;
        }

        public String getPriority() { return priority; }
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

    public void add(String priority, Object value)
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

    public void heapSort()
    {
        for (int i = m_count - 1; i > 0; i--)
        {
            swap(0, i);
            recurseTrickleDown(0, i);
        }
    }

    public Object[] toObjArray()
    {
        Object[] newArr = new Object[m_count];

        for (int i = 0; i < m_count; i++)
        {
            newArr[i] = m_heap[i].getValue();
        }

        return newArr;
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
        DSAMinHeapEntry temp;
        int parentIndex;

        parentIndex = (index - 1) / 2;

        /*
        if ((index > 0) &&
            (m_heap[index].getPriority() >
                m_heap[parentIndex].getPriority()))
                */
        /*
        if ((index > 0) &&
            (m_heap[index].getPriority().compareTo(
                m_heap[parentIndex].getPriority()) > 0))
                */
        if ((index > 0) &&
            (m_heap[index].getPriority().compareTo(
                m_heap[parentIndex].getPriority()) < 0))
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
            /*
            if ((rightChild < numItems) &&
                (m_heap[leftChild].getPriority() <
                    m_heap[rightChild].getPriority()))
                    */
            /*
            if ((rightChild < numItems) &&
                (m_heap[leftChild].getPriority().compareTo(
                    m_heap[rightChild].getPriority()) < 0))
                    */
            if ((rightChild < numItems) &&
                (m_heap[leftChild].getPriority().compareTo(
                    m_heap[rightChild].getPriority()) > 0))
            {
                largeIndex = rightChild;
            }

            /*
            if (m_heap[largeIndex].getPriority() >
                    m_heap[index].getPriority())
                    */
            /*
            if (m_heap[largeIndex].getPriority().compareTo(
                    m_heap[index].getPriority()) > 0)
                    */
            if (m_heap[largeIndex].getPriority().compareTo(
                    m_heap[index].getPriority()) < 0)
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

    public int getSize() { return m_heap.length; }
    public int getUsedSize() { return m_count; }
}
