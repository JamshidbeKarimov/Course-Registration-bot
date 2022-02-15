package telegramBot.userBotPackage.services;

import lombok.SneakyThrows;
import model.Card;
import model.Course;
import model.MyCourse;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import repositories.ResponseRepository;
import services.CardService;
import services.CourseService;
import services.MyCourseService;
import services.UserService;
import telegramBot.userBotPackage.UserState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jsonFile.FileUrls.studentExcelUrl;

public class UserBotService implements ResponseRepository {
    UserService userService = new UserService();
    CourseService courseService = new CourseService();
    MyCourseService myCourseService = new MyCourseService();
    CardService cardService = new CardService();
    @SneakyThrows
    public SendMessage sharePhoneNumber(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Welcome to PDP Online registration bot.\uD83D\uDE0A\nSend your phone number");


        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("\uD83D\uDCDE Share your phone number >");
        button.setRequestContact(true);

        keyboardRow.add(button);
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);


        return sendMessage;
    }

    @SneakyThrows
    public SendMessage shareLocation(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Share your location >");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("\uD83D\uDCCD Share location >");
        button.setRequestLocation(true);

        keyboardRow.add(button);
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }

    public SendMessage enterAge(String chatId){
        SendMessage sendMessage = new SendMessage(chatId, "Enter your age: ");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

        return sendMessage;
    }

    public SendMessage getAge(String chatId, int age){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        if(age == -1){
            sendMessage.setText("WRONG INPUT, TRY AGAIN.\nAge should contain numbers only");
        }
        else if(age == 0){
            sendMessage.setText("Sorry we offer courses for ages only between 10 and 35.");
        }
        else{
            sendMessage.setText(SUCCESS);
            User user = new User();
            user.setAge(age);
            user.setUserState(UserState.MAIN_MENU);
            sendMessage.setReplyMarkup(mainMenuKeyboard());
            userService.editByChatId(chatId, user);
        }

        return sendMessage;
    }

    public SendMessage mainMenu(String chatId){
        SendMessage sendMessage = new SendMessage(chatId, "MAIN MENU");
        sendMessage.setReplyMarkup(mainMenuKeyboard());
        return sendMessage;
    }

    public SendMessage courses(String chatId, int userAge){
        SendMessage sendMessage = new SendMessage(chatId, "\uD83D\uDCDC AVAILABLE COURSES FOR YOUR AGE \uD83D\uDCDC");
        sendMessage.setReplyMarkup(coursesMarkup(userAge));

        return sendMessage;
    }

    public SendMessage courseInfo(String chatId, String courseIdStr){
        SendMessage sendMessage = new SendMessage(chatId, "");
        Course course = courseService.get(UUID.fromString(courseIdStr));
        sendMessage.setText(course.toString());
        sendMessage.setReplyMarkup(courseInfoMarkup(course));

        return sendMessage;
    }

    public SendMessage myCourses(String chatId, UUID studentId){

        SendMessage sendMessage = new SendMessage(chatId, myCoursesText(studentId));
        sendMessage.setReplyMarkup(myCoursesMarkup(studentId));

        return sendMessage;
    }

    public SendMessage myBalance(String chatId, UUID studentId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("You have " + userService.get(studentId).getBalance() + " credits \uD83E\uDE99");
        return sendMessage;
    }

    public SendMessage fillBalance(String chatId, UUID studentId){
        SendMessage sendMessage = new SendMessage(chatId, "One credit costs 1500 sums\uD83D\uDCB0\n");
        sendMessage.setReplyMarkup(fillBalanceMarkup(studentId));

        return sendMessage;
    }

    public SendMessage adminPassword(String chatId){
        return new SendMessage(chatId, "Enter admin password: ");
    }

    public SendMessage adminPanel(String chatId){
        SendMessage sendMessage = new SendMessage(chatId, "ADMIN MAIN MENU");
        sendMessage.setReplyMarkup(adminMainMenuKeyboard());

        return sendMessage;
    }

    public SendMessage cardType(String chatId){
        SendMessage sendMessage = new SendMessage(chatId, "Here are supported card types\n \uD83D\uDD30 CHOOSE ONE TYPE \uD83D\uDD30");
        sendMessage.setReplyMarkup(cardTypes());

        return sendMessage;
    }

    public SendMessage cardCreated(String chatId){
        return new SendMessage(chatId, SUCCESS);
    }

    public SendMessage wrongCardNumber(String chatId){
        return new SendMessage(chatId, WRONG_CARD_NUMBER);
    }

    public SendDocument studentList(String chatId){
        SendDocument document = new SendDocument();
        document.setChatId(chatId);
        userService.writeToExcel(studentExcelUrl);
        document.setDocument(new InputFile(new File(studentExcelUrl)));

        return document;
    }

    public SendDocument sendCourseInfo(String chatId, UUID courseId){
        return new SendDocument(chatId, new InputFile(new File(courseService.get(courseId).getMoreInfoPathName())));
    }

    public EditMessageText registerForCourse(String chatId, Integer messageId, UUID studentId, UUID courseId){
        EditMessageText editMessageText = new EditMessageText(createMyCourse(studentId, courseService.get(courseId)));
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId);
        editMessageText.setReplyMarkup(registeredMarkup());
        return editMessageText;
    }

    public EditMessageText editMyCourse(String chatId, Integer messageId, UUID myCourseId){
        MyCourse myCourse = myCourseService.get(myCourseId);
        EditMessageText edit = new EditMessageText(myCourseEditText(myCourse.getCourseId()) + "\n"
                + (myCourse.isPaid() ? myCourse.toString() : ""));
        edit.setMessageId(messageId);
        edit.setChatId(chatId);
        edit.setReplyMarkup(myCourseEditMarkup(myCourse.isPaid()));


        return edit;
    }

    public EditMessageText editToAvailableCourses(String chatId, Integer messageId, int userAge){
        EditMessageText edit = new EditMessageText("\uD83D\uDCDC AVAILABLE COURSES FOR YOUR AGE \uD83D\uDCDC");
        edit.setChatId(chatId);
        edit.setMessageId(messageId);
        edit.setReplyMarkup(coursesMarkup(userAge));

        return edit;
    }

    public EditMessageText editToMyCourses(String chatId, Integer messageId, UUID studentId){
        EditMessageText edit = new EditMessageText(myCoursesText(studentId));
        edit.setMessageId(messageId);
        edit.setChatId(chatId);
        edit.setReplyMarkup(myCoursesMarkup(studentId));
        return edit;
    }

    public EditMessageText chooseCard(String chatId, Integer messageId, UUID userId){
        EditMessageText edit = new EditMessageText("\uD83D\uDCB3 CHOOSE CARD \uD83D\uDCB3");
        edit.setReplyMarkup(myCardsMarkup(userId));
        edit.setChatId(chatId);
        edit.setMessageId(messageId);
        return edit;
    }

    public EditMessageText enterCardNumber(String chatId, Integer messageId){
        EditMessageText edit = new EditMessageText("Enter card number (8888 9999 3434 4343)");
        edit.setMessageId(messageId);
        edit.setChatId(chatId);

        return edit;
    }


    public DeleteMessage deleteMessage(String chatId, Integer messageId){
        return new DeleteMessage(chatId, messageId);
    }


    private InlineKeyboardMarkup coursesMarkup(int age){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Course course: courseService.getList()) {
            if(age >= course.getStartAge() && age <= course.getEndAge()) {
                InlineKeyboardButton button = new InlineKeyboardButton(course.getName());
                button.setCallbackData(course.getId().toString());
                rows.add(List.of(button));
            }
        }

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardMarkup courseInfoMarkup(Course course){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("\uD83D\uDCDD Register");
        button.setCallbackData(course.getId().toString());
        row.add(button);

        if(course.getMoreInfoPathName() != null) {
            button = new InlineKeyboardButton("More Info in file");
            button.setCallbackData("moreInfo");
            row.add(button);
        }

        button = new InlineKeyboardButton("\uD83D\uDDD1 delete message");
        button.setCallbackData("deleteMessage");
        row.add(button);

        rows.add(row);

        button = new InlineKeyboardButton();
        button.setText("\uD83C\uDF10 Full info \uD83D\uDDDE");
        button.setUrl(course.getInfoUrl());
        rows.add(List.of(button));

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardMarkup registeredMarkup(){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("\uD83D\uDCCB MY COURSES");
        button.setCallbackData("\uD83D\uDCCB MY COURSES");
        row.add(button);

        button = new InlineKeyboardButton("\uD83D\uDD0D AVAILABLE COURSES");
        button.setCallbackData("\uD83D\uDD0D AVAILABLE COURSES");
        row.add(button);
        rows.add(row);

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardMarkup myCoursesMarkup(UUID studentId){
        List<MyCourse> myCourses = myCourseService.getListById(studentId);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        int i = 1;
        for (MyCourse myCourse:myCourses) {
            InlineKeyboardButton button = new InlineKeyboardButton("" + i++);
            button.setCallbackData(myCourse.getId().toString());
            row.add(button);

            if(row.size() == 3){
                rows.add(row);
                row = new ArrayList<>();
            }
        }

        if(row.size() != 0){
            rows.add(row);
        }

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardMarkup myCourseEditMarkup(boolean isPaid){
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button;
        if(isPaid){
            button = new InlineKeyboardButton("✅ Paid");
            button.setCallbackData("noting");
        }
        else{
            button = new InlineKeyboardButton("\uD83D\uDCB3 Pay \uD83D\uDCB5");
            button.setCallbackData("pay");
        }
        row.add(button);
        button = new InlineKeyboardButton("⬅ Back to My Courses");
        button.setCallbackData("\uD83D\uDCCB MY COURSES");
        row.add(button);

        return new InlineKeyboardMarkup(List.of(row));
    }

    private InlineKeyboardMarkup fillBalanceMarkup(UUID studentId){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (Card card: cardService.getListById(studentId)) {
            InlineKeyboardButton button = new InlineKeyboardButton(card.getCardNumber());
            button.setCallbackData(card.getCardNumber());

            row.add(button);

            if (row.size() == 2) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }

        InlineKeyboardButton button = new InlineKeyboardButton("\uD83D\uDCB3 Add card");
        button.setCallbackData("addCard");
        row.add(button);
        rows.add(row);

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardMarkup myCardsMarkup(UUID userId){
        List<Card> myCardList = cardService.getListById(userId);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (Card card: myCardList) {
            InlineKeyboardButton button = new InlineKeyboardButton("\uD83D\uDCB3 " + card.getCardNumber());
            button.setCallbackData(card.getId().toString());

            row.add(button);

            if(row.size() == 3){
                rows.add(row);
                row = new ArrayList<>();
            }
        }

        if(row.size() != 0) {
            rows.add(row);
            row = new ArrayList<>();
        }

        if(myCardList.size() != 6){
            InlineKeyboardButton button = new InlineKeyboardButton("➕ Add Card");
            button.setCallbackData("choseCardType");
            rows.add(List.of(button));
        }

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardMarkup cardTypes(){
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("\uD83D\uDD4A HUMO");
        button.setCallbackData("humo");
        row.add(button);

        button = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF UZCARD");
        button.setCallbackData("uzcard");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton("\uD83C\uDF0F VISA");
        button.setCallbackData("visa");
        row.add(button);

        button = new InlineKeyboardButton("\uD83D\uDCB3 MASTER");
        button.setCallbackData("master");
        row.add(button);

        rows.add(row);

        return new InlineKeyboardMarkup(rows);
    }

    private ReplyKeyboardMarkup mainMenuKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("\uD83D\uDD0D AVAILABLE COURSES");
        row.add("\uD83D\uDCCB MY COURSES");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("\uD83D\uDCB2 MY BALANCE");
        row.add("\uD83D\uDCB3 FILL BALANCE");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB DIRECT MESSAGE \uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDCBB");
        keyboardRows.add(row);

        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup adminMainMenuKeyboard(){
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("\uD83D\uDD0D ALL COURSES");
        row.add("\uD83D\uDCE5 STUDENTS");
        keyboardRows.add(row);

        return new ReplyKeyboardMarkup(keyboardRows);
    }

    public User createStudent(Message message){
        if(!message.hasContact())
            return null;

        String phoneNumber = message.getContact().getPhoneNumber();
        User user = new User();

        user.setPhoneNumber(phoneNumber);
        user.setChatId(message.getChatId().toString());
        if (message.getChat().getUserName() != null)
            user.setUsername(message.getChat().getUserName());

        user.setName(message.getContact().getFirstName());
        user.setUserState(UserState.START_LOCATION);

        return user;
    }

    private String createMyCourse(UUID studentId, Course course){
        MyCourse myCourse = new MyCourse(course.getId(), studentId, false);
        myCourse.setName(course.getName());

        if(myCourseService.add(myCourse) != null){
            return SUCCESS +"\nYou should pay the course fee within 20 days";
        }
        return ALREADY_REGISTERED;
    }

    private String myCoursesText(UUID studentId){
        List<MyCourse> myCourses = myCourseService.getListById(studentId);

        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (MyCourse course: myCourses) {
            sb.append(++i +". " + course.getName() + "\t||\tStatus: " + (course.isPaid() ? "paid" : "unpaid"  + "\n\n"));
        }

        sb.append("❗️Press one of the buttons to see more information");
        return sb.toString();
    }

    private String myCourseEditText(UUID courseId){
        return courseService.get(courseId).toString();
    }
}
