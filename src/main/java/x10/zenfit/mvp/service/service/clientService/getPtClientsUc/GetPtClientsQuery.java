package x10.zenfit.mvp.service.service.clientService.getPtClientsUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPtClientsQuery {

    private String trainerId;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 8;

    private String search;

    private ClientStatus status;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";
}

