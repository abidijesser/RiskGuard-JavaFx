package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Department;
import models.Employee;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class departmentService implements depService<Department> {
    private Connection connection;
    public departmentService() {

        this.connection = MyDB.getInstance().getConnection();
    }






    @Override
    public void add(Department department) throws SQLException {
        String sql = "INSERT INTO departments (local, chef_dep, code) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, department.getLocal());
            statement.setString(2, department.getChef_dep());
            statement.setInt(3, department.getCode());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Department department) throws SQLException {
        String sql = "UPDATE departments SET  local = ?, chef_dep = ?, code = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ;

            preparedStatement.setString(1, department.getLocal());
            preparedStatement.setString(2, department.getChef_dep());
            preparedStatement.setInt(3, department.getCode());
            preparedStatement.setInt(4, department.getIdep());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idep) throws SQLException {
        Statement ste = connection.createStatement();
        String query = "delete from departments where id = '" + idep + "'";
        ste.executeUpdate(query);
    }

    @Override
    public List<Department> getAll() throws SQLException {
        String req = "select * from departments";

        List<Department> list1 = new ArrayList<>();
        Statement ste = connection.createStatement();
        ResultSet rs = ste.executeQuery(req);

        while (rs.next()) {
            Department r = new Department();
            r.setIdep(rs.getInt("idep"));
            r.setLocal(rs.getString("local"));
            r.setChef_dep(rs.getString("chef_dep"));
            r.setCode(rs.getInt("code"));

            list1.add(r);

        }
        return list1;
    }

    public ObservableList<Department> getDepartmentList() throws SQLException {
        ObservableList<Department> departmentList = FXCollections.observableArrayList();

        String query = "SELECT * FROM departments";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            while (rs.next()) {
                Department department = new Department();
                // Assuming the correct column name for department ID is 'id'
                department.setIdep(rs.getInt("id"));
                department.setLocal(rs.getString("local"));
                department.setChef_dep(rs.getString("chef_dep"));
                department.setCode(rs.getInt("code"));

                departmentList.add(department);
            }
        } catch (SQLException e) {
            // Handle SQLException
            throw new SQLException("Error occurred while fetching departments.", e);
        }

        return departmentList;
    }


    @Override
    public Department getById(int iddepartment) throws SQLException {

        String sql = "SELECT  `local`,`chef_dep`,`code` FROM `departments` WHERE `id` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, iddepartment);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {


            String local = resultSet.getString("local");
            String chef_dep = resultSet.getString("chef_dep");
            int code = resultSet.getInt("code");


            return new Department( iddepartment, local, chef_dep, code);
        } else {
            // Handle the case when no matching record is found
            return null;
        }
    }

}
