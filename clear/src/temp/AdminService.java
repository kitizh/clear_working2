package com.restaurant.services;

import com.restaurant.entities.AccessType;
import com.restaurant.entities.UserAccess;
import com.restaurant.repositories.AccessTypeRepository;
import com.restaurant.repositories.UserAccessRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AccessTypeRepository accessTypeRepository;
    private final UserAccessRepository userAccessRepository;

    public AdminService(UserRepository userRepository, AccessTypeRepository accessTypeRepository, UserAccessRepository userAccessRepository) {
        this.userRepository = userRepository;
        this.accessTypeRepository = accessTypeRepository;
        this.userAccessRepository = userAccessRepository;
    }

    public Optional<String[]> getTabsListForUser(User userId) {
        Optional<UserAccess> userAccess = userAccessRepository.findByUser(userId); // Ensure type matches repository
        if (userAccess.isPresent()) {
            AccessType accessId = userAccess.get().getAccessType();
            Optional<AccessType> accessType = accessTypeRepository.findByAccessId(accessId);
            return accessType.map(AccessType::getTabsList); // Ensure AccessType uses String[]
        }
        return Optional.empty();
    }

}
