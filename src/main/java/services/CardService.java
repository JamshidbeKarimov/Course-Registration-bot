package services;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.Card;
import model.Course;
import repositories.CardRepository;
import repositories.ResponseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardService extends CardRepository implements ResponseRepository {
    @Override
    public Card get(UUID cardId) {
        for (Card card: getList()){
            if(card.getId().equals(cardId))
                return card;
        }
        return null;
    }

    @Override
    public String add(Card card) {
        List<Card> cardList = getList();

        for (Card existCard:cardList) {
            if(existCard.getCardNumber().equals(card.getCardNumber()))
                return ERROR_EXIST_CARD;
        }

        cardList.add(card);
        setCardsListToFile(cardList);
        return SUCCESS;
    }

    @Override
    public List<Card> getList() {
        return getCardListFromFile();
    }

    @Override
    public List<Card> getListById(UUID ownerId) {
        List<Card> myCardList = new ArrayList<>();

        for (Card card :getList()) {
            if(card.getOwnerId().equals(ownerId) && card.getCardNumber() != null)
                myCardList.add(card);
        }
        return myCardList;
    }

    @Override
    public String editById(UUID id, Card editedCard) {
        Card card = get(editedCard.getId());

        if(editedCard.getOwnerId() != null)
            card.setOwnerId(editedCard.getOwnerId());

        return SUCCESS;
    }

    private List<Card> getCardListFromFile() {
        String cardJsonStringFromFile = FileUtils.readFromFile(FileUrls.cardStorageUrl);
        List<Card> cardList;
        try {
            cardList = Json.objectMapper.readValue(cardJsonStringFromFile, CollectionsTypeFactory.listOf(Course.class));
        } catch (Exception e) {
            System.out.println(e);
            cardList = new ArrayList<>();
        }
        return cardList;
    }

    @SneakyThrows
    private void setCardsListToFile(List<Card> cardList) {
        String newCardJsonFromObject = Json.prettyPrint(cardList);
        FileUtils.writeToFile(FileUrls.cardStorageUrl, newCardJsonFromObject);
    }

}
