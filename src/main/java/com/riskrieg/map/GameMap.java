package com.riskrieg.map;

import com.aaronjyoder.util.json.moshi.MoshiUtil;
import com.riskrieg.constant.Constants;
import com.riskrieg.map.alignment.InterfaceAlignment;
import com.riskrieg.map.graph.Edge;
import com.riskrieg.map.graph.MapData;
import com.riskrieg.map.graph.Territory;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.traverse.BreadthFirstIterator;

@SuppressWarnings("unused")
public record GameMap(String name) implements Comparable<GameMap> {

  public GameMap {
    Objects.requireNonNull(name);
    if (name.isBlank()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    if (!isValid(name)) {
      throw new IllegalArgumentException(name + ": invalid map or correct map files do not exist");
    }
  }

  public String getBaseLayerPath() {
    return Constants.MAP_PATH + name + "/" + name + "-base.png";
  }

  public String getTextLayerPath() {
    return Constants.MAP_PATH + name + "/" + name + "-text.png";
  }

  @Override
  public String name() {
    return getInfo().name();
  }

  public String displayName() {
    return getInfo().displayName();
  }

  public String author() {
    return getInfo().author();
  }

  public InterfaceAlignment alignment() {
    return getInfo().alignment();
  }

  public MapStatus status() {
    return getInfo().status();
  }

  // Public methods

  public Optional<Territory> getTerritory(String name) {
    Iterator<Territory> iterator = new BreadthFirstIterator<>(getGraph());
    while (iterator.hasNext()) {
      Territory territory = iterator.next();
      if (territory.name().equalsIgnoreCase(name)) {
        return Optional.of(territory);
      }
    }
    return Optional.empty();
  }

  public Set<Territory> getTerritories() {
    return getData().vertices();
  }

  public Set<Territory> getNeighbors(Territory territory) {
    return Graphs.neighborSetOf(getGraph(), territory);
  }

  public boolean neighbors(Territory source, Territory target) {
    return getNeighbors(source).contains(target);
  }

  // Private methods

  private boolean isValid(String name) {
    return MoshiUtil.read(Constants.MAP_PATH + name + "/" + name + ".json", MapInfo.class) != null
        && MoshiUtil.read(Constants.MAP_PATH + name + "/graph/" + name + ".json", MapData.class) != null;
  }

  private MapInfo getInfo() {
    return MoshiUtil.read(Constants.MAP_PATH + name + "/" + name + ".json", MapInfo.class);
  }

  private Graph<Territory, Edge> getGraph() {
    MapData mapData = getData();

    Graph<Territory, Edge> graph = GraphTypeBuilder.<Territory, Edge>undirected()
        .allowingMultipleEdges(false).allowingSelfLoops(false).edgeClass(Edge.class).weighted(false).buildGraphBuilder()
        .addVertices(mapData.vertices().toArray(new Territory[0])).build();

    for (Edge edge : mapData.edges()) {
      Edge e = new Edge(edge.source(), edge.target());
      graph.addEdge(edge.source(), edge.target(), e);
    }
    return graph;
  }

  private MapData getData() {
    return MoshiUtil.read(Constants.MAP_PATH + name + "/graph/" + name + ".json", MapData.class);
  }

  @Override
  public int compareTo(GameMap o) {
    return Integer.compare(o.getTerritories().size(), this.getTerritories().size());
  }

}
