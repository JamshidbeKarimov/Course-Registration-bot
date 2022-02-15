package model;

import enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Card extends BaseModel {
    private String cardNumber;
    private UUID ownerId;
    private CardType cardType;
    private double balance;

}
