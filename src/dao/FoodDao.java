package dao;

import entity.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FoodDao {

    private Connection connection;
    private final String GET_ALL_FOOD_QUERY = "SELECT * FROM food";
    private final String GET_FOOD_BY_ID_QUERY = "SELECT * FROM food WHERE id = ?";
    private final String CREATE_NEW_FOOD_QUERY = "INSERT INTO food (name, quantity, price) VALUES (?, ?, ?)";
    private final String UPDATE_FOOD_QUERY = "UPDATE food SET name = ?, quantity = ?, price = ? WHERE id = ?";
    private final String DELETE_FOOD_QUERY = "DELETE FROM food WHERE id = ?";

    public FoodDao() {
        connection = DBConnection.getConnection();
    }

    public List<Food> getAllFood() throws SQLException {
        ResultSet resultSet = connection.prepareStatement(GET_ALL_FOOD_QUERY).executeQuery();
        List<Food> foodList = new ArrayList<>();

        while (resultSet.next()) {
            foodList.add(generateFoodItem(resultSet));
        }

        return foodList;
    }

    public Food getFoodById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_FOOD_BY_ID_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return generateFoodItem(resultSet);
        }

        return new Food(0, "", "", 0.0);
    }

    public void createNewFood(String name, String quantity, Double price) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CREATE_NEW_FOOD_QUERY);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, quantity);
        preparedStatement.setDouble(3, price);
        preparedStatement.executeUpdate();
    }

    public void updateFood(Food foodItem) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FOOD_QUERY);

        preparedStatement.setString(1, foodItem.getName());
        preparedStatement.setString(2, foodItem.getQuantity());
        preparedStatement.setDouble(3, foodItem.getPrice());
        preparedStatement.setInt(4, foodItem.getId());

        preparedStatement.executeUpdate();
    }

    public void deleteFood(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FOOD_QUERY);
        preparedStatement.setInt(1, id);

        preparedStatement.executeUpdate();
    }

    private Food generateFoodItem(ResultSet resultSet) throws SQLException {

        return new Food(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getDouble(4)
        );
    }

}
