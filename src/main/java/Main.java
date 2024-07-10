import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) {
        try {
            // Criando um cliente HttpClient
            HttpClient client = HttpClient.newHttpClient();

            String[] moedas = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};
            String apiKey = "927b3b04ac6c90d3724c2734";
            String moedaBase = "USD";
            String urlAPI = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + moedaBase;

            // Criando uma requisição HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlAPI))
                    .GET()
                    .build();

            // Enviando a requisição e recebendo a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Convertendo a resposta para JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(response.body());
            JsonObject jsonobj = root.getAsJsonObject();

            // Imprimindo o status code e o corpo da resposta
            //System.out.println("Status code: " + response.statusCode());
            //System.out.println("Response body: " + response.body());

            // Exibindo um valor específico da resposta JSON
            String baseCode = jsonobj.get("base_code").getAsString();

            String tipoMoeda = getNomeMoeda(baseCode);

            System.out.println(baseCode + " - " + tipoMoeda);

            // Calcular e imprimir a taxa de conversão
            String moedaDestino = "BRL";
            double valorParaConverter = 1.0; // valor em moedaBase
            double taxaConversao = getTaxaConversao(jsonobj, moedaDestino);
            double valorConvertido = valorParaConverter * taxaConversao;

            System.out.println("Taxa de conversão de " + moedaBase + " para " + moedaDestino + ": " + taxaConversao;
            System.out.println(valorParaConverter + " " + moedaBase + " é igual a " + valorConvertido + " " + moedaDestino);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obter o nome da moeda a partir do código
    private static String getNomeMoeda(String baseCode) {
        switch (baseCode) {
            case "ARS":
                return "Peso argentino";
            case "BOB":
                return "Boliviano boliviano";
            case "BRL":
                return "Real brasileiro";
            case "CLP":
                return "Peso chileno";
            case "COP":
                return "Peso colombiano";
            case "USD":
                return "Dólar americano";
            default:
                return "Moeda desconhecida";
        }
    }

    // Método para obter a taxa de conversão de uma moeda destino
    private static double getTaxaConversao(JsonObject jsonobj, String moedaDestino) {
        JsonObject rates = jsonobj.getAsJsonObject("conversion_rates");
        return rates.get(moedaDestino).getAsDouble();
    }
}
