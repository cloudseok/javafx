package com.itgroup.controller;

import com.itgroup.bean.Player;
import com.itgroup.dao.PlayerDao;
import com.itgroup.utility.Utility;
import javafx.event.ActionEvent;
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

public class PlayerUpdateController implements Initializable {
    @FXML
    private TextField fxmlPnum;
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

    private Player oldBean;
    private Player newBean;

    public void setBean(Player bean) {
        this.oldBean = bean;
        fillPreviousData();
        fxmlPnum.setVisible(false);
    }

    private void fillPreviousData() {
        fxmlPnum.setText(String.valueOf(oldBean.getPnum()));
        fxmlName.setText(oldBean.getName());
        fxmlWeight.setValue(Utility.getWeightName(oldBean.getWeight(), "key"));
        fxmlRank.setText(oldBean.getRank());
        fxmlRecord.setText(oldBean.getRecord());
        fxmlImage.setText(oldBean.getImage());
        fxmlNationality.setText(oldBean.getNationality());
        fxmlStyle.setText(oldBean.getStyle());
        fxmlAge.setText(String.valueOf(oldBean.getAge()));
        fxmlDebut.setValue(Utility.getDatePicker(oldBean.getDebut()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("선수 수정");
    }

    @FXML
    public void onPlayerUpdate(ActionEvent event) {
        if (validationCheck()) {
            PlayerDao dao = new PlayerDao();
            int result = dao.updateData(newBean);
            if (result != -1) {
                closeStage(event);
            } else {
                System.out.println("수정 실패");
            }
        } else {
            System.out.println("유효성 검사를 통과하지 못했습니다.");
        }
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean validationCheck() {
        try {
            int pnum = Integer.parseInt(fxmlPnum.getText().trim());
            String name = fxmlName.getText().trim();
            String weight = fxmlWeight.getSelectionModel().getSelectedItem();
            String rank = fxmlRank.getText().trim();
            String record = fxmlRecord.getText().trim();
            String image = fxmlImage.getText().trim();
            String nationality = fxmlNationality.getText().trim();
            String style = fxmlStyle.getText().trim();
            int age = Integer.parseInt(fxmlAge.getText().trim());
            LocalDate debutDate = fxmlDebut.getValue();

            if (!isValidName(name) || !isValidWeight(weight) || !isValidRank(rank) || !isValidRecord(record) || !isValidImage(image) || !isValidNationality(nationality) || !isValidStyle(style) || !isValidAge(age) || !isValidDebut(debutDate)) {
                return false;
            }

            newBean = new Player(pnum, name, Utility.getWeightName(weight, "value"), rank, record, image, nationality, style, age, debutDate.toString().replace("-", "/"));
            return true;

        } catch (NumberFormatException e) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 숫자 형식", "무효한 숫자 형식", "올바른 숫자 형식을 입력해 주세요."});
            return false;
        }
    }

    private boolean isValidName(String name) {
        if (name.length() <= 0 || name.length() >= 21) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 이름", "길이 제한 위반", "이름은 1글자 이상 20글자 이하이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean isValidWeight(String weight) {
        if (weight == null || weight.isEmpty()) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 체급", "체급미선택", "원하시는 체급를 반드시 선택해 주세요."});
            return false;
        }
        return true;
    }

    private boolean isValidRank(String rank) {
        if (rank.length() <= 0 || (rank.length() == 1 && !rank.equals("C"))) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 랭킹", "텍스트 허용 위반", "랭킹은 1글자 이상이거나 챔피언을 뜻하는 'C' 이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean isValidRecord(String record) {
        if (record.length() <= 4 || record.length() >= 25) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 전적", "길이 제한 위반", "전적은 5글자 이상 20글자 이하이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean isValidImage(String image) {
        if (image.length() < 5 || (!image.endsWith(".jpg") && !image.endsWith(".png"))) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 이미지", "필수 입력 체크", "이미지는 필수 입력 사항이며, 확장자는 '.jpg' 또는 '.png' 이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean isValidNationality(String nationality) {
        if (nationality.length() <= 0 || nationality.length() >= 21) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 국적", "길이 제한 위반", "국적은 1글자 이상 20글자 이하이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean isValidStyle(String style) {
        if (style.length() <= 0 || style.length() >= 16) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 스타일", "길이 제한 위반", "스타일은 1글자 이상 15글자 이하이어야 합니다."});
            return false;
        }
        return true;
    }

    private boolean isValidAge(int age) {
        if (age < 14 || age > 61) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 나이", "허용 숫자 위반", "나이는 15세 이상 60세 이하로 입력해 주세요."});
            return false;
        }
        return true;
    }

    private boolean isValidDebut(LocalDate debut) {
        if (debut == null) {
            Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유효성 검사 : 데뷔일자", "무효한 날짜 형식", "올바른 데뷔 일자 형식을 입력해 주세요."});
            return false;
        }
        return true;
    }
}
