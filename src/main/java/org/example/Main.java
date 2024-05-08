package org.example;

import services.employeeService;

import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
/*
        employee p1 = new employee(4, 7, "fffffffffffff", "000000000000000@gmail.com45", 500);
         // Assuming department ID 7


// Check if the employee's name is not null before adding it to the database
        if (p1.getNom() != null && !p1.getNom().isEmpty()) {
            employeeService service = new employeeService();

            try {
                service.add(p1);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.err.println("Employee's name is null or empty.");
        }
*/

/*
        departmentService service = new departmentService();  ;
        try {
            service.delete(11);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
*/
        /*
        department p1 = new department( 10, "looop", "realmadrid", 7878);
        departmentService service = new departmentService();
        try {
            service.update(p1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
*/
        /*departmentService service = new departmentService();  ;
        try {

            System.out.println(service.getDepartmentList());
        } catch (SQLException e) {
            System.out.println(e.getMessage());}
*/
        employeeService service = new employeeService();  ;
        try {

            System.out.println(service.getEmployeeList());
        } catch (SQLException e) {
            System.out.println(e.getMessage());}
        }}