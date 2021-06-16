package it.polito.tdp.imdb.model;

import it.polito.tdp.imdb.db.ImdbDAO;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		ImdbDAO dao = new ImdbDAO();
		
		model.creaGrafo(2006);
		System.out.println("Grafo creato con "+model.getNVertici()+" vertici e "+model.getNArchi()+" archi.\n");

	}

}
