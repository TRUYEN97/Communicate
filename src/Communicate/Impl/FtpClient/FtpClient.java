/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Communicate.Impl.FtpClient;

import Communicate.FtpClient.FtpGetConnection;
import Communicate.AbsShowException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Administrator
 */
public class FtpClient extends AbsShowException {

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

    public static FtpClient getConnection(String host, int port, String user, String password) throws IOException {
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            if (ftpconncetor == null) {
                return null;
            }
            return new FtpClient(host, port, user, password);
        }
    }

    public boolean upStringToFTP(String data, String ftpFile) {
        if (data == null || ftpFile == null) {
            return false;
        }
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
        if (localFile == null || newFtpFile == null) {
            return false;
        }
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
        if (!checkFileFtpExists(FtpFile) || localFile == null) {
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
        if (oldName == null || newName == null) {
            return false;
        }
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

    public boolean makeFtpDirectory(String dir) {
        if (dir == null) {
            return false;
        }
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            return ftpClient.makeDirectory(dir);
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    public boolean checkFileFtpExists(String filePath) {
        if (filePath == null) {
            return false;
        }
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            try ( InputStream inputStream = ftpClient.retrieveFileStream(filePath)) {
                return (inputStream != null && ftpClient.getReplyCode() != 550);
            }
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    public boolean checkFtpDirectoryExists(String dirPath) {
        if (dirPath == null) {
            return false;
        }
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            ftpClient.changeWorkingDirectory(dirPath);
            return ftpClient.getReplyCode() != 550;
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

    public byte[] readByteFile(String ftpFolder, String ftpFileName) {
        if (ftpFolder == null || ftpFileName == null) {
            return null;
        }
        return readByteFile(String.format("%s/%s", ftpFolder, ftpFileName));
    }

    public byte[] readByteFile(String ftpFilepath) {
        ftpFilepath = checkPath(ftpFilepath);
        if (!checkFileFtpExists(ftpFilepath)) {
            return null;
        }
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            try ( InputStream in = ftpClient.retrieveFileStream(ftpFilepath)) {
                return in.readAllBytes();
            }
        } catch (IOException ex) {
            showException(ex);
            return null;
        }
    }

    public String readStringFile(String ftpFolder, String ftpFileName) {
        if (ftpFolder == null || ftpFileName == null) {
            return null;
        }
        return readStringFile(String.format("%s/%s", ftpFolder, ftpFileName));
    }

    public String readStringFile(String ftpFilepath) {
        if (ftpFilepath == null) {
            return null;
        }
        ftpFilepath = checkPath(ftpFilepath);
        if (!checkFileFtpExists(ftpFilepath)) {
            return null;
        }
        try ( FtpGetConnection ftpconncetor = FtpGetConnection.getConnection(host, port, user, password)) {
            FTPClient ftpClient = ftpconncetor.getFTPClient();
            StringBuilder builder = new StringBuilder();
            try ( InputStreamReader input = new InputStreamReader(ftpClient.retrieveFileStream(ftpFilepath))) {
                if (input == null) {
                    return null;
                }
                try ( BufferedReader buffer = new BufferedReader(input)) {
                    String line;
                    if ((line = buffer.readLine()) != null) {
                        builder.append(line);
                        while ((line = buffer.readLine()) != null) {
                            builder.append("\r\n").append(line);
                        }
                    }
                }
            }
            return builder.toString();
        } catch (IOException ex) {
            showException(ex);
            return null;
        }
    }

    private String checkPath(String ftpFilepath) {
        ftpFilepath = ftpFilepath.replaceAll("//", "/");
        ftpFilepath = ftpFilepath.replaceAll("\\\\/", "\\\\");
        ftpFilepath = ftpFilepath.replaceAll("\\\\\\\\", "\\\\");
        return ftpFilepath;
    }

}
