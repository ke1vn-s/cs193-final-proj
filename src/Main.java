import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {

        double calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;

        Menu menu = DiningApi.getMenu();
        Scanner scanner = new Scanner(System.in);

        String continueLogging = "y";
        while (continueLogging.equalsIgnoreCase("y")) {
            

            System.out.println("\n---- Wiley Dining Menu ----"); 
            
            ArrayList<Meal> openMeals = getOpenMeals(menu);

            if (openMeals.isEmpty()) {
                System.out.println("No meals are currently open.");
                return;
            }

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

            System.out.println("\n---------------------------");
            
            while (!chosenItem.isNutritionReady()) {
                System.out.println("\nNutrition information is not available for " + chosenItem.getName() + ".");
                
                System.out.println("\n---- " + chosenStation.getName() + " Items ----");
                for (int i = 0; i < chosenStation.getItems().size(); i++) {
                        Item item = chosenStation.getItems().get(i);
                        System.out.println((i + 1) + ". " + item.getName());
                    }
                System.out.println("\n---------------------------");
                
                System.out.print("\nSelect another item to log (1-" + chosenStation.getItems().size() + "): ");
                itemChoice = scanner.nextInt();
                chosenItem = chosenStation.getItems().get(itemChoice - 1);

                System.out.println("\n---------------------------");
            } 
            
            ItemNutrition nutrition = DiningApi.getNutrition(chosenItem.getID());
            System.out.print("\nHow many servings of " + chosenItem.getName() +
                                " (" + getLabel(nutrition, "Serving Size") + ")?: ");
            
            double servings = scanner.nextDouble();

            calories += getValue(nutrition, "Calories") * servings;
            protein += getValue(nutrition, "Protein") * servings;
            carbs += getValue(nutrition, "Carbohydrates") * servings;
            fat += getValue(nutrition, "Fat") * servings;

            System.out.println("\n---------------------------");
            
            System.out.println("\nLogged " + (int) servings + " serving(s) of " + chosenItem.getName() + ".");

            System.out.println("\n---------------------------");

            System.out.print("\nContinued logging? (y/n): ");
            continueLogging = scanner.next();

            while (!continueLogging.equalsIgnoreCase("y") && !continueLogging.equalsIgnoreCase("n")) {
                System.out.println("\nInvalid input. Please enter 'y' or 'n'.");
                System.out.println("\n---------------------------");
                System.out.print("\nContinued logging? (y/n): ");
                continueLogging = scanner.next();
            }

        
        }

        System.out.println("\n---- Nutrition Summary ----");
        System.out.println("\nTotal Calories: " + String.format("%.0f", calories) + "calories");
        System.out.println("Total Protein: " + String.format("%.0f", protein) + "g");
        System.out.println("Total Carbohydrates: " + String.format("%.0f", carbs) + "g");
        System.out.println("Total Fat: " + String.format("%.0f", fat) + "g");

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

    public static double getValue(ItemNutrition nutrition, String name) {
        for (NutritionFact fact : nutrition.getNutrition()) {
            if (fact.getName().equalsIgnoreCase(name) && fact.getValue() != null) {
                return fact.getValue();
            }
        }
        return 0;

    }

    public static String getLabel(ItemNutrition nutrition, String name) {
        for (NutritionFact fact : nutrition.getNutrition()) {
            if (fact.getName().equalsIgnoreCase(name)) {
                return fact.getLabelValue();
            }
        }
        return "";
    }

}