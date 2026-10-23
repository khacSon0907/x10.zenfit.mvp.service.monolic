package x10.zenfit.mvp.service.service.clientService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.clientService.createClientUc.CreateClientRequest;
import x10.zenfit.mvp.service.service.clientService.createClientUc.ICreateClientUc;
import x10.zenfit.mvp.service.service.clientService.deleteClientUc.IDeleteClientUc;
import x10.zenfit.mvp.service.service.clientService.getAllClientsUc.IGetAllClientsUc;
import x10.zenfit.mvp.service.service.clientService.getClientUc.IGetClientUc;
import x10.zenfit.mvp.service.service.clientService.updateClientUc.IUpdateClientUc;
import x10.zenfit.mvp.service.service.clientService.updateClientUc.UpdateClientRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreClientServiceImpl implements ICoreClientService {

    private final ICreateClientUc createClientUc;
    private final IGetClientUc getClientUc;
    private final IGetAllClientsUc getAllClientsUc;
    private final IUpdateClientUc updateClientUc;
    private final IDeleteClientUc deleteClientUc;

    @Override
    public ClientResponse createClient(CreateClientRequest request) {
        return createClientUc.createClient(request);
    }

    @Override
    public ClientResponse getClientById(String id) {
        return getClientUc.getClientById(id);
    }

    @Override
    public List<ClientResponse> getAllClients(String assignedTrainerId) {
        return getAllClientsUc.getAllClients(assignedTrainerId);
    }

    @Override
    public ClientResponse updateClient(String id, UpdateClientRequest request) {
        return updateClientUc.updateClient(id, request);
    }

    @Override
    public void deleteClient(String id) {
        deleteClientUc.deleteClient(id);
    }
}
