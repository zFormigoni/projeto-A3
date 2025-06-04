package com.mycompany.projetoa3.telas.gasto;

import com.mycompany.projetoa3.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GastoDAO {

    public static boolean inserirGasto(Gasto gasto) {
        // Adicionar eh_recorrente ao SQL INSERT
        String sql = "INSERT INTO tb_gastos (descricao, categoria_id, data, valor, cpf_usuario, eh_recorrente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gasto.getDescricao());
            stmt.setInt(2, gasto.getIdCategoria()); // Usar getIdCategoria
            stmt.setDate(3, new java.sql.Date(gasto.getDataGasto().getTime()));
            stmt.setDouble(4, gasto.getValor());
            stmt.setString(5, gasto.getCpfUsuario());
            stmt.setBoolean(6, gasto.isEhRecorrente()); // Salvar o novo campo
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir gasto: " + e.getMessage());
            e.printStackTrace(); // Ajuda a identificar o problema durante o desenvolvimento
            return false;
        }
    }

    public static boolean atualizarGasto(Gasto gasto) {
        // Adicionar eh_recorrente ao SQL UPDATE
        String sql = "UPDATE tb_gastos SET descricao = ?, valor = ?, data = ?, categoria_id = ?, eh_recorrente = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gasto.getDescricao());
            stmt.setDouble(2, gasto.getValor());
            stmt.setDate(3, new java.sql.Date(gasto.getDataGasto().getTime()));
            stmt.setInt(4, gasto.getIdCategoria()); // Usar getIdCategoria
            stmt.setBoolean(5, gasto.isEhRecorrente()); // Atualizar o novo campo
            stmt.setInt(6, gasto.getId());
            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar gasto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean excluirGasto(int id) {
        String sql = "DELETE FROM tb_gastos WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasExcluidas = stmt.executeUpdate();
            return linhasExcluidas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir gasto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lista todos os gastos do sistema, incluindo nome da categoria e se é recorrente
    public static List<Gasto> listarGastos() {
        List<Gasto> lista = new ArrayList<>();
        String sql = "SELECT g.id, g.descricao, g.categoria_id, c.nome AS nome_categoria, g.data, g.valor, g.cpf_usuario, g.eh_recorrente " +
                     "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id"));
                gasto.setDescricao(rs.getString("descricao"));
                gasto.setIdCategoria(rs.getInt("categoria_id")); // Usar setIdCategoria
                gasto.setNomeCategoria(rs.getString("nome_categoria"));
                gasto.setDataGasto(rs.getDate("data"));
                gasto.setValor(rs.getDouble("valor"));
                gasto.setCpfUsuario(rs.getString("cpf_usuario"));
                gasto.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                lista.add(gasto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar gastos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Busca um gasto por ID, incluindo nome da categoria e se é recorrente
    // Mantido com 'l' minúsculo ("Porld") para compatibilidade com TelaGastos.java do PDF,
    // embora a convenção Java seja "PorId".
    public static Gasto buscarGastoPorld(int id) {
        String sql = "SELECT g.id, g.descricao, g.valor, g.data, g.categoria_id, c.nome AS nome_categoria, g.cpf_usuario, g.eh_recorrente " +
                     "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id WHERE g.id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id"));
                gasto.setDescricao(rs.getString("descricao"));
                gasto.setValor(rs.getDouble("valor"));
                gasto.setDataGasto(rs.getDate("data"));
                gasto.setIdCategoria(rs.getInt("categoria_id")); // Usar setIdCategoria
                gasto.setNomeCategoria(rs.getString("nome_categoria"));
                gasto.setCpfUsuario(rs.getString("cpf_usuario"));
                gasto.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                return gasto;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar gasto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Lista gastos por usuário, incluindo nome da categoria e se é recorrente
    public static List<Gasto> listarGastosPorUsuario(String cpfUsuario) {
        List<Gasto> lista = new ArrayList<>();
        String sql = "SELECT g.id, g.descricao, g.categoria_id, c.nome AS nome_categoria, g.data, g.valor, g.eh_recorrente " +
                     "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id " +
                     "WHERE g.cpf_usuario = ? ORDER BY g.data DESC"; // Ordena por data mais recente
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpfUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Gasto gasto = new Gasto();
                    gasto.setId(rs.getInt("id"));
                    gasto.setDescricao(rs.getString("descricao"));
                    gasto.setIdCategoria(rs.getInt("categoria_id")); // Usar setIdCategoria
                    gasto.setNomeCategoria(rs.getString("nome_categoria"));
                    gasto.setDataGasto(rs.getDate("data"));
                    gasto.setValor(rs.getDouble("valor"));
                    gasto.setCpfUsuario(cpfUsuario); // CPF já é conhecido, mas pode ser pego do rs se a query mudar
                    gasto.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                    lista.add(gasto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar gastos por usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Lista gastos filtrados, incluindo nome da categoria e se é recorrente
    public static List<Gasto> listarGastosFiltrados(
            String cpfUsuario,
            String dataInicio, // Espera-se formato "YYYY-MM-DD"
            String dataFim,    // Espera-se formato "YYYY-MM-DD"
            Double valorMin,
            Double valorMax,
            String nomeCategoriaFiltro) {
        List<Gasto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT g.id, g.data, g.descricao, g.categoria_id, c.nome AS nome_categoria, g.valor, g.cpf_usuario, g.eh_recorrente " +
            "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id " +
            "WHERE g.cpf_usuario = ?"
        );
        List<Object> params = new ArrayList<>();
        params.add(cpfUsuario);

        if (dataInicio != null && !dataInicio.trim().isEmpty()) {
            sql.append(" AND g.data >= ?");
            params.add(java.sql.Date.valueOf(dataInicio));
        }
        if (dataFim != null && !dataFim.trim().isEmpty()) {
            sql.append(" AND g.data <= ?");
            params.add(java.sql.Date.valueOf(dataFim));
        }
        if (valorMin != null) {
            sql.append(" AND g.valor >= ?");
            params.add(valorMin);
        }
        if (valorMax != null) {
            sql.append(" AND g.valor <= ?");
            params.add(valorMax);
        }
        if (nomeCategoriaFiltro != null && !nomeCategoriaFiltro.trim().isEmpty()) {
            sql.append(" AND c.nome = ?");
            params.add(nomeCategoriaFiltro);
        }
        sql.append(" ORDER BY g.data DESC, g.valor DESC"); // Ordena por data mais recente e depois por valor

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id"));
                gasto.setDataGasto(rs.getDate("data"));
                gasto.setDescricao(rs.getString("descricao"));
                gasto.setIdCategoria(rs.getInt("categoria_id")); // Usar setIdCategoria
                gasto.setNomeCategoria(rs.getString("nome_categoria"));
                gasto.setValor(rs.getDouble("valor"));
                gasto.setCpfUsuario(rs.getString("cpf_usuario")); // Pega o CPF do resultado da query
                gasto.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                lista.add(gasto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar gastos filtrados: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
