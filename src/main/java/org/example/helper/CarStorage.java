package org.example.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.CarDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CarStorage {
    private static final String FILE_NAME = "data/UserCarFilter/carDetails.json"; // Файл для зберігання даних
    private final ObjectMapper objectMapper;

    public CarStorage() {
        objectMapper = new ObjectMapper();
    }

    public void saveCarDetails(Map<Long, CarDetails> carDetailsMap) {
        try {
            objectMapper.writeValue(new File(FILE_NAME), carDetailsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Long, CarDetails> loadCarDetails() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                return objectMapper.readValue(file, objectMapper.getTypeFactory()
                        .constructMapType(HashMap.class, Long.class, CarDetails.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
