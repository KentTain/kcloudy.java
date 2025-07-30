package kc.service.training;

import java.util.List;

import kc.dto.PaginatedBaseDTO;
import kc.dto.training.TeacherDTO;
import kc.enums.training.AccountStatus;

public interface ITeacherService {
	List<TeacherDTO> findAll();
	PaginatedBaseDTO<TeacherDTO> findPaginatedTeachersByNameAndType(int pageIndex, int pageSize, String name, AccountStatus status);
	TeacherDTO GetTeacherById(int id);
	boolean SaveTeacher(TeacherDTO model);
	boolean SoftRemoveTeacherById(int id);
}
