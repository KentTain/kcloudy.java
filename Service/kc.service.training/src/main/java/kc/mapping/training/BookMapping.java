package kc.mapping.training;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import kc.model.training.Course;
import kc.model.training.Book;
import kc.dto.training.CourseDTO;
import kc.dto.training.BookDTO;
import kc.enums.training.CourseStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface BookMapping {
	// BookMapping INSTANCE = Mappers.getMapper(BookMapping.class);

	Book toBook(BookDTO source);
	@Mappings({ @Mapping(target = "editMode", ignore = true)})
	BookDTO fromBook(Book source);

	List<BookDTO> toBookDtoList(List<Book> source);

	
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
