package com.mycompany.projetoa3.telas.gasto;



import com.mycompany.projetoa3.ConexaoDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GastoDAO {

    public boolean adicionarGasto(Gasto gasto) {
        String sql = "INSERT INTO gastos (data, descricao, valor, categoria_id, usuario_cpf) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(gasto.getData().getTime()));
            stmt.setString(2, gasto.getDescricao());
            stmt.setBigDecimal(3, gasto.getValor());
            stmt.setInt(4, gasto.getCategoriaId());
            stmt.setString(5, gasto.getUsuarioCpf());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar gasto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Gasto> buscarGastosPorUsuarioCpf(String usuarioCpf) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT g.id, g.data, g.descricao, g.valor, c.nome as nome_categoria, g.categoria_id, g.usuario_cpf " +
                     "FROM gastos g " +
                     "JOIN categorias c ON g.categoria_id = c.id " +
                     "WHERE g.usuario_cpf = ? " +
                     "ORDER BY g.data DESC, g.id DESC";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuarioCpf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Gasto gasto = new Gasto(
                        rs.getInt("id"),
                        rs.getDate("data"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("valor"),
                        rs.getString("nome_categoria"),
                        rs.getInt("categoria_id"),
                        rs.getString("usuario_cpf")
                );
                gastos.add(gasto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar gastos por usuário CPF: " + e.getMessage());
            e.printStackTrace();
        }
        return gastos;
    }

    // Opcional: métodos para atualizar e excluir gastos aqui, se precisar.
}
