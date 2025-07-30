package kc.mapping.training;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import kc.model.training.Course;
import kc.model.training.Student;
import kc.model.training.Teacher;
import kc.enums.training.AccountStatus;
import kc.enums.training.CourseStatus;
import kc.dto.training.CourseDTO;
import kc.dto.training.StudentDTO;
import kc.dto.training.TeacherDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CourseMapping {
	@Mappings({ @Mapping(source = "status", target = "status") })
	Course toCourse(CourseDTO source);
	@Mappings({ @Mapping(target = "editMode", ignore = true),
		@Mapping(source = "status", target = "status") })
	CourseDTO fromCourse(Course source);
	
	List<CourseDTO> toCourseDtoList(List<Course> source);
	

	@Mappings({ @Mapping(target = "courses", ignore = true),
		@Mapping(source = "status", target = "status") })
	Teacher toTeacher(TeacherDTO source);
	@Mappings({ @Mapping(target = "courses", ignore = true),
		@Mapping(source = "status", target = "status") })
	TeacherDTO fromTeacher(Teacher source);

	List<TeacherDTO> toTeacherDtoList(List<Teacher> source);

	@Mappings({ @Mapping(target = "courses", ignore = true),
		@Mapping(source = "status", target = "status") })
	Student toStudent(StudentDTO source);
	@Mappings({ @Mapping(target = "courses", ignore = true),
		@Mapping(source = "status", target = "status") })
	StudentDTO fromStudent(Student source);

	List<StudentDTO> toStudentDtoList(List<Student> source);

	

	default CourseStatus toCourseStatus(int status) {
		return CourseStatus.valueOf(status);
	}
	default int fromCourseStatus(CourseStatus status) {
		return status.getIndex();
	}

	default AccountStatus toAccountStatus(int status) {
		return AccountStatus.valueOf(status);
	}
	default int fromAccountStatus(AccountStatus status) {
		return status.getIndex();
	}
	
}
