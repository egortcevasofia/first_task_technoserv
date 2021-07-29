import service.DoRecursionService;

public class StartFindAllCombinations {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Неверно заданы пути для файлов чтения и записи," +
                    " в параметры необходимо передать два пути к расположению файлов:" +
                    " первый - путь для считывания, второй - для записи");
            return;
        }

        DoRecursionService doRecursionService = new DoRecursionService();
        doRecursionService.findAllPossibleCombination(args[0], args[1]);
    }
}
