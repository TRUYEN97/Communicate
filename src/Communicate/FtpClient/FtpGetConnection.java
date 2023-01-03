/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Communicate.FtpClient;

import java.io.Closeable;
import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Administrator
 */
public class FtpGetConnection implements Closeable {

    private final FTPClient ftpClient;

    private FtpGetConnection() {
        this.ftpClient = new FTPClient();
    }

    public static FtpGetConnection getConnection(String host, int port, String user, String password) throws IOException {
        FtpGetConnection connection = new FtpGetConnection();
        if (connection.connect(host, port) && connection.login(user, host)) {
            return connection;
        }
        return null;
    }

    public FTPClient getFTPClient() {
        return ftpClient;
    }

    protected boolean connect(String host, int port) throws IOException {
        if (host == null || port < 0) {
            return false;
        }
        ftpClient.connect(host, port);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            return false;
        }
        return true;
    }

    protected boolean login(String user, String pass) throws IOException {
        if (user == null || pass == null) {
            return false;
        }
        boolean sucess = ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return sucess && ftpClient.isConnected();
    }

    @Override
    public void close() throws IOException {
        if (this.ftpClient != null) {
            this.ftpClient.disconnect();
        }
    }
}
