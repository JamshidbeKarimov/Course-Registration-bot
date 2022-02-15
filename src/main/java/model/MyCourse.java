package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class MyCourse extends BaseModel{
    private UUID courseId;
    private UUID studentId;
    private boolean isPaid;

    @Override
    public String toString() {
        return "Paid date: " + (new SimpleDateFormat("dd/MM/yyyy").format(updatedDate));
    }
}
