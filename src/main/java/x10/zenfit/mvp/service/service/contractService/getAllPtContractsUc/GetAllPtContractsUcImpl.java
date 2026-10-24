package x10.zenfit.mvp.service.service.contractService.getAllPtContractsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;
import x10.zenfit.mvp.service.service.contractService.PtContractResponse;
import x10.zenfit.mvp.service.service.contractService.PtContractResponseMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAllPtContractsUcImpl implements IGetAllPtContractsUc {

    private final IPtContractRepository ptContractRepository;
    private final IClientRepository clientRepository;

    @Override
    public List<PtContractResponse> getAllPtContracts(String trainerId, String clientId, ContractStatus status) {
        List<PtContractEntity> contracts = ptContractRepository.findAll();

        // Áp dụng bộ lọc
        if (trainerId != null && !trainerId.isBlank()) {
            contracts = contracts.stream()
                    .filter(c -> Objects.equals(c.getTrainerId(), trainerId.trim()))
                    .toList();
        }

        if (clientId != null && !clientId.isBlank()) {
            contracts = contracts.stream()
                    .filter(c -> Objects.equals(c.getClientId(), clientId.trim()))
                    .toList();
        }

        if (status != null) {
            contracts = contracts.stream()
                    .filter(c -> c.getStatus() == status)
                    .toList();
        }

        // Lấy danh sách clientId cần nạp thông tin
        List<String> clientIds = contracts.stream()
                .map(PtContractEntity::getClientId)
                .filter(id -> id != null && !id.isBlank())
                .distinct()
                .toList();

        Map<String, ClientResponse> clientMap = new HashMap<>();
        for (String cId : clientIds) {
            clientRepository.findById(cId).ifPresent(client ->
                    clientMap.put(cId, ClientResponseMapper.toResponse(client))
            );
        }

        return contracts.stream()
                .map(contract -> PtContractResponseMapper.toResponse(contract, clientMap.get(contract.getClientId())))
                .toList();
    }
}
