package com.example.myapplication;

public class LFA_Girls {
    public String LFA_Girls_M(double height, int age){
        String status  = "Null";
        double negasd3[] = {
                43.6, 47.8, 51.0, 53.5,
                55.6, 57.4, 58.9, 60.3,
                61.7, 62.9, 64.1, 65.2,
                66.3, 67.3, 68.3, 69.3,
                70.2, 71.1, 72.0, 72.8,
                73.7, 74.5, 75.2, 76.0
        };
        double negasd2[] = {
                45.4, 49.8, 53.0, 55.6,
                57.8, 59.6, 61.2, 62.7,
                64.0, 65.3, 66.5, 67.7,
                68.9, 70.0, 71.0, 72.0,
                73.0, 74.0, 74.9, 75.8,
                76.7, 77.5, 78.4, 79.2
        };
        double negasd1[] = {
                47.3, 51.7, 55.0, 57.7,
                59.9, 61.8, 63.5, 65.0,
                66.4, 67.7, 69.0, 70.3,
                71.4, 72.6, 73.7, 74.8,
                75.8, 76.8, 77.8, 78.8,
                79.7, 80.6, 81.5, 82.3
        };
        double median[] = {
                49.1, 53.7, 57.1, 59.8,
                62.1, 64.0, 65.7, 67.3,
                68.7, 70.1, 71.5, 72.8,
                74.0, 75.2, 76.4, 77.5,
                78.6, 79.7, 80.7, 81.7,
                82.7, 83.7, 84.6, 85.5
        };
        double posisd1[] = {
                51.0, 55.6, 59.1, 61.9,
                64.3, 66.2, 68.0, 69.6,
                71.1, 72.6, 73.9, 75.3,
                76.6, 77.8, 79.1, 80.2,
                81.4, 82.5, 83.6, 84.7,
                85.7, 86.7, 87.7, 88.7
        };
        double posisd2[] = {
                52.9, 57.6, 61.1, 64.0,
                66.4, 68.5, 70.3, 71.9,
                73.5, 75.0, 76.4, 77.8,
                79.2, 80.5, 81.7, 83.0,
                84.2, 85.4, 86.5, 87.6,
                88.7, 89.8, 90.8, 91.9
        };
        double posisd3[] = {
                54.7, 59.5, 63.2, 66.1,
                68.6, 70.7, 72.5, 74.2,
                75.8, 77.4, 78.9, 80.3,
                81.7, 83.1, 84.4, 85.7,
                87.0, 88.2, 89.4, 90.6,
                91.7, 92.9, 94.0, 95.0
        };

        FindStatusWFA stunted = new FindStatusWFA();
        status = stunted.StatusFinder_Stunted(age, height, negasd3, negasd2, negasd1, median,
                posisd1, posisd2, posisd3);

        return status;
    }
}
