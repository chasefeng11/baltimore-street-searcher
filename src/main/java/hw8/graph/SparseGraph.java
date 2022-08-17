package hw8.graph;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;



/**
 * An implementation of Graph ADT using incidence lists
 * for sparse graphs where most nodes aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {
  LinkedHashSet<VertexNode<V>> vertices;
  LinkedHashSet<EdgeNode<E>> edges;

  /**
   * Creates a Sparse Graph instance.
   */
  public SparseGraph() {
    vertices = new LinkedHashSet<>();
    edges = new LinkedHashSet<>();
  }

  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      if (gv.owner != this) {
        throw new PositionException();
      }
      return gv;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      if (ge.owner != this) {
        throw new PositionException();
      }
      return ge;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  private boolean isVertexValid(Vertex<V> vertex) {
    // return (vertex != null && vertices.contains(convert(vertex)));
    if (vertex != null) {
      VertexNode<V> node = convert(vertex);
      return vertices.contains(node);
    }
    return false;
  }

  private boolean isEdgeValid(Edge<E> edge) {
    return (edge != null && edges.contains(convert(edge)));
  }

  // O(1) because adding to a hash set is O(1)
  @Override
  public Vertex<V> insert(V v) throws InsertionException {
    if (v == null) {
      throw new InsertionException();
    }

    VertexNode<V> vertexNode = new VertexNode<>(v);
    vertexNode.owner = this;

    if (!vertices.add(vertexNode)) {
      throw new InsertionException();
    }

    return vertexNode;
  }

  // O(1) because contains on a linked hash set is O(1) and adding to a linked list is O(1)
  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
          throws PositionException, InsertionException {
    if (!isVertexValid(from) || !isVertexValid(to)) {
      throw new PositionException();
    }

    VertexNode<V> fromNode = convert(from);
    VertexNode<V> toNode = convert(to);

    EdgeNode<E> edgeNode = new EdgeNode<>(fromNode, toNode, e);
    edgeNode.owner = this;

    if (fromNode.equals(toNode)) {
      throw new InsertionException();
    }

    if (!edges.add(edgeNode)) {
      throw new InsertionException();
    }

    fromNode.outgoingEdges.add(edgeNode);
    toNode.incomingEdges.add(edgeNode);
    return edgeNode;
  }

  // O(d) because contains on a linked hash set is O(1) and removing from the linked list is O(d)
  @Override
  public V remove(Vertex<V> v) throws PositionException, RemovalException {
    if (!isVertexValid(v)) {
      throw new PositionException();
    }

    VertexNode<V> vertexNode = convert(v);

    if (!vertexNode.outgoingEdges.isEmpty() || !vertexNode.incomingEdges.isEmpty()) {
      throw new RemovalException();
    }

    vertices.remove(vertexNode);
    return vertexNode.get();
  }

  // O(d) because contains on a linked hash set is O(1) and removing from the linked list is O(d)
  @Override
  public E remove(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = convert(e);

    if (!isVertexValid(edgeNode.from) || !isVertexValid(edgeNode.to)) {
      throw new PositionException();
    }

    VertexNode<V> from = convert(from(e));
    VertexNode<V> to = convert(to(e));

    if (!edges.remove(edgeNode)) {
      throw new PositionException();
    }

    from.outgoingEdges.remove(edgeNode);
    to.incomingEdges.remove(edgeNode);

    return edgeNode.get();
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    return Collections.unmodifiableSet(vertices);
  }

  @Override
  public Iterable<Edge<E>> edges() {
    return Collections.unmodifiableSet(edges);
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    if (!isVertexValid(v)) {
      throw new PositionException();
    }

    VertexNode<V> vertexNode = convert(v);
    return Collections.unmodifiableList(vertexNode.outgoingEdges);
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    if (!isVertexValid(v)) {
      throw new PositionException();
    }

    VertexNode<V> vertexNode = convert(v);
    return Collections.unmodifiableList(vertexNode.incomingEdges);
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    if (!isEdgeValid(e)) {
      throw new PositionException();
    }
    return convert(e).from;
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    if (!isEdgeValid(e)) {
      throw new PositionException();
    }
    return convert(e).to;
  }

  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    if (!isVertexValid(v)) {
      throw new PositionException();
    }
    convert(v).label = l;
  }

  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    if (!isEdgeValid(e)) {
      throw new PositionException();
    }
    convert(e).label = l;
  }

  @Override
  public Object label(Vertex<V> v) throws PositionException {
    if (!isVertexValid(v)) {
      throw new PositionException();
    }

    return convert(v).label;
  }

  @Override
  public Object label(Edge<E> e) throws PositionException {
    if (!isEdgeValid(e)) {
      throw new PositionException();
    }
    return convert(e).label;
  }

  @Override
  public void clearLabels() {
    for (Edge<E> e: edges) {
      label(e, null);
    }

    for (Vertex<V> v: vertices) {
      label(v, null);
    }
  }

  @Override
  public String toString() {
    GraphPrinter<V, E> gp = new GraphPrinter<>(this);
    return gp.toString();
  }

  // Class for a vertex of type V
  private final class VertexNode<V> implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    Object label;

    LinkedList<EdgeNode<E>> incomingEdges;
    LinkedList<EdgeNode<E>> outgoingEdges;

    VertexNode(V v) {
      this.data = v;
      this.label = null;

      this.incomingEdges = new LinkedList<>();
      this.outgoingEdges = new LinkedList<>();
    }

    @Override
    public V get() {
      return this.data;
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || !(o instanceof VertexNode)) {
        return false;
      }
      VertexNode<V> other = (VertexNode<V>) (o);
      return (this.data.equals(other.data) && this.owner.equals(other.owner));
    }

    @Override
    public int hashCode() {
      return Objects.hash(owner, data);
    }
  }

  //Class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;
    VertexNode<V> to;
    Object label;
    // TODO You may need to add fields/methods here!

    // Constructor for a new edge
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
    }

    @Override
    public E get() {
      return this.data;
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || !(o instanceof EdgeNode)) {
        return false;
      }
      EdgeNode<E> other = (EdgeNode<E>) (o);
      return (this.from.equals(other.from) && this.to.equals(other.to) && this.owner.equals(other.owner));
    }

    @Override
    public int hashCode() {
      return Objects.hash(owner, from, to);
    }
  }
}
