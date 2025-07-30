package kc.service.offering;

import kc.dto.PaginatedBaseDTO;
import kc.enums.offering.*;
import kc.dto.offering.*;

import java.util.List;

public interface IOfferingService {

    /*------------------------------------------商品-----------------------------------------------*/
    List<OfferingDTO> findAllOfferingsByIds(List<Integer> ids);

    PaginatedBaseDTO<OfferingDTO> findPaginatedOfferingsByType(int pageIndex, int pageSize, OfferingType offeringType);

    PaginatedBaseDTO<OfferingDTO> findPaginatedOfferingsByFilter(
            int pageIndex, int pageSize, String code, String name, OfferingStatus status);

    OfferingDTO GetOfferingById(int id);

    @org.springframework.transaction.annotation.Transactional
    boolean RemoveOffering(int id, String currentUserId, String currentUserName);

    boolean PublishOffering(int id, boolean isAgree, String content, String currentUserId, String currentUserName);

    boolean ExistOfferingName(int id, String name);

    @org.springframework.transaction.annotation.Transactional
    boolean SaveOffering(OfferingDTO data, String currentUserId, String currentUserName);

    @org.springframework.transaction.annotation.Transactional
    boolean RemoveProductById(int id, String currentUserId, String currentUserName);

    @org.springframework.transaction.annotation.Transactional
    boolean RemoveOfferingImageById(boolean isSingleOffering, int id);

    /*------------------------------------------商品日志-----------------------------------------------*/
    PaginatedBaseDTO<OfferingOperationLogDTO> findPaginatedOfferingLogsByFilter(
            int pageIndex, int pageSize, String code, String name);
}
