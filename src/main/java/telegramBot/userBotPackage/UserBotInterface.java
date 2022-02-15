package telegramBot.userBotPackage;

import services.CourseService;
import services.MyCourseService;
import services.UserService;
import telegramBot.userBotPackage.services.UserBotService;

public interface UserBotInterface {

    UserService userService = new UserService();
    MyCourseService myCourseService = new MyCourseService();
    CourseService courseService = new CourseService();
    UserBotService userBotService = new UserBotService();

    String BOT_USERNAME = "unicorn_java_bot";
    String BOT_TOKEN = "5030960493:AAEqt8z_gTBWjIlH4QMjVu341hd7bykvExs";

    String AVAILABLE_COURSES = "\uD83D\uDD0D AVAILABLE COURSES";
    String MY_BALANCE = "\uD83D\uDCB2 MY BALANCE";
    String MY_COURSES = "\uD83D\uDCCB MY COURSES";
    String FILL_BALANCE = "\uD83D\uDCB3 FILL BALANCE";
    String DIRECT = "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB DIRECT MESSAGE \uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDCBB";
    String DELETE_MESSAGE = "deleteMessage";
    String PAY = "pay";
    String ADD_CARD = "addCard";
    String CHOOSE_CARD_TYPE = "choseCardType";
    String ADMIN = "/admin"; // secretword walala
    String PASSWORD = "walala";
    String STUDENTS = "\uD83D\uDCE5 STUDENTS";
    String MORE_INFO = "moreInfo";
    String CARD_TYPE = "master_visa_uzcard_humo";

}
