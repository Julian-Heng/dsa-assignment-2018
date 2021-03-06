import java.util.*;

/**
 *  Name:     DSAGraph
 *  Source:   Practical 6
 *
 *  Modifications:
 *      Implemented Dijkstra's algorithm and removed unnecessary
 *      functions like the printing functions for adjacency list and
 *      adjacency matrix
 *
 *  Author:   Julian Heng (19473701)
 **/

public class DSAGraph<E,F>
{
    /**
     *  Name:     DSAGraphVertex
     *  Purpose:  A graph vertex node in the graph
     **/

    private class DSAGraphVertex<E,F>
    {
        private String label;
        private E value;
        private DSALinkedList<DSAGraphVertex<E,F>> links;
        private DSALinkedList<DSAGraphEdge<E,F>> edgeList;
        private DSAGraphVertex<E,F> prevVertex;
        private int distanceFromSource;

        // Alternate Constructor
        public DSAGraphVertex(String newLabel, E newValue)
        {
            label = newLabel;
            value = newValue;
            links = new DSALinkedList<DSAGraphVertex<E,F>>();
            edgeList = new DSALinkedList<DSAGraphEdge<E,F>>();
            prevVertex = null;
            distanceFromSource = Integer.MAX_VALUE;
        }

        // Getters
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

            // Loop until edge is found
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

            // Loop until edge is found
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

        // Setters
        public void addEdge(
            DSAGraphVertex<E,F> newVertex,
            int edgeWeight,
            F edgeValue)
        {
            String edgeLabel;
            DSAGraphEdge<E,F> edge;

            if (validateVertex(newVertex))
            {
                // Create edge object
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

    /**
     *  Name:     DSAGraphEdge
     *  Purpose:  A graph edge node to hold edge weight and object
     **/

    private class DSAGraphEdge<E,F>
    {
        String label;
        int weight;
        F edgeValue;
        DSAGraphVertex<E,F> fromVertex, toVertex;

        // Alternate Constructor
        public DSAGraphEdge(String inLabel, int inWeight, F inValue)
        {
            label = inLabel;
            weight = inWeight;
            edgeValue = inValue;
        }

        // Setters
        public void setFrom(DSAGraphVertex<E,F> inFrom)
        {
            fromVertex = inFrom;
        }

        public void setTo(DSAGraphVertex<E,F> inTo)
        {
            toVertex = inTo;
        }

        // Getters
        public String getLabel() { return label; }
        public int getEdgeWeight() { return weight; }
        public F getEdgeValue() { return edgeValue; }

        public DSAGraphVertex<E,F> getFrom() { return fromVertex; }
        public DSAGraphVertex<E,F> getTo() { return toVertex; }
    }

    private DSALinkedList<DSAGraphVertex<E,F>> vertices;
    private int vertexCount;
    private int edgeCount;

    // Default Constructor
    public DSAGraph()
    {
        vertices = new DSALinkedList<DSAGraphVertex<E,F>>();
        vertexCount = 0;
        edgeCount = 0;
    }

    /**
     *  Name:     addVertex
     *  Purpose:  Add a vertex to the list of verticies
     *  Imports:
     *    - newLabel : A String for the label
     *    - newValue : A Generic object for the value contained
     *                 in the vertex
     *  Exports:
     *    - none
     **/

    public void addVertex(String newLabel, E newValue)
    {
        DSAGraphVertex<E,F> newVertex = null;
        if (validateLabel(newLabel))
        {
            newVertex = new DSAGraphVertex<E,F>(newLabel, newValue);
            this.addVertex(newVertex);
        }
    }

    /**
     *  Name:     addVertex
     *  Purpose:  Add a vertex to the list of verticies
     *  Imports:
     *    - inVertex : A vertex object
     *  Exports:
     *    - none
     **/

    public void addVertex(DSAGraphVertex<E,F> inVertex)
    {
        vertices.insertLast(inVertex);
        vertexCount++;
    }

    /**
     *  Name:     addEdge
     *  Purpose:  Add an edge in between 2 vertices
     *  Imports:
     *    - label1     : Source label
     *    - label2     : Target label to connect the edge to
     *    - edgeWeight : The weight of the edge
     *    - edgeValue  : A generic for the value contained in the edge
     *  Exports:
     *    - none
     **/

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

    /**
     *  Name:     addEdge
     *  Purpose:  Add an edge in between 2 verticies
     *  Imports:
     *    - vertex1    : Source vertex
     *    - vertex2    : Target vertex to connect the edge to
     *    - edgeWeight : The weight of the edge
     *    - edgeValue  : A generic for the value contained in the edge
     *  Exports:
     *    - none
     **/

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

    // Getters
    public int getVertexCount() { return vertexCount; }
    public int getEdgeCount() { return edgeCount; }

    /**
     *  Name:     getVertexValue
     *  Purpose:  Get the value in a selected vertex
     *  Imports:
     *    - label : Search label
     *  Exports:
     *    - Generic object
     **/

    public E getVertexValue(String label)
    {
        return this.getVertexValue(getVertex(label));
    }

    /**
     *  Name:     getVertexValue
     *  Purpose:  Get the value in a selected vertex
     *  Imports:
     *    - vertex1 : The search vertex
     *  Exports:
     *    - Generic object
     **/

    public E getVertexValue(DSAGraphVertex<E,F> vertex1)
    {
        return vertex1.getValue();
    }

    /**
     *  Name:     getEdgeValue
     *  Purpose:  Get the value in a selected edge
     *  Imports:
     *    - label1 : Search label 1
     *    - label2 : Search label 2
     *  Exports:
     *    - Generic Object
     **/

    public F getEdgeValue(String label1, String label2)
    {
        DSAGraphVertex<E,F> vertex1 = null;
        DSAGraphVertex<E,F> vertex2 = null;

        E edgeValue;

        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        return this.getEdgeValue(vertex1, vertex2);
    }

    /**
     *  Name:     getEdgeValue
     *  Purpose:  Get the value in a selected edge
     *  Imports:
     *    - vertex1 : The search vertex 1
     *    - vertex2 : The serach vertex 2
     *  Exports:
     *    - Generic object
     **/

    public F getEdgeValue(
        DSAGraphVertex<E,F> vertex1,
        DSAGraphVertex<E,F> vertex2)
    {
        return vertex1.getEdgeValue(vertex2);
    }

    /**
     *  Name:     getEdgeWeight
     *  Purpose:  Get the edge weight in between 2 vertex
     *  Imports:
     *    - label1 : The search label 1
     *    - label2 : The search label 2
     *  Exports:
     *    - Integer
     **/

    public int getEdgeWeight(String label1, String label2)
    {
        DSAGraphVertex<E,F> vertex1 = null;
        DSAGraphVertex<E,F> vertex2 = null;

        E edgeValue;

        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        return this.getEdgeWeight(vertex1, vertex2);
    }

    /**
     *  Name:     getEdgeWeight
     *  Purpose:  Get the edge weight in between 2 vertex
     *  Imports:
     *    - vertex1 : The search vertex 1
     *    - vertex2 : The search vertex 2
     *  Exports:
     *    - Integer
     **/

    public int getEdgeWeight(
        DSAGraphVertex<E,F> vertex1,
        DSAGraphVertex<E,F> vertex2)
    {
        return vertex1.getEdgeWeight(vertex2);
    }

    /**
     *  Name:     getVertex
     *  Purpose:  Get the vertex by label
     *  Imports:
     *    - searchLabel : String for label
     *  Exports:
     *    - findVertex : The vertex with the search label
     **/

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

    /**
     *  Name:     getAdjacent
     *  Purpose:  Get the adjacent of a vertex
     *  Imports:
     *    - vertex : The vertex to get the adjacency list
     *  Exports:
     *    - Linked list of the adjacent vertices
     **/

    public DSALinkedList<DSAGraphVertex<E,F>> getAdjacent(
        DSAGraphVertex<E,F> vertex)
    {
        return vertex.getAdjacent();
    }

    /**
     *  Name:     dijkstra
     *  Purpose:  Get the shortest path from one vertex to another
     *  Imports:
     *    - label1 : A String for the label for the source vertex
     *    - label2 : A String for the label for the target vertex
     *  Exports:
     *    - DSALinkedList which represents the path
     **/

    public DSALinkedList<E> dijkstra(String label1, String label2)
    {
        DSAGraphVertex<E,F> vertex1 = null;
        DSAGraphVertex<E,F> vertex2 = null;

        vertex1 = getVertex(label1);
        vertex2 = getVertex(label2);

        return this.dijkstra(vertex1, vertex2);
    }

    /**
     *  Name:     dijkstra
     *  Purpose:  Get the shortest path from one vertex to another
     *  Imports:
     *    - source : The source vertex
     *    - terget : The target vertex
     *  Exports:
     *    - DSALinkedList which represents the path
     **/

    public DSALinkedList<E> dijkstra(
        DSAGraphVertex<E,F> source,
        DSAGraphVertex<E,F> target)
    {
        DSAGraphVertex<E,F> tempVertex = null;
        DSAGraphVertex<E,F> vertexAdjacent = null;
        Iterator<DSAGraphVertex<E,F>> iter = null;
        Iterator<DSAGraphVertex<E,F>> iterInVertex = null;

        DSALinkedList<E> path;
        DSAMinHeap queue;
        boolean reached = false;
        int alt;

        iter = vertices.iterator();

        // Dijkstra's algorithm
        // Sourced from:
        //     https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Using_a_priority_queue

        // Ideally we would rather change the priority of
        // the items in the queue, but since we did not
        // implement that, we'll just re-add to the queue. Hence
        // the large size
        queue = new DSAMinHeap((int)Math.pow(vertexCount, 2));
        path = new DSALinkedList<E>();

        // Set default values to all vertices and edges and add to the queue
        while (iter.hasNext())
        {
            tempVertex = iter.next();
            tempVertex.setDistanceFromSource(
                (tempVertex != source) ? Integer.MAX_VALUE : 0
            );

            tempVertex.setPrevVertex(null);
            queue.add(tempVertex.getDistanceFromSource(), tempVertex);
        }

        // While queue is not empty and haven't reached
        while (! queue.isEmpty() && ! reached)
        {
            // Get vertex with the least distance from source
            tempVertex = (DSAGraph<E,F>.DSAGraphVertex<E,F>)queue.remove();
            iter = tempVertex.getAdjacent().iterator();

            // For each adjacent vertex
            while (iter.hasNext() && ! reached)
            {
                vertexAdjacent = iter.next();

                // If it is the target, set to true
                if (vertexAdjacent == target)
                {
                    reached = true;
                }

                if (tempVertex != vertexAdjacent.getPrevVertex())
                {
                    // Get distance from source
                    alt = tempVertex.getDistanceFromSource() +
                        this.getEdgeWeight(
                            tempVertex,
                            vertexAdjacent
                        );

                    // If distance is shorter than the recorded distance
                    // from source
                    if (alt < vertexAdjacent.getDistanceFromSource())
                    {
                        // Set distance and the vertex to access it
                        vertexAdjacent.setDistanceFromSource(alt);
                        vertexAdjacent.setPrevVertex(tempVertex);
                        queue.add(alt, vertexAdjacent);
                    }
                }
            }
        }

        // Starting at the target, insert previous vertex
        // to the end of the linked list until we've reached
        // the source
        tempVertex = target;

        while (tempVertex != source)
        {
            path.insertFirst(tempVertex.getValue());
            tempVertex = tempVertex.getPrevVertex();
        }

        return path;
    }

    /**
     *  Name:     validateLabel
     *  Purpose:  Check if label is valid
     *  Imports:
     *    - inLabel : A string for the label
     *  Exports:
     *    - isUnique : Boolean
     **/

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

    /**
     *  Name:     err
     *  Purpose:  Formats an error line
     *  Imports:
     *    - msg : The error message
     *  Exports:
     *    - Formatted string
     **/

    private String err(String msg)
    {
        return "\u001B[31mError\u001B[0m: " + msg;
    }
}
