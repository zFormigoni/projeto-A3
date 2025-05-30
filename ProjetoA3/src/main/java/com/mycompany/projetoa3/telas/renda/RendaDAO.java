package com.mycompany.projetoa3.telas.renda;

import com.mycompany.projetoa3.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendaDAO {

    public boolean adicionarRenda(Renda renda) {
        String sql = "INSERT INTO rendas (data, descricao, valor, categoria_id, usuario_cpf) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(renda.getData().getTime()));
            stmt.setString(2, renda.getDescricao());
            stmt.setBigDecimal(3, renda.getValor());
            stmt.setInt(4, renda.getCategoriaId());
            stmt.setString(5, renda.getUsuarioCpf());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar renda: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Renda> buscarRendasPorUsuarioCpf(String usuarioCpf) {
        List<Renda> rendas = new ArrayList<>();
        String sql = "SELECT r.id, r.data, r.descricao, r.valor, c.nome as nome_categoria, r.categoria_id, r.usuario_cpf " +
                     "FROM rendas r " +
                     "JOIN categorias c ON r.categoria_id = c.id " +
                     "WHERE r.usuario_cpf = ? " +
                     "ORDER BY r.data DESC, r.id DESC";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuarioCpf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Renda renda = new Renda(
                        rs.getInt("id"),
                        rs.getDate("data"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("valor"),
                        rs.getString("nome_categoria"),
                        rs.getInt("categoria_id"),
                        rs.getString("usuario_cpf")
                );
                rendas.add(renda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar rendas por usuário CPF: " + e.getMessage());
            e.printStackTrace();
        }
        return rendas;
    }

    // Opcional: métodos para atualizar e excluir rendas aqui, se precisar.
}
