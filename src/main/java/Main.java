import java.util.List;

public class Main {
    public static void main(String[] args) {
        // task one
        Parser.parseCSVtoJSON();
        // task two
        Parser.parseXMLtoJSON();
        // task three
        List<Employee> list = SerializableJson.serializable("data.json");
        for (Employee employee : list){
            System.out.println(employee);
        }
    }
}
