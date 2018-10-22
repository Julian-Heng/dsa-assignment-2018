import java.util.*;
import java.io.*;

public class DSAGraph<E,F>
{
    private class DSAGraphVertex<E,F>
    {
        private String label;
        private E value;
        private DSALinkedList<DSAGraphVertex<E,F>> links;
        private DSALinkedList<DSAGraphEdge<E,F>> edgeList;
        private DSAGraphVertex<E,F> prevVertex;
        private int distanceFromSource;
        private boolean visited;

        public DSAGraphVertex(String newLabel, E newValue)
        {
            label = newLabel;
            value = newValue;
            links = new DSALinkedList<DSAGraphVertex<E,F>>();
            edgeList = new DSALinkedList<DSAGraphEdge<E,F>>();
            prevVertex = null;
            distanceFromSource = Integer.MAX_VALUE;
            visited = false;
        }

        public String getLabel() { return label; }
        public E getValue() { return value; }

        public F getEdgeValue(DSAGraphVertex<E,F> toVertex)
        {
            Iterator<DSAGraphEdge<E,F>> iter;
            DSAGraphEdge<E,F> inEdgeList;
            F edgeValue;
            boolean match;

            iter = edgeList.iterator();
            edgeValue = null;
            match = false;

            while (iter.hasNext() && ! match)
            {
                inEdgeList = iter.next();

                if (inEdgeList.getTo() == toVertex)
                {
                    match = true;
                    edgeValue = inEdgeList.getEdgeValue();
                }
            }

            return edgeValue;
        }

        public int getEdgeWeight(DSAGraphVertex<E,F> toVertex)
        {
            Iterator<DSAGraphEdge<E,F>> iter;
            DSAGraphEdge<E,F> inEdgeList;
            int edgeWeight = -1;
            boolean match;

            iter = edgeList.iterator();
            match = false;

            while (iter.hasNext() && ! match)
            {
                inEdgeList = iter.next();

                if (inEdgeList.getTo() == toVertex)
                {
                    match = true;
                    edgeWeight = inEdgeList.getEdgeWeight();
                }
            }

            return edgeWeight;
        }

        public DSALinkedList<DSAGraphVertex<E,F>> getAdjacent()
        {
            return links;
        }

        public DSALinkedList<DSAGraphEdge<E,F>> getEdges()
        {
            return edgeList;
        }

        public void addEdge(
            DSAGraphVertex<E,F> newVertex,
            int edgeWeight,
            F edgeValue)
        {
            String edgeLabel;
            DSAGraphEdge<E,F> edge;

            if (validateVertex(newVertex))
            {
                edgeLabel = label + " <-> " + newVertex.getLabel();
                edge = new DSAGraphEdge<E,F>(edgeLabel, edgeWeight, edgeValue);
                edge.setFrom(this);
                edge.setTo(newVertex);

                links.insertLast(newVertex);
                edgeList.insertLast(edge);
            }
        }

        public void setPrevVertex(DSAGraphVertex<E,F> inPrev)
        {
            prevVertex = inPrev;
        }

        public void setDistanceFromSource(int inDist)
        {
            distanceFromSource = inDist;
        }

        public DSAGraphVertex<E,F> getPrevVertex() { return prevVertex; }
        public int getDistanceFromSource() { return distanceFromSource; }

        public void setVisited() { visited = true; }
        public void clearVisited() { visited = false; }
        public boolean getVisited() { return visited; }

        public String toString()
        {
            String out;
            DSAGraphVertex<E,F> linkNode = null;
            Iterator<DSAGraphVertex<E,F>> iter = links.iterator();

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
            DSAGraphVertex<E,F> newVertex)
        {
            DSAGraphVertex<E,F> linkNode = null;
            Iterator<DSAGraphVertex<E,F>> iter = links.iterator();
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

    private class DSAGraphEdge<E,F>
    {
        String label;
        int weight;
        F edgeValue;
        DSAGraphVertex<E,F> fromVertex, toVertex;
        boolean visited;

        public DSAGraphEdge(String inLabel, int inWeight, F inValue)
        {
            label = inLabel;
            weight = inWeight;
            edgeValue = inValue;
            visited = false;
        }

        public void setVisited() { visited = true; }
        public void clearVisited() { visited = false; }

        public void setFrom(DSAGraphVertex<E,F> inFrom)
        {
            fromVertex = inFrom;
        }

        public void setTo(DSAGraphVertex<E,F> inTo)
        {
            toVertex = inTo;
        }

        public String getLabel() { return label; }
        public int getEdgeWeight() { return weight; }
        public F getEdgeValue() { return edgeValue; }
        public boolean getVisited() { return visited; }

        public DSAGraphVertex<E,F> getFrom() { return fromVertex; }
        public DSAGraphVertex<E,F> getTo() { return toVertex; }
    }

    private DSALinkedList<DSAGraphVertex<E,F>> vertices;
    private int vertexCount;
    private int edgeCount;

    public DSAGraph()
    {
        vertices = new DSALinkedList<DSAGraphVertex<E,F>>();
        vertexCount = 0;
        edgeCount = 0;
    }

    public void addVertex(String newLabel, E newValue)
    {
        DSAGraphVertex<E,F> newVertex = null;
        if (validateLabel(newLabel))
        {
            newVertex = new DSAGraphVertex<E,F>(newLabel, newValue);
            this.addVertex(newVertex);
        }
    }

    public void addVertex(DSAGraphVertex<E,F> inVertex)
    {
        vertices.insertLast(inVertex);
        vertexCount++;
    }

    public void addEdge(
        DSAGraphVertex<E,F> vertex1,
        DSAGraphVertex<E,F> vertex2,
        int edgeWeight,
        F edgeValue)
    {
        vertex1.addEdge(vertex2, edgeWeight, edgeValue);
        vertex2.addEdge(vertex1, edgeWeight, edgeValue);
        edgeCount++;
    }

    public void addEdge(
        String label1,
        String label2,
        int edgeWeight,
        F edgeValue)
    {
        DSAGraphVertex<E,F> vertex1 = null;
        DSAGraphVertex<E,F> vertex2 = null;
        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        this.addEdge(vertex1, vertex2, edgeWeight, edgeValue);
    }

    public E getVertexValue(String label)
    {
        return this.getVertexValue(getVertex(label));
    }

    public E getVertexValue(DSAGraphVertex<E,F> vertex1)
    {
        return vertex1.getValue();
    }

    public F getEdgeValue(
        DSAGraphVertex<E,F> vertex1,
        DSAGraphVertex<E,F> vertex2)
    {
        return vertex1.getEdgeValue(vertex2);
    }

    public F getEdgeValue(String label1, String label2)
    {
        DSAGraphVertex<E,F> vertex1 = null;
        DSAGraphVertex<E,F> vertex2 = null;

        E edgeValue;

        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        return this.getEdgeValue(vertex1, vertex2);
    }

    public int getEdgeWeight(
        DSAGraphVertex<E,F> vertex1,
        DSAGraphVertex<E,F> vertex2)
    {
        return vertex1.getEdgeWeight(vertex2);
    }

    public int getEdgeWeight(String label1, String label2)
    {
        DSAGraphVertex<E,F> vertex1 = null;
        DSAGraphVertex<E,F> vertex2 = null;

        E edgeValue;

        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        return this.getEdgeWeight(vertex1, vertex2);
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public int getEdgeCount()
    {
        return edgeCount;
    }

    public DSAGraphVertex<E,F> getVertex(String searchLabel)
    {
        DSAGraphVertex<E,F> findVertex = null;
        DSAGraphVertex<E,F> inVertex = null;
        Iterator<DSAGraphVertex<E,F>> iter = vertices.iterator();
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

    public DSALinkedList<DSAGraphVertex<E,F>> getAdjacent(
        DSAGraphVertex<E,F> vertex)
    {
        return vertex.getAdjacent();
    }

    public boolean isAdjacent(
        DSAGraphVertex<E,F> vertex1,
        DSAGraphVertex<E,F> vertex2)
    {
        boolean isAdjacent = false;
        DSAGraphVertex<E,F> inVertex = null;
        Iterator<DSAGraphVertex<E,F>> iter = vertex2.getAdjacent().iterator();

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
        DSAStack<DSAGraphVertex<E,F>> stack;
        DSAGraphVertex<E,F> inVertex = null;
        DSAGraphVertex<E,F> tempVertex = null;

        String path = "";
        String smallestKey = "";
        String startLabel = "";
        String endLabel = "";

        stack = new DSAStack<DSAGraphVertex<E,F>>();

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
        DSAQueue<DSAGraphVertex<E,F>> queue;
        DSAGraphVertex<E,F> inVertex = null;
        DSAGraphVertex<E,F> tempVertex = null;
        Iterator <DSAGraphVertex<E,F>> iter = null;

        String path = "";
        String smallestKey = "";
        String startLabel = "";
        String endLabel = "";

        queue = new DSAQueue<DSAGraphVertex<E,F>>();

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

    public DSAStack<E> dijkstra(String label1, String label2)
    {
        DSAGraphVertex<E,F> vertex1 = null;
        DSAGraphVertex<E,F> vertex2 = null;

        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        return this.dijkstra(vertex1, vertex2);
    }

    public DSAStack<E> dijkstra(
        DSAGraphVertex<E,F> source,
        DSAGraphVertex<E,F> target)
    {
        DSAGraphVertex<E,F> tempVertex = null;
        DSAGraphVertex<E,F> vertexAdjacent = null;
        Iterator<DSAGraphVertex<E,F>> iter = null;
        Iterator<DSAGraphVertex<E,F>> iterInVertex = null;

        DSAStack<E> path;
        DSAMinHeap queue;
        boolean reached = false;
        int alt;

        markAllNew();

        iter = vertices.iterator();
        queue = new DSAMinHeap((int)Math.pow(vertexCount, 2));
        path = new DSAStack<E>();

        while (iter.hasNext())
        {
            tempVertex = iter.next();

            if (tempVertex == source)
            {
                tempVertex.setDistanceFromSource(0);
            }
            else
            {
                tempVertex.setDistanceFromSource(Integer.MAX_VALUE);
            }

            tempVertex.setPrevVertex(null);
            queue.add(tempVertex.getDistanceFromSource(), tempVertex);
        }

        while (! queue.isEmpty() && ! reached)
        {
            tempVertex = (DSAGraph<E,F>.DSAGraphVertex<E,F>)queue.remove();
            iter = tempVertex.getAdjacent().iterator();

            while (iter.hasNext() && ! reached)
            {
                vertexAdjacent = iter.next();

                if (vertexAdjacent == target)
                {
                    reached = true;
                }

                if (! vertexAdjacent.getVisited())
                {
                    vertexAdjacent.setVisited();
                    alt = tempVertex.getDistanceFromSource()
                        + this.getEdgeWeight(
                            tempVertex,
                            vertexAdjacent
                    );

                    if (alt < vertexAdjacent.getDistanceFromSource())
                    {
                        vertexAdjacent.setDistanceFromSource(alt);
                        vertexAdjacent.setPrevVertex(tempVertex);
                        queue.add(alt, vertexAdjacent);
                    }
                }
            }
        }

        tempVertex = target;

        while (tempVertex != source)
        {
            path.push(tempVertex.getValue());
            tempVertex = tempVertex.getPrevVertex();
        }

        iter = vertices.iterator();

        while (iter.hasNext())
        {
            tempVertex = iter.next();
            tempVertex.setDistanceFromSource(Integer.MAX_VALUE);
            tempVertex.setPrevVertex(null);
        }

        return path;
    }

    private void markAllNew()
    {
        DSAGraphVertex<E,F> inVertex = null;
        DSAGraphEdge<E,F> inEdge;
        Iterator<DSAGraphVertex<E,F>> iter = vertices.iterator();
        Iterator<DSAGraphEdge<E,F>> edgeIter;

        while (iter.hasNext())
        {
            inVertex = iter.next();
            inVertex.clearVisited();
            edgeIter = inVertex.getEdges().iterator();
            while (edgeIter.hasNext())
            {
                inEdge = edgeIter.next();
                inEdge.clearVisited();
            }
        }
    }

    private String getSmallestKey()
    {
        DSAGraphVertex<E,F> inVertex = null;
        Iterator<DSAGraphVertex<E,F>> iter = vertices.iterator();
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

    private DSAGraphVertex<E,F> getNextNewAdjacent(
        DSAGraphVertex<E,F> vertex)
    {
        Iterator<DSAGraphVertex<E,F>> iter = vertex.getAdjacent().iterator();
        DSAGraphVertex<E,F> inVertex = null;
        DSAGraphVertex<E,F> unvisitedVertex = null;

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

    private boolean validateLabel(String inLabel)
    {
        boolean isUnique = true;
        DSAGraphVertex<E,F> inVertex = null;
        Iterator<DSAGraphVertex<E,F>> iter = vertices.iterator();

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
