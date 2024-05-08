package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Employee;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class employeeService implements empService<Employee> {
    private Connection connection;
    public employeeService() {
        this.connection = MyDB.getInstance().getConnection();
    }

    @Override
    public  void add(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee (idep,nom, email, salaire) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employee.getIdep());
            statement.setString(2, employee.getNom());
            statement.setString(3, employee.getEmail());
            statement.setInt(4, employee.getSalaire());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }













    @Override
    public void update(Employee employee) throws SQLException {
        String sql = "update employee set nom = ?,idep = ? , email = ?, salaire = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employee.getNom());
            preparedStatement.setInt(2, employee.getIdep());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setDouble(4, employee.getSalaire());

            preparedStatement.setInt(5, employee.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(int id) throws SQLException {
        Statement ste = connection.createStatement();
        String query = "delete from employee where id = '" + id + "'";
        ste.executeUpdate(query);
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        return List.of();
    }


    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> Employees = new ArrayList<>();

        String sql = "SELECT * FROM employee";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                // Assuming your Employee class has appropriate setters
                employee.setId(resultSet.getInt("id"));
                employee.setNom(resultSet.getString("nom"));
                employee.setEmail(resultSet.getString("email"));
                employee.setSalaire(resultSet.getInt("salaire"));
                // You might also want to fetch the department information here
                // For example:
                // employee.setDepartment(fetchDepartment(resultSet.getInt("idep")));

                Employees.add(employee);
            }
        } catch (SQLException e) {
            // Handle SQLException
            throw new SQLException("Error occurred while fetching employees.", e);
        }

        return Employees;
    }

    public ObservableList<Employee> getEmployeeList() throws SQLException {

        ObservableList<Employee> employeeList = FXCollections.observableArrayList();

        List <Employee> emp = new ArrayList<>();
        Statement stm = connection.createStatement();
        String query = "select * from employee";
        ResultSet rs;
        rs = stm.executeQuery(query);
        while (rs.next()) {
            Employee r = new Employee();
            r.setIdep(rs.getInt("idep"));
            r.setId(rs.getInt("id"));
            r.setSalaire((int) rs.getFloat("salaire"));
            r.setNom(rs.getString("nom"));
            r.setEmail(rs.getString("email"));

            employeeList.add(r);
        }
        return employeeList;

    }



    @Override
    public Employee getById(int idemployee) throws SQLException {
        String sql = "SELECT `idep`, `nom`,`email`,`salaire` FROM `employee` WHERE `id` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idemployee);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int idep = resultSet.getInt("idep");

            String nom = resultSet.getString("nom");
            String email = resultSet.getString("email");
            int salaire = resultSet.getInt("salaire");


            return new Employee(idemployee, idep,nom,email,salaire);
        } else {
            // Handle the case when no matching record is found
            return null;
        }

    }


    public List<Employee> getEmployeeForPage(int pageIndex, int itemsPerPage) throws SQLException {
        // Calculez l'index de départ en fonction du numéro de page et du nombre d'éléments par page
        int startIndex = pageIndex * itemsPerPage;

        MyDB myDB = new MyDB();
        Connection connection = myDB.getConnection();


        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Préparez la requête SQL pour récupérer les employés de la page actuelle
            String query = "SELECT * FROM employee LIMIT ?, ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, startIndex);
            statement.setInt(2, itemsPerPage);
            resultSet = statement.executeQuery();

            // Parcourez le résultat et créez une liste d'objets Employee
            List<Employee> employeeList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String email = resultSet.getString("email");
                int salaire =  resultSet.getInt("salaire");
                int idep = resultSet.getInt("idep");
                Employee employee = new Employee(id,idep,nom, email, salaire);
                employeeList.add(employee);
            }
            return employeeList;
        } finally {
            // Fermez les ressources
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


}
