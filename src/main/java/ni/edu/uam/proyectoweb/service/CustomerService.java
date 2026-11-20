package ni.edu.uam.proyectoweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ni.edu.uam.proyectoweb.model.Customer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    // Se leen desde las variables de entorno del sistema operativo o IDE
    private static final String URL = System.getenv("SUPABASE_URL");
    private static final String API_KEY = System.getenv("SUPABASE_API_KEY");

    public List<Customer> obtenerCustomers() throws IOException, InterruptedException {

        // Validación de seguridad por si las variables no se cargaron correctamente
        if (URL == null || URL.isBlank() || API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("Error: Las variables de entorno SUPABASE_URL o SUPABASE_API_KEY no están configuradas.");
        }

        HttpClient cliente = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = cliente.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Error al obtener los customers. Código HTTP: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode datos = mapper.readTree(response.body());

        if(!datos.isArray()){
            throw new IOException("La respuesta no contiene un formato JSON válido");
        }

        List<Customer> customers = new ArrayList<>();
        for(JsonNode nodo: datos){
            Customer customer = new Customer (
                    nodo.path("id").asInt(),
                    nodo.path("first_name").asText(),
                    nodo.path("last_name").asText(),
                    nodo.path("email").asText(),
                    nodo.path("phone").asText(),
                    nodo.path("country").asText(),
                    nodo.path("city").asText(),
                    nodo.path("address").asText(),
                    nodo.path("annual_income").asDouble(),
                    nodo.path("credit_score").asInt(),
                    nodo.path("status").asText()
            );
            customers.add(customer);
        }
        return customers;
    }
}