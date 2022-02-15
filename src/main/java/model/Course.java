package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Course extends BaseModel{

    private double price;
    private int duration; // in months
    private String info;
    private int startAge;
    private int endAge;
    private boolean isOnline;
    private String infoUrl;
    private String moreInfoPathName;

    @Override
    public String toString() {
        return "Course name: " + name + "\n"
                + "Course type: " + (isOnline ? "online\n" : "offline\n")
                + "Course info: " + info + "\n"
                + "Course duration: " + duration + " months\n"
                + "Age limit: " + startAge + "-" + endAge + "\n"
                + "Course price (monthly): " + price + "\n"
                + (new SimpleDateFormat("dd/MM/yyyy").format(updatedDate));
    }
}
