package kc.mapping.training;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import kc.model.training.Curriculum;
import kc.model.training.ClassRoom;
import kc.dto.training.CurriculumDTO;
import kc.dto.training.ClassRoomDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface ClassRoomMapping {
	// ClassRoomMapping INSTANCE = Mappers.getMapper(ClassRoomMapping.class);

	ClassRoom toClassRoom(ClassRoomDTO source);
	@Mappings({ @Mapping(target = "editMode", ignore = true)})
	ClassRoomDTO fromClassRoom(ClassRoom source);

	List<ClassRoomDTO> toClassRoomDtoList(List<ClassRoom> source);

	
	@Mappings({ @Mapping(target = "classRoom", ignore = true), 
		@Mapping(target = "course", ignore = true)})
	Curriculum toCurriculum(CurriculumDTO source);
	@Mappings({ @Mapping(target = "classRoom", ignore = true), 
		@Mapping(target = "course", ignore = true),
		@Mapping(target = "editMode", ignore = true)})
	CurriculumDTO fromCurriculum(Curriculum source);

	List<CurriculumDTO> toCurriculumDtoList(List<Curriculum> source);

}
