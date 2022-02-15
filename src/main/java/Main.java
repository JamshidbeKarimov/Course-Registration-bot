import model.Course;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import services.CourseService;
import telegramBot.userBotPackage.UserBot;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new UserBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

//
////
        Scanner scannerStr = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);

        CourseService courseService = new CourseService();



        while(true){
            Course course = new Course();

            System.out.print("\nEnter name: ");
            course.setName(scannerStr.nextLine());

            System.out.print("\nEnter duration :");
            course.setDuration(scannerInt.nextInt());

            System.out.print("\nEnter info: ");
            course.setInfo(scannerStr.nextLine());

            System.out.print("\nEnter start age: ");
            course.setStartAge(scannerInt.nextInt());

            System.out.print("\nEnter end age: ");
            course.setEndAge(scannerInt.nextInt());

            System.out.println("\nEnter price: ");
            course.setPrice(scannerInt.nextDouble());

            courseService.add(course);
        }


    }
}
