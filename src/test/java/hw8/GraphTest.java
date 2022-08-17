package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Position;
import hw8.graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class GraphTest {

  protected Graph<String, String> graph;

  @BeforeEach
  public void setupGraph() {
    this.graph = createGraph();
  }

  protected abstract Graph<String, String> createGraph();

  // Test cases for inserting a vertex

  @Test
  @DisplayName("insert(v) returns a vertex with given data")
  public void canGetVertexAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  @DisplayName("insert(null) throws exception")
  public void insertNullVertexThrowsInsertionException() {
    try {
      Vertex<String> v = graph.insert(null);
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v) throws exception when v is already in the graph")
  public void insertRepeatingVertexThrowsInsertionException() {
    try {
      Vertex<String> firstVertex = graph.insert("v1");
      Vertex<String> secondVertex = graph.insert("v1");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("multiple vertex insertions all return vertices with given data")
  public void insertSequenceOfMultipleVertices() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    assertEquals(v1.get(), "v1");
    assertEquals(v2.get(), "v2");
    assertEquals(v3.get(), "v3");
  }


  // Test cases for inserting an edge

  @Test
  @DisplayName("insert(U, V, e) returns an edge with given data")
  public void canGetEdgeAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
  }

  @Test
  @DisplayName("insert(null, V, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenFirstVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(null, v, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, null, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenSecondVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(v, null, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v1, v2, e) throws exception when v1 is not in the graph")
  public void insertEdgeThrowsPositionExceptionWhenFirstVertexIsNoLongerValid() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Vertex<String> v3 = graph.insert("v3");

      graph.remove(v1);
      graph.insert(v1, v2, "v1-v2");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v1, v2, e) throws exception when v1 is not in the graph")
  public void insertEdgeThrowsPositionExceptionWhenSecondVertexIsNoLongerValid() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Vertex<String> v3 = graph.insert("v3");

      graph.remove(v2);
      graph.insert(v1, v2, "v1-v2");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v1, v2, e) throws exception when v1 and v2 are already connected")
  public void insertExistingEdgeThrowsInsertionException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");

      graph.insert(v1, v2, "first label");
      graph.insert(v1, v2, "second label");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v1, v1, e) throws exception for creating a self-loop")
  public void insertLoopEdgeThrowsInsertionException() {
    try {
      Vertex<String> v1 = graph.insert("vertex");
      Vertex<String> v2 = graph.insert("vertex");

      graph.insert(v1, v2, "first label");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(U, V, e) returns an edge with given data even when an edge going the opposite way exists")
  public void insertEdgeWhenReverseEdgeExists() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");

    graph.insert(v1, v2, "v1-v2");

    Edge<String> secondEdge = graph.insert(v2, v1, "v2-v1");
    assertEquals(secondEdge.get(), "v2-v1");
  }

  @Test
  @DisplayName("multiple vertex insertions all return vertices with given data")
  public void insertSequenceOfMultipleEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");

    Edge<String> firstEdge = graph.insert(v1, v2, "v1-v2");

    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> secondEdge = graph.insert(v3, v1, "v3-v1");
    Edge<String> thirdEdge = graph.insert(v2, v4, "v2-v4");
    Edge<String> fourthEdge = graph.insert(v4, v2, "v4-v2");

    assertEquals(firstEdge.get(), "v1-v2");
    assertEquals(secondEdge.get(), "v3-v1");
    assertEquals(thirdEdge.get(), "v2-v4");
    assertEquals(fourthEdge.get(), "v4-v2");
  }

  // Test cases for getting endpoints of an edge

  @Test
  @DisplayName("from(edge) returns the source of the specified valid edge")
  public void getFromValidEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");

    Edge<String> firstEdge = graph.insert(v1, v2, "v1-v2");
    assertEquals(graph.from(firstEdge), v1);
  }

  @Test
  @DisplayName("to(edge) returns the source of the specified valid edge")
  public void getToValidEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");

    Edge<String> firstEdge = graph.insert(v1, v2, "v1-v2");
    assertEquals(graph.to(firstEdge), v2);
  }

  @Test
  @DisplayName("from(null) throws exception")
  public void fromNullEdgeThrowsException() {
    try {
      Vertex<String> from = graph.from(null);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("to(null) throws exception")
  public void toNullEdgeThrowsException() {
    try {
      Vertex<String> to = graph.to(null);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("from(edge) throws exception when the edge is not in the graph")
  public void fromEdgeThrowsExceptionWhenEdgeNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");

      Edge<String> firstEdge = graph.insert(v1, v2, "v1-v2");
      graph.remove(firstEdge);

      Vertex<String> from = graph.from(firstEdge);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("to(edge) throws exception when the edge is not in the graph")
  public void toEdgeThrowsExceptionWhenEdgeNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");

      Edge<String> firstEdge = graph.insert(v1, v2, "v1-v2");
      graph.remove(firstEdge);

      Vertex<String> to = graph.to(firstEdge);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  // Test cases for removing a vertex

  @Test
  @DisplayName("remove(v1) returns the value of the unattached vertex")
  public void removeValidUnattachedVertex() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");

    assertEquals(graph.remove(v1), "v1");
  }

  @Test
  @DisplayName("remove(null) throws exception for vertices")
  public void removeNullVertexThrowsException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.remove((Vertex<String>) (null));
      fail("The expected exception was not thrown!");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v1) throws exception when v1 is not in the graph")
  public void removeVertexThrowsExceptionWhenVertexNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.remove(v1);
      graph.remove(v1);
      fail("The expected exception was not thrown!");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v1) throws exception when v1 has incoming edges")
  public void removeVertexThrowsExceptionWhenVertexHasIncomingEdges() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v2, v1, "v2-v1");
      graph.remove(v1);
      fail("The expected exception was not thrown!");
    } catch (RemovalException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v1) throws exception when v1 has outgoing edges")
  public void removeVertexThrowsExceptionWhenVertexHasOutgoingEdges() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "v1-v2");
      graph.remove(v1);
      fail("The expected exception was not thrown!");
    } catch (RemovalException ex) {
      return;
    }
  }

  // Test cases for removing an edge

  @Test
  @DisplayName("remove(edge) returns the value of the edge")
  public void removeValidEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");

    Edge<String> firstEdge = graph.insert(v1, v2, "v1-v2");
    String output = graph.remove(firstEdge);
    assertEquals(output, "v1-v2");
  }

  @Test
  @DisplayName("remove(null) throws exception for edges")
  public void removeNullEdgeThrowsException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "first edge");
      graph.remove((Edge<String>) (null));
      fail("The expected exception was not thrown!");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(edge) throws exception when the edge is not in the graph")
  public void removeEdgeThrowsExceptionWhenEdgeNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> firstEdge = graph.insert(v1, v2, "first edge");
      Edge<String> secondEdge = graph.insert(v2, v1, "second edge");
      graph.remove(firstEdge);
      graph.remove(firstEdge);
      fail("The expected exception was not thrown!");
    } catch (PositionException ex) {
      return;
    }
  }

  // Test cases for iterating over vertices (i.e. vertices() function)

  @Test
  @DisplayName("vertices() generates the correct number of elements")
  public void iterateVerticesGeneratesCorrectCount() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v1, v3, "v1-v3");
    graph.insert(v2, v4, "v2-v4");
    graph.insert(v7, v6, "v7-v6");

    Iterable<Vertex<String>> vertices = graph.vertices();

    int count = 0;
    for (Vertex<String> v: vertices) {
      count++;
    }
    assertEquals(count, 7);
  }

  @Test
  @DisplayName("vertices() generates the correct vertices")
  public void iterateVerticesGeneratesCorrectVertices() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v1, v3, "v1-v3");
    graph.insert(v2, v4, "v2-v4");
    graph.insert(v7, v6, "v7-v6");

    int numVertex = 1;
    Iterable<Vertex<String>> vertices = graph.vertices();
    for (Vertex<String> v: vertices) {
      assertEquals(v.get(), "v" + numVertex);
      numVertex++;
    }
  }

  // Test cases for iterating over edges (i.e. edges() function)

  @Test
  @DisplayName("edges() generates the correct number of elements")
  public void iterateEdgesGeneratesCorrectCount() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v1, v3, "v1-v3");
    graph.insert(v2, v4, "v2-v4");
    graph.insert(v3, v5, "v3-v5");
    graph.insert(v4, v6, "v4-v6");
    graph.insert(v5, v7, "v5-v7");

    int count = 0;
    for (Edge<String> e: graph.edges()) {
      count++;
    }
    assertEquals(count, 5);
  }

  @Test
  @DisplayName("edges() generates the correct vertices")
  public void iterateEdgesGeneratesCorrectEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v1, v3, "v1-v3");
    graph.insert(v2, v4, "v2-v4");
    graph.insert(v3, v5, "v3-v5");
    graph.insert(v4, v6, "v4-v6");
    graph.insert(v5, v7, "v5-v7");

    int numVertex = 1;
    for (Edge<String> e: graph.edges()) {
      assertEquals(e.get(), "v" + numVertex + "-v" + (numVertex + 2));
      numVertex++;
    }
  }



  // Test cases for iterating over outgoing edges (i.e. outgoing() function)

  @Test
  @DisplayName("outgoing() generates the correct number of elements")
  public void iterateOutgoingEdgesGeneratesCorrectCount() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v1, v3, "v1-v3");
    graph.insert(v1, v5, "v1-v5");
    graph.insert(v1, v7, "v1-v7");

    Iterable<Edge<String>> outgoingEdges = graph.outgoing(v1);

    int count = 0;
    for (Edge<String> e: outgoingEdges) {
      count++;
    }
    assertEquals(count, 3);
  }

  @Test
  @DisplayName("outgoing() generates the correct edges")
  public void iterateOutgoingEdgesGeneratesCorrectEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v1, v3, "v1-v3");
    graph.insert(v1, v5, "v1-v5");
    graph.insert(v1, v7, "v1-v7");

    int numVertex = 3;
    Iterable<Edge<String>> outgoingEdges = graph.outgoing(v1);

    for (Edge<String> e: outgoingEdges) {
      assertEquals(e.get(), "v1-v" + numVertex);
      numVertex += 2;
    }
  }

  @Test
  @DisplayName("outgoing(null) throws an exception")
  public void outgoingThrowsExceptionWhenCalledOnNull() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Vertex<String> v3 = graph.insert("v3");
      Vertex<String> v4 = graph.insert("v4");
      Vertex<String> v5 = graph.insert("v5");
      Vertex<String> v6 = graph.insert("v6");
      Vertex<String> v7 = graph.insert("v7");

      graph.insert(v1, v3, "v1-v3");
      graph.insert(v1, v5, "v1-v5");
      graph.insert(v1, v7, "v1-v7");

      Iterable<Edge<String>> outgoingEdges = graph.outgoing(null);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }


  @Test
  @DisplayName("outgoing(v1) throws an exception when v1 is not in the graph")
  public void outgoingThrowsExceptionWhenCalledOnMissingVertex() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Vertex<String> v3 = graph.insert("v3");
      Vertex<String> v4 = graph.insert("v4");
      Vertex<String> v5 = graph.insert("v5");
      Vertex<String> v6 = graph.insert("v6");
      Vertex<String> v7 = graph.insert("v7");

      graph.remove(v1);
      Iterable<Edge<String>> outgoingEdges = graph.outgoing(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  // Test cases for iterating over incoming edges (i.e. incoming() function)

  @Test
  @DisplayName("incoming() generates the correct number of elements")
  public void iterateIncomingEdgesGeneratesCorrectCount() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v3, v1, "v3-v1");
    graph.insert(v5, v1, "v5-v1");
    graph.insert(v7, v1, "v7-v1");

    Iterable<Edge<String>> incomingEdges = graph.incoming(v1);

    int count = 0;
    for (Edge<String> e: incomingEdges) {
      count++;
    }
    assertEquals(count, 3);
  }

  @Test
  @DisplayName("incoming() generates the correct edges")
  public void iterateIncomingEdgesGeneratesCorrectEdges() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Vertex<String> v5 = graph.insert("v5");
    Vertex<String> v6 = graph.insert("v6");
    Vertex<String> v7 = graph.insert("v7");

    graph.insert(v3, v1, "v3-v1");
    graph.insert(v5, v1, "v5-v1");
    graph.insert(v7, v1, "v7-v1");

    int numVertex = 3;
    Iterable<Edge<String>> incomingEdges = graph.incoming(v1);

    for (Edge<String> e: incomingEdges) {
      assertEquals(e.get(), "v" + numVertex + "-v1");
      numVertex += 2;
    }
  }

  @Test
  @DisplayName("incoming(null) throws an exception")
  public void incomingThrowsExceptionWhenCalledOnNull() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Vertex<String> v3 = graph.insert("v3");
      Vertex<String> v4 = graph.insert("v4");
      Vertex<String> v5 = graph.insert("v5");
      Vertex<String> v6 = graph.insert("v6");
      Vertex<String> v7 = graph.insert("v7");

      graph.insert(v3, v1, "v3-v1");
      graph.insert(v5, v1, "v5-v1");
      graph.insert(v7, v1, "v7-v1");

      Iterable<Edge<String>> incomingEdges = graph.incoming(null);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("incoming(v1) throws an exception when v1 is not in the graph")
  public void incomingThrowsExceptionWhenCalledOnMissingVertex() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Vertex<String> v3 = graph.insert("v3");
      Vertex<String> v4 = graph.insert("v4");
      Vertex<String> v5 = graph.insert("v5");
      Vertex<String> v6 = graph.insert("v6");
      Vertex<String> v7 = graph.insert("v7");

      graph.remove(v1);
      Iterable<Edge<String>> incomingEdges = graph.incoming(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  // Test cases for labelling/delabelling vertices

  @Test
  @DisplayName("setting and getting the label of a vertex returns the correct value")
  public void labelValidVertex() {
    Vertex<String> v1 = graph.insert("v1");
    graph.label(v1, "First label");
    assertEquals(graph.label(v1), "First label");
  }

  @Test
  @DisplayName("label(null, o) throws exception for vertices")
  public void setLabelNullVertexThrowsException() {
    try {
      graph.label((Vertex<String>) (null), "First label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(null) throws exception for vertices")
  public void getLabelNullVertexThrowsException() {
    try {
      graph.label((Vertex<String>) (null));
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v1, o) throws an exception when v1 is not in the graph")
  public void setLabelVertexThrowsExceptionWhenNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.remove(v1);
      graph.label(v1, "First label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v1) throws an exception when v1 is not in the graph")
  public void getLabelVertexThrowsExceptionWhenNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.label(v1, "First label");
      graph.remove(v1);
      String label = (String) graph.label(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }


  // Test cases for labelling/delabelling edges

  @Test
  @DisplayName("setting and getting the label of an edge returns the correct value")
  public void labelValidEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> edge = graph.insert(v1, v2, "v1-v2");

    graph.label(edge, "First label");
    assertEquals(graph.label(edge), "First label");
  }

  @Test
  @DisplayName("label(null, o) throws exception for edge")
  public void setLabelNullEdgeThrowsException() {
    try {
      graph.label((Edge<String>) (null), "First label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(null) throws exception for edges")
  public void getLabelNullEdgesThrowsException() {
    try {
      graph.label((Edge<String>) (null));
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(edge, o) throws an exception when edge is not in the graph")
  public void setLabelEdgeThrowsExceptionWhenNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> edge = graph.insert(v1, v2, "v1-v2");

      graph.remove(edge);
      graph.label(edge, "First label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(edge) throws an exception when edge is not in the graph")
  public void getLabelEdgeThrowsExceptionWhenNotInGraph() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");

      Edge<String> edge = graph.insert(v1, v2, "v1-v2");
      graph.label(edge, "First label");

      graph.remove(edge);
      String label = (String) graph.label(edge);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  // Test cases for clearLabels()

  @Test
  @DisplayName("clearLabels() resets all vertex and edge labels")
  public void testClearLabels() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> firstEdge = graph.insert(v1, v2, "v1-v2");
    Edge<String> secondEdge = graph.insert(v3, v4, "v3-v4");
    Edge<String> thirdEdge = graph.insert(v2, v1, "v2-v1");

    graph.label(v1, "first label");
    graph.label(v3, "second label");
    graph.label(firstEdge, "third label");
    graph.label(secondEdge, "fourth label");

    graph.clearLabels();

    Iterable<Vertex<String>> vertices = graph.vertices();
    Iterable<Edge<String>> edges = graph.edges();

    for (Vertex<String> v: vertices) {
      assertEquals(graph.label(v), null);
    }

    for (Edge<String> e: edges) {
      assertEquals(graph.label(e), null);
    }
  }



}

