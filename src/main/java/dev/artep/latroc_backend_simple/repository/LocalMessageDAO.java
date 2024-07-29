package dev.artep.latroc_backend_simple.repository;

import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocalMessageDAO extends CrudRepository<Message, Long> {
    List<Message> findBySenderOrReceiver(LocalUser sender, LocalUser receiver);
}
