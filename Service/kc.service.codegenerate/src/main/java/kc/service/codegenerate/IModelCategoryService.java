package kc.service.codegenerate;

import kc.dto.codegenerate.ModelCategoryDTO;
import kc.dto.codegenerate.ModelCategoryNameExistsDTO;
import kc.enums.codegenerate.ModelType;
import kc.service.base.IServiceBase;

import java.util.List;

public interface IModelCategoryService extends IServiceBase {
	List<ModelCategoryDTO> findRootModelCategoriesByName(String name);

	List<ModelCategoryDTO> findRootModelCategoriesByName(ModelType modelType, String name);

	List<ModelCategoryDTO> findByModelTypeAndApplicationId(String applicationId, ModelType modelType, String name);

	ModelCategoryDTO getModelCategoryById(int id);

	boolean saveModelCategory(ModelCategoryDTO data);

	boolean removeModelCategory(int id);

	boolean existsModelCategoryName(ModelCategoryNameExistsDTO search);
}
