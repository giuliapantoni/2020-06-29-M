package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.Adiacenza;
import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;
	private Map<Integer, Director> idMap = new HashMap<Integer, Director>();
	private Graph<Director, DefaultWeightedEdge> grafo;
	private List<Director> vertici;
	private List<Adiacenza> adiacenze;
	
	public Model() {
		this.dao = new ImdbDAO();
		for(Director d : this.dao.listAllDirectors()) {
			idMap.put(d.id, d);
		}
	}
	
	public List<Integer> getAnni(){
		return this.dao.getAnni();
	}
	
	public void creaGrafo(Integer anno, Map<Integer, Director> idMap) {
		// creo il grafo
		this.grafo = new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		// aggiungo i vertici
		this.vertici = this.dao.getVertici(anno, idMap);
		Graphs.addAllVertices(this.grafo, vertici);
		// aggiungo gli archi
		this.adiacenze = this.dao.getAdiacenze(anno, idMap);
		for(Adiacenza a : adiacenze) {
			if(this.grafo.containsVertex(a.getD1()) && this.grafo.containsVertex(a.getD2()) && a.getPeso() != null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
			}
		}
	}
	
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Map<Integer, Director> getMap(){
		return this.idMap;
	}
	
	public List<Director> getVertici(){
		List<Director> registi = new ArrayList<Director>(this.grafo.vertexSet());
		return registi;
	}
	
	
	public List<Director> getAdiacenti(Director d){
		
		List<Director> vicini = Graphs.neighborListOf(this.grafo, d);
		for(Director dr : vicini) {
			dr.peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(d, dr));
		}
		return vicini;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
