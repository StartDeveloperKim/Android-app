package com.example.aiapplication.medicine.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MedicineCombinationUtil {

    public static List<String> generateCombinations(List<String> medicines) {
        List<List<String>> combinations = new ArrayList<>();
        generateCombinationsHelper(medicines, medicines.size(), 0, new ArrayList<>(), combinations);

        List<String> medicineCombinations = new ArrayList<>();
        for (List<String> combination : combinations) {
            medicineCombinations.add(concatenateListElements(combination));
        }
        return medicineCombinations;
    }

    private static String concatenateListElements(List<String> combination) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : combination) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    private static void generateCombinationsHelper(List<String> inputList, int length, int startIndex, List<String> currentCombination, List<List<String>> combinations) {
        if (currentCombination.size() == length) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = startIndex; i < inputList.size(); i++) {
            currentCombination.add(inputList.get(i));
            generateCombinationsHelper(inputList, length, i + 1, currentCombination, combinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}
