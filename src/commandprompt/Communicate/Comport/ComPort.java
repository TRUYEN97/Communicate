/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate.Comport;

import Time.WaitTime.ITimer;
import commandprompt.AbstractStream.AbsStreamReadable;
import commandprompt.AbstractStream.SubClass.ReadStreamOnTime;
import commandprompt.Communicate.IReadable;
import commandprompt.Communicate.ISender;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.JOptionPane;

public class ComPort implements ISender, IReadable {

    private Enumeration portList;
    private CommPortIdentifier portId;
    private SerialPort serialPort;
    private BufferedWriter out;
    private AbsStreamReadable input;

    public ComPort(String port, int baudrate) {
        portList = CommPortIdentifier.getPortIdentifiers();
        input = new ReadStreamOnTime();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equalsIgnoreCase(port)) {
                    try {
                        serialPort = (SerialPort) portId.open(port.toUpperCase(), 2000);
                        serialPort.setSerialPortParams(baudrate,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        input.setReader(serialPort.getInputStream());
                        out = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showConfirmDialog(null, String.format("Com port initialization failed\r\n%s-%s\r\n%s",
                                port, baudrate, this.getClass().getName()));
                    }
                }
            }
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
            return true;
        } catch (IOException ex) {
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
    public String readAll(ITimer tiker) {
        return input.readAll(tiker);
    }

    @Override
    public String readUntil(String regex) {
        return input.readUntil(regex);
    }

    @Override
    public String readUntil(String regex, ITimer tiker) {
        return input.readUntil(regex, tiker);
    }
}
