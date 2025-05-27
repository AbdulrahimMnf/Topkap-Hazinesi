package org.example.topkapihazinensi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.topkapihazinensi.models.Category;
import org.example.topkapihazinensi.models.Expense;
import org.example.topkapihazinensi.untils.DatabaseConnection;
import org.example.topkapihazinensi.untils.UserSession;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class IndexController {

    @FXML
    public VBox categoryContainer;

    @FXML
    public VBox expensesTable;

    @FXML
    public Label usertbl;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Label totalLbl;


    private final DatabaseConnection databaseConnection;
    private final Alert alert;
    private String selectExpenceSQL;


    public IndexController() {

        // plan basit getirme eyleminin query ile oynamak filtre için olabilecek en kolay
        // yöntem yani defualt olarak ilk herşeyi getirecek ardında filtre method
        // bu "selectExpenceSQL" değiştirerek loadExpences method çağıracaklar
        this.selectExpenceSQL = "SELECT * FROM expenses where `user_id`=" + UserSession.getInstance().getUserId() ;

        // Tekrarları önlemek için obj fazla kullanılacak ve önemli yerler içinnnnn
        databaseConnection = new DatabaseConnection();
        alert = new Alert(Alert.AlertType.INFORMATION);
    }

    @FXML
    protected void initialize() {
        this.usertbl.setText(UserSession.getInstance().getUsername() + " - " + UserSession.getInstance().getUserEmail());
        loadCategories();
        loadExpenses();
    }



    @FXML
    private TableView<Expense> sexpensesTable;

    @FXML
    private TableColumn<Expense, String> descColumn;

    @FXML
    private TableColumn<Expense, Double> amountColumn;

    @FXML
    private TableColumn<Expense, String> dateColumn;

    @FXML
    private TableColumn<Expense, Button> deleteColumn;



    // init table to fill it with data
    private void initializeTable() {
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        deleteColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }




    // trying to build a clean code  -> common used function
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Close btn listener
    @FXML
    protected void onCloseBtn() {
        System.exit(0);
    }



    // Logout Function
    @FXML
    protected void logout(ActionEvent event) throws IOException {
        UserSession.clearSession(); // oturumu temizle

        Parent root = FXMLLoader.load(getClass().getResource("auth.fxml")); // login sayfasını yükle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root)); // yeni sahneyi ayarla
        stage.show(); // sahneyi göster
    }





    //------------------------- Categories Management Section ------------------------------------------------------------


    @FXML
    protected void allCategoriesClick(ActionEvent event)
    {
        System.out.println("clicked all categories");
        this.selectExpenceSQL = "SELECT * FROM expenses where `user_id`=" + UserSession.getInstance().getUserId() ;
        loadCategories();
        loadExpenses();
    }



    @FXML
    protected void OnCreateCategoryBtnClicked(ActionEvent event) {

        Stage owner = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(owner);
        modalStage.setTitle("Create a Category Input Form");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label descLabel = new Label("Description:");
        TextArea descField = new TextArea();
        descField.setPrefRowCount(3);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setOnAction(e -> {
            String name = nameField.getText();
            String desc = descField.getText();
            saveCategories(name, desc);
            modalStage.close();
        });

        cancelBtn.setOnAction(e -> modalStage.close());

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setPadding(new Insets(10));
        buttons.setStyle("-fx-alignment: center;");

        VBox layout = new VBox(10,
                nameLabel, nameField,
                descLabel, descField,
                buttons
        );
        layout.setPadding(new Insets(15));

        modalStage.setScene(new Scene(layout));
        modalStage.showAndWait();
    }

    // Load Categories
    private void loadCategories(){


        categoryContainer.getChildren().clear();

        // Inside your loadCategories() method:
        String sql = "SELECT * FROM categories where `user_id` =" + UserSession.getInstance().getUserId();
        List<Map<String, Object>> rows = DatabaseConnection.SelectExecute(sql);

        for (Map<String, Object> row : rows) {
            String categoryName = row.get("name").toString();
            int categoryId = Integer.parseInt(row.get("id").toString());

            // Create card container
            HBox card = new HBox(10);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10; -fx-border-color: #ddd; -fx-border-radius: 10;");
            card.setMinHeight(50);

            // Category label
            Label categoryLabel = new Label(categoryName);
            categoryLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

            // Spacer between label and buttons
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Select button
            Button selectButton = new Button(">");
            selectButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold");
            selectButton.setOnAction(e -> {
                findById(categoryId);
            });

            // Delete button
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> {
                deleteCategory(categoryId);
            });

            // Add components to card
            card.getChildren().addAll(categoryLabel, spacer, deleteButton , selectButton);

            // Add card to container
            categoryContainer.getChildren().add(card);
        }
    }


    // Find Expenses By category Id
    private void findById(int id) {
        this.selectExpenceSQL = "SELECT * FROM expenses WHERE `category_id` = " + id;
        loadExpenses();
    }

    // Add Category
    private  void saveCategories(String name, String description)
    {
        String sql = "INSERT INTO categories(name,description,user_id) VALUES('"+ name +"','"+description+"',"+ UserSession.getInstance().getUserId()+")";
        Boolean category = databaseConnection.CUDExecute(sql);
        if (category) {
            alert.setTitle("Category Saved");
            alert.setHeaderText(name + " - " + description);
            loadCategories();
            alert.showAndWait();
        }
    }

    // Delete Category
    private void deleteCategory(int Id)
    {
        String sql = "DELETE FROM categories WHERE `id` = " + Id;
        Boolean category = databaseConnection.CUDExecute(sql);
        if (category) {
            alert.setTitle("Delete");
            alert.setHeaderText("Category Removed");
            loadCategories();
            alert.showAndWait();
        }
    }

    //------------------------- End Categories Management Section ------------------------------------------------------------













    //------------------------- Expenses Management Section ------------------------------------------------------------

    // load expenses
    private void loadExpenses() {


        initializeTable();
        double sum = 0;
        ObservableList<Expense> expenseList = FXCollections.observableArrayList();


        List<Map<String, Object>> rows = DatabaseConnection.SelectExecute(this.selectExpenceSQL);

        for (Map<String, Object> row : rows) {
            String description = (String) row.get("description");
            double amount = ((Number) row.get("amount")).doubleValue();
            sum += amount;

            String category = (String) row.get("category");
            String date = row.get("created_at").toString(); // format if needed
            String userId = row.get("user_id").toString();

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> {
                alert.setTitle("Delete Category");
                alert.setHeaderText(row.get("id").toString());
                alert.showAndWait();
            });
            // now i have to init it in table

            Expense expense = new Expense(description, amount, 0, date, userId);
            expenseList.add(expense);
        }


        this.totalLbl.setText("Total: " +  String.valueOf(sum));
        sexpensesTable.setItems(expenseList);

        addDeleteButtonToTable();
    }


    private  Boolean deleteBtnState = false;

    // add delete btn to table
    private void addDeleteButtonToTable() {
        if (!deleteBtnState) {
        TableColumn<Expense, Void> colBtn = new TableColumn<>("Delete");

        Callback<TableColumn<Expense, Void>, TableCell<Expense, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Expense, Void> call(final TableColumn<Expense, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Expense expense = getTableView().getItems().get(getIndex());
                            // Perform delete logic here
                            deleteExpence(expense.getDescription() , expense.getAmount());

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }

                };
            }
        };

        colBtn.setCellFactory(cellFactory);
        sexpensesTable.getColumns().add(colBtn);
        deleteBtnState = true;
    }
    }


    // Delete Expenses
    public void deleteExpence(String description , Double amount)
    {
        String sql = "DELETE FROM expenses WHERE `description` = '" + description + "' AND `amount` = " + amount;
        Boolean execute = DatabaseConnection.CUDExecute(sql);
        if (execute) {
            alert.setTitle("Delete");
            alert.setHeaderText("Delete Expense successfully");
            loadExpenses();
            alert.showAndWait();
        }
    }


    // Search btn clicked
    @FXML
    private void onSearchByDateClicked() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String from = fromDatePicker.getValue().toString() + " 00:00:00";


        // To eklemesin diye otomatik almali
        String to = (toDatePicker.getValue() != null)
                ? toDatePicker.getValue().toString() + " 23:59:59"
                : LocalDateTime.now().format(formatter);


        this.selectExpenceSQL = "SELECT * FROM expenses WHERE created_at BETWEEN '" + from + "' AND '" + to + "'"
                + " AND user_id = '" + UserSession.getInstance().getUserId() + "'";
        loadExpenses();
    }

    // Open Create Form
    @FXML
    private void onExpenceCreateBtnClicked(ActionEvent event)
    {
        Stage owner = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(owner);
        modalStage.setTitle("Add New Expense");

        Label categoryLabel = new Label("Category:");
        ComboBox<Category>  categoryBox = new ComboBox<>();


        // DIKKATTTTTTTTTTTTTTTTTTTTTTTT bu kodu kaldirmayi unutmak
        String sql = "SELECT * FROM categories where `user_id` = " + UserSession.getInstance().getUserId();
        List<Map<String, Object>> rows = DatabaseConnection.SelectExecute(sql);


        for (Map<String, Object> row : rows) {
            int id = (int) row.get("id");
            String name = row.get("name").toString();

            Category category = new Category(id, name);
            categoryBox.getItems().add(category);
        }


        categoryBox.setPromptText("Select a category");

// Amount input
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");

// Description input
        Label descLabel = new Label("Description:");
        TextArea descField = new TextArea();
        descField.setPromptText("Optional description");
        descField.setPrefRowCount(3);

// Buttons
        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        saveBtn.setOnAction(e -> {
            try {
                Category selected = categoryBox.getValue();
                int category_id = selected.getId();
                String category_name = selected.getName();


                System.out.println("Category: " + category_name+ " - Id :  " + category_id);

                if (category_name == null || category_name.isEmpty()) {
                    throw new IllegalArgumentException("Please select a category.");
                }

                double amount = Double.parseDouble(amountField.getText());
                String description = descField.getText();

                System.out.println(category_id);
                Expense expense = new Expense(description, amount, category_id, null, null);
                saveExpense(expense);
                modalStage.close();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Please enter a valid number for amount.").showAndWait();
            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
            }
        });

        cancelBtn.setOnAction(e -> modalStage.close());

// Layout
        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));

        formGrid.add(categoryLabel, 0, 0);
        formGrid.add(categoryBox, 1, 0);

        formGrid.add(amountLabel, 0, 1);
        formGrid.add(amountField, 1, 1);

        formGrid.add(descLabel, 0, 2);
        formGrid.add(descField, 1, 2);

        VBox root = new VBox(15, formGrid, buttonBox);
        root.setPadding(new Insets(10));

        modalStage.setScene(new Scene(root));
        modalStage.showAndWait();


    }

    // Save Expense to Database
    private void saveExpense(Expense expense) {
        String sql = "INSERT INTO `expenses` (`description`, `amount`, `category_id`,  `user_id`) VALUES ("
                + "'" + expense.getDescription() + "', "
                + expense.getAmount() + ", "
                + "'" + expense.getCategory() + "', "
                + "'" + UserSession.getInstance().getUserId() + "')";
        //expense.getUserId()
        boolean response =  DatabaseConnection.CUDExecute(sql);
        if (response) {
            loadExpenses();
        }

    }



    //------------------------- End Expenses Management Section -------------------------------------------------------









    //------------------------- Report Management Section ------------------------------------------------------------


    public void showReportsModal() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Your Reports");

        VBox modalLayout = new VBox(10);
        modalLayout.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox reportContainer = new VBox(10); // Cards will be added here
        scrollPane.setContent(reportContainer);

        modalLayout.getChildren().add(scrollPane);

        // Load report cards into this container
        loadReportsInto(reportContainer);

        Scene modalScene = new Scene(modalLayout, 500, 600);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }


    public void showCreateReportModal() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Create New Report");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Report type choice
        Label typeLabel = new Label("Report Type:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("daily", "weekly", "monthly", "yearly", "custom");

        // Start and end date
        Label startLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker();

        Label endLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker();

        // Submit button
        Button saveBtn = new Button("Save Report");
        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> {
            String type = typeBox.getValue();
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();

            if (type == null || start == null || end == null) {
                showAlert("Please fill all fields.");
                return;
            }

            createReport(type, start, end);
            modalStage.close();
        });

        layout.getChildren().addAll(typeLabel, typeBox, startLabel, startDatePicker, endLabel, endDatePicker, saveBtn);

        Scene scene = new Scene(layout, 350, 300);
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }


    private void loadReportsInto(VBox container) {
        container.getChildren().clear();

        String sql = "SELECT * FROM reports WHERE user_id = " + UserSession.getInstance().getUserId();
        List<Map<String, Object>> rows = DatabaseConnection.SelectExecute(sql);

        for (Map<String, Object> row : rows) {
            String type = row.get("report_type").toString();
            String start = row.get("start_date").toString();
            String end = row.get("end_date").toString();
            String createdAt = row.get("created_at").toString();
            String totalAmount = row.get("total_amount").toString();
            String expenseCount = row.get("expense_count").toString();

            HBox card = new HBox(10);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-border-color: #ccc; -fx-border-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

            VBox reportInfo = new VBox(5);
            Label typeLabel = new Label("📊 Type: " + type);
            Label dateRange = new Label("📅 " + start + " → " + end);
            Label stats = new Label("💸 " + totalAmount + " | " + expenseCount + " expenses");
            Label createdLabel = new Label("🕓 Created: " + createdAt);

            typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            stats.setStyle("-fx-text-fill: #444;");

            reportInfo.getChildren().addAll(typeLabel, dateRange, stats, createdLabel);

            card.getChildren().add(reportInfo);
            container.getChildren().add(card);
        }
    }


    private void createReport(String type, LocalDate start, LocalDate end) {

        // run this to get between data
        String get_expenses_sql = "SELECT * FROM `expenses` WHERE `user_id` = " + UserSession.getInstance().getUserId() +
                " AND `created_at` BETWEEN '" + start.toString() + "' AND '" + end.toString() + "'";

        String query = "SELECT * FROM `reports` WHERE `user_id` = " + UserSession.getInstance().getUserId() +
                " AND `start_date` = '" + start.toString() + "' AND `end_date` = '" + end.toString() + "'";

        List<Map<String, Object>> reportData = DatabaseConnection.SelectExecute(query);

//        String sql = "INSERT INTO reports (`user_id`, `report_type`, `start_date`, `end_date`, `total_amount`, `expense_count`, `report_data`, `created_at`) VALUES("
//                + UserSession.getInstance().getUserId() + ", '"
//                + type + "', '"
//                + start.toString() + "', '"
//                + end.toString() + "', "
//                + 100.0 + ", "
//                + reportData.size() + ", '"
//                + reportData + "', NOW())";

        String sql = "INSERT INTO reports (`user_id`, `report_type`, `start_date`, `end_date`, `total_amount`, `expense_count`, `report_data`, `created_at`) VALUES("
                + UserSession.getInstance().getUserId() + ", '"
                + type + "', '"
                + start.toString() + "', '"
                + end.toString() + "', "
                + 0.0 + ", "
                +"0"+ ", '"
                + "0" + "', NOW())";
        Boolean state =  DatabaseConnection.CUDExecute(sql);
        if (state) {
            showAlert("Report created successfully.");
        }
        else {
            showAlert("Report creation failed.");
        }

    }


    //------------------------- End Report Management Section ------------------------------------------------------------




}
