package path;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import graph.Graph;
import graph.Node;
import gui.TextIO;

/** @author davidgries */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/** JUnit tests for class Path. */
public class PathTest {

	/** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and no edges. <br>
	 * Test a 1-node path from Ithaca to Ithaca and <br>
	 * an empty shortest path from Ithaca to Truck Depot */
	@Test
	public void test10OneNodeBoard() {
		Graph g= getGraph("data/Maps/OneNodeBoard.txt");
		Node n= g.getNode("Ithaca");
		List<Node> path= Path.shortestPath(n, n);
		List<Node> expected= new LinkedList<>();
		expected.add(n);
		assertEquals(expected, path);
		// System.out.println("Random: "+new Random(128).nextInt());
	}

	/** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and no edges. <br>
	 * Test a 1-node path from Ithaca to Ithaca and <br>
	 * an empty shortest path from Ithaca to Truck Depot */
	@Test
	public void test20NoEdges() {
		Graph g= getGraph("data/Maps/TwoNodeNoEdge.txt");
		Node n= g.getNode("Ithaca");
		List<Node> path= Path.shortestPath(n, n);
		List<Node> expected= new LinkedList<>();
		expected.add(n);
		assertEquals(expected, path);

		Node n1= g.getNode("Truck Depot");
		List<Node> path1= Path.shortestPath(n, n1);
		List<Node> expected1= new LinkedList<>();
		assertEquals(expected1, path1);
	}

	/** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and <br>
	 * an edge between them. <br>
	 * Test a 1-node path from Ithaca to Ithaca, <br>
	 * a 2-node shortest path from Ithaca to Truck Depot, and <br>
	 * a 2-node shortest path from Truck Depot to Ithaca */
	@Test
	public void test30TwoNodeOneEdge() {
		Graph g= getGraph("data/Maps/TwoNodeBoard.txt");
		Node n= g.getNode("Ithaca");
		List<Node> path= Path.shortestPath(n, n);
		List<Node> expected= new LinkedList<>();
		expected.add(n);
		assertEquals(expected, path);

		Node n1= g.getNode("Truck Depot");
		List<Node> path1= Path.shortestPath(n, n1);
		List<Node> expected1= new LinkedList<>();
		expected1.add(n);
		expected1.add(n1);
		assertEquals(expected1, path1);

		List<Node> path2= Path.shortestPath(n1, n);
		List<Node> expected2= new LinkedList<>();
		expected2.add(n1);
		expected2.add(n);
		assertEquals(expected2, path2);
	}

	/** Test all shortest paths on map TestBoard1.txt */
	@Test
	public void test40MapTestBoard1() {
		Graph g= getGraph("data/Maps/TestBoard1.txt");
		PathData pd= new PathData("data/Maps/TestBoard1distances.txt", g);
		assertEquals(3, pd.size);
		checkAllShortestPaths(g, pd);
	}

	/** Test all shortest paths on map seed16.txt */
	@Test
	public void test50MapSeed16() {
		Graph g= getGraph("data/Maps/Seed16.txt");
		PathData pd= new PathData("data/Maps/Seed16distances.txt", g);
		assertEquals(6, pd.size);
		checkAllShortestPaths(g, pd);
	}

	/** Test all shortest paths on map ...t */
	@Test
	public void test60MapBoard3() {
		Graph g= getGraph("data/Maps/Board3.txt");
		PathData pd= new PathData("data/Maps/Board3distances.txt", g);
		assertEquals(10, pd.size);
		checkAllShortestPaths(g, pd);
	}

	/** Test all shortest paths on map ...t */
	@Test
	public void test70MapTestBoard2() {
		Graph g= getGraph("data/Maps/TestBoard2.txt");
		PathData pd= new PathData("data/Maps/TestBoard2distances.txt", g);
		assertEquals(34, pd.size);
		checkAllShortestPaths(g, pd);
	}

	/** Test all shortest paths on map seeded with 128 */
	@Test
	public void test80MapSeeded128() {
		Graph g= getGraph("data/Maps/seed128.txt");
		PathData pd= new PathData("data/Maps/seed128distances.txt", g);
		assertEquals(50, pd.size);
		checkAllShortestPaths(g, pd);
	}

	/** Check the shortest paths in g from each node to each node, as given by <br>
	 * Paths.shortestPaths, matches that in pd. <br>
	 * Use the ordering of nodes as given in pd.names. */
	public void checkAllShortestPaths(Graph g, PathData pd) {
		for (int r= 0; r < pd.size; r= r + 1) {
			for (int c= 0; c < pd.size; c= c + 1) {
				// check shortest path distance from node r to node c
				List<Node> list= Path.shortestPath(pd.nodes[r], pd.nodes[c]);
				assertEquals(pd.dist[r][c], Path.pathSum(list));

				// check that first node of path and last node of path are correct
				if (list.size() > 0) {
					assertEquals(pd.nodes[r], list.get(0));
					assertEquals(pd.nodes[c], list.get(list.size() - 1));
				}
			}
		}

	}

	/** Return a graph for file with path s. */
	public Graph getGraph(String s) {
		try {
			return Graph.getJsonGraph(new JSONObject(TextIO.read(new File(s))));
		} catch (IOException e) {
			throw new RuntimeException("IO Exception reading in graph " + s);
		}
	}
}
