package services;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.Course;
import repositories.CourseRepository;
import repositories.ResponseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseService extends CourseRepository implements ResponseRepository {
    @Override
    public Course get(UUID courseId) {
        for (Course course: getList()) {
            if(course.getId().equals(courseId))
                return course;
        }
        return null;
    }

    @Override
    public String add(Course course) {
        List<Course> courseList = getList();
        for (Course c:courseList) {
            if(c.getName().equals(course.getName()))
                return null;
        }

        courseList.add(course);
        setCoursesListToFile(courseList);
        return SUCCESS;
    }

    @Override
    public List<Course> getList() {
        return getCoursesListFromFile();
    }

    @Override
    public List<Course> getListById(UUID id) {
        return null;
    }

    @Override
    public String editById(UUID courseId, Course editedCourse) {
        Course course = get(courseId);
        List<Course> courseList = getList();
        int ind = courseList.indexOf(course);

        if(editedCourse.getName() != null)
            course.setName(editedCourse.getName());
        if(editedCourse.getDuration() != 0)
            course.setDuration(editedCourse.getDuration());
        if(editedCourse.getStartAge() != 0)
            course.setStartAge(editedCourse.getStartAge());
        if(editedCourse.getEndAge() != 0)
            course.setEndAge(editedCourse.getEndAge());
        if(editedCourse.getInfo() != null)
            course.setInfo(editedCourse.getInfo());

        courseList.set(ind, course);
        setCoursesListToFile(courseList);
        return SUCCESS;
    }


    private List<Course> getCoursesListFromFile(){
        String courseJsonStringFromFile = FileUtils.readFromFile(FileUrls.courseUrl);
        List<Course> courseList;
        try {
            courseList = Json.objectMapper.readValue(courseJsonStringFromFile, CollectionsTypeFactory.listOf(Course.class));
        } catch (Exception e) {
            System.out.println(e);
            courseList = new ArrayList<>();
        }
        return courseList;
    }

    @SneakyThrows
    private void setCoursesListToFile(List<Course> courseList) {
        String newCourseJsonFromObject = Json.prettyPrint(courseList);
        FileUtils.writeToFile(FileUrls.courseUrl, newCourseJsonFromObject);
    }
}
