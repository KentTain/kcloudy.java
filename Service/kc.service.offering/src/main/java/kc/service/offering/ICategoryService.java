package kc.service.offering;

import kc.dto.PaginatedBaseDTO;
import kc.dto.account.UserSimpleDTO;
import kc.dto.offering.*;
import kc.enums.offering.OfferingPriceType;
import kc.enums.offering.OfferingPropertyType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryService {
    /*------------------------------------------商品类别-----------------------------------------------*/
    List<CategoryDTO> findAllByPriceTypeAndPropertyTypes(OfferingPriceType priceType, List<OfferingPropertyType> propertyTypes);

    List<CategoryDTO> GetRootOfferingCategoriesByName(String name);

    CategoryDTO GetCategoryById(int id);

    boolean SaveCategory(CategoryDTO data);

    boolean RemoveCategory(int id);

    boolean ExistCategoryName(int id, String name);

    //https://blog.wuwii.com/jpa-specification.html
    /*------------------------------------------负责人设置-----------------------------------------------*/
    List<CategoryManagerDTO> findAllManagersByCategoryId(int categoryId);

    PaginatedBaseDTO<CategoryManagerDTO> findPaginatedManagersByCategoryId(int pageIndex, int pageSize, int categoryId);

    boolean RemoveSpecificationById(int specId);

    @Transactional
    boolean AddCategoryManagers(int categoryId, List<UserSimpleDTO> users, String userId, String userName);

    boolean RemoveCategoryManagerById(int managerId);

    /*------------------------------------------规格设置-----------------------------------------------*/
    List<OfferingSpecificationDTO> findAllSpecificationsByCategoryId(int categoryId);

    @Transactional
    boolean SaveSpecifications(int categoryId, List<OfferingSpecificationDTO> specs, String userId, String userName);

    PaginatedBaseDTO<PropertyProviderDTO> findPaginatedProvidersByCategoryId(int pageIndex, int pageSize, int categoryId);

    List<PropertyProviderDTO> findAllProvidersByCategoryId(int categoryId);
}
