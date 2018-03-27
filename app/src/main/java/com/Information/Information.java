package com.Information;

/**
 * Created by Cheonjin Park on 2/17/2018.
 */

public class Information {

    public Information() {

    }

    //Ranges
    public final static int O3_RANGE = 100;
    public final static int CO_RANGE = 200;
    public final static int NO2_RANGE = 300;
    public final static int SO2_RANGE = 400;
    public final static int Dust_RANGE = 500;

    public final static String O3 = "O3";
    public final static String CO = "CO";
    public final static String NO2 = "NO2";
    public final static String SO2 = "SO2";
    public final static String DUST = "DUST";


    public final static String SAFE_COLOUR = "#447CFC00";


    public final static String GOOD_COLOUR = "";
    public final static String MODERATE_COLOUR = "#44FFFF00";
    public final static String USG_COLOUR = "#44FF8C00"; //Unhealthy for Sensitive Groups
    public final static String UNHEALTHY_COLOUR = "#44000080";
    public final static String VERY_UNHEALTHY_COLOUR = "#444B0082";
    public final static String HAZARDOUS_COLOUR = "#44FF0000";


    public final static String DANGER_1 = "#44FF8C00";
    public final static String DANGER_2 = "#44FF0000";

    public final static String O3_STROKE_COLOUR = "#000000";


    //for now I made only 4 kind I do not know about the dust standards
    public String define_color(String type, double value) {
        //if the type is not correct return null
        String color = null;
        switch (type) {
            case O3:
                if (value >= 0 && value <= 4.4) {
                    return SAFE_COLOUR;
                }
                if (value >= 4.5 && value <= 9.4) {
                    return MODERATE_COLOUR;
                }

                if (value >= 9.5 && value <= 12.4) {
                    return USG_COLOUR;
                }

                if (value >= 12.5 && value <= 15.4) {
                    return UNHEALTHY_COLOUR;
                }

                if (value >= 15.5 && value <= 30.4) {
                    return VERY_UNHEALTHY_COLOUR;
                }

                if (value >= 30.5) {
                    return HAZARDOUS_COLOUR;
                }
                return color;

            case CO:
                if (value >= 0 && value <= 0.054) {
                    return SAFE_COLOUR;
                }
                if (value >= 0.055 && value <= 0.070) {
                    return MODERATE_COLOUR;
                }

                if (value >= 0.071 && value <= 0.085) {
                    return USG_COLOUR;
                }

                if (value >= 0.086 && value <= 0.105) {
                    return UNHEALTHY_COLOUR;
                }

                if (value >= 0.106 && value <= 0.404) {
                    return VERY_UNHEALTHY_COLOUR;
                }

                if (value >= 0.405) {
                    return HAZARDOUS_COLOUR;
                }
                return color;

            case NO2:

                if (value >= 0 && value <= 0.053) {
                    return SAFE_COLOUR;
                }
                if (value >= 0.054 && value <= 0.100) {
                    return MODERATE_COLOUR;
                }

                if (value >= 0.101 && value <= 0.360) {
                    return USG_COLOUR;
                }

                if (value >= 0.361 && value <= 0.649) {
                    return UNHEALTHY_COLOUR;
                }

                if (value >= 0.650 && value <= 1.249) {
                    return VERY_UNHEALTHY_COLOUR;
                }

                if (value >= 1.250) {
                    return HAZARDOUS_COLOUR;
                }
                return color;

            case SO2:

                if (value >= 0 && value <= 0.035) {
                    return SAFE_COLOUR;
                }
                if (value >= 0.036 && value <= 0.075) {
                    return MODERATE_COLOUR;
                }

                if (value >= 0.076 && value <= 0.185) {
                    return USG_COLOUR;
                }

                if (value >= 0.186 && value <= 0.304) {
                    return UNHEALTHY_COLOUR;
                }

                if (value >= 0.305 && value <= 0.604) {
                    return VERY_UNHEALTHY_COLOUR;
                }

                if (value >= 0.605) {
                    return HAZARDOUS_COLOUR;
                }
                return color;

            case DUST:
                return SAFE_COLOUR;


        }

        return color;
    }

}


