package book;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.io.*;
 
public class FXMLDocumentController implements Initializable {

    @FXML private TableView<Osoba> tabela;
    @FXML private TableColumn<Osoba, String> imie, numer;
    @FXML private TextField imieIn, numerIn;
    @FXML Button add, delete,modify;
    private final String sciezka = "C:\\JAVAFX\\adres_book\\book.txt";
    
    ObservableList<Osoba> dane = FXCollections.observableArrayList();
    
    @FXML
    public void wyswietl(MouseEvent e) {
        int i = tabela.getSelectionModel().getSelectedIndex();
        imieIn.setText(imie.getCellData(i));
        numerIn.setText(numer.getCellData(i));
    }
    
    @FXML
    public void dodaj() throws IOException {
        dane.add(new Osoba(imieIn.getText(),numerIn.getText()));
        
        PrintWriter out = null;
        try {           
            out = new PrintWriter(new FileWriter(sciezka,true));
            out.println(imieIn.getText() + "," + numerIn.getText());
        } finally {
            if(out != null) out.close();
        }
    }  
    
    @FXML
    public void modyfikuj() throws IOException {
        int i = tabela.getSelectionModel().getSelectedIndex();
        int l = dane.size();
        int x;    
        
        if(i >= 0) {
            String tempi = imie.getCellData(i);
            String tempn = numer.getCellData(i);
            if(!imieIn.getText().matches(tempi) && !imieIn.getText().isEmpty() && !numerIn.getText().matches(tempn) && !numerIn.getText().isEmpty()) {
                dane.remove(i);
                dane.add(i,new Osoba(imieIn.getText(),numerIn.getText()));
            } else {
                if(!imieIn.getText().matches(tempi) && !imieIn.getText().isEmpty()) {
                    dane.remove(i);
                    dane.add(i,new Osoba(imieIn.getText(),tempn));
                }
                if(!numerIn.getText().matches(tempn) && !numerIn.getText().isEmpty()) {
                    dane.remove(i);
                    dane.add(i,new Osoba(tempi,numerIn.getText()));
                }
            }                      
        }
        
        PrintWriter out = null;
        try {           
            out = new PrintWriter(new FileWriter(sciezka));
            for(x=0;x<l;x++) out.println(imie.getCellData(x) + "," + numer.getCellData(x));
        } finally {
            if(out != null) out.close();
        }
    }
    
    @FXML
    public void usun() throws IOException {
        int l = dane.size();
        int x = tabela.getSelectionModel().getSelectedIndex();
        int i = 0;
        String[] tab = new String[l];
        String linia;
        BufferedReader in = null;
        
        try {
            in = new BufferedReader(new FileReader(sciezka));
            linia = in.readLine();
            while(linia != null) {  
                tab[i] = linia;
                i++;
                linia = in.readLine();         
            }
            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {} 
        
        PrintWriter out = null;
        try {           
            out = new PrintWriter(new FileWriter(sciezka));
            for(i=0;i<l;i++) if(i != x) out.println(tab[i]);
        } finally {
            if(out != null) out.close();
        }
        
        dane.remove(x);       
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        BufferedReader in = null;
        
        try {
            in = new BufferedReader(new FileReader(sciezka));
            String linia = in.readLine();
            while(linia != null) {
                int x = linia.indexOf(",");
                dane.add(new Osoba(linia.substring(0, x),linia.substring(x+1, linia.length())));
                linia = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {}        
    
        tabela.itemsProperty().setValue(dane);
        imie.setCellValueFactory(new PropertyValueFactory<Osoba, String>("im"));
        numer.setCellValueFactory(new PropertyValueFactory<Osoba, String>("num"));      
        
    }    
    
} 
