package com.itgroup.controller;

import com.itgroup.bean.Player;
import com.itgroup.dao.PlayerDao;
import com.itgroup.utility.Paging;
import com.itgroup.utility.Utility;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UFCController implements Initializable {
    @FXML public ImageView imageLink,imageLink1,imageLink2,imageLink3,imageLink4,imageLink5;
    @FXML private ImageView imageView;

    private PlayerDao dao = null;
    private String mode = null;
    private HostServices hostServices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView.setStyle("-fx-opacity: 0.9;");

        this.dao = new PlayerDao();
        setTableColumns();
        setPagination(0);

        ChangeListener<Player> tableListener = new ChangeListener<Player>() {
            @Override
            public void changed(ObservableValue<? extends Player> observableValue, Player oldValue, Player newValue) {
                if (newValue != null) {
                    System.out.println("선수 정보");
                    System.out.println(newValue);

                    String imageFile = "";
                    if (newValue.getImage() != null) {
                        imageFile = Utility.IMAGE_PATH + newValue.getImage().trim();
                    } else {
                        imageFile = Utility.IMAGE_PATH + "noImage.jpg";
                    }

                    Image someImage = null; // 이미지 객체
                    if (getClass().getResource(imageFile) == null) {
                        imageView.setImage(null);
                    } else {
                        someImage = new Image(getClass().getResource(imageFile).toString());
                        System.out.println(imageView == null);
                        imageView.setImage(someImage);
                    }
                }
            }
        };
        playerTable.getSelectionModel().selectedItemProperty().addListener(tableListener);
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    @FXML
    public void onImageClick(javafx.scene.input.MouseEvent event) {  // 이미지별 링크 참조 asdf
        String url = "";
        ImageView clickedImageView = (ImageView) event.getSource();

        switch (clickedImageView.getId()) {
            case "imageLink":
                url = "https://welcome.ufcfightpass.com/region/row";
                break;
            case "imageLink1":
                url = "https://www.ufc.com/";
                break;
            case "imageLink2":
                url = "https://www.instagram.com/ufc/";
                break;
            case "imageLink3":
                url = "https://twitter.com/ufc";
                break;
            case "imageLink4":
                url =  "https://www.ufcstore.eu/en/";
                break;
            case "imageLink5":
                url =  "https://www.ufcstore.eu/en/customer-help-desk/hd-1";
                break;
        }
        openLink(url);
    }

    private void openLink(String url) {
        if (hostServices != null && !url.isEmpty()) {
            hostServices.showDocument(url);
        }
    }

    @FXML
    private Pagination pagination;

    private void setPagination(int pageIndex) {
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(pageIndex);
        pagination.setPrefHeight(600.0);

        imageView.setImage(null);
    }

    private Node createPage(Integer pageIndex) {
        int totalCount = 0;
        totalCount = dao.getTotalCount(this.mode);

        Paging pageInfo = new Paging(String.valueOf(pageIndex + 1), "15", totalCount, null, this.mode, null);
        pagination.setPageCount(pageInfo.getTotalPage());
        fillTableData(pageInfo);
        VBox vbox = new VBox(playerTable);
        return vbox;
    }

    @FXML private void fillTableData(Paging pageInfo) {
        List<Player> playerList = dao.getPaginationData(pageInfo);
        ObservableList<Player> dataList = FXCollections.observableArrayList(playerList);
        playerTable.setItems(dataList);
    }

    @FXML private TableView<Player> playerTable;

    private void setTableColumns() {
        String[] fields = {"rank", "name", "weight", "record", "nationality", "style" , "age" ,"debut"};
        String[] colNames = {"랭킹", "이름", "체급", "전적", "국적", "스타일", "나이", "데뷔"};

        TableColumn tableColumn = null;

        for (int i = 0; i < fields.length; i++) {
            tableColumn = playerTable.getColumns().get(i);
            tableColumn.setText(colNames[i]);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(fields[i]));
            tableColumn.setStyle("-fx-alignment:center;");
        }
    }

    public void onInsert(ActionEvent event) throws Exception {
        String fxmlFile = Utility.FXML_PATH + "PlayerInsert.fxml";
        URL url = getClass().getResource(fxmlFile);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent container = fxmlLoader.load();

        Scene scene = new Scene(container);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("선수 등록하기");
        stage.showAndWait();

        setPagination(0);
    }

    public void onUpdate(ActionEvent event) throws Exception {
        int idx = playerTable.getSelectionModel().getSelectedIndex();

        if (idx >= 0) {
            System.out.println("선택된 색인 번호 : " + idx);

            String fxmlFile = Utility.FXML_PATH + "PlayerUpdate.fxml";
            URL url = getClass().getResource(fxmlFile);
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent container = fxmlLoader.load();

            Player bean = playerTable.getSelectionModel().getSelectedItem();
            PlayerUpdateController controller = fxmlLoader.getController();
            controller.setBean(bean);

            Scene scene = new Scene(container);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("선수정보 수정 하기");
            stage.showAndWait();
            setPagination(0);
        } else {
            String[] message = new String[]{"선수 선택 확인", "선수 미선택", "수정 하고자 하는 선수을 선택해 주세요."};
            Utility.showAlert(Alert.AlertType.ERROR, message);
        }
    }

    public void onDelete(ActionEvent event) {
        int idx = playerTable.getSelectionModel().getSelectedIndex();

        if (idx >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("삭제 여부");
            alert.setHeaderText("삭제 항목 선택 대화상자");
            alert.setContentText("선수를 삭제 하시겠습니까?");

            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                Player bean = playerTable.getSelectionModel().getSelectedItem();
                int pnum = bean.getPnum();
                int cnt = -1;
                cnt = dao.deleteData(pnum);

                if (cnt != -1) {
                    System.out.println("삭제 완료");
                    setPagination(0);
                } else {
                    System.out.println("삭제 실패");
                }
            } else {
                System.out.println("삭제를 취소하였습니다.");
            }
        } else {
            String[] message = {"삭제할 목록 확인", "삭제할 대상 미선택", "삭제할 행 선택"};
            Utility.showAlert(Alert.AlertType.WARNING, message);
        }
    }
    @FXML private ComboBox<String> fieldSearch;

    public void choiceSelect(ActionEvent event) {
        String weight = fieldSearch.getSelectionModel().getSelectedItem();
        System.out.println("검색 체급 : " + weight);

        this.mode = Utility.getWeightName(weight, "value");
        System.out.println("필드 검색 모드 : " + this.mode);

        setPagination(0);
    }

    public void onClosing(ActionEvent event) {
        System.out.println("프로그램 종료.");
        Platform.exit();
    }
}
