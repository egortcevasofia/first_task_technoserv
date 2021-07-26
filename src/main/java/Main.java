import service.FindCombinationService;
import service.ReadAndValidateService;

public class Main {
    public static void main(String[] args) {
        FindCombinationService findCombinationService = new FindCombinationService();
        findCombinationService.findCombination(args[0], args[1]);
    }
}
