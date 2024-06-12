package model.dao;

import model.entity.Customer;
import model.entity.Order;
import model.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderDaoImpl implements OrderDao{
    @Override
    public int addNewOrder(Order order) {
        String sql = """
                INSERT INTO "order" (id,order_name, order_description,cus_id,ordered_at)
                VALUES (?,?,?,?,?)
                """;
        String sql1 =
                """
                        INSERT INTO "product_order"
                        VALUES (?,?)
                        """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/food_panda_db",
                        "postgres",
                        "123"
                );
                PreparedStatement preparedStatement
                        = connection.prepareStatement(sql);
                PreparedStatement preparedStatement1
                         = connection.prepareStatement(sql1);
                Statement statement
                        = connection.createStatement();
                ){
            preparedStatement.setInt(1,order.getId());
            preparedStatement.setString(2,order.getOrderName());
            preparedStatement.setString(3, order.getOrderDescription());
            preparedStatement.setInt(4,order.getCustomer().getId());
            preparedStatement.setDate(5,order.getOrderedAt());
            // product order
            for(Product product: order.getProductList()){
                preparedStatement1.setInt(1,product.getId());
                preparedStatement1.setInt(2,order.getId());
            }
            int rowAffected = preparedStatement.executeUpdate();
            int rowAffected1 = preparedStatement1.executeUpdate();

            String message = rowAffected>0 ? "Insert successfully": "Insert failed";
            System.out.println(message);
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return 0;
    }

    @Override
    public int deleteOrderById(Integer id) {
        String sql = """
                DELETE FROM "order"
                WHERE id = ?
                """;
        try (
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/food_panda_db",
                        "postgres",
                        "123"
                );
                PreparedStatement preparedStatement
                         = connection.prepareStatement(sql);
                ){
            Order order = searchOrderById(id);
            String message = order==null? "Cannot find order": "Found Order";
            System.out.println(message);
            if(order!=null){
                preparedStatement.setInt(1,id);
                int rowAffected = preparedStatement.executeUpdate();
                String message1 = rowAffected>0 ? "Deleted Successfully": "Cannot delete";
                System.out.println(message1);
                return rowAffected;
            }

        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return 0;
    }

    @Override
    public int updateOrderById(Integer id) {
        String sql = """
                UPDATE "order"
                SET order_name = ?, order_description = ?
                WHERE id = ?
                """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/food_panda_db",
                        "postgres",
                        "123"
                );
                PreparedStatement preparedStatement
                         = connection.prepareStatement(sql);
                ){
            Order order = searchOrderById(id);
            if(order!=null){
                System.out.print("[+] Order name: ");
                preparedStatement.setString(1,new Scanner(System.in).nextLine());
                System.out.print("[+] Order description: ");
                preparedStatement.setString(2,new Scanner(System.in).nextLine());
                preparedStatement.setInt(3,id);
                int rowAffected = preparedStatement.executeUpdate();
                if(rowAffected>0){
                    System.out.println("Update successfully");
                    return rowAffected;
                }
            }else {
                System.out.println("Order is not found");
            }
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return 0;
    }

    @Override
    public Order searchOrderById(Integer id) {
        String sql = """
                SELECT * FROM "order"
                WHERE id = ?
                """;
        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/food_panda_db",
                        "postgres",
                        "123"
                );
                PreparedStatement preparedStatement
                         = connection.prepareStatement(sql);
                ) {
            Order order = null;
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                order = Order.builder()
                        .id(resultSet.getInt("id"))
                        .orderName(resultSet.getString("order_name"))
                        .orderDescription(resultSet.getString("order_description"))
                        .customer(Customer.builder()
                                .id(resultSet.getInt("cus_id"))
                                .build())
                        .productList(new ArrayList<>())
                        .build();
            }
            return order;

        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public List<Order> queryAllOrders() {
        String sql = """
                       SELECT * FROM "order"
                       INNER JOIN customer c ON "order".cus_id = c.id
                """;
        //

        try(
                Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/food_panda_db",
                        "postgres",
                        "123"
                );
                Statement statement = connection.createStatement();
                ){
            ResultSet resultSet = statement.executeQuery(sql);
            List<Order> orderList = new ArrayList<>();
            while (resultSet.next()){
                orderList.add(
                      Order.builder()
                              .id(resultSet.getInt("id"))
                              .orderName(resultSet.getString("order_name"))
                              .orderDescription(resultSet.getString("order_description"))
                              .orderedAt(resultSet.getDate("ordered_at"))
                              .customer(Customer.builder()
                                      .id(resultSet.getInt("cus_id"))
                                      .name(resultSet.getString("name"))
                                      .email(resultSet.getString("email"))
                                      .password(resultSet.getString("password"))
                                      .isDeleted(resultSet.getBoolean("is_deleted"))
                                      .createdDate(resultSet.getDate("created_date"))
                                      .bio(resultSet.getString("bio"))
                                      .build())
                              .build()
                );
            }
            return orderList;
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return new ArrayList<>();
    }
}
