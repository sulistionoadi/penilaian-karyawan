/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan;

import absen.karyawan.service.AbsenServiceImpl;
import absen.karyawan.service.AbsenService;
import absen.karyawan.ui.LoginDialog;
import absen.karyawan.ui.MainFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author adi
 */
public class Main {

    private static MainFrame mainFrame;
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost/absen_karyawan?zeroDateTimeBehavior=convertToNull";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";
    private static AbsenService service;

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static AbsenService getService() {
        return service;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                initLogin();
            }
        });
    }
    
    public static void initLogin() {
        try {
            if (mainFrame == null) {
                mainFrame = new MainFrame();
            }
            mainFrame.isiMapUser();
            if(new LoginDialog().showLogin()){
                Class.forName(DB_DRIVER);
                Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                service = new AbsenServiceImpl(conn);

                mainFrame.setVisible(true);
                mainFrame.initSecurity();
                mainFrame.setVisible(true);
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(Main.getMainFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Main.getMainFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
