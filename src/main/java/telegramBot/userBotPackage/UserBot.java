package telegramBot.userBotPackage;

import enums.CardType;
import lombok.SneakyThrows;
import model.Card;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import repositories.ResponseRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class UserBot extends TelegramLongPollingBot implements UserBotInterface, ResponseRepository{
    HashMap<Integer, UserState> messageState = new HashMap<>();
    HashMap<Integer, String> messageCourse = new HashMap<>();
    List<Card> unfinishedCardList = new ArrayList<>();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            User currentUser = userService.login(chatId);
            UserState userState = UserState.MAIN_MENU;


            if (message.hasContact()) {
                userState = UserState.START_CONTACT_SHARED;
            }
            else if (message.hasLocation()) {
                userState = UserState.START_LOCATION;
            }
            else if (currentUser == null) {
                userState = UserState.START_NEW_USER;
            }
            else if(currentUser.getLocation() == null){
                userState = UserState.START_CONTACT_SHARED;
            }
            else if(currentUser.getAge() == 0) {
                userState = UserState.START_AGE;
            }
            else if(message.hasText() && !message.getText().equals("/start")){
                switch (message.getText()){
                    case AVAILABLE_COURSES -> {
                        userState = UserState.AVAILABLE_COURSES;
                    }
                    case MY_COURSES -> {
                        userState = UserState.MY_COURSES;
                    }
                    case MY_BALANCE -> {
                        userState = UserState.MY_BALANCE;
                    }
                    case FILL_BALANCE -> {
                        userState = UserState.FILL_BALANCE;
                    }
                    case DIRECT -> {
                        userState = UserState.DIRECT_MESSAGE;
                    }
                    case ADMIN -> {
                        userState = UserState.ADMIN;
                    }
                    case PASSWORD -> {
                        userState = UserState.ADMIN_PANEL;
                    }
                    case STUDENTS -> {
                        userState = UserState.STUDENTS;
                    }
                }
            }
            else if(message.getText().equals("/start")) {
                userState = UserState.MAIN_MENU;
            }
            else{
                userState = currentUser.getUserState();
            }

            switch (userState) {
                case START_NEW_USER: {
                    execute(userBotService.sharePhoneNumber(chatId));
                } break;
                case START_CONTACT_SHARED: {
                    userService.add(userBotService.createStudent(message));
                    execute(userBotService.shareLocation(chatId));
                } break;
                case START_LOCATION: {
                    Location location = message.getLocation();
                    currentUser.setLocation(location);
                    currentUser.setUserState(UserState.START_AGE);
                    userService.editByChatId(chatId, currentUser);
                    execute(userBotService.enterAge(chatId));
                } break;
                case START_AGE: {
                    execute(userBotService.getAge(chatId, getAge(message.getText())));
                } break;
                case MAIN_MENU: {
                    execute(userBotService.mainMenu(chatId));
                } break;
                case AVAILABLE_COURSES: {
                  currentUser.setUserState(UserState.COURSE_INFO);
                  userService.editByChatId(chatId, currentUser);
                  messageState.put(execute(userBotService.courses(chatId,
                          currentUser.getAge())).getMessageId(), UserState.COURSE_INFO);
                } break;
                case MY_COURSES: {
                    currentUser.setUserState(UserState.MY_COURSES_EDIT);
                    userService.editByChatId(chatId, currentUser);
                    messageState.put(execute(userBotService.myCourses(chatId,
                            currentUser.getId())).getMessageId(), UserState.MY_COURSES_EDIT);
                } break;
                case MY_BALANCE: {
                    execute(userBotService.myBalance(chatId, currentUser.getId()));
                } break;
                case FILL_BALANCE: {
                    execute(userBotService.fillBalance(chatId, currentUser.getId()));
                } break;
                case ADMIN: {
                    execute(userBotService.adminPassword(chatId));
                } break;
                case ADMIN_PANEL: {
                    execute(userBotService.adminPanel(chatId));
                } break;
                case STUDENTS: {
                    execute(userBotService.studentList(chatId));
                } break;
                case CARD_NUMBER: {
                    if(checkCardNumber(message.getText()) == 0) {
                        addCardNumber(currentUser.getId(), message.getText());
                        execute(userBotService.cardCreated(chatId));
                    }
                    else{
                      execute(userBotService.wrongCardNumber(chatId));
                    }
                } break;
                case DIRECT_MESSAGE: {

                } break;
            }
        }
        else if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callData = callbackQuery.getData();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            String chatId = callbackQuery.getMessage().getChatId().toString();
            User currentUser = userService.login(chatId);
            UserState userState = currentUser.getUserState();


            if(callData.equals(DELETE_MESSAGE))
                userState = UserState.DELETE_MESSAGE;
            else if(callData.equals(PAY))
                userState = UserState.PAY;
            else if(callData.equals(MY_COURSES))
                userState = UserState.MY_COURSES;
            else if(callData.equals(AVAILABLE_COURSES))
                userState = UserState.AVAILABLE_COURSES;
            else if(callData.equals(CHOOSE_CARD_TYPE))
                userState = UserState.CHOOSE_CARD_TYPE;
            else if(callData.equals(MORE_INFO))
                userState = UserState.MORE_INFO;
            else if(CARD_TYPE.contains(callData))
                userState = UserState.ADD_CARD;
            else if(messageState.get(messageId) != null)
                userState = messageState.get(messageId);
            else
                userState = currentUser.getUserState();



            switch (userState){
                case COURSE_INFO -> {
                    messageState.put(messageId, UserState.COURSE_INFO);
                    currentUser.setUserState(UserState.REGISTER_FOR_COURSE);
                    userService.editByChatId(chatId, currentUser);
                    Integer id = execute(userBotService.courseInfo(chatId, callData)).getMessageId();
                    messageState.put(id, UserState.REGISTER_FOR_COURSE);
                    messageCourse.put(id, callData);
                }
                case REGISTER_FOR_COURSE -> {
                    execute(userBotService.registerForCourse(chatId,
                            messageId, currentUser.getId(), UUID.fromString(callData)));
                }
                case MY_COURSES_EDIT -> {
                    execute(userBotService.editMyCourse(chatId, messageId, UUID.fromString(callData)));
                }
                case PAY -> {
                    messageState.put(messageId, UserState.CHOOSE_CARD);
                    execute(userBotService.chooseCard(chatId, messageId, currentUser.getId()));
                }
                case CHOOSE_CARD_TYPE -> {
                    messageState.put(execute(userBotService.cardType(chatId)).getMessageId(), UserState.ADD_CARD);
                    createUnfinishedCard(callData, currentUser.getId());
                }
                case ADD_CARD -> {
                    currentUser.setUserState(UserState.CARD_NUMBER);
                    userService.editByChatId(chatId, currentUser);
                    execute(userBotService.enterCardNumber(chatId, messageId));
                }
                case MY_COURSES -> {
                    currentUser.setUserState(UserState.MY_COURSES_EDIT);
                    userService.editByChatId(chatId, currentUser);
                    execute(userBotService.editToMyCourses(chatId, messageId, currentUser.getId()));
                    messageState.put(messageId, UserState.MY_COURSES_EDIT);
                }
                case AVAILABLE_COURSES -> {
                    currentUser.setUserState(UserState.COURSE_INFO);
                    userService.editByChatId(chatId, currentUser);
                    execute(userBotService.editToAvailableCourses(chatId, messageId, currentUser.getAge()));
                    messageState.put(messageId, UserState.COURSE_INFO);
                }
                case DELETE_MESSAGE -> {
                    execute(userBotService.deleteMessage(chatId, messageId));
                    messageState.remove(messageId);
                }
                case MORE_INFO -> {
                    execute(userBotService.sendCourseInfo(chatId, UUID.fromString(messageCourse.get(messageId))));
                }
            }
        }
    }


    private int getAge(String ageStr){
        if(ageStr.matches("[0-9]+")){
            int age = Integer.parseInt(ageStr);

            if(age < 10 || age > 35)
                return 0;

            return age;
        }

        return -1;
    }

    private void createUnfinishedCard(String cardType, UUID userId){
        Card card = new Card();
        card.setCardType(CardType.valueOf(cardType));
        card.setOwnerId(userId);

        unfinishedCardList.add(card);
    }

    private int checkCardNumber(String cardNumber){
        if (cardNumber.length() < 16 || cardNumber.length() > 16 || !cardNumber.matches("[0-9]+"))
            return 0;
        return -1;
    }

    private void addCardNumber(UUID userId, String cardNumber){
        for (Card card: unfinishedCardList) {
            if(card.getOwnerId().equals(userId)){
                card.setCardNumber(cardNumber);
            }
        }
    }


}
