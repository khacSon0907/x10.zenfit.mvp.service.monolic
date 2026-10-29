package x10.zenfit.mvp.service.service.clientService.searchClientsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchClientsUcImpl implements ISearchClientsUc {

    private final IClientRepository clientRepository;

    @Override
    public List<ClientResponse> searchClients(String keyword, String assignedTrainerId) {
        List<ClientEntity> matched = clientRepository.searchClients(keyword, assignedTrainerId);
        return matched.stream()
                .map(ClientResponseMapper::toResponse)
                .toList();
    }
}
