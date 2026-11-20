package ni.edu.uam.proyectoweb.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.proyectoweb.model.Customer;
import ni.edu.uam.proyectoweb.service.CustomerService;

import java.util.List;

public class CustomerController {

    @FXML private TableView<Customer> tablaCustomers;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colFirstName;
    @FXML private TableColumn<Customer, String> colLastName;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colCountry;
    @FXML private TableColumn<Customer, String> colCity;
    @FXML private TableColumn<Customer, Double> colIncome;
    @FXML private TableColumn<Customer, String> colStatus;

    @FXML private Button btnConsultar;
    @FXML private Label lblEstado;

    private CustomerService service = new CustomerService();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("annualIncome"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void consultarCustomers() {
        Task<List<Customer>> task = new Task<List<Customer>>() {
            @Override
            protected List<Customer> call() throws Exception {
                return service.obtenerCustomers();
            }
        };

        btnConsultar.disableProperty().bind(task.runningProperty());
        lblEstado.setText("Consultando Supabase API...");

        task.setOnSucceeded(event -> {
            List<Customer> customers = task.getValue();
            tablaCustomers.setItems(FXCollections.observableArrayList(customers));
            lblEstado.setText("Customers encontrados: " + customers.size());
        });

        task.setOnFailed(event -> {
            Throwable error = task.getException();
            lblEstado.setText("Error: " + error.getMessage());
        });

        Thread hilo = new Thread(task);
        hilo.setDaemon(true);
        hilo.start();
    }
}