package com.itgroup.controller;

import javafx.event.ActionEvent;
import com.itgroup.bean.Player;
import com.itgroup.dao.PlayerDao;
import com.itgroup.utility.Utility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PlayerInsertController implements Initializable {
    @FXML
    private TextField fxmlName;
    @FXML
    private ComboBox<String> fxmlWeight;
    @FXML
    private TextField fxmlRank;
    @FXML
    private TextField fxmlRecord;
    @FXML
    private TextField fxmlImage;
    @FXML
    private TextField fxmlNationality;
    @FXML
    private TextField fxmlStyle;
    @FXML
    private TextField fxmlAge;
    @FXML
    private DatePicker fxmlDebut;

    private PlayerDao dao;
    private Player bean;

    @FXML
    public void onPlayerInsert(ActionEvent event) {
        if (validationCheck()) {
            if (insertDatabase() == 1) {
                closeStage(event);
            } else {
                System.out.println("등록 실패");
            }
        }
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private int insertDatabase() {
        int cnt = dao.insertData(this.bean);
        if (cnt == -1) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"선수 등록", "선수 등록 실패", "선수 등록을 실패하였습니다."});
        }
        return cnt;
    }

    private boolean validationCheck() {
        if (!validateName() || !validateWeight() || !validateRank() || !validateRecord() || !validateImage() ||
                !validateNationality() || !validateStyle() || !validateAge() || !validateDebut()) {
            return false;
        }

        this.bean = new Player();
        bean.setName(fxmlName.getText().trim());
        bean.setWeight(Utility.getWeightName(fxmlWeight.getSelectionModel().getSelectedItem(), "value"));
        bean.setRank(fxmlRank.getText().trim());
        bean.setRecord(fxmlRecord.getText().trim());
        bean.setImage(fxmlImage.getText().trim());
        bean.setNationality(fxmlNationality.getText().trim());
        bean.setStyle(fxmlStyle.getText().trim());
        bean.setAge(Integer.parseInt(fxmlAge.getText().trim()));
        bean.setDebut(fxmlDebut.getValue().toString().replace("-", "/"));
        return true;
    }

    private boolean validateName() {
        String name = fxmlName.getText().trim();
        if (name.length() <= 0 || name.length() >= 21) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 이름", "길이 제한 위반", "이름은 1글자 이상 20글자 이하이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean validateWeight() {
        int selectedIndex = fxmlWeight.getSelectionModel().getSelectedIndex();
        if (selectedIndex == 0) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 체급", "체급미선택", "원하시는 체급를 반드시 선택해 주세요."});
            return false;
        }
        return true;
    }

    private boolean validateRank() {
        String rank = fxmlRank.getText().trim();
        if (rank.length() <= 0 || rank.length() > 1 || (rank.length() == 1 && !rank.equals("C"))) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 랭킹", "텍스트 허용 위반", "랭킹은 1글자 이상이거나 챔피언을 뜻하는 'C' 이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean validateRecord() {
        String record = fxmlRecord.getText().trim();
        if (record.length() >= 21) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 전적", "길이 제한 위반", "전적은 20글자 이하 이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean validateImage() {
        String image = fxmlImage.getText().trim();
        if (image.length() < 5) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 이미지", "필수 입력 체크", "이미지는 필수 입력 사항니다."});
            return false;
        }
        if (!image.endsWith(".jpg") && !image.endsWith(".png")) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 이미지", "확장자 점검", "이미지의 확장자는 '.jpg' 또는 '.png' 이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean validateNationality() {
        String nationality = fxmlNationality.getText().trim();
        if (nationality.length() >= 21) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 국적", "길이 제한 위반", "국적은 1글자 이상 20글자 이하이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean validateStyle() {
        String style = fxmlStyle.getText().trim();
        if (style.length() >= 16) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 스타일", "길이 제한 위반", "스타일은 1글자 이상 15글자 이하이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean validateAge() {
        try {
            int age = Integer.parseInt(fxmlAge.getText().trim());
            if (age < 14 || age > 61) {
                Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 나이", "허용 숫자 위반", "나이는 15세이상 60세 이하로 입력해 주세요."});
                return false;
            }
        } catch (NumberFormatException ex) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 나이", "무효한 숫자 형식", "올바른 숫자 형식을 입력해 주세요."});
            return false;
        }
        return true;
    }

    private boolean validateDebut() {
        LocalDate debut = fxmlDebut.getValue();
        if (debut == null) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 데뷔일자", "무효한 날짜 형식", "올바른 데뷔 일자 형식을 입력해 주세요."});
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dao = new PlayerDao();
        fxmlWeight.getSelectionModel().select(0);
    }
}
