import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) throws Exception {
        String date = LocalDate.now().toString(); // ex: 2026-11-07
        String url = "https://api.hfs.purdue.edu/menus/v2/locations/Wiley/" + date;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Menu menu = new Gson().fromJson(response.body(), Menu.class);

        System.out.println("Status: " + response.statusCode());

    }
}