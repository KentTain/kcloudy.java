package kc.service.training;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import kc.dataaccess.training.ITeacherRepository;
import kc.dto.PaginatedBaseDTO;
import kc.dto.training.TeacherDTO;
import kc.enums.training.AccountStatus;
import kc.model.training.Teacher;
import kc.framework.extension.StringExtensions;
import kc.mapping.training.TeacherMapping;

@Service
public class TeacherService implements ITeacherService {

	@Autowired
	private ITeacherRepository _teacherRepository;

	@Autowired
	private TeacherMapping _teacherMapping;

	@Override
	public List<TeacherDTO> findAll() {
		List<Teacher> data = _teacherRepository.findAll();
		return _teacherMapping.toTeacherDtoList(data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PaginatedBaseDTO<TeacherDTO> findPaginatedTeachersByNameAndType(int pageIndex, int pageSize,
			String name, AccountStatus status) {

		@SuppressWarnings("rawtypes")
		Specification querySpec = new Specification() {
			private static final long serialVersionUID = -2465449533413817728L;

			@Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
            	List<Predicate> predicates = new ArrayList<>();
                if(null != status){
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if(!StringExtensions.isNullOrEmpty(name)){
                    predicates.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        Page<Teacher> data = _teacherRepository.findAll(querySpec, pageable);
        
		int total = data.getSize();
		List<TeacherDTO> rows = _teacherMapping.toTeacherDtoList(data.getContent());
		return new PaginatedBaseDTO<TeacherDTO>(pageIndex, pageSize, total, rows);
	}

	@Override
	public TeacherDTO GetTeacherById(int id) {
		Teacher data = _teacherRepository.findByTeacherId(id);
		return _teacherMapping.fromTeacher(data);
	}

	@Override
	public boolean SaveTeacher(TeacherDTO model) {
		Teacher data = _teacherMapping.toTeacher(model);
		return _teacherRepository.saveAndFlush(data) != null;
	}

	@Override
	public boolean SoftRemoveTeacherById(int id) {
		Teacher data = _teacherRepository.findByTeacherId(id);
		data.setDeleted(true);
		return _teacherRepository.saveAndFlush(data) != null;
	}

}
