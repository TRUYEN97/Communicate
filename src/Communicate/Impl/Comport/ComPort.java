/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communicate.Impl.Comport;

import Communicate.IConnect;
import com.fazecast.jSerialComm.SerialPort;
import AbstractStream.SubClass.ReadStream;
import Communicate.AbsCommunicate;
import Communicate.IReadStream;
import Communicate.ISender;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

public class ComPort extends AbsCommunicate implements ISender, IReadStream, IConnect {

    private SerialPort serialPort;

    public ComPort() {
        input = new ReadStream();
    }

    @Override
    public synchronized boolean connect(String port, int baudrate) {
        for (String commPort : getCommPorts()) {
            if (commPort.equalsIgnoreCase(port)) {
                return openComm(port, baudrate);
            }
        }
        return false;
    }

    @Override
    public boolean isConnect() {
        return serialPort != null && serialPort.isOpen();
    }

    private boolean openComm(String port, int baudrate) {
        try {
            close();
            this.serialPort = SerialPort.getCommPort(port.toUpperCase());
            if (!serialPort.openPort() || !serialPort.isOpen() || !serialPort.setComPortParameters(
                    baudrate,
                    8, // data bits
                    SerialPort.ONE_STOP_BIT,
                    SerialPort.NO_PARITY)) {
                return false;
            }
            input.setReader(serialPort.getInputStream());
            out = new PrintStream(serialPort.getOutputStream());
            return true;
        } catch (IOException e) {
            showException(e);
            return false;
        }
    }

    public static Set<String> getCommPorts() {
        Set<String> returnValue = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports != null && ports.length > 0) {
            for (SerialPort port : ports) {
                returnValue.add(port.getSystemPortName());
            }
        }
        return returnValue;
    }

    @Override
    public void close() throws IOException {
        closePort();
        closeOutput();
        closeInput();
    }

    private void closePort() {
        if (isConnect()) {
            serialPort.closePort();
        }
    }

    private void closeOutput() throws IOException {
        if (out != null) {
            out.close();
        }
    }

}
