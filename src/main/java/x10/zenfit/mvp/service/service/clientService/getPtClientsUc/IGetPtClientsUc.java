package x10.zenfit.mvp.service.service.clientService.getPtClientsUc;

public interface IGetPtClientsUc {

    PaginatedClientResponse execute(GetPtClientsQuery query);
}

