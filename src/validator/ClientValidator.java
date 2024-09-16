package validator;

import domain.entities.Client;
import exceptions.InvalidClientException;

import java.util.UUID;

public class ClientValidator {

    public void validate(Client client) throws InvalidClientException {
        validateName(client.getName());
        validateAddress(client.getAddress());
        validatePhone(client.getPhone());
        validateIsProfessional(client.isProfessional());
        if (client.getId() != null) {
            validateId(client.getId());
        }
    }

    public void validateName(String name) throws InvalidClientException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidClientException("Le nom du client est obligatoire.");
        }
        if (name.length() < 3) {
            throw new InvalidClientException("Le nom du client doit contenir au moins 3 caractères.");
        }
        if (name.length() > 100) {
            throw new InvalidClientException("Le nom du client ne peut pas dépasser 100 caractères.");
        }
    }

    private void validateAddress(String address) throws InvalidClientException {
        if (address == null || address.trim().isEmpty()) {
            throw new InvalidClientException("L'adresse du client est obligatoire.");
        }
        if (address.length() > 255) {
            throw new InvalidClientException("L'adresse ne peut pas dépasser 255 caractères.");
        }
    }

    private void validatePhone(String phone) throws InvalidClientException {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new InvalidClientException("Le numéro de téléphone doit être composé de 10 chiffres.");
        }
    }

    private void validateIsProfessional(boolean isProfessional) throws InvalidClientException {
        if (!isProfessional && !isProfessional) {
            throw new InvalidClientException("Le champ 'professionnel' doit être spécifié.");
        }
    }

    public void validateId(UUID id) throws InvalidClientException {
        if (id == null) {
            throw new InvalidClientException("L'ID du client est invalide.");
        }
        try {
            UUID.fromString(id.toString());
        } catch (IllegalArgumentException e) {
            throw new InvalidClientException("Le format de l'ID est invalide.");
        }
    }
}
