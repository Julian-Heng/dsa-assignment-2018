import java.util.*;
import java.io.*;

public class DSAGraph<E> implements Serializable
{
    private class DSAGraphVertex<E> implements Serializable
    {
        private String label;
        private E value;
        private DSALinkedList<DSAGraphVertex<E>> links;
        private boolean visited;

        public DSAGraphVertex(String newLabel, E newValue)
        {
            label = newLabel;
            value = newValue;
            links = new DSALinkedList<DSAGraphVertex<E>>();
            visited = false;
        }

        public String getLabel() { return label; }
        public E getValue() { return value; }

        public DSALinkedList<DSAGraphVertex<E>> getAdjacent()
        {
            return links;
        }

        public void addEdge(DSAGraphVertex<E> newVertex)
        {
            if (validateVertex(newVertex))
            {
                links.insertLast(newVertex);
            }
        }

        public void setVisited() { visited = true; }
        public void clearVisited() { visited = false; }
        public boolean getVisited() { return visited; }

        public String toString()
        {
            String out;
            DSAGraphVertex<E> linkNode = null;
            Iterator<DSAGraphVertex<E>> iter = links.iterator();

            out = "Label: " + label + "\n"
                + "Value: " + value + "\n"
                + "Links: ";

            while (iter.hasNext())
            {
                out += iter.next().getLabel() + ", ";
            }
            out = out.substring(0, out.length() - 2) + "\n";
            return out;
        }

        private boolean validateVertex(
            DSAGraphVertex<E> newVertex)
        {
            DSAGraphVertex<E> linkNode = null;
            Iterator<DSAGraphVertex<E>> iter = links.iterator();
            boolean isUnique = true;

            while ((iter.hasNext()) && (isUnique))
            {
                linkNode = iter.next();
                if (linkNode == newVertex)
                {
                    isUnique = false;
                }
            }

            return isUnique;
        }
    }

    private DSALinkedList<DSAGraphVertex<E>> vertices;
    private int vertexCount;
    private int edgeCount;

    public DSAGraph()
    {
        vertices = new DSALinkedList<DSAGraphVertex<E>>();
        vertexCount = 0;
        edgeCount = 0;
    }

    public void addVertex(String newLabel, E newValue)
    {
        DSAGraphVertex<E> newVertex = null;
        if (validateLabel(newLabel))
        {
            newVertex = new DSAGraphVertex<E>(newLabel, newValue);
            vertices.insertLast(newVertex);
            vertexCount++;
        }
    }

    public void addEdge(
        DSAGraphVertex<E> vertex1,
        DSAGraphVertex<E> vertex2)
    {
        vertex1.addEdge(vertex2);
        edgeCount++;
    }

    public void addEdge(String label1, String label2)
    {
        DSAGraphVertex<E> vertex1 = null;
        DSAGraphVertex<E> vertex2 = null;
        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        this.addEdge(vertex1, vertex2);
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public int getEdgeCount()
    {
        return edgeCount;
    }

    public DSAGraphVertex<E> getVertex(String searchLabel)
    {
        DSAGraphVertex<E> findVertex = null;
        DSAGraphVertex<E> inVertex = null;
        Iterator<DSAGraphVertex<E>> iter = vertices.iterator();
        boolean isFound = false;

        while ((iter.hasNext()) && (! isFound))
        {
            inVertex = iter.next();
            if (inVertex.getLabel().equals(searchLabel))
            {
                findVertex = inVertex;
                isFound = true;
            }
        }

        if (findVertex == null)
        {
            throw new IllegalArgumentException(err("Label is not found"));
        }

        return findVertex;
    }

    public DSALinkedList<DSAGraphVertex<E>> getAdjacent(
        DSAGraphVertex<E> vertex)
    {
        return vertex.getAdjacent();
    }

    public boolean isAdjacent(
        DSAGraphVertex<E> vertex1,
        DSAGraphVertex<E> vertex2)
    {
        boolean isAdjacent = false;
        DSAGraphVertex<E> inVertex = null;
        Iterator<DSAGraphVertex<E>> iter = vertex2.getAdjacent().iterator();

        while ((iter.hasNext()) && (isAdjacent != true))
        {
            inVertex = iter.next();
            if (vertex1 == inVertex)
            {
                isAdjacent = true;
            }
        }

        return isAdjacent;
    }

    public void displayStructure()
    {
        System.out.print(this.getStructure());
    }

    public String getStructure()
    {
        DSAGraphVertex<E> inVertex = null;
        DSAGraph<E>.DSAGraphVertex<E> inNodeVertex = null;
        Iterator<DSAGraphVertex<E>> iter = vertices.iterator();
        Iterator<DSAGraphVertex<E>> nodeIter = null;
        String raw = "";

        while (iter.hasNext())
        {
            inVertex = iter.next();
            nodeIter = inVertex.getAdjacent().iterator();
            while (nodeIter.hasNext())
            {
                inNodeVertex = nodeIter.next();
                raw += inVertex.getLabel() + " "
                     + inNodeVertex.getLabel() + "\n";
            }
        }

        return raw.trim();
    }

    public void displayList()
    {
        System.out.print(this.getList());
    }

    public void displayMatrix()
    {
        System.out.print(this.getMatrix());
    }

    public String getList()
    {
        DSAGraphVertex<E> inVertex = null;
        DSAGraph<E>.DSAGraphVertex<E> inNodeVertex = null;
        Iterator<DSAGraphVertex<E>> iter = vertices.iterator();
        Iterator<DSAGraphVertex<E>> nodeIter = null;
        String temp, out = "";

        while (iter.hasNext())
        {
            temp = "";
            inVertex = iter.next();
            nodeIter = inVertex.getAdjacent().iterator();
            while (nodeIter.hasNext())
            {
                temp += nodeIter.next().getLabel() + ", ";
            }

            if (temp.length() > 1)
            {
                temp = temp.substring(0, temp.length() - 2);
            }

            out += inVertex.getLabel() + " | " + temp + "\n";
        }

        return out.trim();
    }

    public String getMatrix()
    {
        DSAGraphVertex<E> inVertex1 = null;
        DSAGraphVertex<E> inVertex2 = null;
        Iterator<DSAGraphVertex<E>> iter1 = vertices.iterator();
        Iterator<DSAGraphVertex<E>> iter2 = null;

        int vertexLoop = 0;
        int nodeLoop = 0;

        String[][] raw = new String[vertexCount][vertexCount];
        String[] vertexLabels = new String[vertexCount];
        String out;

        for (int i = 0; i < vertexCount; i++)
        {
            for (int j = 0; j < vertexCount; j++)
            {
                raw[i][j] = "✗";
            }
        }

        while (iter1.hasNext())
        {
            nodeLoop = 0;
            inVertex1 = iter1.next();
            vertexLabels[vertexLoop] = inVertex1.getLabel();

            iter2 = vertices.iterator();

            while (iter2.hasNext())
            {
                inVertex2 = iter2.next();
                if (this.isAdjacent(inVertex1, inVertex2))
                {
                    raw[vertexLoop][nodeLoop] = "✔";
                }
                nodeLoop++;
            }
            vertexLoop++;
        }

        out = formatMatrix(raw, vertexLabels);
        return out;
    }

    public void displayDepthFirstSearch()
    {
        System.out.println(this.depthFirstSearch());
    }

    public void displayBreadthFirstSearch()
    {
        System.out.println(this.breadthFirstSearch());
    }

    public String depthFirstSearch()
    {
        DSAStack<DSAGraphVertex<E>> stack;
        DSAGraphVertex<E> inVertex = null;
        DSAGraphVertex<E> tempVertex = null;

        String path = "";
        String smallestKey = "";
        String startLabel = "";
        String endLabel = "";

        stack = new DSAStack<DSAGraphVertex<E>>();

        markAllNew();
        smallestKey = getSmallestKey();
        inVertex = getVertex(smallestKey);
        inVertex.setVisited();

        stack.push(inVertex);

        while (! stack.isEmpty())
        {
            inVertex = stack.top();
            tempVertex = getNextNewAdjacent(inVertex);
            while (tempVertex != null)
            {
                startLabel = inVertex.getLabel();
                endLabel = tempVertex.getLabel();
                path += startLabel + " -> " + endLabel + ", ";

                tempVertex.setVisited();
                stack.push(tempVertex);

                inVertex = tempVertex;
                tempVertex = getNextNewAdjacent(inVertex);
            }
            stack.pop();
        }

        if (path.length() > 1)
        {
            path = path.substring(0, path.length() - 2);
        }

        return path;
    }

    public String breadthFirstSearch()
    {
        DSAQueue<DSAGraphVertex<E>> queue;
        DSAGraphVertex<E> inVertex = null;
        DSAGraphVertex<E> tempVertex = null;
        Iterator <DSAGraphVertex<E>> iter = null;

        String path = "";
        String smallestKey = "";
        String startLabel = "";
        String endLabel = "";

        queue = new DSAQueue<DSAGraphVertex<E>>();

        markAllNew();
        smallestKey = getSmallestKey();
        inVertex = getVertex(smallestKey);
        inVertex.setVisited();

        queue.enqueue(inVertex);

        while (! queue.isEmpty())
        {
            inVertex = queue.dequeue();
            startLabel = inVertex.getLabel();
            iter = inVertex.getAdjacent().iterator();

            while (iter.hasNext())
            {
                tempVertex = iter.next();
                if (! tempVertex.getVisited())
                {
                    endLabel = tempVertex.getLabel();
                    path += startLabel + " -> " + endLabel + ", ";

                    tempVertex.setVisited();
                    queue.enqueue(tempVertex);
                }
            }
        }

        if (path.length() > 1)
        {
            path = path.substring(0, path.length() - 2);
        }

        return path;
    }

    private void markAllNew()
    {
        DSAGraphVertex<E> inVertex = null;
        Iterator<DSAGraphVertex<E>> iter = vertices.iterator();

        while (iter.hasNext())
        {
            inVertex = iter.next();
            inVertex.clearVisited();
        }
    }

    private String getSmallestKey()
    {
        DSAGraphVertex<E> inVertex = null;
        Iterator<DSAGraphVertex<E>> iter = vertices.iterator();
        String currentKey, smallestKey;

        currentKey = "";
        smallestKey = "";

        while (iter.hasNext())
        {
            inVertex = iter.next();
            currentKey = inVertex.getLabel();
            if (
                (
                    smallestKey.equals("")
                ) || (
                    currentKey.compareTo(smallestKey) < 0
                ))
            {
                smallestKey = currentKey;
            }
        }

        return smallestKey;
    }

    private DSAGraphVertex<E> getNextNewAdjacent(
        DSAGraphVertex<E> vertex)
    {
        Iterator<DSAGraphVertex<E>> iter = vertex.getAdjacent().iterator();
        DSAGraphVertex<E> inVertex = null;
        DSAGraphVertex<E> unvisitedVertex = null;

        while (iter.hasNext() && unvisitedVertex == null)
        {
            inVertex = iter.next();
            if (! inVertex.getVisited())
            {
                unvisitedVertex = inVertex;
            }
        }

        return unvisitedVertex;
    }

    private String formatMatrix(String[][] raw, String[] vertexLabels)
    {
        String padding, topSep, vSep, bottomSep, out;
        int maxLabelLength = 0;

        maxLabelLength = calcMaxStringArrLength(vertexLabels);

        padding = "%" + maxLabelLength + "s";
        out = "│" + formatPadding(padding);
        topSep = "┌" + formatPadding(padding, '─');
        vSep = "├" + formatPadding(padding, '─');
        bottomSep = "└" + formatPadding(padding, '─');

        for (int i = 0; i < vertexCount - 1; i++)
        {
            topSep += "┬" + formatPadding(padding, '─');
            vSep += "┼" + formatPadding(padding, '─');
            bottomSep += "┴" + formatPadding(padding, '─');

            out += "│"
                + String.format(
                    padding,
                    vertexLabels[i]
                );
        }

        topSep += "┬" + formatPadding(padding, '─') + "┐";
        vSep += "┼" + formatPadding(padding, '─') + "┤";
        bottomSep += "┴" + formatPadding(padding, '─') + "┘";

        out = topSep + "\n"
            + out + "│"
            + String.format(
                padding,
                vertexLabels[vertexCount - 1]
            )
            + "│\n";

        for (int i = 0; i < vertexCount; i++)
        {
            out += vSep + "\n";

            out += "│"
                + String.format(
                    padding,
                    vertexLabels[i]
                );

            for (int j = 0; j < raw[i].length - 1; j++)
            {
                out += "│"
                    + String.format(
                        padding,
                        raw[i][j]
                    );
            }

            out += "│"
                + String.format(
                    padding,
                    raw[i][raw[i].length - 1]
                ) + "│\n";
        }

        out += bottomSep;

        return out.trim();
    }

    private int calcMaxStringArrLength(String[] arr)
    {
        int length, maxLength;
        length = 0;
        maxLength = 0;

        for (String i : arr)
        {
            length = i.length();
            if (length > maxLength)
            {
                maxLength = length;
            }
        }

        return maxLength;
    }

    private String formatPadding(String padding)
    {
        return String.format(padding, " ");
    }

    private String formatPadding(String padding, char line)
    {
        return String.format(padding, " ").replace(' ', line);
    }

    private boolean validateLabel(String inLabel)
    {
        boolean isUnique = true;
        DSAGraphVertex<E> inVertex = null;
        Iterator<DSAGraphVertex<E>> iter = vertices.iterator();

        if (vertexCount != 0)
        {
            while ((iter.hasNext()) && (isUnique))
            {
                inVertex = iter.next();
                if (inVertex.getLabel().equals(inLabel))
                {
                    isUnique = false;
                }
            }
        }

        return isUnique;
    }

    private String err(String msg)
    {
        return "\u001B[31mError\u001B[0m: " + msg;
    }
}
