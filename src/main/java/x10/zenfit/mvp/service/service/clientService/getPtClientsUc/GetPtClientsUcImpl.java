package x10.zenfit.mvp.service.service.clientService.getPtClientsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.CommonError;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPtClientsUcImpl implements IGetPtClientsUc {

    private final IClientRepository clientRepository;

    @Override
    public PaginatedClientResponse execute(GetPtClientsQuery query) {
        if (query == null) {
            query = GetPtClientsQuery.builder().build();
        }

        if (query.getTrainerId() == null || query.getTrainerId().isBlank()) {
            throw new BusinessException(CommonError.UNAUTHORIZED, "Không xác định được danh tính huấn luyện viên");
        }

        int validPage = Math.max(query.getPage(), 1);
        int fixedSize = 8; // Business requirement: fixed 8 items per page

        String search = (query.getSearch() != null && !query.getSearch().isBlank())
                ? query.getSearch().trim()
                : null;

        String sortBy = ("fullName".equalsIgnoreCase(query.getSortBy()))
                ? "fullName"
                : "createdAt";

        String sortDirection = ("ASC".equalsIgnoreCase(query.getSortDirection()))
                ? "ASC"
                : "DESC";

        PaginatedResult<ClientEntity> result = clientRepository.findPtClientsWithPaging(
                query.getTrainerId().trim(),
                search,
                query.getStatus(),
                sortBy,
                sortDirection,
                validPage,
                fixedSize
        );

        List<ClientResponse> responseItems = result.getItems()
                .stream()
                .map(ClientResponseMapper::toResponse)
                .toList();

        return PaginatedClientResponse.builder()
                .items(responseItems)
                .page(result.getPage())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .hasNext(result.isHasNext())
                .hasPrevious(result.isHasPrevious())
                .build();
    }
}

