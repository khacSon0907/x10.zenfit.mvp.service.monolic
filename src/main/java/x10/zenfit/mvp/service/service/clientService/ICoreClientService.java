package x10.zenfit.mvp.service.service.clientService;

import x10.zenfit.mvp.service.service.clientService.createClientUc.CreateClientRequest;
import x10.zenfit.mvp.service.service.clientService.updateClientUc.UpdateClientRequest;

import java.util.List;

public interface ICoreClientService {

    ClientResponse createClient(CreateClientRequest request);

    ClientResponse getClientById(String id);

    List<ClientResponse> getAllClients(String assignedTrainerId);

    ClientResponse updateClient(String id, UpdateClientRequest request);

    void deleteClient(String id);
}
