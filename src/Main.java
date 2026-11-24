import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {

        Menu menu = DiningApi.getMenu();
        List<Item> items = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int count = 1;

        System.out.println("---- Wiley Dining Menu ----"); 
        
        ArrayList<Meal> openMeals = getOpenMeals(menu);
        for (int i = 0; i < openMeals.size(); i++) {
            System.out.println((i + 1) + ". " + openMeals.get(i).getType());
        }
        
        System.out.println("---------------------------\n");
        
        System.out.print("Select a number to see menu (1-" + openMeals.size() + "): ");
        
        int choice = scanner.nextInt();
        Meal chosenMeal = openMeals.get(choice - 1);

        System.out.println("\n---- " + chosenMeal.getType() + " Menu ----");

        for (int i = 0; i < chosenMeal.getStations().size(); i++) {
            Station station = chosenMeal.getStations().get(i);
            System.out.println((i + 1) + ". " + station.getName());
        }

        System.out.println("---------------------------\n");
        
        System.out.print("Select a station number to see items (1-" + chosenMeal.getStations().size() + "): ");
        int stationChoice = scanner.nextInt();
        Station chosenStation = chosenMeal.getStations().get(stationChoice - 1);

        System.out.println("\n---- " + chosenStation.getName() + " Items ----");
        
        for (int i = 0; i < chosenStation.getItems().size(); i++) {
                Item item = chosenStation.getItems().get(i);
                System.out.println((i + 1) + ". " + item.getName());
            }
        
        
        System.out.println("---------------------------\n");
        
        System.out.print("Select an item to log (1-" + chosenStation.getItems().size() + "): ");
        int itemChoice = scanner.nextInt();
        Item chosenItem = chosenStation.getItems().get(itemChoice - 1);

        System.out.println("\n-----------------------------------");

    }

    public static ArrayList<Meal> getOpenMeals(Menu menu) {
        List<Meal> meals = menu.getMeals();
        ArrayList<Meal> openMeals = new ArrayList<>();
        if (meals != null) {
            for (int i = 0; i < meals.size(); i++) {

                if (meals.get(i).getStatus().equalsIgnoreCase("Open")) {
                    openMeals.add(meals.get(i));
                }
            }
        }

        return openMeals;
    }
}