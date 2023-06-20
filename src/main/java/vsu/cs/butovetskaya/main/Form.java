package vsu.cs.butovetskaya.main;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import vsu.cs.butovetskaya.task.DecisionTreeLearning;
import vsu.cs.butovetskaya.task.WorkWithTrainTable;

import java.io.IOException;
import java.util.Objects;

public class Form {
    private String sex;
    private Integer age;
    private String pressure;
    private String heartProblems;
    private String smoking;
    private Double height;
    private Double weight;
    private Double HbA1c;
    private Double glucose;
    private Double resultDiabet;
    public String result;

    private double[] inputData = new double[8];

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setHeartProblems(String heartProblems) {
        this.heartProblems = heartProblems;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setHbA1c(Double hbA1c) {
        HbA1c = hbA1c;
    }

    public void setGlucose(Double glucose) {
        this.glucose = glucose;
    }

    public void setResultDiabet(Double resultDiabet) {
        this.resultDiabet = resultDiabet;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHeartProblems() {
        return heartProblems;
    }

    public String getSmoking() {
        return smoking;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getHbA1c() {
        return HbA1c;
    }

    public Double getGlucose() {
        return glucose;
    }

    public Double getResultDiabet() {
        return resultDiabet;
    }

    public String getResult() {
        return result;
    }

    public double[] getInputData() {
        return inputData;
    }

    public void collectInputData() {
        if (Objects.equals(sex, "male")) {
            inputData[0] = 1.0;
        } else if (Objects.equals(sex, "female")) {
            inputData[0] = 0.0;
        }
        inputData[1] = age;
        if (Objects.equals(pressure, "yes")) {
            inputData[2] = 1.0;
        } else if (Objects.equals(pressure, "no")) {
            inputData[2] = 0.0;
        }
        if (Objects.equals(heartProblems, "yes")) {
            inputData[3] = 1.0;
        } else if (Objects.equals(heartProblems, "no")) {
            inputData[3] = 0.0;
        }
        if (Objects.equals(smoking, "sometimes")) {
            inputData[4] = 1.0;
        } else if (Objects.equals(sex, "never")) {
            inputData[4] = 0.0;
        } else if (Objects.equals(sex, "regularly")) {
            inputData[4] = 2.0;
        }
        inputData[5] = Math.round(weight / Math.pow(height / 100, 2));
        inputData[6] = HbA1c;
        inputData[7] = glucose;
    }

    public void result() {
        if (resultDiabet == 1) {
            this.setResult("Вы находитесь в зоне риска развития диабета. Рекомендуем сдать соответствующие анализы и следить за своим здоровьем.");
        } else {
            this.setResult("Бояться нечего. Вы не находитесь в зоне риска развития диабета.");
        }
    }

    public void checkDiabet() throws IOException, InvalidFormatException {
        collectInputData();

        WorkWithTrainTable w = new WorkWithTrainTable();
        String[][] trainTable = w.readTrainTable("diabetes_prediction_dataset1.xlsx");
        trainTable = w.correctTrainTableDiabetes(trainTable);

        DecisionTreeLearning dtl = new DecisionTreeLearning(trainTable, trainTable.length, 25, trainTable.length);
        dtl.createTreeNode();

        setResultDiabet(dtl.dtree.result(inputData));
        result();
    }

}
