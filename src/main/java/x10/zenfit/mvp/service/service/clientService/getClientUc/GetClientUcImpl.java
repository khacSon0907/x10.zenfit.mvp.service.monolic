package x10.zenfit.mvp.service.service.clientService.getClientUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;

@Component
@RequiredArgsConstructor
public class GetClientUcImpl implements IGetClientUc {

    private final IClientRepository clientRepository;

    @Override
    public ClientResponse getClientById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Client ID là bắt buộc");
        }

        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        return ClientResponseMapper.toResponse(client);
    }
}
