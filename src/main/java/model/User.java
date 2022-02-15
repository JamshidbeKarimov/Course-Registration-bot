package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;
import telegramBot.userBotPackage.UserState;

@AllArgsConstructor
@NoArgsConstructor
@Data


public class User extends BaseModel{
    private String chatId;
    private double balance;
    private int age;
    private String phoneNumber;
    private Location location;
    private UserState userState;
    private String username;

}
