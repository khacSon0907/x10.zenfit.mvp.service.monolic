package x10.zenfit.mvp.service.service.clientService.getAllClientsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllClientsUcImpl implements IGetAllClientsUc {

    private final IClientRepository clientRepository;

    @Override
    public List<ClientResponse> getAllClients(String assignedTrainerId) {
        List<ClientEntity> clients;
        if (assignedTrainerId != null && !assignedTrainerId.isBlank()) {
            clients = clientRepository.findByAssignedTrainerId(assignedTrainerId.trim());
        } else {
            clients = clientRepository.findAll();
        }

        return clients.stream()
                .map(ClientResponseMapper::toResponse)
                .toList();
    }
}
