package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField textFieldNAME, textFieldSURNAME,textFieldMAILADDRESS, textFieldPHONENUMBER,textFieldADDRESS,textFieldCITY;
    @FXML
    private DatePicker datePickerBIRTHDAY;
    @FXML
    private TableView<Contact> tableCONTACTS;
    @FXML
    private TableColumn<Contact, String > columnNAME;
    @FXML
    private TableColumn<Contact, String > columnSURNAME;

    private ObservableList<Contact> contacts = FXCollections.observableArrayList(); //izlenebilen obje listesi oluşturuyoruz

    public void filtableContacts(){//Contacts listesini alarak tabloyu doldurur
        columnNAME.setCellValueFactory(new PropertyValueFactory<Contact,String>("name"));//Sütuna Contact sınıfından name stringini kullan diyoruz
        columnSURNAME.setCellValueFactory(new PropertyValueFactory<Contact,String>("surname"));
        tableCONTACTS.setItems(contacts);  //tableView'a  contacts observableArrayList'ini veriyoruz
        tableCONTACTS.refresh();// notifyDataSetChanged() gibi view'ı yeniler

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contacts.add(new Contact("Ege","Candan","candan@gmail.com","+905555555555","Yeni Mahalle","Ankara",23, LocalDate.of(2021,05,01)));
        contacts.add(new Contact("Mehmet","Candan","candan@gmail.com","+905555555555","Yeni Mahalle","Ankara",23, LocalDate.of(2021,05,01)));
        contacts.add(new Contact("Ali","Candan","candan@gmail.com","+905555555555","Yeni Mahalle","Ankara",23, LocalDate.of(2021,05,01)));
        contacts.add(new Contact("Cemil","Candan","candan@gmail.com","+905555555555","Yeni Mahalle","Ankara",23, LocalDate.of(2021,05,01)));
        contacts.add(new Contact("Margaret","Candan","candan@gmail.com","+905555555555","Yeni Mahalle","Ankara",23, LocalDate.of(2021,05,01)));
        contacts.add(new Contact("Mike","Candan","candan@gmail.com","+905555555555","Yeni Mahalle","Ankara",23, LocalDate.of(2021,05,01)));
        contacts.add(new Contact("John","Candan","candan@gmail.com","+905555555555","Yeni Mahalle","Ankara",23, LocalDate.of(2021,05,01)));
        filtableContacts(); //isteyi doldurduğumuz için view'ı set ediyoruz


    }
}
