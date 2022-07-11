/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate.Comport;

import commandprompt.Communicate.IConnect;
import Time.WaitTime.AbsTime;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import commandprompt.AbstractStream.AbsStreamReadable;
import commandprompt.AbstractStream.SubClass.ReadStream;
import commandprompt.Communicate.IReadable;
import commandprompt.Communicate.ISender;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

public class ComPort implements ISender, IReadable, IConnect {

    private BufferedWriter out;
    private final AbsStreamReadable input;
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

    private boolean openComm(String port, int baudrate) {
        try {
            disConnect();
            this.serialPort = SerialPort.getCommPort(port.toUpperCase());
            if (!serialPort.openPort() || !serialPort.isOpen() || !serialPort.setComPortParameters(
                    baudrate,
                    8, // data bits
                    SerialPort.ONE_STOP_BIT,
                    SerialPort.NO_PARITY)) {
                return false;
            }
            input.setReader(serialPort.getInputStream());
            out = new BufferedWriter(new PrintWriter(serialPort.getOutputStream()));
            return true;
        } catch (SerialPortInvalidPortException e) {
            return false;
        }
    }

    public Set<String> getCommPorts() {
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
    public boolean disConnect() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            serialPort = null;
        }
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            return input.disConnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendCommand(String command) {
        input.clearResult();
        return insertCommand(command);
    }

    @Override
    public boolean insertCommand(String command) {
        if (!isConnect()) {
            return false;
        }
        try {
            out.write(command);
            out.newLine();
            out.flush();
            Thread.sleep(100);
            return true;
        } catch (IOException | InterruptedException ex) {
            System.err.println(ex);
            return false;
        }
    }

    @Override
    public String readLine() {
        return input.readLine();
    }

    @Override
    public String readAll() {
        return input.readAll();
    }

    @Override
    public String readAll(AbsTime tiker) {
        return input.readAll(tiker);
    }

    @Override
    public String readUntil(String regex) {
        return input.readUntil(regex);
    }

    @Override
    public String readUntil(String regex, AbsTime tiker) {
        return input.readUntil(regex, tiker);
    }

    @Override
    public boolean isConnect() {
        return serialPort != null && serialPort.isOpen() && out != null;
    }

    @Override
    public String readLine(AbsTime tiker) {
        return input.readLine(tiker);
    }
}
