import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SerializableJson {
    public static List serializable(String fileName) {
        String json = readString(fileName);
        return jsonToList(json);
    }

    private static List jsonToList(String json) {
        List<Employee> list = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        JSONParser parser = new JSONParser();
        try {
            JSONArray array = (JSONArray) parser.parse(json);
            for (Object object : array) {
                list.add(gson.fromJson(object.toString(), Employee.class));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static String readString(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}