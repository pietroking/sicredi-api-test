package br.com.sicredi.election.dto.vote;

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
public class VoteResponse {
    private Integer voteId;
    private Integer number;
    private Integer sessionId;
}
