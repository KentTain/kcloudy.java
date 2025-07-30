package kc.service.portal;

import kc.dto.PaginatedBaseDTO;
import kc.dto.portal.*;
import kc.enums.portal.*;

import java.util.List;

public interface IOfferingService {

    /*------------------------------------------商品类别-----------------------------------------------*/
    List<OfferingCategoryDTO> GetRootOfferingCategoriesByName(String name);

    OfferingCategoryDTO GetCategoryById(int id);

    boolean SaveCategory(OfferingCategoryDTO data);

    boolean RemoveCategory(int id);

    boolean ExistCategoryName(int id, String name);

    /*------------------------------------------商品-----------------------------------------------*/
    PaginatedBaseDTO<OfferingDTO> findPaginatedOfferingsByFilter(
            int pageIndex, int pageSize, Integer categoryId, String code, String name, OfferingStatus status);

    OfferingDTO GetOfferingById(int id);

    @org.springframework.transaction.annotation.Transactional
    boolean RemoveOffering(int id, String currentUserId, String currentUserName);

    boolean PublishOffering(int id, boolean isAgree, String content, String currentUserId, String currentUserName);

    boolean ExistOfferingName(int id, String name);

    @org.springframework.transaction.annotation.Transactional
    boolean SaveOffering(OfferingDTO data, String currentUserId, String currentUserName);

    @org.springframework.transaction.annotation.Transactional
    boolean RemoveOfferingImageById(boolean isSingleOffering, int id);

    /*------------------------------------------商品日志-----------------------------------------------*/
    PaginatedBaseDTO<OfferingOperationLogDTO> findPaginatedOfferingLogsByFilter(
            int pageIndex, int pageSize, String code, String name);
}
