package vsu.cs.butovetskaya.main;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import vsu.cs.butovetskaya.task.DecisionTreeLearning;
import vsu.cs.butovetskaya.task.WorkWithTrainTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.Long.sum;

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
    private int[] resultDiabet;
    public String result;
    public double chanceDiabet;

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

    public void setResultDiabet(int[] resultDiabet) {
        this.resultDiabet = resultDiabet;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setChanceDiabet(double chanceDiabet) {
        this.chanceDiabet = chanceDiabet;
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

    public int[] getResultDiabet() {
        return resultDiabet;
    }

    public String getResult() {
        return result;
    }

    public double[] getInputData() {
        return inputData;
    }

    public double getChanceDiabet() {
        return chanceDiabet;
    }

    public void collectInputData() {
        if (Objects.equals(sex, "Мужчина")) {
            inputData[0] = 1.0;
        } else if (Objects.equals(sex, "Женщина")) {
            inputData[0] = 0.0;
        }
        inputData[1] = age;
        if (Objects.equals(pressure, "Да")) {
            inputData[2] = 1.0;
        } else if (Objects.equals(pressure, "Нет")) {
            inputData[2] = 0.0;
        }
        if (Objects.equals(heartProblems, "Да")) {
            inputData[3] = 1.0;
        } else if (Objects.equals(heartProblems, "Нет")) {
            inputData[3] = 0.0;
        }
        if (Objects.equals(smoking, "Иногда")) {
            inputData[4] = 1.0;
        } else if (Objects.equals(sex, "Никогда")) {
            inputData[4] = 0.0;
        } else if (Objects.equals(sex, "Регулярно")) {
            inputData[4] = 2.0;
        }
        inputData[5] = Math.round(weight / Math.pow(height / 100, 2));
        inputData[6] = HbA1c;
        inputData[7] = glucose;
    }

    public void result() {
        int sum = 0;
        for (int i = 0; i < resultDiabet.length; i++) {
            sum += resultDiabet[i];
        }
        setChanceDiabet(((double) resultDiabet[1] / sum) * 100);
        if (chanceDiabet >= 50) {
            this.setResult("Вы находитесь в зоне риска развития диабета. Рекомендуем сдать соответствующие анализы и следить за своим здоровьем.");
        } else {
            this.setResult("Бояться нечего. Вы не находитесь в зоне риска развития диабета.");
        }
        if (chanceDiabet == 100) {
            chanceDiabet = 99.9;
        }
    }

    public void checkDiabet() throws IOException, InvalidFormatException {
        collectInputData();

        WorkWithTrainTable w = new WorkWithTrainTable();
        String[][] trainTable = w.readTrainTable("diabetes_prediction_dataset1.xlsx");
        trainTable = w.correctTrainTableDiabetes(trainTable);

        // здесь параметры могут меняться от случая к случаю - нужно подбирать под таблицу тренировки
        DecisionTreeLearning dtl = new DecisionTreeLearning(trainTable, 50, 40);
        dtl.createTreeNode();

        dtl.dtree.print();
        System.out.println(dtl.dtree.countLeaf);

        setResultDiabet(dtl.dtree.checkResult(inputData));

        result();
    }

}
