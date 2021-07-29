package service;

import domain.Department;
import domain.Employee;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;


public class DoRecursionService {
    private ReadAndValidateService readAndValidateService = new ReadAndValidateService();

    public void findAllPossibleCombination(String pathForRead, String pathForWrite) {
        ArrayList<Department> listOfDepartments = readAndValidateService.readFromFile(pathForRead);
        readAndValidateService.printToConsole(listOfDepartments);

        for (int i = 0; i < listOfDepartments.size(); i++) {
            for (int j = i + 1; j < listOfDepartments.size(); j++) {
                writeAllPossibleCombinationToFile(listOfDepartments.get(i), listOfDepartments.get(j), pathForWrite);
            }
        }
    }


    private void writeAllPossibleCombinationToFile(Department leastDepartment, Department biggestDepartment, String pathForWrite) {
        try (FileWriter writer = new FileWriter(pathForWrite, false)) {
            BigDecimal bigSalary = biggestDepartment.getAverageSalary();
            BigDecimal smallSalary = leastDepartment.getAverageSalary();
            writer.write("Возможен перевод из департамента " + biggestDepartment.getName() + " в департамент "
                    + leastDepartment.getName() + ", где средние зарплаты: " + bigSalary.toString()
                    + " и " + smallSalary.toString() + " соответственно, следующих сотрудников: \n");

            ArrayList<Employee> listOfEmployee = (ArrayList<Employee>) biggestDepartment
                    .getEmployeeList();

            ArrayList<ArrayList<Employee>> listOfCombination = combination(0, listOfEmployee, new ArrayList<>());

            for (ArrayList<Employee> oneListCombination : listOfCombination) {
                if (bigSalary.compareTo(getSum(oneListCombination)) > 0
                        && getSum(oneListCombination).compareTo(smallSalary) > 0) {

                    writer.write(lineForWrite(oneListCombination, biggestDepartment, leastDepartment));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }

    private String lineForWrite(ArrayList<Employee> oneListCombination, Department biggestDepartment, Department leastDepartment) {
        BigDecimal bigSalary = biggestDepartment.getSumSalary();
        BigDecimal smallSalary = leastDepartment.getSumSalary();

        BigDecimal newBigAverageSalary = (bigSalary.subtract(getSum(oneListCombination)))
                .divide(new BigDecimal(biggestDepartment.getEmployeeList().size() - 1), 2, RoundingMode.HALF_UP);

        BigDecimal newSmallAverageSalary = (smallSalary.add(getSum(oneListCombination)))
                .divide(new BigDecimal(leastDepartment.getEmployeeList().size() + 1), 2, RoundingMode.HALF_UP);

        return oneListCombination.toString() + " from " + biggestDepartment.getName()
                + " to " + leastDepartment.getName()
                + " после перевода среднии зарплаты составлят соответственно, \n"
                + newBigAverageSalary.toString() + " и " + newSmallAverageSalary.toString() + "\n";
    }

    private BigDecimal getSum(ArrayList<Employee> listOfCombination) {
        BigDecimal result = new BigDecimal(BigInteger.ZERO);
        for (Employee employee : listOfCombination) {
            result = result.add(employee.getSalary());
        }
        return result;
    }





    private static ArrayList<ArrayList<Employee>> combination(int count, ArrayList<Employee> listOfEmployee, ArrayList<Employee> list) {
        ArrayList<ArrayList<Employee>> finalList = new ArrayList<>();
        for (int i = 0; i < listOfEmployee.size(); i++) {
            if (list.contains(listOfEmployee.get(i))) break;
            ArrayList<Employee> newList = new ArrayList<>();
            newList.add(listOfEmployee.get(i));
            newList.addAll(list);
            finalList.add(newList);

            if (count < listOfEmployee.size() - 1) {
                ArrayList<Employee> list1 = new ArrayList<>(list);
                list1.add(listOfEmployee.get(i));

                finalList.addAll(combination(count + 1, listOfEmployee, list1));
            }
        }

        return finalList;
    }


}
