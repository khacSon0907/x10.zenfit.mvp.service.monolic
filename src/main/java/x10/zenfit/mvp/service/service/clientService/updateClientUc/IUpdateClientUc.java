package x10.zenfit.mvp.service.service.clientService.updateClientUc;

import x10.zenfit.mvp.service.service.clientService.ClientResponse;

public interface IUpdateClientUc {

    ClientResponse updateClient(String id, UpdateClientRequest request);
}
