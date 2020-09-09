package sample;

public class StatsHandler {

    private double numBiology = 0.0;
    private double numSoftwareEngineering = 0.0;
    private double numMathematics = 0.0;
    private double numArt = 0.0;
    private double numUnderwaterBasketWeaving = 0.0;

    private int numA = 0;
    private int numB = 0;
    private int numC = 0;
    private int numD = 0;
    private int numF = 0;

    void clearAllValues(){
        numBiology = 0;
        numSoftwareEngineering = 0;
        numMathematics = 0;
        numArt = 0;
        numUnderwaterBasketWeaving = 0;
        numA = 0;
        numB = 0;
        numC = 0;
        numD = 0;
        numF = 0;
    }

    void incNumBiology(){
        numBiology++;
    }
    void incNumSoftwareEngineering(){
        numSoftwareEngineering++;
    }
    void incNumMathematics(){
        numMathematics++;
    }
    void incNumArt(){
        numArt++;
    }
    void incNumUnderwaterBasketWeaving(){
        numUnderwaterBasketWeaving++;
    }

    double getNumBiology(){
        return numBiology;
    }
    double getNumSoftwareEngineering(){
        return numSoftwareEngineering;
    }
    double getNumMathematics(){
        return numMathematics;
    }
    double getNumArt(){
        return numArt;
    }
    double getNumUnderwaterBasketWeaving(){
        return numUnderwaterBasketWeaving;
    }

    double totalStudents(){
        return this.numBiology
                + this.numSoftwareEngineering
                + this.numMathematics
                + this.numArt
                + this.numUnderwaterBasketWeaving;
    }

    //for pie chart

    //pie chart percentages

    //method 1 formatting
    /*double getPercentBiology(){
        return (numBiology / totalStudents())*100;
    }
    double getPercentSoftwareEngineering(){
        return (numSoftwareEngineering / totalStudents())*100;
    }
    double getPercentMathematics(){
        return (numMathematics / totalStudents())*100;
    }
    double getPercentArt(){
        return (numArt / totalStudents())*100;
    }
    double getPercentUnderwaterBasketWeaving(){
        return (numUnderwaterBasketWeaving / totalStudents())*100;
    }*/

    //method 2 formatting
    String getPercentBiology(){
        double percent = (numBiology / totalStudents())*100;
        if(percent == 0.0){
            return "Biology (0) 0%";
        }
        else{
            return String.format("Biology (%.0f) %.2f", numBiology, percent) + "%";
        }

    }
    String getPercentSoftwareEngineering(){
        double percent = (numSoftwareEngineering / totalStudents())*100;
        if(percent == 0.0){
            return "Software Engineering (0) 0%";
        }
        else{
            return String.format("Software Engineering (%.0f) %.2f", numSoftwareEngineering, percent) + "%";
        }
    }
    String getPercentMathematics(){
        double percent = (numMathematics / totalStudents())*100;
        if(percent == 0.0){
            return "Mathematics (0) 0%";
        }
        else{
            return String.format("Mathematics (%.0f) %.2f", numMathematics, percent) + "%";
        }
    }
    String getPercentArt(){
        double percent = (numArt / totalStudents())*100;
        if(percent == 0.0){
            return "Art (0) 0%";
        }
        else{
            return String.format("Art (%.0f) %.2f", numArt, percent) + "%";
        }
    }
    String getPercentUnderwaterBasketWeaving(){
        double percent = (numUnderwaterBasketWeaving / totalStudents())*100;
        if(percent == 0.0){
            return "Underwater Basket Weaving (0) 0%";
        }
        else{
            return String.format("Underwater Basket Weaving (%.0f) %.2f", numUnderwaterBasketWeaving, percent) + "%";
        }
    }

    //bar chart methods

    void incNumA(){
        numA++;
    }
    void incNumB(){
        numB++;
    }
    void incNumC(){
        numC++;
    }
    void incNumD(){
        numD++;
    }
    void incNumF(){
        numF++;
    }

    int getNumA(){
        return numA;
    }
    int getNumB(){
        return numB;
    }
    int getNumC(){
        return numC;
    }
    int getNumD(){
        return numD;
    }
    int getNumF(){
        return numF;
    }






}
