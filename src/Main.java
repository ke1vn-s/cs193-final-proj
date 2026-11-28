import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        double calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;

        Menu menu;
        try {
            menu = DiningApi.getMenu();
        } catch (Exception e) {
            System.out.println("Couldn't retrieve menu. Check your internet connection and try again.");
            return;
        }
        if (menu == null || menu.getMeals() == null) {
            System.out.println("No meals are currently available.");
            return;
        }

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
            
            int choice = inRangeChecker(scanner, "Select a number to see menu (1-" + openMeals.size() + "): ", 1, openMeals.size());
            Meal chosenMeal = openMeals.get(choice - 1);

            System.out.println("\n---- " + chosenMeal.getType() + " Menu ----");

            for (int i = 0; i < chosenMeal.getStations().size(); i++) {
                Station station = chosenMeal.getStations().get(i);
                System.out.println((i + 1) + ". " + station.getName());
            }

            System.out.println("---------------------------\n");
            
            int stationChoice = inRangeChecker(scanner, "Select a station number to see items (1-" + chosenMeal.getStations().size() + "): ", 1, chosenMeal.getStations().size());
            Station chosenStation = chosenMeal.getStations().get(stationChoice - 1);


            System.out.println("\n---- " + chosenStation.getName() + " Items ----");
            
            for (int i = 0; i < chosenStation.getItems().size(); i++) {
                    Item item = chosenStation.getItems().get(i);
                    System.out.println((i + 1) + ". " + item.getName());
                }
            
            
            System.out.println("---------------------------\n");
            
            int itemChoice = inRangeChecker(scanner, "Select an item to log (1-" + chosenStation.getItems().size() + "): ", 1, chosenStation.getItems().size());
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
                
                itemChoice = inRangeChecker(scanner, "Select an item to log (1-" + chosenStation.getItems().size() + "): ", 1, chosenStation.getItems().size());
                chosenItem = chosenStation.getItems().get(itemChoice - 1);

                System.out.println("\n---------------------------");
            } 
            
            ItemNutrition nutrition;
            try {
                nutrition = DiningApi.getNutrition(chosenItem.getID());
            } catch (Exception e) {
                System.out.println("Couldn't retrieve nutrition information. Check your internet connection and try again.");
                continue;
            }
            
            double servings = doubleChecker(scanner, "How many servings of " + chosenItem.getName() +
                                " (" + getLabel(nutrition, "Serving Size") + ")?: ");

            calories += getValue(nutrition, "Calories") * servings;
            protein += getValue(nutrition, "Protein") * servings;
            carbs += getValue(nutrition, "Total Carbohydrate") * servings;
            fat += getValue(nutrition, "Total Fat") * servings;

            System.out.println("\n---------------------------");
            
            System.out.println("\nLogged " + servings + " serving(s) of " + chosenItem.getName() + ".");

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
        System.out.println("\nTotal Calories: " + String.format("%.0f", calories) + " calories");
        System.out.println("Total Protein: " + String.format("%.0f", protein) + "g");
        System.out.println("Total Carbohydrates: " + String.format("%.0f", carbs) + "g");
        System.out.println("Total Fat: " + String.format("%.0f", fat) + "g");

    }

    public static ArrayList<Meal> getOpenMeals(Menu menu) {
        List<Meal> meals = menu.getMeals();
        ArrayList<Meal> openMeals = new ArrayList<>();
        if (meals != null) {
            for (int i = 0; i < meals.size(); i++) {

                if ("Open".equalsIgnoreCase(meals.get(i).getStatus())) {
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

    public static int inRangeChecker(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("\nInvalid input. Please enter a number between " + min + " and " + max + ".");
                    System.out.println("\n---------------------------");
                }
            } else {
                System.out.println("\nInvalid input. Please enter a number between " + min + " and " + max + ".");
                System.out.println("\n---------------------------");
                scanner.next();
            }
        }
    }

    public static double doubleChecker(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double input = scanner.nextDouble();
                if (input > 0) {
                    return input;
                } else {
                    System.out.println("\nInvalid input. Please enter a positive number.");
                    System.out.println("\n---------------------------");
                }
            } else {
                System.out.println("\nInvalid input. Please enter a positive number.");
                System.out.println("\n---------------------------");
                scanner.next();
            }
        }
    }

}