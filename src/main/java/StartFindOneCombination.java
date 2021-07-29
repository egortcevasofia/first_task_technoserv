import service.FindCombinationService;


public class StartFindOneCombination {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Неверно заданы пути для файлов чтения и записи," +
                    " в параметры необходимо передать два пути к расположению файлов:" +
                    " первый - путь для считывания, второй - для записи");
            return;
        }

        FindCombinationService findCombinationService = new FindCombinationService();
        findCombinationService.findCombination(args[0], args[1]);

    }

}


