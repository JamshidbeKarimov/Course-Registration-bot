package enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor



public enum CardType {
    UZCARD("uzcard"),
    HUMO("humo"),
    VISA("visa"),
    MASTER("master");

    private String name;


}
