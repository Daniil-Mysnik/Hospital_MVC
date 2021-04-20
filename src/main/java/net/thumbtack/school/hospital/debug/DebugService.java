package net.thumbtack.school.hospital.debug;

import net.thumbtack.school.hospital.debug.dao.DebugDAO;
import net.thumbtack.school.hospital.dto.response.EmptyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugService {
    private final DebugDAO debugDAO;

    @Autowired
    public DebugService(DebugDAO debugDAO) {
        this.debugDAO = debugDAO;
    }

    public EmptyResponse clearDB() {
        debugDAO.clear();
        return new EmptyResponse();
    }

}
