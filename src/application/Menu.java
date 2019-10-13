package application;

import dao.FoodDao;
import entity.Food;
import util.Printer;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Menu {
    
    private Scanner scanner = new Scanner(System.in);
    private List<String> options = Arrays.asList(
            "Display current food items",
            "Display a specific food item",
            "Create a food item",
            "Update a food item",
            "Delete a food item",
            "Exit"
    );

    private FoodDao foodDao = new FoodDao();

    void start() {

        String selection;

        do {
            printMenu();
            selection = scanner.nextLine();

            try {
                switch (selection) {
                    case "1": displayAllFood();
                        break;
                    case "2": displayFood();
                        break;
                    case "3": createFood();
                        break;
                    case "4": updateFood();
                        break;
                    case "5": deleteFood();
                        break;
                    case "6":
                        Printer.printAlert("Goodbye!");
                        break;
                    default:
                        Printer.printAlert("Invalid selection. Please try again.");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Printer.printAlert("There was an error retrieving the selected information.");
            }

            Printer.printInstructions("Press enter to continue...");
            scanner.nextLine();

        } while (!selection.equals("6"));

    }

    private void printMenu() {
        Printer.printTitle("Main Menu");
        Printer.printInstructions("Select an Option below:");

        for (int i = 0; i < options.size(); i++) {
            Printer.printOption(options.get(i), i + 1);
        }
    }

    private void displayAllFood() throws SQLException {

        List<Food> allFood = foodDao.getAllFood();

        Printer.printTitle("All Food Items");

        if (allFood.size() == 0) Printer.printAlert("There are no foods to display...");

        for (Food food : allFood) {
            printFoodItem(food);
        }

        Printer.printSectionEnd();
    }

    private void displayFood() throws SQLException {
        Printer.printTitle("Food Item");
        printFoodItem(getFoodById());
        Printer.printSectionEnd();
    }

    private void printFoodItem(Food foodItem) {
        Printer.printResponse("#" + foodItem.getId() + " " + foodItem.getName() + " $" + foodItem.getPrice() + " per " + foodItem.getQuantity());
    }

    private void createFood() throws SQLException {
        Printer.printTitle("New Food Item");
        Printer.printInstructions("Enter the new food name: ");
        String name = scanner.nextLine().trim();

        Printer.printInstructions("Enter the quantity: ");
        String quantity = scanner.nextLine().trim();

        if (quantity.equals("") || quantity.equals("1")) quantity = "1 item";

        Printer.printInstructions("Enter the price: ");
        Double price = scanner.nextDouble();

        foodDao.createNewFood(name, quantity, price);

        Printer.printSectionEnd();
    }

    private void updateFood() throws SQLException {
        Printer.printTitle("Update Food Item");

        Food foodItem = getFoodById();

        printFoodItem(foodItem);
        Printer.printInstructions("Select what you want to update: ");
        Printer.printOption("Name", 1);
        Printer.printOption("Price", 2);
        Printer.printOption("Quantity", 3);
        Printer.printOption("Cancel", 0);

        int selection = Integer.parseInt(scanner.nextLine());

        switch (selection) {
            case 1: setFoodName(foodItem);
                break;
            case 2: setFoodPrice(foodItem);
                break;
            case 3: setFoodQuantity(foodItem);
                break;
            default: // ANY OTHER KEY TO CANCEL
                Printer.printAlert("Cancelling update...\n");
                break;
        }

        Printer.printSectionEnd();
    }

    private void setFoodName(Food foodItem) throws SQLException {
        Printer.printInstructions("Enter the new name: ");
        foodItem.setName(scanner.nextLine());

        foodDao.updateFood(foodItem);
    }

    private void setFoodPrice(Food foodItem) throws SQLException {
        Printer.printInstructions("Enter the new price: ");
        foodItem.setPrice(Double.parseDouble(scanner.nextLine()));

        foodDao.updateFood(foodItem);
    }

    private void setFoodQuantity(Food foodItem) throws SQLException {
        Printer.printInstructions("Enter the new quantity: ");
        foodItem.setQuantity(scanner.nextLine());

        foodDao.updateFood(foodItem);
    }

    private void deleteFood() throws SQLException {
        Printer.printTitle("Delete Food Item");

        Food foodItem = getFoodById();
        printFoodItem(foodItem);
        Printer.printAlert("Are you sure you want to delete this item? Y or N? ");

        if (scanner.nextLine().toUpperCase().charAt(0) == 'Y') {
            foodDao.deleteFood(foodItem.getId());
        } else {
            Printer.printAlert("Cancelling delete...");
        }

        Printer.printSectionEnd();
    }

    private Food getFoodById() throws SQLException {
        Printer.printInstructions("Enter the food id: ");
        int id = Integer.parseInt(scanner.nextLine());
        return foodDao.getFoodById(id);
    }

}
