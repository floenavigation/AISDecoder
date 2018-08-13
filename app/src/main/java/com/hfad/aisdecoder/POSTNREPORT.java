package com.hfad.aisdecoder;

public class POSTNREPORT {

    private int msgInd;
    private int repeatInd;
    private long mmsi;
    private int status;
    private int turn;
    private int speed;
    private int accuracy;
    private double lon;
    private double lat;
    private long course;
    private int heading;
    private int sec;
    private int maneuver;
    private int raim;
    private long radio;

    public POSTNREPORT()
    {
//		System.out.println(bin);
        msgInd = -1;
        repeatInd = -1;
        mmsi =  -1;
        status = -1;
        turn = -1;
        speed = -1;
        accuracy = -1;
        lon = -1;
        lat = -1;
        course = -1;
        heading = -1;
        sec = -1;
        maneuver = -1;
        raim = -1;
        radio = -1;

    }

    public int getMsgInd()
    {
        return msgInd;
    }

    public int getRepeatInd()
    {
        return repeatInd;
    }

    public long getMMSI()
    {
        return mmsi;
    }

    public int getStatus()
    {
        return status;
    }

    public int getTurn()
    {
        return turn;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getAccuracy()
    {
        return accuracy;
    }

    public double getLongitude()
    {
        return lon;
    }

    public double getLatitude()
    {
        return lat;
    }

    public long getCourse()
    {
        return course;
    }

    public int getHeading()
    {
        return heading;
    }

    public int getSeconds()
    {
        return sec;
    }

    public int getManeuver()
    {
        return maneuver;
    }

    public int getRaim()
    {
        return raim;
    }

    public long getRadio()
    {
        return radio;
    }

    public void setData(StringBuilder bin)
    {
//		System.out.println(bin);
        msgInd = (int)strbuildtodec(0,5,6,bin,int.class);
        repeatInd = (int)strbuildtodec(6,7,2,bin,int.class);
        mmsi =  (long)strbuildtodec(8,37,30,bin,long.class);
        status = (int)strbuildtodec(38,41,4,bin,int.class);
        turn = (int)strbuildtodec(42,49,8,bin,int.class);
        speed = (int)strbuildtodec(50,59,10,bin,int.class);
        accuracy = (int)strbuildtodec(60,60,1,bin,int.class);
        lon = (long)strbuildtodec(61,88,28,bin,long.class)/600000.0;
        lat = (long)strbuildtodec(89,115,27,bin,long.class)/600000.0;
        course = (long)strbuildtodec(116,127,12,bin,long.class);
        heading = (int)strbuildtodec(128,136,9,bin,int.class);
        sec = (int)strbuildtodec(137,142,6,bin,int.class);
        maneuver = (int)strbuildtodec(143,144,2,bin,int.class);
        raim = (int)strbuildtodec(148,148,1,bin,int.class);
        radio = (long)strbuildtodec(149,167,19,bin,long.class);
    }

    public static <T> Object strbuildtodec(int begin, int end, int len, StringBuilder binLocal, Class<?> type)
    {
        char[] array = new char[len];
        binLocal.getChars(begin,(end + 1),array,0);

        long decimal = 0;
        for(int pow = len; pow > 0; pow--)
        {
            if(array[pow - 1] == '1')
                decimal += Math.pow(2,len - pow);
        }
//		System.out.println("dec: " + decimal);
        if(type == int.class)
            return (int)(long)decimal;
        else
            return decimal;
        //return Integer.parseInt(new String(array));
    }

    /*
    public void display()
    {
        System.out.println("msdInd: " + msgInd);
        System.out.println("repeatInd: " + repeatInd);
        System.out.println("mmsi: " + mmsi);
        System.out.println("status: " + status);
        System.out.println("turn: " + turn);
        System.out.println("speed: " + speed);
        System.out.println("accuracy: " + accuracy);
        System.out.println("lon: " + lon);
        System.out.println("lat: " + lat);
        System.out.println("course: " + course);
        System.out.println("heading: " + heading);
        System.out.println("sec: " + sec);
        System.out.println("maneuver: " + maneuver);
        System.out.println("raim: " + raim);
        System.out.println("radio: " + radio);
    }
    */
};
