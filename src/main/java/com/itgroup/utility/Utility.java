package com.itgroup.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static final String FXML_PATH = "/com/itgroup/fxml/";
    public static final String IMAGE_PATH = "/com/itgroup/images/";
    public static final String CSS_PATH = "/com/itgroup/css/";
    public static final String DATA_PATH = "\\src\\main\\java\\com\\itgroup\\data\\";

    private static final Map<String, String> weightMap = new HashMap<>();

    private static String makeMapData(String weight, String mode) {
        weightMap.put("플라이급", "fly");
        weightMap.put("밴텀급", "bantam");
        weightMap.put("페더급", "feather");
        weightMap.put("라이트급", "light");
        weightMap.put("웰터급", "welter");
        weightMap.put("미들급", "middle");
        weightMap.put("라이트 헤비급", "light heavy");
        weightMap.put("헤비급", "heavy");

        if (mode.equals("value")) {
            return weightMap.get(weight);

        } else {
            for (String key : weightMap.keySet()) {
                if (weightMap.get(key).equals(weight)) {
                    return key;
                }
            }
            return null;
        }
    }

    public static String getWeightName(String weight, String mode) {
        return makeMapData(weight, mode);
    }

    public static LocalDate getDatePicker(String debut) {
        String datePart = debut.split(" ")[0];
        System.out.println(datePart);

        int year = Integer.parseInt(datePart.substring(0, 4));
        int month = Integer.parseInt(datePart.substring(5, 7));
        int day = Integer.parseInt(datePart.substring(8, 10));

        return LocalDate.of(year, month, day);
    }

    public static void showAlert(Alert.AlertType alertType, String[] message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(message[0]);
        alert.setHeaderText(message[1]);
        alert.setContentText(message[2]);
        alert.showAndWait();
    }

}
