/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Communicate.FtpClient;

import Communicate.IConnect;
import Communicate.ILogin;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Administrator
 */
public class FtpClient implements IConnect, ILogin {

    private final FTPClient ftpClient;
    private String host;
    private int port;
    private String user;
    private String pass;

    public FtpClient() {
        this.ftpClient = new FTPClient();
    }

    @Override
    public boolean connect(String host, int port) {
        if (host == null || port < 0) {
            return false;
        }
        try {
            if (isConnect() && !disConnect()) {
                return false;
            }
            this.ftpClient.connect(host, port);
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                System.err.println("Exception in connecting to FTP Server. Reply: " + reply);
                return false;
            }
            this.host = host;
            this.port = port;
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean login(String user, String pass) {
        if (user == null || pass == null) {
            return false;
        }
        try {
            boolean sucess = this.ftpClient.login(user, pass);
            this.ftpClient.enterLocalPassiveMode();
            this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            this.user = user;
            this.pass = pass;
            return sucess;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isConnect() {
        return this.ftpClient != null && this.ftpClient.isConnected();
    }

    public boolean appendFtpFile(String data, String ftpFile) {
        if (!isConnect() && reConnect()) {
            return false;
        }
        File file = new File(ftpFile);
        if (!checkFtpDirectoryExists(file.getParent())) {
            makeFtpDirectory(file.getParent());
        }
        try ( OutputStream stream = this.ftpClient.appendFileStream(ftpFile)) {
            stream.write(data.getBytes());
            stream.flush();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            resetConnect();
        }

    }

    public boolean upStringToFTP(String ftpFile, String data) {
        if (!isConnect()) {
            return false;
        }
        File file = new File(ftpFile);
        if (!checkFtpDirectoryExists(file.getParent())) {
            makeFtpDirectory(file.getParent());
        }
        try ( OutputStream stream = this.ftpClient.storeFileStream(ftpFile)) {
            stream.write(data.getBytes());
            stream.flush();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            resetConnect();
        }
    }

    public boolean uploadFile(String localFile, String hostDir, String newFileName) {
        if (newFileName == null || hostDir == null || hostDir.isBlank()) {
            return false;
        }
        return uploadFile(localFile, String.format("%s/%s", hostDir, newFileName));
    }

    public boolean uploadFile(String localFile, String newFtpFile) {
        if (!isConnect()) {
            return false;
        }
        File file = new File(newFtpFile);
        if (!checkFtpDirectoryExists(file.getParent())) {
            makeFtpDirectory(file.getParent());
        }
        try ( InputStream input = new FileInputStream(new File(localFile))) {
            return this.ftpClient.storeFile(newFtpFile, input);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean downloadFile(String FtpFile, String localFile) {
        if (!isConnect()) {
            return false;
        }
        if (!checkFileFtpExists(FtpFile)) {
            return false;
        }
        File file = new File(localFile);
        if (!isParentExists(file) && !makeParentFile(file)) {
            return false;
        }
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
            return this.ftpClient.retrieveFile(FtpFile, outputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean renameFtpFile(String oldName, String newName) {
        try {
            if (!isConnect() && reConnect()) {
                return false;
            }
            return this.ftpClient.rename(oldName, newName);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean disConnect() {
        try {
            if (this.ftpClient != null && this.ftpClient.isConnected() && this.ftpClient.logout()) {
                this.ftpClient.disconnect();
                return true;
            }
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean makeFtpDirectory(String dir) {
        try {
            return this.ftpClient.makeDirectory(dir);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean checkFileFtpExists(String filePath) {
        if (!isConnect()) {
            return false;
        }
        InputStream inputStream = null;
        try {
            inputStream = ftpClient.retrieveFileStream(filePath);
            return !(inputStream == null || ftpClient.getReplyCode() == 550);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            resetConnect();
        }
    }

    private boolean resetConnect() {
        if (isConnect()) {
            if (!disConnect()) {
                return false;
            }
        }
        return reConnect();
    }

    private boolean reConnect() {
        return connect(host, port) && login(user, pass);
    }

    private boolean checkFtpDirectoryExists(String dirPath) {
        try {
            ftpClient.changeWorkingDirectory(dirPath);
            if (ftpClient.getReplyCode() == 550) {
                return false;
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            resetConnect();
        }
    }

    private boolean makeParentFile(File file) {
        return file.getParentFile().mkdirs();
    }

    private boolean isParentExists(File file) {
        return file.exists() || file.getParentFile().exists();
    }
}
