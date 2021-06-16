/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	model.distruggiGrafo();
    	if(boxAnno.getValue()!=null) {
    		int anno = boxAnno.getValue();
    		model.creaGrafo(anno);
    		txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi.\n", model.getNVertici(),model.getNArchi()));
    		boxRegista.getItems().addAll(model.setCmbBoxRegisti());
    	} else {
    		txtResult.setText("Errore - seleziona un anno.");
    	}
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	if(boxRegista.getValue()!=null) {
    		Director sorgente = boxRegista.getValue();
    		List<Adiacenza> adiacenti = model.getRegistiAdiacenti(sorgente);
    		if(adiacenti.isEmpty()) {
    			txtResult.appendText("Nell'anno selezionato il regista non ha nessun attore condiviso con altri colleghi.");
    			return;
    		}
    		for(Adiacenza a : adiacenti) {
    			txtResult.appendText(String.format("Regista adiacente: %s - Attori condivisi: %.0f.\n", a.getD2(),( a.getPeso())));
    		}
    	}
    	else {
    		txtResult.setText("Errore - seleziona un regista valido.");
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    public void setBoxAnno() {
    	List<Integer> anni = new ArrayList<>();
    	anni.add(2004); anni.add(2005); anni.add(2006);
    	boxAnno.getItems().addAll(anni);
    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	this.setBoxAnno();
    	
    }
    
}
