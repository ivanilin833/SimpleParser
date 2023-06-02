import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
    private static final String fileCSVName = "data.csv";
    private static final String fileXMLName = "data.xml";

    public static void parseCSVtoJSON() {
        List<Employee> list = parseCSV(columnMapping, fileCSVName);
        String json = listToJson(list);
        writeString(json, "data.json");
    }

    public static void parseXMLtoJSON() {
        List<Employee> list = parseXML(fileXMLName);
        String json = listToJson(list);
        writeString(json, "data2.json");
    }


    private static void writeString(String json, String outFileName) {
        try (FileWriter writer = new FileWriter(outFileName)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> String listToJson(List<T> list) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static List parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean bean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return bean.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List parseXML(String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        List<Employee> list = new ArrayList<>();

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        NodeList employeeList = document.getElementsByTagName("employee");

        for (int i = 0; i < employeeList.getLength(); i++) {
            Element employee = (Element) employeeList.item(i);
            Long id = Long.valueOf(employee.getElementsByTagName("id").item(0).getTextContent());
            String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
            String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
            String country = employee.getElementsByTagName("country").item(0).getTextContent();
            int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());

            list.add(new Employee(id, firstName, lastName, country, age));
        }
        return list;
    }
}
