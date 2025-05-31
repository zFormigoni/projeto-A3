package com.mycompany.projetoa3;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;



public class TelaCadastrar extends JFrame {
   public static ResultadoValidacao validarDados(String cpf, String nome, String telefone, String email, String senha, String confirmarSenha) {
    if (cpf.isEmpty() || nome.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty()) {
        return new ResultadoValidacao(null, "Insira todos os dados");
    }

    if (!senha.equals(confirmarSenha)) {
        return new ResultadoValidacao(null, "As senhas não coincidem!");
    }

    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
        return new ResultadoValidacao(null, "Email inválido!");
    }

    if (!ConexaoDB.consultarEmail(email)) {
        return new ResultadoValidacao(null, "Email já cadastrado!");
    }

    Usuario usuario = new Usuario();
    usuario.setCpf(cpf);
    usuario.setNome(nome);
    usuario.setTelefone(telefone);
    usuario.setEmail(email);

    return new ResultadoValidacao(usuario, null);
}
    
    public TelaCadastrar() {
        setTitle("Criar Conta");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/images/pig_logo.png")).getImage());

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        MaskFormatter cpfMask = null;
        MaskFormatter telefoneMask = null;
        
        try {
            cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_');

        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        //JTextField cpfField = new JTextField(15);
        JFormattedTextField cpfField = new JFormattedTextField(cpfMask);
        cpfField.setColumns(14);

        JTextField nomeField = new JTextField(15);
        
        //JTextField telefoneField = new JTextField(15);
        JFormattedTextField telefoneField = new JFormattedTextField(telefoneMask);
        cpfField.setColumns(15);
        
        JTextField emailField = new JTextField(15);
        JPasswordField senhaField = new JPasswordField(15);
        JPasswordField confirmarSenhaField = new JPasswordField(15);
        JButton btnConfirmar = new JButton("CONFIRMAR");

        painelPrincipal.add(criarCampo("CPF:", cpfField));
        painelPrincipal.add(criarCampo("Nome:", nomeField));
        painelPrincipal.add(criarCampo("Telefone:", telefoneField));
        painelPrincipal.add(criarCampo("Email:", emailField));
        painelPrincipal.add(criarCampo("Senha:", senhaField));
        painelPrincipal.add(criarCampo("Confirmar Senha:", confirmarSenhaField));

        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        painelPrincipal.add(btnConfirmar);

        add(painelPrincipal);

        btnConfirmar.addActionListener(e -> {
            String cpf = cpfField.getText().trim().replaceAll("[^\\d]", "");
            String nome = nomeField.getText().trim();
            String telefone = telefoneField.getText().trim().replaceAll("[^\\d]", "");
            String email = emailField.getText().trim();
            String senha = new String(senhaField.getPassword());
            String confirmarSenha = new String(confirmarSenhaField.getPassword());

            
            
            
            ResultadoValidacao resultado = validarDados(cpf, nome, telefone, email, senha, confirmarSenha);
            
            if (resultado.mensagemErro != null){
                JOptionPane.showMessageDialog(null, resultado.mensagemErro);
                return;
            }
            
            Usuario usuario = resultado.usuario;
            
            //boolean sucesso = ConexaoDB.inserirUsuario(cpf, nome, telefone, email, senha);
            
            boolean sucesso = ConexaoDB.inserirUsuario2(usuario, senha);
            
            if (sucesso) {
                
                SessaoUsuario.setNomeUsuario(nome);
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso!");
                dispose();
            new Navegacao("Resumo", cpf, "usuario").setVisible(true); // <--- TIPO ADICIONADO
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar conta. Verifique os dados.");
            }
        });
    }

    private JPanel criarCampo(String labelTexto, JComponent field) {
        JPanel painelCampo = new JPanel();
        painelCampo.setLayout(new BoxLayout(painelCampo, BoxLayout.Y_AXIS));
        painelCampo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelTexto);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelCampo.add(label);
        painelCampo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelCampo.add(field);
        painelCampo.add(Box.createRigidArea(new Dimension(0, 10)));

        return painelCampo;
    }
}
