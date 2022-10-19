package br.com.candidate.repository;

import br.com.candidate.model.dto.CandidateLoggedDTO;
import br.com.candidate.model.dto.LoginDTO;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.candidate.repository.Repository.getConnection;

@NoArgsConstructor
public class UserRepository {
    public CandidateLoggedDTO login(LoginDTO loginDTO) {
        String sql = loginDTO.getIsCandidate() ? "SELECT * FROM T_SCC_CANDIDATO WHERE DS_EMAIL = ? AND DS_SENHA = ? "
                : "SELECT * FROM T_SCC_EMPRESA WHERE DS_EMAIL = ? AND DS_SENHA = ? ";


        String[] columnName = loginDTO.getIsCandidate()
                ? new String[]{"CD_CANDIDATO", "NM_COMPLETO"}
                : new String[]{"CD_EMPRESA", "NM_EMPRESA"};

        PreparedStatement ps;
        CandidateLoggedDTO candidateLoggedDTO = null;
        ResultSet rs;
        try {
            ps = getConnection().prepareCall(sql);
            ps.setString(1, loginDTO.getEmail());
            ps.setString(2, loginDTO.getSenha());
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    candidateLoggedDTO = new CandidateLoggedDTO(
                            rs.getInt(columnName[0]),
                            rs.getString(columnName[1]),
                            loginDTO.getIsCandidate()
                    );

                }
            }
            return candidateLoggedDTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
