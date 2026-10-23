package x10.zenfit.mvp.service.service.clientService.getAllClientsUc;

import x10.zenfit.mvp.service.service.clientService.ClientResponse;

import java.util.List;

public interface IGetAllClientsUc {

    List<ClientResponse> getAllClients(String assignedTrainerId);
}
