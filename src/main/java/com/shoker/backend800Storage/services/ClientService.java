package com.shoker.backend800Storage.services;


import com.shoker.backend800Storage.DTO.ClientDTO;
import com.shoker.backend800Storage.exeption.ResourceNotFoundException;
import com.shoker.backend800Storage.models.Client;
import com.shoker.backend800Storage.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ClientService {
    private final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientDTO> getAllClients() {
        logger.info("Fetching all clients");
        return clientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClientDTO createClient(ClientDTO clientDTO) {
        logger.info("Creating new client: {}", clientDTO.getName());
        Client client = convertToEntity(clientDTO);
        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + id));
        return convertToDTO(client);
    }

    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for this id :: " + id));

        client.setName(clientDTO.getName());
        client.setLastName(clientDTO.getLastName());
        client.setMobile(clientDTO.getMobile());

        logger.info("Updating client: {}", id);
        Client updatedClient = clientRepository.save(client);
        return convertToDTO(updatedClient);
    }

    private ClientDTO convertToDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());
        clientDTO.setLastName(client.getLastName());
        clientDTO.setMobile(client.getMobile());
        return clientDTO;
    }

    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        if (clientDTO.getId() != null) {
            client.setId(clientDTO.getId());
        }
        client.setName(clientDTO.getName());
        client.setLastName(clientDTO.getLastName());
        client.setMobile(clientDTO.getMobile());
        return client;
    }
}
