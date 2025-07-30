package kc.mapping.training;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import kc.model.training.CourseRecord;
import kc.dto.training.CourseRecordDTO;
import kc.enums.training.CourseRecordStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface CourseRecordMapping {
	// CourseRecordMapping INSTANCE = Mappers.getMapper(CourseRecordMapping.class);

	@Mappings({ @Mapping(source = "status", target = "status")})
	CourseRecord toCourseRecord(CourseRecordDTO source);
	@Mappings({ @Mapping(source = "status", target = "status")})
	CourseRecordDTO fromCourseRecord(CourseRecord source);

	List<CourseRecordDTO> toCourseRecordDtoList(List<CourseRecord> source);

	default CourseRecordStatus toStatus(int Status) {
		return CourseRecordStatus.valueOf(Status);
	}
	default int fromStatus(CourseRecordStatus type) {
		return type.getIndex();
	}

}
