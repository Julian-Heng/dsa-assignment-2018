import java.util.*;
import java.io.*;

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

    public boolean isEmpty()
    {
        boolean isEmpty = false;
        isEmpty = (count == 0);
        return isEmpty;
    }

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

    public void flush()
    {
        while (! this.isEmpty())
        {
            this.pop();
        }
    }

    public Iterator<E> iterator()
    {
        return stack.iterator();
    }

    private String err(String msg)
    {
        String errMsg;
        errMsg = "\u001B[31merror\u001B[0m: " + msg;
        return errMsg;
    }
}
