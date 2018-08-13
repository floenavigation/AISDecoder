package com.hfad.aisdecoder;

import static com.hfad.aisdecoder.POSTNREPORT.strbuildtodec;

public class AIVDM {

    private String packetName;
    private int fragCount;
    private int fragNum;
    private int seqMsgID;
    private char channelCode;
    private String payload;
    private String eod;

    AIVDM()
    {
        packetName = null;
        fragCount = -1;
        fragNum = -1;
        seqMsgID = -1;
        channelCode = '-';
        payload = null;
        eod = null;
    }

    public String getPacketName()
    {
        return packetName;
    }

    public int getFragCount()
    {
        return fragCount;
    }

    public int getSeqMsgID()
    {
        return seqMsgID;
    }

    public char getChannelCode()
    {
        return channelCode;
    }

    public String getPayload()
    {
        return payload;
    }

    public String getEod()
    {
        return eod;
    }


    //Should be called by the WiFi Service
    public void setData(String[] dataExtr)
    {
        packetName = dataExtr[0];
        fragCount = Integer.parseInt(dataExtr[1]);
        fragNum = Integer.parseInt(dataExtr[2]);
        seqMsgID = (("".equals(dataExtr[3]))? -1 : Integer.parseInt(dataExtr[3]));
        channelCode = dataExtr[4].charAt(0);
        payload = dataExtr[5];
        eod = dataExtr[6].split("\\*")[1];
    }

    public StringBuilder decodePayload()
    {

        char[] array = payload.toCharArray();
        StringBuilder binary = new StringBuilder();

        //converting to ascii
        for(char ch : array)
        {
            int val = (int)ch;
            int value = asciitodec(val);
            String binVal = String.format("%6s",Integer.toString(value,2)).replace(' ','0');
            binary.append(binVal);
        }

        return binary;
    }

    public void msgTypedecoding(StringBuilder bin, POSTNREPORT posObj)
    {

        //POSTNREPORT posObj = new POSTNREPORT();
        int num = (int)strbuildtodec(0,5,6,bin,int.class);
        switch(num)
        {
            case 1 :
            case 2 :
            case 3 :
                posObj.setData(bin);
                posObj.getMMSI();
                break;

        }


    }

    public int asciitodec(int val)
    {
        val = val - 48;
        if(val > 40)
            val -= 8;
        return val;
    }

    /*
    public void display()
    {
        System.out.println("packetname: " + packetName);
        System.out.println("fragcount: " + fragCount);
        System.out.println("fragNum: " + fragNum);
        System.out.println("seqMsgID: " + seqMsgID);
        System.out.println("channelCode: " + channelCode);
        System.out.println("payload: " + payload);
        System.out.println("eod: " + eod);
    }
    */

};
