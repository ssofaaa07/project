package com.test;

import java.util.ArrayList;

public class Form {
    private String sex;
    private Integer age;
    private String heartProblems;
    private String pressure;
    private String smoking;
    private Float height;
    private Float weight;
    ArrayList<String> list = new ArrayList<String>();

    private Float result;

    public Float getHeight() {
        return height;
    }

    public Float getWeight() {
        return weight;
    }

    public Integer getAge() {
        return age;
    }

    public String getHeartProblems() {
        return heartProblems;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSex() {
        return sex;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setHeartProblems(String heartProblems) {
        this.heartProblems = heartProblems;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
    public void setResult(Float result) {
        this.result = result;
    }

    public void collectResult() {
        list.add(sex);
        list.add(String.valueOf(age));
        list.add(heartProblems);
        list.add(pressure);
        list.add(smoking);
        list.add(String.valueOf(height));
        list.add(String.valueOf(weight));
    }
}
