import java.util.*;
import java.io.*;

public class DSALinkedList<E> implements Iterable<E>
{
    private class DSAListNode<E>
    {
        private E m_object;
        private DSAListNode<E> m_next, m_prev;

        public DSAListNode(E inObj)
        {
            m_object = inObj;
            m_next = null;
            m_prev = null;
        }

        public void setValue(E inObj) { m_object = inObj; }
        public void setNext(DSAListNode<E> inNext) { m_next = inNext; }
        public void setPrev(DSAListNode<E> inPrev) { m_prev = inPrev; }
        public E getValue() { return m_object; }
        public DSAListNode<E> getNext() { return m_next; }
        public DSAListNode<E> getPrev() { return m_prev; }
    }

    private class DSALinkedListIterator<E> implements Iterator<E>
    {
        private DSALinkedList<E>.DSAListNode<E> iterNext;

        public DSALinkedListIterator(DSALinkedList<E> list)
        {
            iterNext = list.head;
        }

        public boolean hasNext()
        {
            boolean hasNext = false;
            hasNext = (iterNext != null);
            return hasNext;
        }

        public E next()
        {
            E value = null;

            if (iterNext != null)
            {
                value = iterNext.getValue();
                iterNext = iterNext.getNext();
            }
            return value;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    public DSAListNode<E> head;
    public DSAListNode<E> tail;

    public DSALinkedList()
    {
        head = null;
        tail = null;
    }

    public boolean isEmpty()
    {
        boolean isEmpty = false;
        isEmpty = (head == null);
        return isEmpty;
    }

    public void insertFirst(E newValue)
    {
        DSAListNode<E> newNode;
        newNode = new DSAListNode<E>(newValue);

        if (! validateObject(newValue))
        {
            throw new IllegalArgumentException(
                err("Invalid object")
            );
        }
        else
        {
            if (this.isEmpty())
            {
                head = newNode;
                tail = newNode;
            }
            else
            {
                head.setPrev(newNode);
                newNode.setNext(head);
                head = newNode;
            }
        }
    }

    public void insertLast(E newValue)
    {
        DSAListNode<E> newNode, currentNode;
        newNode = new DSAListNode<E>(newValue);

        if (! validateObject(newValue))
        {
            throw new IllegalArgumentException(
                err("Invalid object")
            );
        }
        else
        {
            if (this.isEmpty())
            {
                head = newNode;
                tail = newNode;
            }
            else
            {
                newNode.setPrev(tail);
                tail.setNext(newNode);
                tail = newNode;
            }
        }
    }

    public E peekFirst()
    {
        E nodeValue;

        if (this.isEmpty())
        {
            throw new IllegalArgumentException(
                err("Linked list is empty")
            );
        }
        else
        {
            nodeValue = head.getValue();
        }
        return nodeValue;
    }

    public E peekLast()
    {
        DSAListNode<E> currentNode;
        E nodeValue;

        if (this.isEmpty())
        {
            throw new IllegalArgumentException(
                err("Linked list is empty")
            );
        }
        else
        {
            nodeValue = tail.getValue();
        }
        return nodeValue;
    }

    public E removeFirst()
    {
        E nodeValue;

        if (this.isEmpty())
        {
            throw new IllegalArgumentException(
                err("Linked list is empty")
            );
        }
        else if (tail.getPrev() == null)
        {
            nodeValue = tail.getValue();
            head = null;
            tail = null;
        }
        else
        {
            nodeValue = tail.getValue();
            tail = tail.getPrev();
            tail.setNext(null);
        }
        return nodeValue;
    }

    public E removeLast()
    {
        E nodeValue;

        if (this.isEmpty())
        {
            throw new IllegalArgumentException(
                err("Linked list is empty")
            );
        }
        else if (head.getNext() == null)
        {
            nodeValue = head.getValue();
            head = null;
            tail = null;
        }
        else
        {
            nodeValue = tail.getValue();
            tail = tail.getPrev();
            tail.setNext(null);
        }
        return nodeValue;
    }

    public Iterator<E> iterator()
    {
        return new DSALinkedListIterator<E>(this);
    }

    private boolean validateObject(Object inObj)
    {
        boolean isValid = false;
        isValid = (inObj != null);
        return isValid;
    }

    private String err(String msg)
    {
        String errMsg;
        errMsg = "\u001B[31mNode\u001B[0m: " + msg;
        return errMsg;
    }
}
