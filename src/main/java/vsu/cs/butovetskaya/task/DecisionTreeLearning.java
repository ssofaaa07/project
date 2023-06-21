package vsu.cs.butovetskaya.task;

import java.util.*;

public class DecisionTreeLearning {

    String[][] trainTable;
    ArrayList<double[]> currentTable;
    ArrayList<double[]> currentTableLeft;
    ArrayList<double[]> currentTableRight;
    double[][] currentInfoGain;
    ArrayList<Integer> listOfClasses;
    double[][] questions;
    int maxDepth = 10000;
    int minSamplesLeaf = 1;
    public DecisionTree dtree = new DecisionTree();

    public DecisionTreeLearning(String[][] trainTable) {
        this.trainTable = trainTable;
        this.currentTable = new ArrayList<>();
        double[][] arr = new double[trainTable.length - 1][trainTable[0].length];
        for (int i = 0; i < trainTable.length - 1; i++) {
            for (int j = 0; j < trainTable[0].length; j++) {
                arr[i][j] = Double.parseDouble(String.valueOf(trainTable[i+1][j]));
            }
        }
        currentTable.addAll(Arrays.asList(arr));
        createListOfClasses();
    }

    public DecisionTreeLearning(String[][] trainTable, int maxDepth, int minSamplesLeaf) {
        this.trainTable = trainTable;
        this.currentTable = new ArrayList<>();
        double[][] arr = new double[trainTable.length - 1][trainTable[0].length];
        for (int i = 0; i < trainTable.length - 1; i++) {
            for (int j = 0; j < trainTable[0].length; j++) {
                arr[i][j] = Double.parseDouble(String.valueOf(trainTable[i+1][j]));
            }
        }
        currentTable.addAll(Arrays.asList(arr));
        createListOfClasses();
        this.maxDepth = maxDepth;
        this.minSamplesLeaf = minSamplesLeaf;
    }

    public void setTrainTable(String[][] trainTable) {
        this.trainTable = trainTable;
    }

    /**
     * Поиск вопросов таблицы. Вычисляются все возможные вопросы по значениям атрибутов.
     * Берется среднее по каждой паре отсортированного массива значений атрибута.
     */
    public void findQuestions() {
            this.questions = new double[currentTable.size() - 1][currentTable.get(0).length - 1];
            double[] sortedArr = new double[currentTable.size()];
            for (int c = 0; c < currentTable.get(0).length - 1; c++) {
                for (int r = 0; r < currentTable.size(); r++) {
                    sortedArr[r] = currentTable.get(r)[c];
                }
                sortedArr = Arrays.stream(sortedArr).sorted().toArray();
                for (int r = 0; r < sortedArr.length - 1; r++) {
                    double value = (sortedArr[r] + sortedArr[r + 1]) / 2;
                    questions[r][c] = value;
                }
                Arrays.fill(sortedArr, '0');
            }
    }

    /**
     * Создание листа классов. В случае с таблицей диабета их всего 2:
     * 0 - риска развития диабета нет
     * 1 - риск развития диабета есть
     */
    public void createListOfClasses() {
        HashSet<Integer> setOfClasses = new HashSet<>();
        int item;
        for (int i = 1; i < trainTable.length; i++) {
            item = (int) Double.parseDouble(trainTable[i][trainTable[0].length - 1]);
            setOfClasses.add(item);
        }
        listOfClasses = new ArrayList<>();
        listOfClasses.addAll(setOfClasses);
    }

    /**
     * Подсчёт прироста информации.
     * Энтропия текущей таблицы -
     * - (отношение размеров таблицы левого разбиения к текущей таблице * энтропия будущего левого разбиения по текущему вопросу) -
     * - (отношение размеров таблицы правого разбиения к текущей таблице * энтропия будущего правого разбиения по текущему вопросу)
     *
     * Конечное разбиение проиходит по тому вопросу, чей прирост информации был максимален.
     * @return прирост информации
     */
    public double infoGain() {
        return entropy(currentTable) - ((double) currentTableLeft.size() / currentTable.size()) * entropy(currentTableLeft) -
                ((double) currentTableRight.size() / currentTable.size()) * entropy(currentTableRight);
    }

    /**
     * Создание таблиц левого и правого поддерева по конкретному вопросу.
     * @param row - строка вопроса (значение)
     * @param col - столбик вопроса (атрибут)
     */
    public void createCurrentTables(int row, int col) {
        currentTableLeft = new ArrayList<>();
        currentTableRight = new ArrayList<>();
        for (double[] str : currentTable) {
            if (str[col] <= questions[row][col]) {
                currentTableLeft.add(str);
            } else {
                currentTableRight.add(str);
            }
        }

    }

    /**
     * Подсчет вероятности. Отношение кол-ва строк, относящихся к классу classItem, к общему кол-ву строк.
     * @param table - таблица, для которой нужно подсчитать вероятность
     * @param classItem - класс, для которого рассчитывается вероятность в данной таблице
     * @return вероятность
     */
    public double chance(List<double[]> table, double classItem) {
        int count = 0;
        for (double[] str : table) {
            if (str[currentTable.get(0).length - 1] == classItem) {
                count++;
            }
        }
        return (double) count / (table.size());
    }

    /**
     * Сбор всех значений прироста информации по всем вопросам текущей таблицы.
     * Координаты значения прироста информации совпадают с координатами вопросов текущей таблицы.
     *
     * Конечное разбиение проиходит по тому вопросу, чей прирост информации был максимален.
     */
    public void compareInfoGain() {
        this.currentInfoGain = new double[currentTable.size() - 1][currentTable.get(0).length - 1];
        for (int r = 0; r < questions.length; r++) {
            for (int c = 0; c < questions[0].length; c++) {
                createCurrentTables(r, c);
                currentInfoGain[r][c] = infoGain();
            }
        }
    }

    /**
     * Расчет энтропии Шенона.
     * @param table таблица, для которой нужно подсчитать энтропию
     * @return значение энтропии
     */
    public double entropy(List<double[]> table) {
        double entropy = 0;
        for (Integer classItem : listOfClasses) {
            double p = chance(table, classItem);
            if (p == 0) {
                entropy += 0;
            } else {
                entropy += p * (Math.log(p) / Math.log(2));
            }
        }
        return -entropy;
    }

    /**
     * Нахождение максимального прироста информации.
     *
     * Для удобства сделан такой вывод
     * @return [значение энтропии, строка энтропии (по которой можно узнать значение вопроса из questions),
     * столбец энтропии (по которому можно узнать атрибут вопроса из questions)]
     */
    public double[] findMaxGain() {
        double maxGain = -1;
        int colOfMaxGain = -1;
        int rowOfMaxGain = -1;
        for (int r = 0; r < currentInfoGain.length; r++) {
            for (int c = 0; c < currentInfoGain[0].length; c++) {
                if (maxGain < currentInfoGain[r][c]) {
                    maxGain = currentInfoGain[r][c];
                    rowOfMaxGain = r;
                    colOfMaxGain = c;
                }
            }
        }
        return new double[]{maxGain, rowOfMaxGain, colOfMaxGain};
    }

    /**
     * Подсчёт кол-ва строчек в текущей таблице, относящихся к определенному классу из listOfClasses.
     * Нужно для поля value в узле дерева.
     * @return лист с кол-вом каждого класса
     */
    public int[] countClasses() {
        int[] countClasses = new int[listOfClasses.size()];
        int count = 0;
        for (int i = 0; i < listOfClasses.size(); i++) {
            for (int r = 0; r < currentTable.size(); r++) {
                if (currentTable.get(r)[currentTable.get(0).length - 1] == listOfClasses.get(i)) {
                    count++;
                }
            }
            countClasses[i] = count;
            count = 0;
        }
        return countClasses;
    }

    /**
     * Является ли разбиение листом дерева?
     * @param countClasses кол-во строк в таблице, относящихся к определённому классу
     * @return true/false
     */
    private boolean isLeaf(int[] countClasses) {
        for (int classItem : countClasses) {
            if (classItem == 0) {
                return true;
            }
        }
        return false;
    }

    public DecisionTree.DecisionTreeNode createTreeNode() {
        DecisionTree.DecisionTreeNode dtreenode;
        this.currentTableLeft = new ArrayList<>();
        this.currentTableRight = new ArrayList<>();
        findQuestions();
        if (trainTable.length - 1 == currentTable.size()) {
            dtree.depthOfTree = 0;
            dtree.countLeaf = 0;
        }
        int[] countClasses = countClasses();
        if (!isLeaf(countClasses) && currentTable.size() > minSamplesLeaf && dtree.depthOfTree <= maxDepth) {
            compareInfoGain();
            double[] question = findMaxGain();
            double ent = question[0];
            int queRow = (int) question[1];
            int queCol = (int) question[2];

            dtreenode = new DecisionTree.DecisionTreeNode(new double[]{queCol, questions[queRow][queCol]}, ent, currentTable.size(), countClasses);

            if (trainTable.length - 1 == currentTable.size()) {
                dtree.root = dtreenode;
            }
            dtree.depthOfTree += 1;
            dtreenode.leaf = false;

            ArrayList<double[]> oldCurrentTable = currentTable;

            createCurrentTables(queRow, queCol);
            this.currentTable = currentTableLeft;
            dtreenode.setLeftNode(this.createTreeNode());

            this.currentTable = oldCurrentTable;
            findQuestions();
            createCurrentTables(queRow, queCol);
            this.currentTable = currentTableRight;
            dtreenode.setRightNode(this.createTreeNode());
        } else {
            dtreenode = new DecisionTree.DecisionTreeNode(null, 0, currentTable.size(), countClasses);
            dtreenode.setLeaf(true);
            dtreenode.setLeftNode(null);
            dtreenode.setRightNode(null);
            dtree.countLeaf += 1;
        }
        return dtreenode;
    }

}

