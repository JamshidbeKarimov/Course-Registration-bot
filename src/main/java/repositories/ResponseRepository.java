package repositories;

public interface ResponseRepository {

    String SUCCESS = "SUCCESSFULLY COMPLETED";

    String ALREADY_REGISTERED = "You already registered for this course";
    String ALREADY_EXIST = "THIS USER ALREADY EXISTS";
    String AGE_RESTRICTION = "SORRY, YOUR AGE DOESN'T MATCH OUR REQUIREMENTS. IT SHOULD BE BETWEEN 10-25";

    String ERROR_EXIST_CARD = "CARD ALREADY EXIST";
    String WRONG_CARD_NUMBER = "CARD NUMBER SHOULD CONTAIN ONLY 16 (SIXTEEN) NUMBERS";
}
