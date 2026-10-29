package x10.zenfit.mvp.service.service.clientService.searchClientsUc;

import x10.zenfit.mvp.service.service.clientService.ClientResponse;

import java.util.List;

public interface ISearchClientsUc {

    List<ClientResponse> searchClients(String keyword, String assignedTrainerId);
}
