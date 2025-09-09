/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Administrador
 */
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.Usuario;
import utils.Util;
//import utils.Utils;

public class UsuarioController {

    public boolean autenticar(String usuario, String senha) {

        //Montar o comando a ser executado
        //os ? são variáveis que são preenchidas mais adiante
        String sql = "SELECT * from TBUSUARIO WHERE email = ? and senha = ? and ativo = 1";

        //Cria uma instância do gerenciador de conexão
        //Conexão com o banco de dados
        GerenciadorConexao gerenciador = new GerenciadorConexao();

        //Declara as variáveis com nulas antes do try
        //para poder usar no finally
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {
            //prepara o sql, analisandoi o formato e as váriaveis
            comando = gerenciador.prepararComando(sql);

            //define o valor de cada variável (?) pela posição em que aparecem
            comando.setString(1, usuario);
            comando.setString(2, senha);

            //executa o comando e guarda o resultado da consulta
            //o resultado é semelhante a uma grade
            resultado = comando.executeQuery();

            //resultado.next() - tenta avançar para a próxima linha
            //caso consiga retorna true
            if (resultado.next()) {
                //Se conseguyiu acançar para a prox linha é porquê achou 
                return true;
            }
        } catch (SQLException e) {

            //caso ocorra um erro relacio0nado ao banco de dados
            //exibe popup com o erro
            JOptionPane.showMessageDialog(null, e.getMessage());

        } finally {
            //depois de executar o try, dando erro ou não executa o finally
            gerenciador.fecharConexao(comando, resultado);
        }

        return false;

    }

    public boolean inserir(Usuario usu) {

        //Montar o comando a ser executado
        //os ? são variáveis que são preenchidas mais adiante
        String sql = "INSERT INTO TBUSUARIO(nome, email, senha, datanasc, ativo, imagem) VALUES (?,?,?,?,?,?)";

        //Cria uma instância do gerenciador de conexão
        //Conexão com o banco de dados
        GerenciadorConexao gerenciador = new GerenciadorConexao();

        //Declara as variáveis com nulas antes do try
        //para poder usar no finally
        PreparedStatement comando = null;

        try {
            //prepara o sql, analisandoi o formato e as váriaveis
            comando = gerenciador.prepararComando(sql);
            byte[] iconBytes = Util.converterIconToBytes(usu.getImagem());

            //define o valor de cada variável (?) pela posição em que aparecem
            comando.setString(1, usu.getNome());
            comando.setString(2, usu.getEmail());
            comando.setString(3, usu.getSenha());
            comando.setDate(4, new java.sql.Date(usu.getDataNascimento().getTime()));
            comando.setBoolean(5, usu.isAtivo());
            comando.setBytes(6, iconBytes);

            //executa o comando e guarda o resultado da consulta
            //o resultado é semelhante a uma grade
            comando.executeUpdate();
            return true;

        } catch (SQLException e) {

            //caso ocorra um erro relacio0nado ao banco de dados
            //exibe popup com o erro
            JOptionPane.showMessageDialog(null, e.getMessage());

        } finally {
            //depois de executar o try, dando erro ou não executa o finally
            gerenciador.fecharConexao(comando);
        }

        return false;

    }

    public List<Usuario> consultar(int opcaoFiltro, String filtro) {

        //Montar o comando a ser executado
        //os ? são variáveis que são preenchidas mais adiante
        String sql = "";

        if (!filtro.equals("")) {
            if (opcaoFiltro == 0) {
                sql = "SELECT * from TBUSUARIO WHERE pkusuario =" + filtro;
            } else if (opcaoFiltro == 1) {
                sql = "SELECT * from TBUSUARIO WHERE nome LIKE '%" + filtro + "%'";
            } else if (opcaoFiltro == 2) {
                sql = "SELECT * from TBUSUARIO WHERE email LIKE '%" + filtro + "%' ";
            } else if (opcaoFiltro == 3) {
                if (filtro.equals("Ativo")) {
                    sql = "SELECT * from TBUSUARIO WHERE Ativo = 1";
                } else if (filtro.equals("Inativo")) {
                    sql = "SELECT * from TBUSUARIO WHERE Ativo = 0";
                }
            }
        } else {
            sql = "SELECT * from TBUSUARIO";
        }

        //Cria uma instância do gerenciador de conexão
        //Conexão com o banco de dados
        GerenciadorConexao gerenciador = new GerenciadorConexao();

        //Declara as variáveis com nulas antes do try
        //para poder usar no finally
        PreparedStatement comando = null;
        ResultSet resultado = null;

        //Crio a lista de usuários, vazia ainda
        List<Usuario> lista = new ArrayList<>();

        try {
            //prepara o sql, analisandoi o formato e as váriaveis
            comando = gerenciador.prepararComando(sql);

            //executa o comando e guarda o resultado da consulta
            //o resultado é semelhante a uma grade
            resultado = comando.executeQuery();

            //resultado.next() - tenta avançar para a próxima linha
            //caso consiga retorna true
            while (resultado.next()) {
                Usuario usu = new Usuario();

                usu.setPkusuario(resultado.getInt("pkusuario"));
                usu.setNome(resultado.getString("nome"));
                usu.setEmail(resultado.getString("email"));
                usu.setSenha(resultado.getString("senha"));
                usu.setDataNascimento(resultado.getDate("datanasc"));
                usu.setAtivo(resultado.getBoolean("ativo"));

                lista.add(usu);
            }
        } catch (SQLException e) {

            //caso ocorra um erro relacio0nado ao banco de dados
            //exibe popup com o erro
            JOptionPane.showMessageDialog(null, e.getMessage());

        } finally {
            //depois de executar o try, dando erro ou não executa o finally
            gerenciador.fecharConexao(comando, resultado);
        }

        return lista;

    }

    public boolean alterar(Usuario usu) {

        //Montar o comando a ser executado
        //os ? são variáveis que são preenchidas mais adiante
        String sql = "UPDATE TBUSUARIO SET nome = ?, email = ?, datanasc = ?, ativo = ?";

        if (usu.getSenha() != null) {
            sql += ", senha = ?";
        }

        sql += " WHERE pkUsuario = ?";

        //Cria uma instância do gerenciador de conexão
        //Conexão com o banco de dados
        GerenciadorConexao gerenciador = new GerenciadorConexao();

        //Declara as variáveis com nulas antes do try
        //para poder usar no finally
        PreparedStatement comando = null;

        try {
            //prepara o sql, analisandoi o formato e as váriaveis
            comando = gerenciador.prepararComando(sql);

            //define o valor de cada variável (?) pela posição em que aparecem
            comando.setString(1, usu.getNome());
            comando.setString(2, usu.getEmail());
            comando.setDate(3, new java.sql.Date(usu.getDataNascimento().getTime()));
            comando.setBoolean(4, usu.isAtivo());

            if (usu.getSenha() != null) {
                comando.setString(5, usu.getSenha());
                comando.setInt(6, usu.getPkusuario());
            } else{
                comando.setInt(5, usu.getPkusuario());
            }

            //executa o comando e guarda o resultado da consulta
            //o resultado é semelhante a uma grade
            comando.executeUpdate();
            return true;

        } catch (SQLException e) {

            //caso ocorra um erro relacio0nado ao banco de dados
            //exibe popup com o erro
            JOptionPane.showMessageDialog(null, e.getMessage());

        } finally {
            //depois de executar o try, dando erro ou não executa o finally
            gerenciador.fecharConexao(comando);
        }

        return false;

    }
    
    public boolean deletar(int pkUsuario) {

        //Montar o comando a ser executado
        //os ? são variáveis que são preenchidas mais adiante
        String sql = "DELETE FROM TBUSUARIO WHERE pkusuario=?";

        //Cria uma instância do gerenciador de conexão
        //Conexão com o banco de dados
        GerenciadorConexao gerenciador = new GerenciadorConexao();

        //Declara as variáveis com nulas antes do try
        //para poder usar no finally
        PreparedStatement comando = null;

        try {
            //prepara o sql, analisandoi o formato e as váriaveis
            comando = gerenciador.prepararComando(sql);

            //define o valor de cada variável (?) pela posição em que aparecem
            comando.setInt(1, pkUsuario);
 

            //executa o comando e guarda o resultado da consulta
            //o resultado é semelhante a uma grade
            comando.executeUpdate();
            return true;

        } catch (SQLException e) {

            //caso ocorra um erro relacio0nado ao banco de dados
            //exibe popup com o erro
            JOptionPane.showMessageDialog(null, e.getMessage());

        } finally {
            //depois de executar o try, dando erro ou não executa o finally
            gerenciador.fecharConexao(comando);
        }

        return false;

    }

}
