package application;

import dao.FoodDao;
import entity.Food;
import util.Printer;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private Printer printer = Printer.getInstance();
    private Scanner scanner = new Scanner(System.in);
    private List<String> options = Arrays.asList(
            "Display current food items",
            "Display a specific food item",
            "Create a food item",
            "Update a food item",
            "Delete a food item",
            "Exit"
    );

    FoodDao foodDao = new FoodDao();

    public void start() {

        String selection = "";

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
                        printer.printAlert("Goodbye!");
                        break;
                    default:
                        printer.printAlert("Invalid selection. Please try again.");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                printer.printAlert("There was an error retrieving the selected information.");
            }

            printer.printInstructions("Press enter to continue...");
            scanner.nextLine();

        } while (!selection.equals("6"));

    }

    private void printMenu() {
        printer.printTitle("Main Menu");
        printer.printInstructions("Select an Option below:");

        for (int i = 0; i < options.size(); i++) {
            printer.printOption(options.get(i), i + 1);
        }
    }

    private void displayAllFood() throws SQLException {

        List<Food> allFood = foodDao.getAllFood();

        printer.printTitle("All Food Items");

        if (allFood.size() == 0) printer.printAlert("There are no foods to display...");

        for (Food food : allFood) {
            printFoodItem(food);
        }

        printer.printSectionEnd();
    }

    private void displayFood() throws SQLException {
        printer.printTitle("Food Item");
        printFoodItem(getFoodById());
        printer.printSectionEnd();
    }

    private void printFoodItem(Food foodItem) throws SQLException {
        printer.printResponse("#" + foodItem.getId() + " " + foodItem.getName() + " $" + foodItem.getPrice().doubleValue() + " per " + foodItem.getQuantity());
    }

    private void createFood() throws SQLException {
        printer.printTitle("New Food Item");
        printer.printInstructions("Enter the new food name: ");
        String name = scanner.nextLine().trim();

        printer.printInstructions("Enter the quantity: ");
        String quantity = scanner.nextLine().trim();

        if (quantity.equals("") || quantity.equals("1")) quantity = "1 item";

        printer.printInstructions("Enter the price: ");
        Double price = scanner.nextDouble();

        foodDao.createNewFood(name, quantity, price);

        printer.printSectionEnd();
    }

    private void updateFood() throws SQLException {
        printer.printTitle("Update Food Item");

        Food foodItem = getFoodById();

        printFoodItem(foodItem);
        printer.printInstructions("Select what you want to update: ");
        printer.printOption("Name", 1);
        printer.printOption("Price", 2);
        printer.printOption("Quantity", 3);
        printer.printOption("Cancel", 0);

        int selection = Integer.parseInt(scanner.nextLine());

        switch (selection) {
            case 1: setFoodName(foodItem);
                break;
            case 2: setFoodPrice(foodItem);
                break;
            case 3: setFoodQuantity(foodItem);
                break;
            default: // ANY OTHER KEY TO CANCEL
                printer.printAlert("Cancelling update...\n");
                break;
        }

        printer.printSectionEnd();
    }

    private void setFoodName(Food foodItem) throws SQLException {
        printer.printInstructions("Enter the new name: ");
        foodItem.setName(scanner.nextLine());

        foodDao.updateFood(foodItem);
    }

    private void setFoodPrice(Food foodItem) throws SQLException {
        printer.printInstructions("Enter the new price: ");
        foodItem.setPrice(Double.parseDouble(scanner.nextLine()));

        foodDao.updateFood(foodItem);
    }

    private void setFoodQuantity(Food foodItem) throws SQLException {
        printer.printInstructions("Enter the new quantity: ");
        foodItem.setQuantity(scanner.nextLine());

        foodDao.updateFood(foodItem);
    }

    private void deleteFood() throws SQLException {
        printer.printTitle("Delete Food Item");

        Food foodItem = getFoodById();
        printFoodItem(foodItem);
        printer.printAlert("Are you sure you want to delete this item? Y or N? ");

        if (scanner.nextLine().toUpperCase().charAt(0) == 'Y') {
            foodDao.deleteFood(foodItem.getId());
        } else {
            printer.printAlert("Cancelling delete...");
        }

        printer.printSectionEnd();
    }

    private Food getFoodById() throws SQLException {
        printer.printInstructions("Enter the food id: ");
        int id = Integer.parseInt(scanner.nextLine());
        return foodDao.getFoodById(id);
    }

}
