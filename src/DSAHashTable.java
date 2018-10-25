import java.util.*;

public class DSAHashTable
{
    private class DSAHashEntry
    {
        private String key;
        private Object value;
        private int state;

        public DSAHashEntry()
        {
            setKey(null);
            setValue(null);
            setState(EMPTY);
        }

        public DSAHashEntry(String inKey, Object inValue)
        {
            setKey(inKey);
            setValue(inValue);
            setState(USED);
        }

        public String getKey() { return key; }
        public Object getValue() { return value; }
        public int getState() { return state; }

        public void setKey(String inKey) { key = inKey; }
        public void setValue(Object inValue) { value = inValue; }
        public void setState(int inState) { state = inState; }
    }

    public static final int EMPTY = 0;
    public static final int USED = 1;
    public static final int WAS_OCCUPIED = -1;
    public static final int MIN_SIZE = 10;

    private DSAHashEntry[] m_hashTable;
    private int numUsedTotal;
    private int numUsed;
    private double load;

    public DSAHashTable()
    {
        m_hashTable = this.makeTable(nextPrime(MIN_SIZE));
    }

    public DSAHashTable(int maxSize)
    {
        m_hashTable = this.makeTable(nextPrime(maxSize));
    }

    public void put(String inKey, Object inValue)
    {
        if (containsKey(inKey))
        {
            throw new IllegalArgumentException("Keys must be unique");
        }
        else
        {
            DSAHashEntry newEntry = new DSAHashEntry(inKey, inValue);
            this.put(newEntry);
        }
    }

    private void put(DSAHashEntry inEntry)
    {
        int newSize;

        try
        {
            m_hashTable[findNextAvailable(inEntry.getKey())] = inEntry;

            numUsed++;
            numUsedTotal++;
            this.updateLoad();

            if (load > 0.7)
            {
                this.resize(nextPrime(numUsedTotal * 2));
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Object get(String inKey)
    {
        return m_hashTable[find(inKey)].getValue();
    }

    public Object remove(String inKey)
    {
        Object returnValue;
        int hashIndex;

        hashIndex = this.find(inKey);
        returnValue = m_hashTable[hashIndex].getValue();

        m_hashTable[hashIndex].setValue(null);
        m_hashTable[hashIndex].setState(WAS_OCCUPIED);

        numUsed--;
        this.updateLoad();

        if (load < 0.3 && m_hashTable.length > (2 * MIN_SIZE))
        {
            this.resize(nextPrime(numUsedTotal / 2));
        }

        return returnValue;
    }

    public boolean containsKey(String inKey)
    {
        return find(inKey) != -1 ? true : false;
    }

    public int getCount() { return numUsed; }
    public int getUsedCount() { return numUsedTotal; }
    public double getLoad() { return load; }
    public int getSize() { return m_hashTable.length; }

    public boolean isEmpty()
    {
        return numUsed == 0;
    }

    public DSALinkedList<String> convertKeyToLinkedList()
    {
        DSALinkedList<String> returnList = new DSALinkedList<String>();
        for (int i = 0; i < m_hashTable.length; i++)
        {
            if (m_hashTable[i].getState() == 1)
            {
                returnList.insertLast(m_hashTable[i].getKey());
            }
        }
        return returnList;
    }

    public DSALinkedList<Object> convertValueToLinkedList()
    {
        DSALinkedList<Object> returnList = new DSALinkedList<Object>();
        for (int i = 0; i < m_hashTable.length; i++)
        {
            if (m_hashTable[i].getState() == 1)
            {
                returnList.insertLast(m_hashTable[i].getValue());
            }
        }
        return returnList;
    }

    private int hash(String key)
    {
        int hashIndex = 0;

        for (int i = 0; i < key.length(); i++)
        {
            hashIndex ^= ((hashIndex << 5) + (hashIndex << 2) + key.charAt(i));
        }

        return Math.abs(hashIndex % m_hashTable.length);
    }

    private int stepHash(int inKeyHash)
    {
        int step;
        int max_step;

        step = 5 - (inKeyHash % 5);

        return step;
    }

    private int find(String inKey)
    {
        int hashIndex, origIndex, step;
        boolean found, giveUp;

        hashIndex = hash(inKey);
        origIndex = hashIndex;
        step = stepHash(hashIndex);
        found = false;
        giveUp = false;

        while ((! found) && (! giveUp))
        {
            if (m_hashTable[hashIndex].getState() == 0)
            {
                giveUp = true;
            }
            else if (m_hashTable[hashIndex].getKey().equals(inKey))
            {
                found = true;
            }
            else
            {
                hashIndex += step;
                hashIndex %= m_hashTable.length;

                if (hashIndex == origIndex)
                {
                    giveUp = true;
                }
            }
        }

        if (! found)
        {
            hashIndex = -1;
        }

        return hashIndex;
    }

    private int findNextAvailable(String inKey)
    {
        int hashIndex, origIndex, step;
        boolean foundNext, checkedAll;

        hashIndex = hash(inKey);
        origIndex = hashIndex;
        step = stepHash(hashIndex);
        foundNext = false;
        checkedAll = false;

        while (! foundNext && ! checkedAll)
        {
            if (m_hashTable[hashIndex].getState() != 1)
            {
                foundNext = true;
            }
            else
            {
                hashIndex += step;
                hashIndex %= m_hashTable.length;

                if (hashIndex == origIndex)
                {
                    checkedAll = true;
                }
            }
        }

        if (checkedAll)
        {
            throw new IllegalArgumentException(
                "No available slot for next index, " + inKey +
                " | " + hashIndex +
                " | " + step
            );
        }

        return hashIndex;
    }

    private int nextPrime(int start)
    {
        int currentPrime, i;
        boolean isPrime;

        isPrime = false;
        currentPrime = start;

        if ((start % 2) == 0)
        {
            currentPrime++;
        }

        currentPrime -= 2;

        while (! isPrime)
        {
            currentPrime += 2;

            i = 3;
            isPrime = true;

            while (((i * i) <= currentPrime) && isPrime)
            {
                if ((currentPrime % i) == 0)
                {
                    isPrime = false;
                }
                else
                {
                    i += 2;
                }
            }
        }

        return currentPrime;
    }

    private DSAHashTable.DSAHashEntry[] makeTable(int size)
    {
        DSAHashEntry[] table;
        table = new DSAHashTable.DSAHashEntry[size];

        for (int i = 0; i < size; i++)
        {
            table[i] = new DSAHashEntry();
        }

        return table;
    }

    private void resize(int size)
    {
        DSAHashTable newTable;

        newTable = new DSAHashTable(size);
        numUsed = 0;
        numUsedTotal = 0;

        for (int i = 0; i < m_hashTable.length; i++)
        {
            if (m_hashTable[i].getState() == 1)
            {
                newTable.put(
                    m_hashTable[i].getKey(),
                    m_hashTable[i].getValue()
                );
            }
        }

        m_hashTable = newTable.m_hashTable;
        this.updateHard();
    }

    private void updateHard()
    {
        numUsed = 0;
        numUsedTotal = 0;
        load = 0;

        for (int i = 0; i < m_hashTable.length; i++)
        {
            if (m_hashTable[i].getState() == 1)
            {
                numUsed++;
            }

            if (m_hashTable[i].getState() != 0)
            {
                numUsedTotal++;
            }
        }

        this.updateLoad();
    }

    private void updateLoad()
    {
        load = (double)numUsed / (double)m_hashTable.length;
    }

}
