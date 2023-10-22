package com.example.myapplication;

public class BMICalculator {

    // BMI categories for children based on age and gender
    private static final String[] BMI_CATEGORIES = {
            "Very severely underweight", "Severely underweight", "Underweight",
            "Normal", "Overweight", "Obese"
    };

    // BMI cutoff values for boys (0-59 months) based on WHO growth standards
    private static final double[][] BOYS_BMI_VALUES = {
            {13.0, 13.7, 14.4, 15.2, 15.9, 16.6}, // 0-2 months
            {12.2, 12.9, 13.6, 14.3, 15.0, 15.7}, // 3-5 months
            // ... (add more rows for different age groups)
    };

    // BMI cutoff values for girls (0-59 months) based on WHO growth standards
    private static final double[][] GIRLS_BMI_VALUES = {
            {12.9, 13.6, 14.3, 15.1, 15.8, 16.5}, // 0-2 months
            {12.1, 12.8, 13.5, 14.2, 14.9, 15.6}, // 3-5 months
            // ... (add more rows for different age groups)
    };
    public String calculateBMIStatus(int ageInMonths, double weightInKg, double heightInMeters, String gender) {
        double bmi = weightInKg / (heightInMeters * heightInMeters);

        double[][] bmiValues;
        if ("male".equalsIgnoreCase(gender)) {
            bmiValues = BOYS_BMI_VALUES;
        } else if ("female".equalsIgnoreCase(gender)) {
            bmiValues = GIRLS_BMI_VALUES;
        } else {
            return "Invalid gender";
        }

        int ageGroupIndex = getAgeGroupIndex(ageInMonths);
        if (ageGroupIndex >= 0 && ageGroupIndex < bmiValues.length) {
            double[] bmiCutoffs = bmiValues[ageGroupIndex];
            if (bmi < bmiCutoffs[0]) {
                return BMI_CATEGORIES[0]; // Very severely underweight
            } else if (bmi < bmiCutoffs[1]) {
                return BMI_CATEGORIES[1]; // Severely underweight
            } else if (bmi < bmiCutoffs[2]) {
                return BMI_CATEGORIES[2]; // Underweight
            } else if (bmi < bmiCutoffs[3]) {
                return BMI_CATEGORIES[3]; // Normal
            } else if (bmi < bmiCutoffs[4]) {
                return BMI_CATEGORIES[4]; // Overweight
            } else {
                return BMI_CATEGORIES[5]; // Obese
            }
        } else {
            return "Invalid age";
        }
    }

    private int getAgeGroupIndex(int ageInMonths) {
        if (ageInMonths >= 0 && ageInMonths < 3) {
            return 0; // 0-2 months
        } else if (ageInMonths >= 3 && ageInMonths < 6) {
            return 1; // 3-5 months
        }
        // Add more age groups as needed
        // else if (ageInMonths >= 6 && ageInMonths < 12) {
        //     return 2; // 6-11 months
        // }
        // ...

        return -1; // Invalid age group
    }
}
