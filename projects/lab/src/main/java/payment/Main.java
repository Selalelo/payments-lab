package payment;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;

public class Main {
    public static void main(String[] args) throws ISOException {
        // Step 1: create a message and set its MTI
        ISOMsg msg = new ISOMsg();
        msg.setMTI("0200");

        // Step 2: set field 3 (processing code)
        msg.set(3, "000000");

        // Step 3: give it a packager, then pack to bytes
        msg.setPackager(new ISO87APackager());
        byte[] packed = msg.pack();

        // Step 4: print the result
        System.out.println("As text: " + new String(packed));
        System.out.println("As hex : " + ISOUtil.hexString(packed));
    }
}