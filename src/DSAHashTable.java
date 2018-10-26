import java.util.*;

/**
 *  Name:     DSAHashTable
 *  Source:   Practical 8
 *
 *  Modifications:
 *      Fixed double hashing and changed certain coding
 *      styles
 *
 *  Author:   Julian Heng (19473701)
 **/

public class DSAHashTable
{
    /**
     *  Name:     DSAHashEntry
     *  Purpose:  A hash entry in the hash table
     **/

    private class DSAHashEntry
    {
        private String key;
        private Object value;
        private int state;

        // Default Constructor
        public DSAHashEntry()
        {
            setKey(null);
            setValue(null);
            setState(EMPTY);
        }

        // Alternate Constructor
        public DSAHashEntry(String inKey, Object inValue)
        {
            setKey(inKey);
            setValue(inValue);
            setState(USED);
        }

        // Getters
        public String getKey() { return key; }
        public Object getValue() { return value; }
        public int getState() { return state; }

        // Setters
        public void setKey(String inKey) { key = inKey; }
        public void setValue(Object inValue) { value = inValue; }
        public void setState(int inState) { state = inState; }
    }

    // Constants
    public static final int EMPTY = 0;
    public static final int USED = 1;
    public static final int WAS_OCCUPIED = -1;
    public static final int MIN_SIZE = 10;

    private DSAHashEntry[] m_hashTable;
    private int numUsedTotal;
    private int numUsed;
    private double load;

    // Default Constructor
    public DSAHashTable()
    {
        m_hashTable = this.makeTable(nextPrime(MIN_SIZE));
    }

    // Alternate Constructor
    public DSAHashTable(int maxSize)
    {
        m_hashTable = this.makeTable(nextPrime(maxSize));
    }

    /**
     *  Name:     put
     *  Purpose:  Add an entry to the hash table
     *  Imports:
     *    - inKey   : The key to identify the object
     *    - inValue : The object to be stored
     *
     *  Exports:
     *    - none
     **/

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

    /**
     *  Name:     put
     *  Purpose:  Add an entry to the hash table
     *  Imports:
     *    - inEntry : A hash entry object containing the key and the object
     *
     *  Exports:
     *    - none
     **/

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

    /**
     *  Name:     get
     *  Purpose:  Retrieve an object stored in the hash table
     *  Imports:
     *    - inKey : The key used to identify the object
     *
     *  Exports:
     *    - Object in hash table
     **/

    public Object get(String inKey)
    {
        return m_hashTable[find(inKey)].getValue();
    }

    /**
     *  Name:     remove
     *  Purpose:  Remove an object in the hash table
     *  Imports:
     *    - inKey : The key used to identify the object
     *
     *  Exports:
     *    - Object in hash table
     **/

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

    /**
     *  Name:     containsKey
     *  Purpose:  Check if inputted key exist in the hash table
     *  Imports:
     *    - inKey : The key used to identify the object
     *
     *  Exports:
     *    - boolean
     **/

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

    /**
     *  Name:     convertKeyToLinkedList
     *  Purpose:  Make the table "iterable" by converting to a linked list
     *  Imports:
     *    - none
     *
     *  Exports:
     *    - returnList : A linked list containing objects in the hash table
     **/

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

    /**
     *  Name:     convertValueToLinkedList
     *  Purpose:  Make the table "iterable" by converting to a linked list
     *  Imports:
     *    - none
     *
     *  Exports:
     *    - returnList
     **/

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

    /**
     *  Name:     hash
     *  Purpose:  Calculate the hash from input
     *  Imports:
     *    - key : The key to hash
     *
     *  Exports:
     *    - hashIndex : The index in the table for the hash
     **/

    private int hash(String key)
    {
        int hashIndex = 0;

        for (int i = 0; i < key.length(); i++)
        {
            // Shift-Add-XOR Hash
            hashIndex ^= ((hashIndex << 5) + (hashIndex << 2) + key.charAt(i));
        }

        // Have to use absolute values because they become negative
        // due to integer overflow
        return Math.abs(hashIndex % m_hashTable.length);
    }

    /**
     *  Name:     stepHash
     *  Purpose:  Calculate the step amount for a given hash
     *  Imports:
     *    - inKeyHash : The index for the hash
     *
     *  Exports:
     *    - step : The step amount
     **/

    private int stepHash(int inKeyHash)
    {
        int step;
        int max_step;

        step = 5 - (inKeyHash % 5);

        return step;
    }

    /**
     *  Name:     find
     *  Purpose:  Find an object in the hash table by the key
     *  Imports:
     *    - inKey : The identifying key
     *
     *  Exports:
     *    - hashIndex : The index for the object
     **/

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

    /**
     *  Name:     findNextAvailable
     *  Purpose:  Find the next available slot in the table from a given key
     *  Imports:
     *    - inKey : The identifying key
     *
     *  Exports:
     *    - hashIndex : The index for the next free available slot
     **/

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

    /**
     *  Name:     nextPrime
     *  Purpose:  Find the next prime number for table size
     *  Imports:
     *    - start : The starting number
     *
     *  Exports:
     *    - currentPrime : The next prime number
     **/

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

    /**
     *  Name:     makeTable
     *  Purpose:  Make a hash table of a given size
     *  Imports:
     *    - size : The size of the table
     *
     *  Exports:
     *    - An array of hash entries
     **/

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

    /**
     *  Name:     resize
     *  Purpose:  Resize and rehash the current table
     *  Imports:
     *    - size : The new table size
     *
     *  Exports:
     *    - none
     **/

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

    /**
     *  Name:     updateHard
     *  Purpose:  Count all items in table
     *  Imports:
     *    - none
     *
     *  Exports:
     *    - none
     **/

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

    /**
     *  Name:     updateLoad()
     *  Purpose:  Update the load varialbe
     *  Imports:
     *    - none
     *
     *  Exports:
     *    - none
     **/

    private void updateLoad()
    {
        load = (double)numUsed / (double)m_hashTable.length;
    }
}
