package br.com.sicredi.election.dto.candidate;

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
public class CandidateResponse {
    private Integer candidateId;
    private String name;
    private String cpf;
    private Integer number;
    private String party;
    private Integer votes;
}
