public class testDSAGraph
{
    public static void run()
    {
        testHarness.header("Testing DSAGraph class");
        testConstructor();
        testInsertVertex();
        testInsertEdge();
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
                testHarness.failed("Vertex count is incorrect");
            }
            else if (edge != 0)
            {
                testHarness.failed("Edge count is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
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
                    testHarness.failed("Vertex count is incorrect");
                }
                else if (edge != 0)
                {
                    testHarness.failed("Edge count is incorrect");
                }
                else
                {
                    testHarness.passed();
                }
            }
            catch (Exception e)
            {
                testHarness.failed(e.getMessage());
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
                    testHarness.failed("Vertex count is incorrect");
                }
                else if (edge != 0)
                {
                    testHarness.failed("Edge count is incorrect");
                }
                else
                {
                    testHarness.passed();
                }
            }
            catch (Exception e)
            {
                testHarness.failed(e.getMessage());
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
            graph.addEdge("a", "b", edgeValue);

            vertex = graph.getVertexCount();
            edge = graph.getEdgeCount();
            returnValue = graph.getEdgeValue("a", "b");

            if (vertex != 2)
            {
                testHarness.failed("Vertex count is incorrect");
            }
            else if (edge != 1)
            {
                testHarness.failed("Edge count is incorrect");
            }
            else if (returnValue != edgeValue)
            {
                testHarness.failed("Edge value is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
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
                    graph.addEdge(label1, label2, edgeValue);
                }

                vertex = graph.getVertexCount();
                edge = graph.getEdgeCount();

                if (vertex != num[i])
                {
                    testHarness.failed("Vertex count is incorrect");
                }
                else if (edge != (num[i] / 2))
                {
                    testHarness.failed("Edge count is incorrect");
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

                    testHarness.passed();
                }
            }
            catch (Exception e)
            {
                testHarness.failed(e.getMessage());
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
                testHarness.failed("Vertex count is incorrect");
            }
            else if (edge != 1)
            {
                testHarness.failed("Edge count is incorrect");
            }
            else if (returnValue != edgeValue)
            {
                testHarness.failed("Edge value is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
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
                        edgeValue
                    );
                }

                vertex = graph.getVertexCount();
                edge = graph.getEdgeCount();

                if (vertex != num[i])
                {
                    testHarness.failed("Vertex count is incorrect");
                }
                else if (edge != (num[i] / 2))
                {
                    testHarness.failed("Edge count is incorrect");
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

                    testHarness.passed();
                }
            }
            catch (Exception e)
            {
                testHarness.failed(e.getMessage());
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
            graph.addEdge("a", "b", edgeValueArr[0]);
            graph.addEdge("a", "c", edgeValueArr[1]);

            vertex = graph.getVertexCount();
            edge = graph.getEdgeCount();

            if (vertex != 3)
            {
                testHarness.failed("Vertex count is incorrectl");
            }
            else if (edge != 2)
            {
                testHarness.failed("Edge count is incorrect");
            }
            else if (graph.getEdgeValue("a", "b") != edgeValueArr[0])
            {
                testHarness.failed("Edge value between a and b is incorrect");
            }
            else if (graph.getEdgeValue("a", "c") != edgeValueArr[1])
            {
                testHarness.failed("Edge value between a and c is incorrect");
            }
            else
            {
                testHarness.passed();
            }
        }
        catch (Exception e)
        {
            testHarness.failed(e.getMessage());
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
