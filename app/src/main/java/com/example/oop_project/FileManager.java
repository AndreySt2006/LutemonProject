package com.example.oop_project;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import com.example.oop_project.model.Lutemon;


public class FileManager {
    private static final String DEFAULT_FILENAME = "lutemons_list.json";

    // Save list to internal storage
    public static void saveList(Context context, List<Lutemon> list, String filename) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load list from internal storage
    public static List<Lutemon> loadList(Context context, String filename) {
        Gson gson = new Gson();
        try (FileInputStream fis = context.openFileInput(filename)) {
            StringBuilder sb = new StringBuilder();
            int content;
            while ((content = fis.read()) != -1) {
                sb.append((char) content);
            }
            Type listType = new TypeToken<List<Lutemon>>(){}.getType();
            return gson.fromJson(sb.toString(), listType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Overload for default filename
    public static void saveList(Context context, List<Lutemon> list) {
        saveList(context, list, DEFAULT_FILENAME);
    }

    public static List<Lutemon> loadList(Context context) {
        return loadList(context, DEFAULT_FILENAME);
    }
}
