package service;

import domain.Department;
import domain.Employee;
import exception.SalaryNotValidException;
import exception.StringNotValidException;


import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReadAndValidateService {
    private Comparator<Department> comparator = (d1, d2) -> d1.getAverageSalary().compareTo(d2.getAverageSalary());



    public ArrayList<Department> readFromFile(String path) {
        Map<String, Department> mapOfDepartment = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            int numberOfLine = 0;
            System.out.println("Строки в которых обнаружена ошибка, не учитываются при расчете средней зарплаты и в поиске комбинаций.");
            System.out.println("");
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(", ");
                if (isValid(words, ++numberOfLine)) {
                    String name = words[0];
                    String departmentName = words[1];
                    BigDecimal salary = new BigDecimal(words[2]);
                    Department department = mapOfDepartment.getOrDefault(departmentName, new Department(departmentName, new ArrayList<>()));
                    department.getEmployeeList().add(new Employee(name, salary));
                    mapOfDepartment.put(departmentName, department);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла проблема при прочтении файла");
        }
        ArrayList<Department> listOfDepartments = new ArrayList<>(mapOfDepartment.values());
        listOfDepartments.sort(comparator);
        return listOfDepartments;
    }




    private Boolean isValid(String[] words, int numberOfLine) {
        try {
            if (words.length != 3)
                throw new StringNotValidException(String.format("В строке %s более или менее 3-х параметров", numberOfLine));
            if (words[0].trim().isEmpty()) throw new StringNotValidException(String.format("В строке %s имя пустое", numberOfLine));
            if (words[1].trim().isEmpty()) throw new StringNotValidException(String.format("В строке %s департамент пустой", numberOfLine));
            if (new BigDecimal(words[2]).scale() >= 3 ) throw new SalaryNotValidException(String.format("В строке %s после запятой более двух знаков", numberOfLine));
            if (new BigDecimal(words[2]).compareTo(new BigDecimal(0)) < 0 ) throw new SalaryNotValidException(String.format("В строке %s в поле зарплата отрицательное число", numberOfLine));

        } catch (NumberFormatException e) {
            System.out.println(String.format("В строке %s в поле зарплата записано не числовое значение", numberOfLine));
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void printToConsole(ArrayList<Department> listOfDepartments){
        for (Department listOfDepartment : listOfDepartments) {
            System.out.println("");
            System.out.println("Департамент: " + listOfDepartment.getName());
            System.out.println("Средняя зарплата: $" + listOfDepartment.getAverageSalary());
            for (int j = 0; j < listOfDepartment.getEmployeeList().size(); j++) {
                System.out.printf("%2d. %-20s $%.2f%n",  j + 1, listOfDepartment.getEmployeeList().get(j).getName(), listOfDepartment.getEmployeeList().get(j).getSalary());
            }
        }

    }

}




