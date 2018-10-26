/**
 *  Name:     testDSAGraph
 *  Source:   Practical 6
 *
 *  Modifications:
 *      Changed testing in order to reflect changes to the graph
 *      such as having an edge class and testing dijkstra's algorithm
 *
 *  Author:   Julian Heng (19473701)
 **/

public class testDSAGraph
{
    public static void main(String[] args)
    {
        run();
    }

    public static void run()
    {
        testHarnessCommons.header("Testing DSAGraph class");
        testConstructor();
        testInsertVertex();
        testInsertEdge();
        testDijkstra();
    }

    public static void testConstructor()
    {
        DSAGraph<Integer,Integer> graph = null;
        int vertex, edge;

        System.out.print("Testing default constructor: ");

        try
        {
            graph = new DSAGraph<Integer,Integer>();
            vertex = graph.getVertexCount();
            edge = graph.getEdgeCount();

            if (vertex != 0)
            {
                testHarnessCommons.failed("Vertex count is incorrect");
            }
            else if (edge != 0)
            {
                testHarnessCommons.failed("Edge count is incorrect");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testInsertVertex()
    {
        DSAGraph<Integer,Integer> graph = null;
        String label;
        int vertex, edge;
        int[] num = {
            1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048
        };

        for (int i : num)
        {
            System.out.print(
                "Testing addVertex() with " + i + " nodes: "
            );

            try
            {
                graph = new DSAGraph<Integer,Integer>();

                for (int j = 0; j < i; j++)
                {
                    label = genString(j);
                    graph.addVertex(label, Integer.valueOf(j));
                }

                vertex = graph.getVertexCount();
                edge = graph.getEdgeCount();

                if (vertex != i)
                {
                    testHarnessCommons.failed("Vertex count is incorrect");
                }
                else if (edge != 0)
                {
                    testHarnessCommons.failed("Edge count is incorrect");
                }
                else
                {
                    testHarnessCommons.passed();
                }
            }
            catch (Exception e)
            {
                testHarnessCommons.failed(e.getMessage());
            }
        }

        for (int i : num)
        {
            System.out.print(
                "Testing addVertex() with " + i + " nodes [Same key]: "
            );

            try
            {
                graph = new DSAGraph<Integer,Integer>();

                for (int j = 0; j < i; j++)
                {
                    graph.addVertex("a", Integer.valueOf(j));
                }

                vertex = graph.getVertexCount();
                edge = graph.getEdgeCount();

                if (vertex != 1)
                {
                    testHarnessCommons.failed("Vertex count is incorrect");
                }
                else if (edge != 0)
                {
                    testHarnessCommons.failed("Edge count is incorrect");
                }
                else
                {
                    testHarnessCommons.passed();
                }
            }
            catch (Exception e)
            {
                testHarnessCommons.failed(e.getMessage());
            }
        }
    }

    public static void testInsertEdge()
    {
        DSAGraph<Integer,Integer> graph = null;
        Integer[] edgeValueArr;
        Integer edgeValue, returnValue;
        String label1, label2;
        int vertex, edge;
        int[] num = {
            1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048
        };

        System.out.print("Testing addEdge(String) with 2 nodes: ");

        try
        {
            graph = new DSAGraph<Integer,Integer>();
            edgeValue = Integer.valueOf(2);
            graph.addVertex("a", Integer.valueOf(1));
            graph.addVertex("b", Integer.valueOf(1));
            graph.addEdge("a", "b", 1, edgeValue);

            vertex = graph.getVertexCount();
            edge = graph.getEdgeCount();
            returnValue = graph.getEdgeValue("a", "b");

            if (vertex != 2)
            {
                testHarnessCommons.failed("Vertex count is incorrect");
            }
            else if (edge != 1)
            {
                testHarnessCommons.failed("Edge count is incorrect");
            }
            else if (returnValue != edgeValue)
            {
                testHarnessCommons.failed("Edge value is incorrect");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        for (int i = 2; i < num.length; i++)
        {
            System.out.print(
                "Testing addEdge(String) with " + num[i] + " nodes: "
            );

            try
            {
                graph = new DSAGraph<Integer,Integer>();
                edgeValueArr = new Integer[(num[i] / 2)];

                for (int j = 0; j < num[i]; j += 2)
                {
                    edgeValue = Integer.valueOf(num[i]);
                    edgeValueArr[(j / 2)] = edgeValue;
                    label1 = genString(j);
                    label2 = genString(j + 1);
                    graph.addVertex(label1, Integer.valueOf(j));
                    graph.addVertex(label2, Integer.valueOf(j + 1));
                    graph.addEdge(label1, label2, 1, edgeValue);
                }

                vertex = graph.getVertexCount();
                edge = graph.getEdgeCount();

                if (vertex != num[i])
                {
                    testHarnessCommons.failed("Vertex count is incorrect");
                }
                else if (edge != (num[i] / 2))
                {
                    testHarnessCommons.failed("Edge count is incorrect");
                }
                else
                {
                    for (int j = 0; j < num[i]; j += 2)
                    {
                        label1 = genString(j);
                        label2 = genString(j + 1);
                        returnValue = graph.getEdgeValue(label1, label2);
                        if (returnValue != edgeValueArr[(j / 2)])
                        {
                            throw new IllegalArgumentException(
                                "Edge value is incorrect"
                            );
                        }
                    }

                    testHarnessCommons.passed();
                }
            }
            catch (Exception e)
            {
                testHarnessCommons.failed(e.getMessage());
            }
        }

        System.out.print("Testing addEdge(Vertex) with 2 nodes: ");

        try
        {
            graph = new DSAGraph<Integer,Integer>();
            graph.addVertex("a", Integer.valueOf(1));
            graph.addVertex("b", Integer.valueOf(1));
            edgeValue = Integer.valueOf(2);

            graph.addEdge(
                graph.getVertex("a"),
                graph.getVertex("b"),
                1,
                edgeValue
            );

            vertex = graph.getVertexCount();
            edge = graph.getEdgeCount();
            returnValue = graph.getEdgeValue(
                    graph.getVertex("a"),
                    graph.getVertex("b")
            );

            if (vertex != 2)
            {
                testHarnessCommons.failed("Vertex count is incorrect");
            }
            else if (edge != 1)
            {
                testHarnessCommons.failed("Edge count is incorrect");
            }
            else if (returnValue != edgeValue)
            {
                testHarnessCommons.failed("Edge value is incorrect");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }

        for (int i = 2; i < num.length; i++)
        {
            System.out.print(
                "Testing addEdge(Vertex) with " + num[i] + " nodes: "
            );

            try
            {
                graph = new DSAGraph<Integer,Integer>();
                edgeValueArr = new Integer[(num[i] / 2)];

                for (int j = 0; j < num[i]; j += 2)
                {
                    edgeValue = Integer.valueOf(num[i]);
                    edgeValueArr[(j / 2)] = edgeValue;
                    label1 = genString(j);
                    label2 = genString(j + 1);
                    graph.addVertex(label1, Integer.valueOf(j));
                    graph.addVertex(label2, Integer.valueOf(j + 1));
                    graph.addEdge(
                        graph.getVertex(label1),
                        graph.getVertex(label2),
                        1,
                        edgeValue
                    );
                }

                vertex = graph.getVertexCount();
                edge = graph.getEdgeCount();

                if (vertex != num[i])
                {
                    testHarnessCommons.failed("Vertex count is incorrect");
                }
                else if (edge != (num[i] / 2))
                {
                    testHarnessCommons.failed("Edge count is incorrect");
                }
                else
                {
                    for (int j = 0; j < num[i]; j += 2)
                    {
                        label1 = genString(j);
                        label2 = genString(j + 1);
                        returnValue = graph.getEdgeValue(
                                graph.getVertex(label1),
                                graph.getVertex(label2)
                        );

                        if (returnValue != edgeValueArr[(j / 2)])
                        {
                            throw new IllegalArgumentException(
                                "Edge value is incorrect"
                            );
                        }
                    }

                    testHarnessCommons.passed();
                }
            }
            catch (Exception e)
            {
                testHarnessCommons.failed(e.getMessage());
            }
        }

        System.out.print("Testing addEdge(String) with 2 edges: ");

        try
        {
            graph = new DSAGraph<Integer,Integer>();
            edgeValueArr = new Integer[2];

            edgeValueArr[0] = Integer.valueOf(1);
            edgeValueArr[1] = Integer.valueOf(2);

            graph.addVertex("a", Integer.valueOf(3));
            graph.addVertex("b", Integer.valueOf(3));
            graph.addVertex("c", Integer.valueOf(3));
            graph.addEdge("a", "b", 1, edgeValueArr[0]);
            graph.addEdge("a", "c", 1, edgeValueArr[1]);

            vertex = graph.getVertexCount();
            edge = graph.getEdgeCount();

            if (vertex != 3)
            {
                testHarnessCommons.failed("Vertex count is incorrectl");
            }
            else if (edge != 2)
            {
                testHarnessCommons.failed("Edge count is incorrect");
            }
            else if (graph.getEdgeValue("a", "b") != edgeValueArr[0])
            {
                testHarnessCommons.failed("Edge value between a and b is incorrect");
            }
            else if (graph.getEdgeValue("a", "c") != edgeValueArr[1])
            {
                testHarnessCommons.failed("Edge value between a and c is incorrect");
            }
            else
            {
                testHarnessCommons.passed();
            }
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static void testDijkstra()
    {
        DSAGraph<String,Integer> graph;
        DSAStack<String> path;

        System.out.print("Testing dijkstra(): ");

        try
        {
            graph = new DSAGraph<String,Integer>();

            graph.addVertex("a", "a");
            graph.addVertex("b", "b");
            graph.addVertex("c", "c");
            graph.addVertex("d", "d");
            graph.addVertex("e", "e");
            graph.addVertex("f", "f");
            graph.addVertex("g", "g");

            graph.addEdge("a", "b", 4, Integer.valueOf(1));
            graph.addEdge("a", "c", 3, Integer.valueOf(1));
            graph.addEdge("a", "e", 7, Integer.valueOf(1));

            graph.addEdge("b", "c", 6, Integer.valueOf(1));
            graph.addEdge("b", "d", 5, Integer.valueOf(1));

            graph.addEdge("c", "d", 11, Integer.valueOf(1));
            graph.addEdge("c", "e", 8, Integer.valueOf(1));

            graph.addEdge("d", "e", 2, Integer.valueOf(1));
            graph.addEdge("d", "g", 5, Integer.valueOf(1));
            graph.addEdge("d", "f", 2, Integer.valueOf(1));

            graph.addEdge("e", "g", 5, Integer.valueOf(1));

            graph.addEdge("f", "g", 3, Integer.valueOf(1));

            path = graph.dijkstra("a", "f");

            if (! path.pop().equals("b") ||
                ! path.pop().equals("d") ||
                ! path.pop().equals("f"))
            {
                throw new IllegalArgumentException(
                    "Wrong path"
                );
            }

            path = graph.dijkstra("f", "a");

            if (! path.pop().equals("d") ||
                ! path.pop().equals("e") ||
                ! path.pop().equals("a"))
            {
                throw new IllegalArgumentException(
                    "Wrong path"
                );
            }

            testHarnessCommons.passed();
        }
        catch (Exception e)
        {
            testHarnessCommons.failed(e.getMessage());
        }
    }

    public static String genString(int n)
    {
        String out = "";
        for (int i = 0; i < n; i++)
        {
            out += "a";
        }
        return out;
    }
}
