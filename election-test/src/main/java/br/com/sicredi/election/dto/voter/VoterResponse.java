package br.com.sicredi.election.dto.voter;

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
public class VoterResponse {
    private Integer voterId;
    private String name;
    private Integer sessionId;
    private String cpf;
    private Boolean statusVote;
}
