
import model.dao.OrderDaoImpl;
import model.entity.Customer;
import model.entity.Order;
import model.entity.Product;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    public static void main(String[] args) {
        List<Integer> pr = new ArrayList<>(List.of(1,2));
        for(Integer p: pr){
            new OrderDaoImpl()
                    .addNewOrder(Order.builder()
                            .id(new Random().nextInt(99999))
                            .orderName("Sokvan")
                            .orderDescription("ម្ទេស 100 គ្រាប់")
                            .orderedAt(Date.valueOf(LocalDate.of(1999,06,06)))
                            .customer(Customer.builder()
                                    .id(6)
                                    .build())
                            .productList(new ArrayList<>(
                                    List.of(Product.builder()
                                            .id(p)
                                            .build())
                            ))
                            .build());
        }
    }
}