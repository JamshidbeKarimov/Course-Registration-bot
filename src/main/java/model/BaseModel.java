package model;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public abstract class BaseModel {
    protected UUID id;
    protected String name;
    protected Date createdDate;
    protected Date updatedDate;

    {
        this.id = UUID.randomUUID();
        this.createdDate = new Date();
        this.updatedDate = new Date();

    }

}
