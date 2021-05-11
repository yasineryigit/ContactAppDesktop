package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField textFieldNAME, textFieldSURNAME, textFieldEMAILADDRESS, textFieldPHONENUMBER, textFieldADDRESS, textFieldCITY, textFieldPOSTALCODE;
    @FXML
    private DatePicker datePickerBIRTHDAY;
    @FXML
    private TableView<Contact> tableCONTACTS;
    @FXML
    private TableColumn<Contact, String> columnNAME;
    @FXML
    private TableColumn<Contact, String> columnSURNAME;

    private ObservableList<Contact> contacts = FXCollections.observableArrayList(); //izlenebilen obje listesi oluşturuyoruz

    @FXML
    private TabPane tabPaneCONTACTS;

    @FXML
    private Tab tabGRAPHIC;

    @FXML
    private Tab tabCONTACT;

    @FXML
    private BarChart<String, Integer> barGraphic;

    @FXML
    private CategoryAxis axisMonths;
    private ObservableList<String> monthNames = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillListWithDummyData();
        filtableContacts(); //isteyi doldurduğumuz için view'ı set ediyoruz
        tableCONTACTS.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showContact(newValue)
        );

        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEPT", "OCT", "NOV", "DEC"};
        monthNames.addAll(Arrays.asList(months));//observableList'imize months arrayini atıyoruz.

        axisMonths.setCategories(monthNames);//Grafikteki category ekseninde aylar yazacak
        tabPaneCONTACTS.getSelectionModel().selectedItemProperty().addListener(//TabPane'de hangi tab'a tıklandıysa oraya gideceğiz
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observableValue, Tab oldValue, Tab newValue) {
                            if(newValue.equals(tabGRAPHIC)){//tabGRAPHIC sekmesine geçilirse grafiği çizdir
                                setBirthDayGraphic(contacts);
                            }
                    }
                }
        );
    }

    public void setBirthDayGraphic(List<Contact> allContacts) {
        barGraphic.getData().clear();
        int[] monthCounter = new int[12];
        for (Contact c : allContacts) {//tüm kişileri gezecek
            int month = c.getDate().getMonthValue() - 1; //hangi aydaysa ay değişkenine atıyor. Dizi yapısı olduğundan 1 çıkarıyoruz
            monthCounter[month]++; //hangi aydan kaç tane varsa sayıyoruz
        }
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }
        barGraphic.getData().add(series);
    }


    public void filtableContacts() {//Contacts listesini alarak tabloyu doldurur
        columnNAME.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));//Sütuna Contact sınıfından name stringini kullan diyoruz
        columnSURNAME.setCellValueFactory(new PropertyValueFactory<Contact, String>("surname"));
        tableCONTACTS.setItems(contacts);  //tableView'a  contacts observableArrayList'ini veriyoruz
        tableCONTACTS.refresh();// notifyDataSetChanged() gibi view'ı yeniler

    }

    public void showContact(Contact contact) {
        if (contact != null) {
            textFieldNAME.setText(contact.getName());
            textFieldSURNAME.setText(contact.getSurname());
            textFieldADDRESS.setText(contact.getAddress());
            textFieldEMAILADDRESS.setText(contact.getEmailAdress());
            textFieldCITY.setText(contact.getCity());
            textFieldPHONENUMBER.setText(contact.getPhoneNumber());
            textFieldPOSTALCODE.setText(String.valueOf(contact.getPostalCode()));
            datePickerBIRTHDAY.setValue(contact.getDate());

        } else {
            textFieldNAME.setText("");
            textFieldSURNAME.setText("");
            textFieldADDRESS.setText("");
            textFieldEMAILADDRESS.setText("");
            textFieldCITY.setText("");
            textFieldPHONENUMBER.setText("");
            textFieldPOSTALCODE.setText("");
            datePickerBIRTHDAY.setValue(LocalDate.of(2000, 01, 01));
        }
    }


    /*
     * Button Actions
     * */
    public void buttonNew() {
        textFieldNAME.setText("");
        textFieldSURNAME.setText("");
        textFieldADDRESS.setText("");
        textFieldEMAILADDRESS.setText("");
        textFieldCITY.setText("");
        textFieldPHONENUMBER.setText("");
        textFieldPOSTALCODE.setText("");
        datePickerBIRTHDAY.setValue(LocalDate.of(2000, 01, 01));
    }

    public void buttonAdd() {
        try {
            contacts.add(new Contact(textFieldNAME.getText(), textFieldSURNAME.getText(), textFieldEMAILADDRESS.getText(), textFieldPHONENUMBER.getText(), textFieldADDRESS.getText(), textFieldCITY.getText(), Integer.parseInt(textFieldPOSTALCODE.getText()), datePickerBIRTHDAY.getValue()));
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Postal code should be a number");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        filtableContacts();
    }

    public void buttonUpdate() { //eski obje ile yeni obkeyi değiştirir
        int selected = tableCONTACTS.getSelectionModel().getSelectedIndex();
        System.out.println("Selected index: " + selected);
        if (selected != -1) {
            contacts.get(selected).setName(textFieldNAME.getText());
            contacts.get(selected).setSurname(textFieldSURNAME.getText());
            contacts.get(selected).setAddress(textFieldADDRESS.getText());
            contacts.get(selected).setCity(textFieldCITY.getText());
            contacts.get(selected).setAddress(textFieldADDRESS.getText());
            contacts.get(selected).setEmailAdress(textFieldEMAILADDRESS.getText());
            contacts.get(selected).setPhoneNumber(textFieldPHONENUMBER.getText());
            contacts.get(selected).setDate(datePickerBIRTHDAY.getValue());
            filtableContacts();//ilgili objenin propertylerini değiştirdiğimiz için table'i guncelliyoruz


        } else {//Eğer seçilen satır yoksa
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Must selected a row from contacts");
            alert.setContentText("For updating, you must select a row");
            alert.showAndWait();

        }
    }

    public void buttonDelete() {//Seçili objeyi siler ve table'ı günceller
        int selected = tableCONTACTS.getSelectionModel().getSelectedIndex();
        if (selected != -1) {
            contacts.remove(selected);
            filtableContacts();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Must selected a row from contacts");
            alert.setContentText("For deleting, you must select a row");
            alert.showAndWait();
        }
    }


    private void fillListWithDummyData() {
        contacts.add(new Contact("Ege", "Nazlı", "person1@gmail.com", "+905555555555", "Yeni Mahalle", "Ankara", 23, LocalDate.of(2021, 05, 01)));
        contacts.add(new Contact("Mehmet", "Haktan", "person2@gmail.com", "+905555555555", "Yeni Mahalle", "Ankara", 23, LocalDate.of(2021, 05, 01)));
        contacts.add(new Contact("Ali", "Veli", "person3@gmail.com", "+905555555555", "Yeni Mahalle", "Ankara", 23, LocalDate.of(2021, 05, 01)));
        contacts.add(new Contact("Cemil", "Alemdar", "person4@gmail.com", "+905555555555", "Yeni Mahalle", "Ankara", 23, LocalDate.of(2021, 05, 01)));
        contacts.add(new Contact("Margaret", "Taşdemir", "person5@gmail.com", "+905555555555", "Yeni Mahalle", "Ankara", 23, LocalDate.of(2021, 05, 01)));
        contacts.add(new Contact("Mike", "Kumsal", "person6@gmail.com", "+905555555555", "Yeni Mahalle", "Ankara", 23, LocalDate.of(2021, 05, 01)));
        contacts.add(new Contact("John", "Dündar", "person7@gmail.com", "+905555555555", "Yeni Mahalle", "Ankara", 23, LocalDate.of(2021, 05, 01)));
    }
}
