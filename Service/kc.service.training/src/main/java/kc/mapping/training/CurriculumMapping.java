package kc.mapping.training;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import kc.model.training.ClassRoom;
import kc.model.training.Course;
import kc.model.training.Curriculum;
import kc.dto.training.ClassRoomDTO;
import kc.dto.training.CourseDTO;
import kc.dto.training.CurriculumDTO;
import kc.enums.training.CourseStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CurriculumMapping {
	// CurriculumMapping INSTANCE = Mappers.getMapper(CurriculumMapping.class);

	Curriculum toCurriculum(CurriculumDTO source);
	@Mappings({ @Mapping(target = "editMode", ignore = true)})
	CurriculumDTO fromCurriculum(Curriculum source);

	List<CurriculumDTO> toCurriculumDtoList(List<Curriculum> source);

	
	@Mappings({ @Mapping(target = "curriculums", ignore = true)})
	ClassRoom toClassRoom(ClassRoomDTO source);
	@Mappings({ @Mapping(target = "curriculums", ignore = true),
		@Mapping(target = "editMode", ignore = true)})
	ClassRoomDTO fromClassRoom(ClassRoom source);

	List<ClassRoomDTO> toClassRoomDtoList(List<ClassRoom> source);

	
	@Mappings({ @Mapping(target = "curriculums", ignore = true),
		@Mapping(target = "books", ignore = true),
		@Mapping(target = "students", ignore = true),
		@Mapping(target = "teachers", ignore = true),
		@Mapping(source = "status", target = "status") })
	Course toCourse(CourseDTO source);
	@Mappings({ @Mapping(target = "curriculums", ignore = true),
		@Mapping(target = "books", ignore = true),
		@Mapping(target = "students", ignore = true),
		@Mapping(target = "teachers", ignore = true),
		@Mapping(source = "status", target = "status") })
	CourseDTO fromCourse(Course source);
	
	List<CourseDTO> toCourseDtoList(List<Course> source);
	

	default CourseStatus toCourseStatus(int status) {
		return CourseStatus.valueOf(status);
	}
	default int fromCourseStatus(CourseStatus status) {
		return status.getIndex();
	}
}
