/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate.Comport;

import Time.WaitTime.AbsTime;
import commandprompt.AbstractStream.AbsStreamReadable;
import commandprompt.AbstractStream.SubClass.ReadStreamOverTime;
import commandprompt.Communicate.IReadable;
import commandprompt.Communicate.ISender;
import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
import javax.swing.JOptionPane;

public class ComPort implements ISender, IReadable, IConnect {

    private final Enumeration portList;
    private CommPortIdentifier portId;
    private SerialPort serialPort;
    private BufferedWriter out;
    private final AbsStreamReadable input;

    public ComPort() {
        portList = CommPortIdentifier.getPortIdentifiers();
        input = new ReadStreamOverTime();
    }

    @Override
    public final boolean connect(String port, int baudrate) {
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (isPortSerial() && portId.getName().equalsIgnoreCase(port)) {
                return setupConnect(port, baudrate);
            }
        }
        return false;
    }

    @Override
    public boolean disConnect() {
        if (serialPort != null) {
            serialPort.close();
        }
        try {
            out.close();
            out = null;
            return input.disConnect();
        } catch (IOException ex) {
            System.err.println(ex);
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
        try {
            out.write(command);
            out.newLine();
            out.flush();
            Thread.sleep(500);
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

    private boolean isPortSerial() {
        return portId.getPortType() == CommPortIdentifier.PORT_SERIAL;
    }

    private boolean setupConnect(String port, int baudrate) throws HeadlessException {
        try {
            serialPort = (SerialPort) portId.open(port.toUpperCase(), 2000);
            serialPort.setSerialPortParams(baudrate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            input.setReader(serialPort.getInputStream());
            out = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));
            return true;
        } catch (IOException | PortInUseException | UnsupportedCommOperationException ex) {
            try {
                System.err.println(ex);
                JOptionPane.showConfirmDialog(null, String.format("Com port initialization failed\r\n%s-%s\r\n%s",
                        port, baudrate, this.getClass().getName()));
                serialPort.close();
                out.close();
            } catch (IOException ex1) {
            }
            return false;
        }
    }

    @Override
    public boolean isConnect() {
        return out != null;
    }

    @Override
    public String readLine(AbsTime tiker) {
        return input.readLine(tiker);
    }
}
