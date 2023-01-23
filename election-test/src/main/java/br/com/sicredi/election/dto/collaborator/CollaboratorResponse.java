package br.com.sicredi.election.dto.collaborator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties
public class CollaboratorResponse {
    private Long collaboratorId;
    private String name;
    private Long sessionId;
    private String cpf;
}
