package x10.zenfit.mvp.service.service.clientService.deleteClientUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;

@Component
@RequiredArgsConstructor
public class DeleteClientUcImpl implements IDeleteClientUc {

    private final IClientRepository clientRepository;

    @Override
    public void deleteClient(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Client ID là bắt buộc");
        }

        clientRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        clientRepository.deleteById(id);
    }
}
