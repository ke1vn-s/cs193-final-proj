import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import com.google.gson.Gson;

public class DiningApi {
    
    public static Menu getMenu() throws Exception {
        String date = LocalDate.now().toString(); // ex: 2026-11-07
        String url = "https://api.hfs.purdue.edu/menus/v2/locations/Wiley/" + date;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), Menu.class);
    }

    public static ItemNutrition getNutrition(String itemId) throws Exception {
        String url = "https://api.hfs.purdue.edu/menus/v2/items/" + itemId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), ItemNutrition.class);
    }

}
