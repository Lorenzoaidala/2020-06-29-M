package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private Graph<Director,DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Director> idMap;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new TreeMap<>();
		dao.listAllDirectors(idMap);
	}
	
	public void creaGrafo(Integer anno) {
		grafo = new SimpleWeightedGraph<Director,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(idMap, anno));
		
		for(Adiacenza a : dao.getArchi(idMap, anno)) {
			if(this.grafo.containsVertex(a.getD1()) && this.grafo.containsVertex(a.getD2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(),a.getPeso());
			}
		}
	}
	
	public int getNVertici() {
		if(this.grafo!=null)
		return this.grafo.vertexSet().size();
		return 0;
	}
	public int getNArchi() {
		if(this.grafo!=null)
		return this.grafo.edgeSet().size();
		return 0;
	}
	
	public Set<Director> setCmbBoxRegisti(){
		return this.grafo.vertexSet();
	}
	public void distruggiGrafo() {
		this.grafo=null;
	}
	
	public List<Adiacenza> getRegistiAdiacenti(Director d){
		List<Adiacenza> result = new ArrayList<>();
		
		List<Director>adiacenti = Graphs.neighborListOf(this.grafo, d);
		for(Director dd : adiacenti) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(d, dd));
			result.add(new Adiacenza(d,dd,peso));
		}
		return result;
	}
}
