
package graphicInterface;


import javax.microedition.rms.*;


public class Records {
    private RecordStore rs = null;


    public void openRecStore(String name){
        try{
            //if it doesn't exist create it
            rs = RecordStore.openRecordStore(name , true);
        }
        catch(Exception e){
           System.err.println(e.toString());
        }
    }

    public void closeRecStore()
    {
        try{
            rs.closeRecordStore();
        }
        catch(Exception e)
        {
            System.err.println(e.toString());
        }
    }

    public void insertRecord(String username , String ip , String port){
        byte[] recUsername = username.getBytes();
        byte[] recIp = ip.getBytes();
        byte[] recPort = port.getBytes();
        try{
            rs.addRecord(recUsername , 0 , recUsername.length);
            rs.addRecord(recIp , 0 , recIp.length);
            rs.addRecord(recPort , 0 , recPort.length);
        }
        catch(Exception e){
            System.err.println(e.toString());
        }
    }

    public boolean checkIfExists(String rsName){
        try{
            rs.openRecordStore(rsName, false);
        }
        catch(RecordStoreNotFoundException e){
            System.out.println("den yparxei i basi");
            return false;
        }
        catch(RecordStoreException e){
            System.err.println(e.toString());
            return false;
        }
        return true;                                                            //Yparxei h bash
    }

    public String readUsername()
    {
        String username = null;
        try
        {
            // Intentionally make this too small to test code below
            byte[] recData = new byte[40];
            int len;

           // if (rs.getRecordSize(1) > recData.length)
           //   recData = new byte[rs.getRecordSize(0)];

            len = rs.getRecord(1, recData, 0);
            username = new String(recData, 0, len);

        }
        catch (Exception e)
        {
          System.err.println(e.toString());
        }
        finally{
            return username;
        }
    }
    public String readIp()
    {
        String ip = null;
        try
        {
            // Intentionally make this too small to test code below
            byte[] recData = new byte[40];
            int len;

            //if (rs.getRecordSize(2) > recData.length)
            //  recData = new byte[rs.getRecordSize(0)];

            len = rs.getRecord(2, recData, 0);
            ip = new String(recData, 0, len);

        }
        catch (Exception e)
        {
          System.err.println(e.toString());
        }
        finally{
            return ip;
        }
    }

    public String readPort()
    {
        String port = null;
        try
        {
            // Intentionally make this too small to test code below
            byte[] recData = new byte[40];
            int len;

            //if (rs.getRecordSize(3) > recData.length)
            //  recData = new byte[rs.getRecordSize(0)];

            len = rs.getRecord(3, recData, 0);
            port = new String(recData, 0, len);

        }
        catch (Exception e)
        {
          System.err.println(e.toString());
        }
        finally{
            return port;
        }
    }

    

}
