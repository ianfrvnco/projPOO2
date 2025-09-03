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
//import model.Usuario;
//import utils.Utils;

public class UsuarioController {
    
    public boolean autenticar(String usuario, String senha){
        
        //Montar o comando a ser executado
        //os ? são variáveis que são preenchidas mais adiante
        String sql= "SELECT * from TBUSUARIO WHERE email = ? and senha = ? and ativo = 1";
    
        //Cria uma instância do gerenciador de conexão
        //Conexão com o banco de dados
        GerenciadorConexao gerenciador = new GerenciadorConexao();
        
        //Declara as variáveis com nulas antes do try
        //para poder usar no finally
        PreparedStatement comando = null;
        ResultSet resultado= null;

        try{
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
         if (resultado.next()){
             //Se conseguyiu acançar para a prox linha é porquê achou 
             return true;
         }
        } catch (SQLException e){
            
        //caso ocorra um erro relacio0nado ao banco de dados
        //exibe popup com o erro
        JOptionPane.showMessageDialog(null, e.getMessage());
         
        } finally {
            //depois de executar o try, dando erro ou não executa o finally
            gerenciador.fecharConexao(comando, resultado);
        }
        
        return false;
    
    }
}
