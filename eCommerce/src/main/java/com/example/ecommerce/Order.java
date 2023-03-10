package com.example.ecommerce;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class Order {

      static TableView<Product> orderTable;                                         //create order table

    public  boolean placeOrder(Customer customer, Product product){                  //to check customer and product in database
        try {
            String placeOrder = "INSERT INTO orders (customer_id, product_id, status) VALUES("+ customer.getId() +","+ product.getId() +",'Ordered')";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdate(placeOrder);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public int placeMultipleOrderProducts(ObservableList<Product> productObservableList, Customer customer){     //customer places many orders
        int count = 0;
        for(Product product : productObservableList){
            if(placeOrder(customer, product))
                count++;
        }
        return count;
    }

    public Pane getAllProducts(){
        ObservableList<Product> productsList = Product.getAllProducts();
        return createTableFromList(productsList);
    }

    public static Pane createTableFromList(ObservableList<Product> orderList) {             //create table with product id, name, price
        TableColumn id = new TableColumn("Id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name = new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn price = new TableColumn("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

//        ObservableList<Product> data = FXCollections.observableArrayList();
//        data.addAll(new Product(123, "Laptop", (double) 234.5),
//                new Product(123, "Laptop", (double) 234.5)
//        );

        orderTable = new TableView<>();
        orderTable.setItems(orderList);
        orderTable.getColumns().addAll(id, name, price);

        Pane tablePane = new Pane();
        tablePane.getChildren().add(orderTable);

        return tablePane;
    }

    public static Pane getOrders(Customer customer){
        if (customer==null) System.out.println("null");
        String order = "SELECT orders.oid, products.name, products.price FROM orders INNER JOIN products ON orders.product_id = products.pid WHERE customer_id = " + customer.getId();
        ObservableList<Product> orderList = Product.getProducts(order);

        return createTableFromList(orderList);
    }

}
