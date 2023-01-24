package br.com.sicredi.election.dto.session;

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
public class SessionRequest {
    private Integer idZone;
    private Integer number;
    private Integer urnNumber;
}
