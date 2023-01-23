import Controller.MainController;
import View.MainView;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();

        MainController mainController = new MainController(600, 8, 0, 8, 3, 8);
//        TimeUnit.SECONDS.sleep(5);
        mainController.populateWorld();
        int maxFood = 8;
        int minFood = 0;
        int foodRegenAmount = 8;
        double foodRegenChance = 3;
        for (int i = 0; i < 1000000; i++) {
            System.out.println("      Year " + i + ": Max Food = " + maxFood + ". Min Food = " + minFood + ". Food Regen Chance: " + foodRegenChance + ". Food Regen Amount: " + foodRegenAmount + ". ");
            for (int j = 0; j < 4; j++) { // Year
                System.out.println("Season " + j + ": Max Food = " + maxFood + ". Min Food = " + minFood + ". Food Regen Chance: " + foodRegenChance + ". Food Regen Amount: " + foodRegenAmount + ". ");
                for (int k = 0; k < random.nextInt(100) + 71 + (j * 10); k++) { // Season
                    mainController.runStep();
                }
                foodRegenAmount -= 1;
                mainController.setFoodRegenAmount(foodRegenAmount);
            }
            foodRegenAmount = 8;
            mainController.setFoodRegenAmount(foodRegenAmount);
            if (random.nextInt(5) == 0){
                foodRegenChance -= 0.11 ;
                mainController.setFoodRegenChance(foodRegenChance);
            }
            else {
                foodRegenChance += 0.02;
                mainController.setFoodRegenChance(foodRegenChance);
            }
        }
        //testMotivations();
    }

    public static void testMotivations(){
        int max = 8;
        int dynamic = 8;
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (max + dynamic > max * 2 - dynamic) {
                count += 1;
                System.out.println(dynamic);
            }
            dynamic--;
        }
        System.out.println("create: " + count);
    }
}

// Useful diagnostics print
//System.out.println("Hunger: " + super.getScores().getHunger() + ", Health: " + super.getScores().getHealth() + ", Age: " + super.getScores().getAge() + ", (" + super.getLocation().getX() + "," + super.getLocation().getY() + ")");
