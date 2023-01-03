/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Communicate.Impl.FtpClient;

import Communicate.FtpClient.FtpGetConnection;
import Communicate.AbsShowException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Administrator
 */
public class FtpClient extends AbsShowException{

    private final String host;
    private final int port;
    private final String user;
    private final String password;

    private FtpClient(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public FtpClient getConnection(String host, int port, String user, String password) {
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            if (ftpconncetor == null) {
                return null;
            }
            return new FtpClient(host, port, user, password);
        } catch (Exception e) {
            showException(e);
            return null;
        }
    }

    public boolean upStringToFTP(String data, String ftpFile) {
        File file = new File(ftpFile);
        makeFtpDirectory(file.getParent());
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            try ( OutputStream stream = ftpClient.storeFileStream(ftpFile)) {
                stream.write(data.getBytes());
                stream.flush();
                return true;
            }
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    public boolean uploadFile(String localFile, String hostDir, String newFileName) {
        if (newFileName == null || hostDir == null || hostDir.isBlank()) {
            return false;
        }
        return uploadFile(localFile, String.format("%s/%s", hostDir, newFileName));
    }

    public boolean uploadFile(String localFile, String newFtpFile) {
        File file = new File(newFtpFile);
        makeFtpDirectory(file.getParent());
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            try ( InputStream input = new FileInputStream(new File(localFile))) {
                return ftpClient.storeFile(newFtpFile, input);
            }
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    public boolean downloadFile(String FtpFile, String localFile) {
        if (!checkFileFtpExists(FtpFile)) {
            return false;
        }
        File file = new File(localFile);
        if (!isParentExists(file) && !makeParentFile(file)) {
            return false;
        }
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            try ( OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile))) {
                return ftpClient.retrieveFile(FtpFile, outputStream);
            }
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    public boolean renameFtpFile(String oldName, String newName) {
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            return ftpClient.rename(oldName, newName);
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    public boolean deleteFolder(String programFolder) {
        if (!checkFtpDirectoryExists(programFolder)) {
            return true;
        }
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            return ftpClient.deleteFile(programFolder);
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    private boolean makeFtpDirectory(String dir) {
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            return ftpClient.makeDirectory(dir);
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    private boolean checkFileFtpExists(String filePath) {
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            try ( InputStream inputStream = ftpClient.retrieveFileStream(filePath)) {
                return !(inputStream == null || ftpClient.getReplyCode() == 550);
            }
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    public boolean checkFtpDirectoryExists(String dirPath) {
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            ftpClient.changeWorkingDirectory(dirPath);
            if (ftpClient.getReplyCode() == 550) {
                return false;
            }
            return true;
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    private boolean makeParentFile(File file) {
        return file.getParentFile().mkdirs();
    }

    private boolean isParentExists(File file) {
        return file.exists() || file.getParentFile().exists();
    }
}
