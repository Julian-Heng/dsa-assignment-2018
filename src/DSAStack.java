import java.util.*;
import java.io.*;

/**
 *  Name:     DSAStack
 *  Source:   Practical 3
 *
 *  Modification:
 *      Converted object field to generics
 *
 *  Author:       Julian Heng (19473701)
 **/

public class DSAStack<E> implements Iterable<E>
{
    public DSALinkedList<E> stack;
    public int count;

    public DSAStack()
    {
        stack = new DSALinkedList<E>();
        count = 0;
    }

    public int getCount() { return count; }
    public boolean isEmpty() { return (count == 0); }

    public void push(E inObj)
    {
        stack.insertLast(inObj);
        count++;
    }

    public E pop()
    {
        E topObj;

        topObj = stack.removeLast();
        count--;
        return topObj;
    }

    public E top()
    {
        E topObj;

        if (isEmpty())
        {
            throw new IllegalArgumentException(err("Stack is empty"));
        }
        else
        {
            topObj = stack.peekLast();
        }

        return topObj;
    }

    public Iterator<E> iterator() { return stack.iterator(); }

    private String err(String msg)
    {
        String errMsg;
        errMsg = "\u001B[31merror\u001B[0m: " + msg;
        return errMsg;
    }
}
