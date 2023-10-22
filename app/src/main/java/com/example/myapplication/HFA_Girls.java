package com.example.myapplication;

public class HFA_Girls {
    public String HFA_Girls_M(double height, int age){
        String status = "Null";
        age = age - 24;
        double negasd3[] = {
                76.0, 76.8, 77.5, 78.1,
                78.8, 79.5, 80.1, 80.7,
                81.3, 81.9, 82.5, 83.1,
                83.6, 84.2, 84.7, 85.3,
                85.8, 86.3, 86.8, 87.4,
                87.9, 88.4, 88.9, 89.3,
                89.8, 90.3, 90.7, 91.2,
                91.7, 92.1, 92.6, 93.0,
                93.4, 93.9, 94.3, 94.7
        };
        double negasd2[] = {
                79.3, 80.0, 80.8, 81.5,
                82.2, 82.9, 83.6, 84.3,
                84.9, 85.6, 86.2, 86.8,
                87.4, 88.0, 88.6, 89.2,
                89.8, 90.4, 90.9, 91.5,
                92.0, 92.5, 93.1, 93.6,
                94.1, 94.6, 95.1, 95.6,
                96.1, 96.6, 97.1, 97.6,
                98.1, 98.5, 99.0, 99.5
        };
        double negasd1[] = {
                82.5, 83.3, 84.1, 84.9,
                85.7, 86.4, 87.1, 87.9,
                88.6, 89.3, 89.9, 90.6,
                91.2, 91.9, 92.5, 93.1,
                93.8, 94.4, 95.0, 95.6,
                96.2, 96.7, 97.3, 97.9,
                98.4, 99.0, 99.5, 100.1,
                100.6, 101.1, 101.6, 102.2,
                102.7, 103.2, 103.7, 104.2
        };
        double median[] = {
                85.7, 86.6, 87.4, 88.3,
                89.1, 89.9, 90.7, 91.4,
                92.2, 92.9, 93.6, 94.4,
                95.1, 95.7, 96.4, 97.1,
                97.7, 98.4, 99.0, 99.7,
                100.3, 100.9, 101.5, 102.1,
                102.7, 103.3, 103.9, 104.5,
                105.0, 105.6, 106.2, 106.7,
                107.3, 107.8, 108.4, 108.9
        };
        double posisd1[] = {
                88.9, 89.9, 90.8, 91.7,
                92.5, 93.4, 94.2, 95.0,
                95.8, 96.6, 97.4, 98.1,
                98.9, 99.6, 100.3, 101.0,
                101.7, 102.4, 103.1, 103.8,
                104.5, 105.1, 105.8, 106.4,
                107.0, 107.7, 108.3, 108.9,
                109.5, 110.1, 110.7, 111.3,
                111.9, 112.5, 113.0, 113.6
        };
        double posisd2[] = {
                92.2, 93.1, 94.1, 95.0,
                96.0, 96.9, 97.7, 98.6,
                99.4, 100.3, 101.1, 101.9,
                102.7, 103.4, 104.2, 105.0,
                105.7, 106.4, 107.2, 107.9,
                108.6, 109.3, 110.0, 110.7,
                111.3, 112.0, 112.7, 113.3,
                114.0, 114.6, 115.2, 115.9,
                116.5, 117.1, 117.7, 118.3
        };
        double posisd3[] = {
                95.4, 96.4, 97.4, 98.4,
                99.4, 100.3, 101.3, 102.2,
                103.1, 103.9, 104.8, 105.6,
                106.5, 107.3, 108.1, 108.9,
                109.7, 110.5, 111.2, 112.0,
                112.7, 113.5, 114.2, 114.9,
                115.7, 116.4, 117.1, 117.7,
                118.4, 119.1, 119.8, 120.4,
                121.1, 121.8, 122.4, 123.1
        };
        FindStatusWFA stunted = new FindStatusWFA();
        status = stunted.StatusFinder_Stunted(age, height, negasd3, negasd2, negasd1, median,
                posisd1, posisd2, posisd3);

        return status;
    }
}
