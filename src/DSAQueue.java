import java.util.*;
import java.io.*;

/**
 *  Name:     DSAQueue
 *  Source:   Practical 3
 *
 *  Modification:
 *      Converted object field to generics
 *
 *  Author:       Julian Heng (19473701)
 **/

public class DSAQueue<E> implements Iterable<E>
{
    public DSALinkedList<E> queue;
    public int count;

    public DSAQueue()
    {
        queue = new DSALinkedList<E>();
        count = 0;
    }

    public int getCount() { return count; }

    public boolean isEmpty()
    {
        boolean isEmpty = false;
        isEmpty = (count == 0);
        return isEmpty;
    }

    public void enqueue(E inObj)
    {
        queue.insertLast(inObj);
        count++;
    }

    public E dequeue()
    {
        E frontObj;
        if (this.isEmpty())
        {
            throw new IllegalArgumentException(err("Queue is empty"));
        }
        else
        {
            frontObj = queue.removeFirst();
            count--;
        }
        return frontObj;
    }

    public E peek()
    {
        E peekObj;
        peekObj = queue.peekFirst();
        return peekObj;
    }

    public void flush()
    {
        while (! this.isEmpty())
        {
            this.dequeue();
        }
    }

    public Iterator<E> iterator()
    {
        return queue.iterator();
    }

    private String err(String msg)
    {
        String errMsg;
        errMsg = "\u001B[31merror\u001B[0m: " + msg;
        return errMsg;
    }
}
