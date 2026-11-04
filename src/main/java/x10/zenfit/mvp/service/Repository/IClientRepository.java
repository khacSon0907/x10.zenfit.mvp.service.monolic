package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;

import java.util.List;
import java.util.Optional;

public interface IClientRepository {

    ClientEntity save(ClientEntity client);

    Optional<ClientEntity> findById(String id);

    Optional<ClientEntity> findByEmail(String email);

    Optional<ClientEntity> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<ClientEntity> findAll();

    List<ClientEntity> findByAssignedTrainerId(String assignedTrainerId);

    List<ClientEntity> searchClients(String keyword, String trainerId);

    PaginatedResult<ClientEntity> findPtClientsWithPaging(
            String trainerId,
            String search,
            ClientStatus status,
            String sortBy,
            String sortDirection,
            int page,
            int size
    );

    List<ClientEntity> findByFullName(String fullName);

    void deleteById(String id);
}
