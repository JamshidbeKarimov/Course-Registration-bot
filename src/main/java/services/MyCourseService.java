package services;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.Course;
import model.MyCourse;
import repositories.MyCourseRepository;
import repositories.ResponseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyCourseService extends MyCourseRepository implements ResponseRepository {
    @Override
    public MyCourse get(UUID myCourseId) {
        for (MyCourse myCourse: getList()) {
            if(myCourse.getId().equals(myCourseId))
                return myCourse;
        }
        return null;
    }

    @Override
    public String add(MyCourse myCourse) {
        List<MyCourse> myCourseList = getList();
        for (MyCourse c:myCourseList) {
            if(c.getName().equals(myCourse.getName()) && c.getStudentId().equals(myCourse.getStudentId()))
                return null;
        }

        myCourseList.add(myCourse);
        setMyCoursesListToFile(myCourseList);
        return SUCCESS;
    }

    @Override
    public List<MyCourse> getList() {
        return getMyCoursesListFromFile();
    }

    @Override
    public List<MyCourse> getListById(UUID studentOrCourseId) {
        List<MyCourse> myCourseList = new ArrayList<>();

        for (MyCourse myCourse:getList()) {
            if(myCourse.getCourseId().equals(studentOrCourseId) || myCourse.getStudentId().equals(studentOrCourseId))
                myCourseList.add(myCourse);
        }

        return myCourseList;
    }

    @Override
    public String editById(UUID myCourseId, MyCourse editedCourse) {
        MyCourse myCourse = get(myCourseId);
        List<MyCourse> myCourseList = getList();
        int ind = myCourseList.indexOf(myCourse);

        if(editedCourse.getName() != null)
            myCourse.setName(editedCourse.getName());

        myCourse.setPaid(editedCourse.isPaid());

        myCourseList.set(ind, myCourse);
        setMyCoursesListToFile(myCourseList);
        return SUCCESS;
    }

    private List<MyCourse> getMyCoursesListFromFile(){
        String myCourseJsonStringFromFile = FileUtils.readFromFile(FileUrls.myCoursesStorageUrl);
        List<MyCourse> myCourseList;
        try {
            myCourseList = Json.objectMapper.readValue(myCourseJsonStringFromFile, CollectionsTypeFactory.listOf(MyCourse.class));
        } catch (Exception e) {
            System.out.println(e);
            myCourseList = new ArrayList<>();
        }
        return myCourseList;
    }

    @SneakyThrows
    private void setMyCoursesListToFile(List<MyCourse> myCourseList) {
        String newMyCourseJsonFromObject = Json.prettyPrint(myCourseList);
        FileUtils.writeToFile(FileUrls.myCoursesStorageUrl, newMyCourseJsonFromObject);
    }
}
