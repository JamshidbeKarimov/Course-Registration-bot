package services;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.User;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import repositories.ResponseRepository;
import repositories.StudentRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService extends StudentRepository implements ResponseRepository {
    @Override
    public User get(UUID studentId) {
        for (User s: getList()) {
            if(s.getId().equals(studentId))
                return s;
        }

        return null;
    }
    @Override
    public User getByChatId(String chatId){
        for (User s: getList()) {
            if(s.getChatId().equals(chatId))
                return s;
        }

        return null;
    }

    @Override
    public String add(User user) {
        if(user == null)
            return null;
        List<User> userList = getList();
        for (User s: userList) {
            if(s.getChatId().equals(user.getChatId()))
                return ALREADY_EXIST;
        }

        userList.add(user);
        setUserListToFile(userList);
        return SUCCESS;
    }

    @Override
    public List<User> getList() {
        return getUserListFromFile();
    }

    @Override
    public List<User> getListById(UUID id) {
        return null;
    }

    @Override
    public String editById(UUID id, User user) {
        return null;
    }


    public String editByChatId(String chatId, User editedUser) {
        User user = getByChatId(chatId);
        List<User> userList = getList();
        int ind = userList.indexOf(user);

        if(editedUser.getName() != null)
            user.setName(editedUser.getName());
        if(editedUser.getAge() !=0 )
            user.setAge(editedUser.getAge());
        if(editedUser.getUserState() != null)
            user.setUserState(editedUser.getUserState());
        if(editedUser.getLocation() != null)
            user.setLocation(editedUser.getLocation());
        if(editedUser.getBalance() != 0)
            user.setBalance(editedUser.getBalance());

        userList.set(ind, user);
        setUserListToFile(userList);
        return SUCCESS;
    }

    public User login(String chatId){
        for (User user :getList()) {
            if(user.getChatId().equals(chatId))
                return user;
        }

        return null;
    }

    private List<User> getUserListFromFile(){
        String userJsonStringFromFile = FileUtils.readFromFile(FileUrls.usersUrl);
        List<User> userList;
        try {
            userList = Json.objectMapper.readValue(userJsonStringFromFile, CollectionsTypeFactory.listOf(User.class));
        } catch (Exception e) {
            System.out.println(e);
            userList = new ArrayList<>();
        }
        return userList;
    }


    @SneakyThrows
    private void setUserListToFile(List<User> userList) {
        String newCourseJsonFromObject = Json.prettyPrint(userList);
        FileUtils.writeToFile(FileUrls.usersUrl, newCourseJsonFromObject);
    }

    public void writeToExcel(String path){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Student Details");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //Set Header Font
        HSSFFont headerFont = workbook.createFont();
        headerFont.setFontName(HSSFFont.FONT_ARIAL);
        headerFont.setFontHeightInPoints((short) 12);

//Set Header Style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);

        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);

        Cell cell0 = row.createCell(0);
        Cell cell1 = row.createCell(1);
        Cell cell2 = row.createCell(2);
        Cell cell3 = row.createCell(3);
        Cell cell4 = row.createCell(4);

        cell0.setCellValue("No:");
        cell1.setCellValue("Name");
        cell2.setCellValue("Age");
        cell3.setCellValue("Balance");
        cell4.setCellValue("Joined Date");

        for (User user : getList()) {
            row = sheet.createRow(rowNum++);
            int cellId = 0;

            cell0 = row.createCell(cellId++);
            cell1 = row.createCell(cellId++);
            cell2 = row.createCell(cellId++);
            cell3 = row.createCell(cellId++);
            cell4 = row.createCell(cellId++);

            cell0.setCellValue(rowNum);
            cell1.setCellValue(user.getName());
            cell2.setCellValue(user.getAge());
            cell3.setCellValue(user.getBalance());
            cell4.setCellValue(format.format(user.getCreatedDate()));
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(path));
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
